/*******************************************************************************
 * Copyright 2013 André Rouél and Dominik Seichter
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

import java.lang.reflect.Method;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.sf.qualitytest.exception.BlueprintCycleException;

/**
 * Defines a blueprint configuration
 * 
 * @author André Rouél
 */
public interface BlueprintConfiguration {

	/**
	 * Construct a Java-Object using a class as a blueprint.
	 * 
	 * @see Blueprint
	 * 
	 * @param <T>
	 * @param clazz
	 *            a class
	 * @return a blue printed instance of {@code T}
	 */
	@Nullable
	<T> T construct(@Nonnull final Class<T> clazz);

	/**
	 * Find a creation strategy that matches on the given method.
	 * 
	 * @param method
	 *            A setter method
	 * 
	 * @return a {@code ValueCreationStrategy} or {@code null}
	 */
	@Nullable
	CreationStrategy<?> findCreationStrategyForMethod(@Nonnull final Method method);

	/**
	 * Find a creation strategy that matches on a given type.
	 * 
	 * @param class A class
	 * 
	 * @return a {@code ValueCreationStrategy} or {@code null}
	 */
	@Nullable
	CreationStrategy<?> findCreationStrategyForType(@Nonnull final Class<?> clazz);

	/**
	 * Handle the situation that a BlueprintCycle was detected for a particular class.
	 * 
	 * @see Blueprint
	 * 
	 * @param <T>
	 * @param session
	 *            The current {@link BlueprintSession}
	 * @param clazz
	 *            The class which caused cycle in the blueprinting graph
	 * @return a blue printed instance of {@code T}
	 */
	@Nullable
	<T> T handleCycle(@Nonnull final BlueprintSession session, @Nonnull final Class<T> clazz);

	/**
	 * Retrieve if public attributes are filled during blueprinting.
	 * 
	 * @return {@code true} if public attributes are filled during blueprinting
	 */
	boolean isWithPublicAttributes();

	/**
	 * Replace every attribute with the type {@code type} with a given value.
	 * 
	 * @param type
	 *            a Java type.
	 * @param creator
	 *            Creation strategy which actually creates a new value.
	 * 
	 * @return the changed blueprint configuration.
	 */
	@Nonnull
	<T> BlueprintConfiguration with(@Nonnull final Class<T> type, @Nullable final CreationStrategy<?> creator);

	/**
	 * Replace every attribute with the type {@code type} with a given value.
	 * 
	 * @param type
	 *            a Java type.
	 * @param value
	 *            value which should be assigned to the attribute
	 * 
	 * @return the changed blueprint configuration.
	 */
	@Nonnull
	<T> BlueprintConfiguration with(@Nonnull final Class<T> type, @Nullable final T value);

	/**
	 * Handle detected cycles in the blueprinting graph using an additional strategy. The default is to throw a
	 * {@link BlueprintCycleException}.
	 * 
	 * @param cycleHandlingStrategy
	 *            Strategy to define how cycles for a certain type are handled
	 * 
	 * @return the changed blueprint configuration
	 */
	@Nonnull
	<T> BlueprintConfiguration with(@Nonnull final CycleHandlingStrategy<T> cycleHandlingStrategy);

	/**
	 * Blueprint everything matching a given {@code MatchingStrategy} using this configuration.
	 * 
	 * @param matchingStrategy
	 *            Matching strategy to define if a given type or method should be constructed using blueprint.
	 * @return the changed blueprint configuration
	 */
	@Nonnull
	<T> BlueprintConfiguration with(@Nonnull final MatchingStrategy matchingStrategy);

	/**
	 * Replace every attribute which matches a given strategy with a given value.
	 * 
	 * @param matcher
	 *            Matching strategy to define if the replaced should be applied.
	 * @param creator
	 *            Creation strategy which actually creates a new value.
	 * 
	 * @return the changed blueprint configuration.
	 */
	@Nonnull
	BlueprintConfiguration with(final MatchingStrategy matcher, final CreationStrategy<?> creator);

	/**
	 * Replace every attribute with the name {@code name} with a given value.
	 * 
	 * @param name
	 *            case insensitive name of an attribute.
	 * @param value
	 *            value which should be assigned to the attribute
	 * 
	 * @return the changed blueprint configuration.
	 */
	@Nonnull
	<T> BlueprintConfiguration with(@Nonnull final String name, @Nullable final T value);

	/**
	 * Configure whether public attributes should be filled with values during blueprinting.
	 * 
	 * @param withPublicAttributes
	 *            if {@code true} public attributes will be filled during blueprinting, otherwise they will be ignored.
	 * 
	 * @return the changed blueprint configuration.
	 */
	@Nonnull
	BlueprintConfiguration withPublicAttributes(final boolean withPublicAttributes);

}
