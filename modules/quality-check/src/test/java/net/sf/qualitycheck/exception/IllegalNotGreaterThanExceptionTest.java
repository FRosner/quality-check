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

import org.junit.Assert;
import org.junit.Test;

public class IllegalNotGreaterThanExceptionTest {

	@Test
	public void construct_withArgs_successful() {
		new IllegalNotGreaterThanException("a^2 + b^2 = c^2", 2);
	}

	@Test
	public void construct_withArgsAndNullCause() {
		new IllegalNotGreaterThanException("a^2 + b^2 = c^2", 2, (Throwable) null);
	}

	@Test
	public void construct_withFilledArgsAndNullCause() {
		final IllegalNotGreaterThanException e = new IllegalNotGreaterThanException("a != b", 2, (Throwable) null);
		Assert.assertEquals("a != b", e.getMessage());
	}

	@Test
	public void construct_withFilledCause() {
		new IllegalNotGreaterThanException(2, new NumberFormatException());
	}

	@Test
	public void construct_withNullCause() {
		new IllegalNotGreaterThanException((Object) null, (Throwable) null);
	}

	@Test
	public void construct_withoutArgs_successful() {
		new IllegalNotGreaterThanException(2);
	}

	@Test
	public void message_without_template() {
		final IllegalNotGreaterThanException e = new IllegalNotGreaterThanException(2);
		Assert.assertEquals("Argument must be greater than a defined value.", e.getMessage());
	}

	@Test
	public void testGetIllegalArgument() {
		final IllegalArgumentHolder<Object> iah = new IllegalNotGreaterThanException(-2);
		Assert.assertEquals(Integer.valueOf(-2), iah.getIllegalArgument());
	}
}
