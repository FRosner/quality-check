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
 * Thrown to indicate that a method was passed arguments which was expected to be lesser than another object but was
 * not.
 * 
 * @author André Rouél
 * @author Dominik Seichter
 */
public class IllegalNotLesserThanException extends RuntimeException {

	private static final long serialVersionUID = 49779498587504287L;

	/**
	 * Default message to indicate that the given arguments must be lesser then another object.
	 */
	protected static final String DEFAULT_MESSAGE = "Argument must be lesser than a defined value.";

	/**
	 * Constructs an {@code IllegalNotLesserThanException} with the default message
	 * {@link IllegalNotLesserThanException#DEFAULT_MESSAGE}.
	 */
	public IllegalNotLesserThanException() {
		super(DEFAULT_MESSAGE);
	}

	/**
	 * Constructs an {@code IllegalNotLesserThanException} with a given message.
	 * 
	 * @param message
	 *            explains why the object must lesser than another object
	 */
	public IllegalNotLesserThanException(@Nonnull final String message) {
		super(message);
	}

	/**
	 * Constructs a new exception with a given message.
	 * 
	 * @param message
	 *            explains why the object must lesser than another object
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A
	 *            {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalNotLesserThanException(@Nonnull final String message, @Nullable final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new exception with the default message {@link IllegalNotLesserThanException#DEFAULT_MESSAGE}.
	 * 
	 * @param cause
	 *            the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method). (A
	 *            {@code null} value is permitted, and indicates that the cause is nonexistent or unknown.)
	 */
	public IllegalNotLesserThanException(@Nullable final Throwable cause) {
		super(DEFAULT_MESSAGE, cause);
	}

}
