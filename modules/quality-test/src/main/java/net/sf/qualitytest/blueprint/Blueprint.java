/*******************************************************************************
 * Copyright 2013 Dominik Seichter
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package net.sf.qualitytest.blueprint;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Random;

import net.sf.qualitycheck.Check;
import net.sf.qualitycheck.Throws;
import net.sf.qualitycheck.exception.IllegalNullArgumentException;
import net.sf.qualitytest.ModifierBits;
import net.sf.qualitytest.blueprint.configuration.DefaultBlueprintConfiguration;
import net.sf.qualitytest.blueprint.configuration.RandomBlueprintConfiguration;
import net.sf.qualitytest.exception.BlueprintException;

/**
 * Blueprinting is a technique that makes writing test easier. For unit-testing you often need data-objects, where the
 * actual content of the objects does not matter. {@code Blueprint} creates data-objects filled with random or defined
 * data automatically based on the "Blue-Print" which is the Class itself.
 * 
 * Blueprinting makes tests more maintainable as they depend less on test-data. Imagine, you add a new required
 * attribute to a class. Usually, you have to add this to all tests using this class. With blueprinting you just have to
 * add it to certain tests where the contents of the logic does actually matter. Most of the time the randomly generated
 * value by {@code Blueprint} is just fine.
 * 
 * {@code Blueprint} is similar to C#'s AutoFixture (https://github.com/AutoFixture/AutoFixture#readme).
 * 
 * A simple example:
 * 
 * <code>
 * 	final BlueprintConfiguration config = new RandomBlueprintConfiguration().with("email", "mail@example.com");
 *  final User user = Blueprint.object(User.class, config);
 * </code>
 * 
 * or simpler
 * 
 * <code>
 *  final User user = Blueprint.random().with("email", "mail@example.com").object(User.class);
 * </code>
 * 
 * {@code Blueprint} offers to custome configurations. A {@code DefaultBlueprintConfiguration} which fills any object
 * using default, empty or 0 values. The second configuration, {@code RandomBlueprintConfiguration} will always generate
 * a random value. Both fill child objects using a deep-tree-search.
 * 
 * Utilities for collections can be found in {@code CollectionBlueprint}.
 * 
 * @see DefaultBlueprintConfiguration
 * @see RandomBlueprintConfiguration
 * 
 * @author Dominik Seichter
 */
public final class Blueprint {

	private static final BlueprintConfiguration DEFAULT_CONFIG = new DefaultBlueprintConfiguration();

	private static final int MAX_ARRAY_SIZE = 7;

	private static final Random RANDOM = new Random();
	private static final String SETTER_PREFIX = "set";

	/**
	 * Blueprint an array value using a {@code DefaultBlueprintConfiguration}.
	 * 
	 * This method will return an array with a random generated dimension between 1 and {@code Blueprint.MAX_ARRAY_SIZE
	 * + 1},
	 * 
	 * @param <T>
	 * @param array
	 *            the class of an array.
	 * @return a valid array.
	 */
	@Throws(IllegalNullArgumentException.class)
	public static <T> Object array(final Class<T> array) {
		return Blueprint.array(array, DEFAULT_CONFIG, new BlueprintSession());
	}

	/**
	 * Blueprint an array value.
	 * 
	 * This method will return an array with a random generated dimension between 1 and {@code Blueprint.MAX_ARRAY_SIZE
	 * + 1},
	 * 
	 * @param <T>
	 * @param array
	 *            the class of an array.
	 * @param config
	 *            a {@code BlueprintConfiguration}
	 * @param session
	 *            A {@code BlueprintSession}
	 * @return a valid array.
	 */
	@Throws(IllegalNullArgumentException.class)
	public static <T> Object array(final Class<T> array, final BlueprintConfiguration config, final BlueprintSession session) {
		Check.notNull(array, "array");
		Check.notNull(config, "config");

		final int arraySize = RANDOM.nextInt(MAX_ARRAY_SIZE) + 1;
		final Object value = Array.newInstance(array.getComponentType(), arraySize);
		if (!array.getComponentType().isPrimitive()) {
			initializeArray(array, arraySize, value, config, session);
		}
		return value;
	}

	/**
	 * Blueprint a Java-Bean.
	 * 
	 * This method will call the default constructor and fill all setters using blueprints.
	 * 
	 * @param <T>
	 * @param clazz
	 *            a class, which must have a default constructor.
	 * @param config
	 *            a BlueprintConfiguration
	 * @param session
	 *            A {@code BlueprintSession}
	 * @return a blue printed instance of {@code T}
	 */
	@Throws(IllegalNullArgumentException.class)
	private static <T> T bean(final Class<T> clazz, final BlueprintConfiguration config, final BlueprintSession session) {
		Check.notNull(clazz, "clazz");
		Check.notNull(config, "config");
		Check.notNull(session, "sesion");

		final T obj = safeNewInstance(clazz);
		bluePrintSetters(obj, clazz, config, session);
		bluePrintPublicAttributes(obj, clazz, config, session);
		return obj;
	}

	/**
	 * Blueprint a field.
	 * 
	 * @param that
	 *            Instance of the object
	 * @param f
	 *            Accessible Field
	 * @param config
	 *            configuration to use.
	 * @param session
	 *            A {@code BlueprintSession}
	 */
	private static void bluePrintField(final Object that, final Field f, final BlueprintConfiguration config, final BlueprintSession session) {
		final ValueCreationStrategy<?> creator = config.findCreationStrategyForType(f.getType());
		final Object value = blueprintObject(f.getType(), config, creator, session);

		safeInvoke(new ExceptionRunnable() {
			@Override
			public void run() throws Exception {
				f.set(that, value);
			}
		});

	}

	/**
	 * Blueprint a method.
	 * 
	 * @param that
	 *            Instance of the object
	 * @param m
	 *            Setter-Method
	 * @param config
	 *            configuration to use.
	 * @param session
	 *            A {@code BlueprintSession}
	 */
	private static void bluePrintMethod(final Object that, final Method m, final BlueprintConfiguration config,
			final BlueprintSession session) {
		final ValueCreationStrategy<?> creator = config.findCreationStrategyForMethod(m);
		final Class<?>[] parameterTypes = m.getParameterTypes();
		final Object[] values = new Object[parameterTypes.length];

		if (creator != null && parameterTypes.length == 1) {
			values[0] = creator.createValue();
		} else {
			for (int i = 0; i < parameterTypes.length; i++) {
				final Class<?> parameter = parameterTypes[i];
				values[i] = object(parameter, config, session);
			}
		}

		safeInvoke(new ExceptionRunnable() {

			@Override
			public void run() throws Exception {
				m.invoke(that, values);

			}
		});

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static <T> T blueprintObject(final Class<T> clazz, final BlueprintConfiguration config, final ValueCreationStrategy<?> creator,
			final BlueprintSession session) {
		if (creator != null) {
			return (T) creator.createValue();
		} else if (clazz.isEnum()) {
			return (T) enumeration((Class<Enum>) clazz);
		} else if (clazz.isArray()) {
			return (T) array(clazz, config, session);
		} else if (clazz.isInterface()) {
			return (T) proxy(clazz, config, session);
		} else if (ModifierBits.isModifierBitSet(clazz.getModifiers(), Modifier.ABSTRACT)) {
			throw new BlueprintException("Abstract classes are currently not supported.");
		} else if (hasPublicDefaultConstructor(clazz)) {
			return bean(clazz, config, session);
		} else {
			return immutable(clazz, config, session);
		}
	}

	/**
	 * Blueprint all public attributes in an object.
	 * 
	 * Does nothing if {@code config.isWithPublicAttributes} is false.
	 * 
	 * @param <T>
	 *            type of object
	 * @param obj
	 *            Instance of the object
	 * @param clazz
	 *            Class of the object
	 * @param config
	 *            Configuration to apply
	 * @param session
	 *            A {@code BlueprintSession}
	 */
	private static <T> void bluePrintPublicAttributes(final T obj, final Class<T> clazz, final BlueprintConfiguration config,
			final BlueprintSession session) {
		if (!config.isWithPublicAttributes()) {
			return;
		}

		for (final Field f : clazz.getFields()) {
			bluePrintField(obj, f, config, session);
		}
	}

	/**
	 * Blueprint all setters in an object.
	 * 
	 * @param <T>
	 *            type of object
	 * @param obj
	 *            Instance of the object
	 * @param clazz
	 *            Class of the object
	 * @param config
	 *            Configuration to apply
	 * @param session
	 *            A {@code BlueprintSession}
	 */
	private static <T> void bluePrintSetters(final T obj, final Class<T> clazz, final BlueprintConfiguration config,
			final BlueprintSession session) {
		for (final Method m : clazz.getMethods()) {
			if (isSetter(m)) {
				bluePrintMethod(obj, m, config, session);
			}
		}
	}

	/**
	 * Return a new configuration for default blueprinting with zero or empty default values.
	 * 
	 * @return a new {@code DefaultBlueprintConfiguration}
	 */
	public static BlueprintConfiguration def() {
		return new DefaultBlueprintConfiguration();
	}

	/**
	 * Blueprint an enum value.
	 * 
	 * This method will return the first enum constant in the enumeration.
	 * 
	 * @param <T>
	 * @param enumClazz
	 *            the class of an enumeration.
	 * @return a valid enum value.
	 */
	public static <T extends Enum<T>> T enumeration(final Class<T> enumClazz) {
		final T[] enumConstants = enumClazz.getEnumConstants();
		return enumConstants.length > 0 ? enumConstants[0] : null;
	}

	/**
	 * Find the first public constructor in a class.
	 * 
	 * @param <T>
	 *            type parameter of the class and constructor.
	 * @param clazz
	 *            the class object
	 */
	private static <T> Constructor<?> findFirstPublicConstructor(final Class<T> clazz) {
		final Constructor<?>[] constructors = clazz.getConstructors();
		for (final Constructor<?> c : constructors) {
			final boolean isPublic = ModifierBits.isModifierBitSet(c.getModifiers(), Modifier.PUBLIC);
			if (isPublic) {
				return c;
			}
		}

		return null;
	}

	/**
	 * Test if a class has a public default constructor (i.e. a public constructor without constructor arguments).
	 * 
	 * @param clazz
	 *            the class object.
	 * @return true if the class has a public default constructor.
	 */
	private static boolean hasPublicDefaultConstructor(final Class<?> clazz) {
		final Constructor<?>[] constructors = clazz.getConstructors();
		for (final Constructor<?> c : constructors) {
			final boolean isPublic = ModifierBits.isModifierBitSet(c.getModifiers(), Modifier.PUBLIC);
			if (isPublic && c.getParameterTypes().length == 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Blueprint an immutable class based on the constructor parameters of the first public constructor.
	 * 
	 * @param <T>
	 *            type parameter of the blueprinted class
	 * @param clazz
	 *            the class object
	 * @param config
	 *            the configuration
	 * @param session
	 *            A {@code BlueprintSession}
	 * @return a new blueprint of the class with all constructor parameters to filled.
	 */
	private static <T> T immutable(final Class<T> clazz, final BlueprintConfiguration config, final BlueprintSession session) {
		final Constructor<?> constructor = findFirstPublicConstructor(clazz);
		if (constructor == null) {
			throw new BlueprintException("No public constructor found.");
		}

		final Class<?>[] parameterTypes = constructor.getParameterTypes();
		final Object[] parameters = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; i++) {
			parameters[i] = object(parameterTypes[i], config, session);
		}

		final T obj = safeNewInstance(constructor, parameters);
		bluePrintSetters(obj, clazz, config, session);
		bluePrintPublicAttributes(obj, clazz, config, session);

		return obj;
	}

	/**
	 * Initalize an array using blueprinted objects.
	 * 
	 * @param <T>
	 *            Type parameter of the array.
	 * @param array
	 *            class of the array
	 * @param arraySize
	 *            size of the array
	 * @param value
	 *            object where the array is stored
	 * @param config
	 *            A {@code BlueprintConfiguration}
	 * @param session
	 *            A {@code BlueprintSession}
	 */
	private static <T> void initializeArray(final Class<T> array, final int arraySize, final Object value,
			final BlueprintConfiguration config, final BlueprintSession session) {
		for (int i = 0; i < arraySize; i++) {
			final Object bluePrint = object(array.getComponentType(), config, session);
			Array.set(value, i, bluePrint);
		}
	}

	/**
	 * Check if a method is setter according to the java
	 * 
	 * @param m
	 *            a method
	 * @return true if this is a setter method
	 */
	protected static boolean isSetter(final Method m) {
		final boolean isNotStatic = !ModifierBits.isModifierBitSet(m.getModifiers(), Modifier.STATIC);
		final boolean isPublic = ModifierBits.isModifierBitSet(m.getModifiers(), Modifier.PUBLIC);
		final boolean isSetterName = m.getName().startsWith(SETTER_PREFIX);
		return isNotStatic && isPublic && isSetterName;
	}

	/**
	 * Blueprint a Java-Object using a {@code DefaultBlueprintConfiguration}.
	 * 
	 * If the object has a default constructor, it will be called and all setters will be called. If the object does not
	 * have a default constructor the first constructor is called and filled with all parameters. Afterwards all setters
	 * will be called.
	 * 
	 * @param <T>
	 * @param clazz
	 *            a class
	 * @return a blue printed instance of {@code T}
	 */
	@Throws(IllegalNullArgumentException.class)
	public static <T> T object(final Class<T> clazz) {
		return Blueprint.object(clazz, DEFAULT_CONFIG, new BlueprintSession());
	}

	/**
	 * Blueprint a Java-Object.
	 * 
	 * If the object has a default constructor, it will be called and all setters will be called. If the object does not
	 * have a default constructor the first constructor is called and filled with all parameters. Afterwards all setters
	 * will be called.
	 * 
	 * @param <T>
	 * @param clazz
	 *            a class
	 * @param config
	 *            a {@code BlueprintConfiguration}
	 * @param session
	 *            a {@code BlueprintSession}
	 * @return a blue printed instance of {@code T}
	 */
	@Throws(IllegalNullArgumentException.class)
	public static <T> T object(final Class<T> clazz, final BlueprintConfiguration config, final BlueprintSession session) {
		Check.notNull(clazz, "clazz");
		Check.notNull(config, "config");

		final ValueCreationStrategy<?> creator = config.findCreationStrategyForType(clazz);

		session.push(clazz);
		final T ret = blueprintObject(clazz, config, creator, session);
		session.pop();
		return ret;
	}

	/**
	 * Create a proxy for an interface, which does nothing.
	 * 
	 * @param <T>
	 *            Class of the interface
	 * @param iface
	 *            an interace
	 * @param config
	 *            {@code BlueprintConfiguration}
	 * @param session
	 *            {@code BlueprintSession}
	 * @return a new dynamic proxy
	 */
	@SuppressWarnings("unchecked")
	private static <T> T proxy(final Class<T> iface, final BlueprintConfiguration config, final BlueprintSession session) {
		return (T) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface }, new BlueprintInvocationHandler(config, session));
	}

	/**
	 * Return a new configuration for random blueprinting.
	 * 
	 * @return a new {@code RandomBlueprintConfiguration}
	 */
	public static BlueprintConfiguration random() {
		return new RandomBlueprintConfiguration();
	}

	/**
	 * Safely invoke a method on an object without having to care about checked exceptions. The runnable is executed and
	 * every exception is converted into a {@code BlueprintException}.
	 * 
	 * @param runnable
	 *            An {@code ExceptionRunnable}
	 * 
	 * @throws BlueprintException
	 *             in case of any error
	 */
	private static void safeInvoke(final ExceptionRunnable runnable) {
		try {
			runnable.run();
		} catch (final Exception e) {
			throw new BlueprintException(e);
		}
	}

	/**
	 * Create a new instance of a class without having to care about the checked exceptions. The class must have an
	 * accessible default constructor.
	 * 
	 * @param <T>
	 *            type parameter of the class to create
	 * @param clazz
	 *            class-object
	 * @return a new instance of the class
	 * 
	 * @throws BlueprintException
	 *             in case of any error
	 */
	private static <T> T safeNewInstance(final Class<T> clazz) {
		try {
			return (T) clazz.newInstance();
		} catch (final InstantiationException e) {
			throw new BlueprintException(e);
		} catch (final IllegalAccessException e) {
			throw new BlueprintException(e);
		}
	}

	/**
	 * Create a new instance of a class without having to care about the checked exceptions using a given constructor.
	 * 
	 * @param constructor
	 *            constructor to call
	 * @param parameters
	 *            constructor arguments
	 * @throws BlueprintException
	 *             in case of any error
	 */
	@SuppressWarnings("unchecked")
	private static <T> T safeNewInstance(final Constructor<?> constructor, final Object[] parameters) {
		try {
			return (T) constructor.newInstance(parameters);
		} catch (final IllegalArgumentException e) {
			throw new BlueprintException(e);
		} catch (final InstantiationException e) {
			throw new BlueprintException(e);
		} catch (final IllegalAccessException e) {
			throw new BlueprintException(e);
		} catch (final InvocationTargetException e) {
			throw new BlueprintException(e);
		}
	}

	/**
	 * <strong>Attention:</strong> This class is not intended to create objects from it.
	 */
	private Blueprint() {
		// This class is not intended to create objects from it.
	}
}
