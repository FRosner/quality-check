/*******************************************************************************
 * Copyright 2012 André Rouél
 * Copyright 2012 Dominik Seichter
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
package com.github.quality.check;

import javax.annotation.Nullable;

import com.github.quality.check.exception.IllegalNullArgumentException;
import com.github.quality.check.exception.IllegalRangeException;

/**
 * This class offers simple static methods to test your arguments to be valid.
 * 
 * @author André Rouél
 * @author Dominik Seichter
 */
public final class Check {

	/**
	 * Ensures that an object reference passed as a parameter to the calling method is not {@code null}.
	 * 
	 * @param reference
	 *            an object reference
	 * @return the non-null reference that was validated
	 * @throws IllegalNullArgumentException
	 *             if the given argument {@code reference} is {@code null}
	 */
	public static <T> T notNull(final @Nullable T reference) {
		if (reference == null) {
			throw new IllegalNullArgumentException();
		}
		return reference;
	}

	/**
	 * Ensures that an object reference passed as a parameter to the calling method is not {@code null}.
	 * 
	 * @param reference
	 *            an object reference
	 * @param name
	 *            name of object reference (in source code)
	 * @return the non-null reference that was validated
	 * @throws IllegalNullArgumentException
	 *             if the given argument {@code reference} is {@code null}
	 */
	public static <T> T notNull(final @Nullable T reference, final String name) {
		if (reference == null) {
			throw new IllegalNullArgumentException(name);
		}
		return reference;
	}

	/**
	 * Ensures that the given arguments are a valid range.
	 * 
	 * A range (<em>start</em>, <em>end</em>, <em>size</em>) is valid if the following conditions are true: - start <=
	 * size - end <= size - start <= end - size >= 0 - start >= 0 - end >= 0
	 * 
	 * If negative numbers are allowed in your range, you must check using { {@link #rangeNegativeOrPositive()}.
	 * 
	 * @param start
	 *            the start value of the range
	 * @param end
	 *            the end value of the range
	 * @param size
	 *            the size value of the range
	 * 
	 * 
	 * @throws IllegalRangeException
	 *             if the given arguments do not form a valid range
	 */
	public static void range(final int start, final int end, final int size) {
		final boolean rangeIsValid = (start <= size) && (end <= size) && (start <= end) && (size >= 0) && (start >= 0) && (end >= 0);

		if (!rangeIsValid) {
			throw new IllegalRangeException(start, end, size);
		}
	}

	/**
	 * <strong>Attention:</strong> This class is not intended to create objects from it.
	 */
	private Check() {
		// This class is not intended to create objects from it.
	}

}
