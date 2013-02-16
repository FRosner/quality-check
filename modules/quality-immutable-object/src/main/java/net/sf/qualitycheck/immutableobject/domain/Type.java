package net.sf.qualitycheck.immutableobject.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import net.sf.qualitycheck.Check;

/**
 * Represents a type which can be a class, interface or annotation
 */
@Immutable
public final class Type {

	/**
	 * Represents the primitive type boolean and not {@link java.lang.Boolean}
	 */
	public static final Type BOOLEAN = new Type(Package.UNDEFINED, Primitive.BOOLEAN.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Boolean}
	 */
	public static final Type BOOLEAN_BOXED = new Type(Package.JAVA_LANG, Boolean.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the primitive type <code>byte</code> and not {@link java.lang.Byte}
	 */
	public static final Type BYTE = new Type(Package.UNDEFINED, Primitive.BYTE.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Byte}
	 */
	public static final Type BYTE_BOXED = new Type(Package.JAVA_LANG, Byte.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the primitive type <code>char</code> and not {@link java.lang.Character}
	 */
	public static final Type CHAR = new Type(Package.UNDEFINED, Primitive.CHAR.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Character}
	 */
	public static final Type CHARACTER_BOXED = new Type(Package.JAVA_LANG, Character.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the primitive type <code>double</code> and not {@link java.lang.Double}
	 */
	public static final Type DOUBLE = new Type(Package.UNDEFINED, Primitive.DOUBLE.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Double}
	 */
	public static final Type DOUBLE_BOXED = new Type(Package.JAVA_LANG, Double.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the primitive type <code>float</code> and not {@link java.lang.Float}
	 */
	public static final Type FLOAT = new Type(Package.UNDEFINED, Primitive.FLOAT.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Float}
	 */
	public static final Type FLOAT_BOXED = new Type(Package.JAVA_LANG, Float.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the primitive type <code>int</code> and not {@link java.lang.Integer}
	 */
	public static final Type INT = new Type(Package.UNDEFINED, Primitive.INT.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Integer}
	 */
	public static final Type INTEGER_BOXED = new Type(Package.JAVA_LANG, Integer.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the primitive type <code>long</code> and not {@link java.lang.Long}
	 */
	public static final Type LONG = new Type(Package.UNDEFINED, Primitive.LONG.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Long}
	 */
	public static final Type LONG_BOXED = new Type(Package.JAVA_LANG, Long.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Pattern to parse a full qualified name of a type
	 * <p>
	 * ^(((\d|\w)+\.)*)((\d|\w)+)(\$((\d|\w)+))?(<(\w.*)>)?$
	 */
	private static final Pattern PATTERN = Pattern.compile("^(((\\d|\\w)+\\.)*)((\\d|\\w)+)(\\$((\\d|\\w)+))?(<(\\w.*)>)?$");

	/**
	 * Represents the primitive type <code>short</code> and not {@link java.lang.Short}
	 */
	public static final Type SHORT = new Type(Package.UNDEFINED, Primitive.SHORT.getName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.Short}
	 */
	public static final Type SHORT_BOXED = new Type(Package.JAVA_LANG, Short.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Represents the type {@link java.lang.String}
	 */
	public static final Type STRING = new Type(Package.JAVA_LANG, String.class.getSimpleName(), GenericDeclaration.UNDEFINED);

	/**
	 * Creates a new instance of {@code GenericDeclaration} when the given declaration is not empty other wise it
	 * returns {@link GenericDeclaration#UNDEFINED}.
	 * 
	 * @param declaration
	 *            declaration of a generic
	 * @return instance of {@code GenericDeclaration} and never null
	 */
	@Nonnull
	private static GenericDeclaration createGenericDeclaration(@Nonnull final String declaration) {
		Check.notNull(declaration, "declaration");
		return declaration.isEmpty() ? GenericDeclaration.UNDEFINED : GenericDeclaration.of(declaration);
	}

	/**
	 * Creates a new instance of {@code Package} when the given name is not empty other wise it returns
	 * {@link Package#UNDEFINED}.
	 * 
	 * @param packageName
	 *            package name
	 * @return instance of {@code Package} and never null
	 */
	@Nonnull
	private static Package createPackage(@Nonnull final String packageName) {
		Check.notNull(packageName, "packageName");
		return packageName.isEmpty() ? Package.UNDEFINED : new Package(packageName);
	}

	@Nonnull
	public static Type of(@Nonnull final Class<?> clazz) {
		Check.notNull(clazz, "clazz");
		return new Type(clazz.getName());
	}

	@Nullable
	private final transient boolean _collectionVariant;

	@Nonnull
	private final GenericDeclaration _genericDeclaration;

	@Nonnull
	private final String _name;

	@Nonnull
	private final Package _package;

	public Type(@Nonnull final Package pkg, @Nonnull final String name, @Nonnull final GenericDeclaration genericDeclaration) {
		_package = Check.notNull(pkg, "pkg");
		_name = Check.notEmpty(name, "name");
		_genericDeclaration = Check.notNull(genericDeclaration, "genericDeclaration");
		_collectionVariant = CollectionVariant.evaluate(pkg, name) != null ? true : false;
	}

	public Type(@Nonnull final String qualifiedName) {
		Check.notNull(qualifiedName, "qualifiedName");
		final Matcher m = PATTERN.matcher(Check.notEmpty(qualifiedName.trim(), "qualifiedName"));
		Check.stateIsTrue(m.matches(), "qualified name must match against: %s", PATTERN.pattern());
		// internal only reference needed to be detected as immutable
		final Package pkg = m.group(7) != null ? createPackage(m.group(1) + m.group(4)) : createPackage(m.group(1));
		final String name = m.group(7) != null ? m.group(7) : Check.notEmpty(m.group(4), "name");
		_collectionVariant = CollectionVariant.evaluate(pkg, name) != null ? true : false;
		_genericDeclaration = m.group(10) != null ? createGenericDeclaration(m.group(10)) : GenericDeclaration.UNDEFINED;
		_package = pkg;
		_name = name;
	}

	public Type(@Nonnull final String packageName, @Nonnull final String typeName, @Nonnull final String genericDeclaration) {
		this(createPackage(Check.notNull(packageName, "packageName")), Check.notEmpty(typeName, "typeName"), createGenericDeclaration(Check
				.notNull(genericDeclaration, "genericDeclaration")));
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Type other = (Type) obj;
		if (!_genericDeclaration.equals(other._genericDeclaration)) {
			return false;
		}
		if (!_package.equals(other._package)) {
			return false;
		}
		if (!_name.equals(other._name)) {
			return false;
		}
		return true;
	}

	@Nonnull
	public GenericDeclaration getGenericDeclaration() {
		return _genericDeclaration;
	}

	@Nonnull
	public String getName() {
		return _name;
	}

	@Nonnull
	public Package getPackage() {
		return _package;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _genericDeclaration.hashCode();
		result = prime * result + _package.hashCode();
		result = prime * result + _name.hashCode();
		return result;
	}

	public boolean isBoolean() {
		return equals(BOOLEAN);
	}

	public boolean isBoxedBoolean() {
		return equals(BOOLEAN_BOXED);
	}

	public boolean isBoxedByte() {
		return equals(BYTE_BOXED);
	}

	public boolean isBoxedCharacter() {
		return equals(CHARACTER_BOXED);
	}

	public boolean isBoxedDouble() {
		return equals(DOUBLE_BOXED);
	}

	public boolean isBoxedFloat() {
		return equals(FLOAT_BOXED);
	}

	public boolean isBoxedInteger() {
		return equals(INTEGER_BOXED);
	}

	public boolean isBoxedLong() {
		return equals(LONG_BOXED);
	}

	public boolean isBoxedShort() {
		return equals(SHORT_BOXED);
	}

	public boolean isByte() {
		return equals(BYTE);
	}

	public boolean isChar() {
		return equals(CHAR);
	}

	public boolean isCollectionVariant() {
		return _collectionVariant;
	}

	public boolean isDouble() {
		return equals(DOUBLE);
	}

	public boolean isFloat() {
		return equals(FLOAT);
	}

	public boolean isInt() {
		return equals(INT);
	}

	public boolean isLong() {
		return equals(LONG);
	}

	public boolean isPrimitive() {
		return Primitive.isPrimitive(_name);
	}

	public boolean isShort() {
		return equals(SHORT);
	}

	public boolean isString() {
		return equals(STRING);
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		if (!_package.getName().isEmpty()) {
			b.append(_package.getName());
			b.append(Characters.DOT);
		}
		b.append(_name);
		if (!_genericDeclaration.getDeclaration().isEmpty()) {
			b.append('<');
			b.append(_genericDeclaration.getDeclaration());
			b.append('>');
		}
		return b.toString();
	}

}
