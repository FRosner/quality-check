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
package net.sf.qualitytest.blueprint.strategy.creation;

import java.util.UUID;

import javax.annotation.Nonnegative;

import net.sf.qualitycheck.ArgumentsChecked;
import net.sf.qualitycheck.Check;

/**
 * Strategy which creates random strings using {@code Blueprint.string()}.
 * 
 * Optionally a maximum length for the generated strings can be specified.
 * 
 * @author Dominik Seichter
 */
public class BlueprintStringCreationStrategy extends ValueCreationStrategy<String> {

	private final int maxLength;

	/**
	 * Create a string creation strategy which creates strings with the default length (standard UUID).
	 */
	public BlueprintStringCreationStrategy() {
		maxLength = -1;
	}

	/**
	 * Create a string creation strategy which does not exceed the given maximum length
	 * 
	 * @param maxLength
	 *            Generated string does not exceed the specified maximum length but is not necessarily filled up to
	 *            maxLength. Must be a positive value.
	 */
	@ArgumentsChecked
	public BlueprintStringCreationStrategy(@Nonnegative final int maxLength) {
		this.maxLength = Check.notNegative(maxLength, "maxLength");
	}

	@Override
	public String createValue(final Class<?> expectedClass) {
		final String ret = UUID.randomUUID().toString();

		if (maxLength >= 0) {
			return ret.substring(0, maxLength);
		} else {
			return ret;
		}
	}

}
