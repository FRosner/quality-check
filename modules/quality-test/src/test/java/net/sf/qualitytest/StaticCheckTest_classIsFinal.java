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
package net.sf.qualitytest;

import net.sf.qualitytest.exception.IllegalNonFinalClassException;

import org.junit.Test;

public class StaticCheckTest_classIsFinal {

	public static final class FinalClass {
		private final int a;

		public FinalClass(final int a) {
			this.a = a;
		}

		public int getA() {
			return a;
		}
	}

	public static class NonFinalClass {
		private final int a;

		public NonFinalClass(final int a) {
			this.a = a;
		}

		public int getA() {
			return a;
		}
	}

	@Test
	public void testDetectFinalClass() {
		StaticCheck.classIsFinal(FinalClass.class);
	}

	@Test(expected = IllegalNonFinalClassException.class)
	public void testDetectNonFinalClass() {
		StaticCheck.classIsFinal(NonFinalClass.class);
	}

}
