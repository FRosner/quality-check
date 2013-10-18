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
package net.sf.qualitycheck.exception;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Thrown to indicate that a method was passed arguments which was expected to be greater than another object but was
 * not.
 * 
 * @author André Rouél
 * @author Dominik Seichter
 */
public class IllegalNotGreaterThanException extends RuntimeException implements IllegalArgumentHolder<Object> {

	private static final long serialVersionUID = 49779498587504287L;

	/**
	 * Default message to indicate that the given arguments must be greater then another object.
	 */
	protected static final String DEFAULT_MESSAGE = "Argument must be greater than a defined value.";

	/**
	 * The illegal value which caused this exception to be thrown.
	 */
	private final Object illegalArgumentValue;

	/**
	 * Constructs an {@code IllegalNotGreaterThanException} with the default message
	 * {@link IllegalNotGreaterThanException#DEFAULT_MESSAGE}.
	 * 
	 * @param illegalArgumentValue
	 *            The illegal value which caused this exception to be thrown.
	 */
	public IllegalNotGreaterThanException(@Nullable final Object illegalArgumentValue) {
		super(DEFAULT_MESSAGE);
		this.illegalArgumentValue = illegalArgumentValue;
	}

	/**
	 * Constructs a new exception with the default message {@link IllegalNotGreaterThanException#DEFAULT_MESSAGE}.
	 * 
	 * @param illegalArgumentValue
	 *            The illegal value which caused this exception to be thrown.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A
	 *            {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalNotGreaterThanException(@Nullable final Object illegalArgumentValue, @Nullable final Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
		this.illegalArgumentValue = illegalArgumentValue;
	}

	/**
	 * Constructs an {@code IllegalNotGreaterThanException} with a given message.
	 * 
	 * @param message
	 *            explains why the object must greater than another object
	 * 
	 * @param illegalArgumentValue
	 *            The illegal value which caused this exception to be thrown.
	 */
	public IllegalNotGreaterThanException(@Nonnull final String message, @Nullable final Object illegalArgumentValue) {
		super(message);
		this.illegalArgumentValue = illegalArgumentValue;
	}

	/**
	 * Constructs a new exception with a given message.
	 * 
	 * @param message
	 *            explains why the object must greater than another object
	 * @param illegalArgumentValue
	 *            The illegal value which caused this exception to be thrown.
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A
	 *            {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalNotGreaterThanException(@Nonnull final String message, @Nullable final Object illegalArgumentValue,
			@Nullable final Throwable cause) {
		super(message, cause);
		this.illegalArgumentValue = illegalArgumentValue;
	}

	@Override
	public Object getIllegalArgument() {
		return illegalArgumentValue;
	}

}
