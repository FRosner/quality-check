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
package net.sf.qualitycheck;

import net.sf.qualitycheck.exception.IllegalNotNullArgumentException;

import org.junit.Test;

public class CheckTest_isNull {

	@Test(expected = IllegalNotNullArgumentException.class)
	public void notNull_withReference_isInvalid() {
		Check.isNull(Long.valueOf(42));
	}

	@Test(expected = IllegalNotNullArgumentException.class)
	public void notNull_withReference_withName_isInvalid() {
		Check.isNull("quality-check", "foo");
	}

	@Test
	public void notNull_withReference_withName_isValid() {
		Check.isNull(null, "foo");
	}

	@Test
	public void null_withReference_isValid() {
		Check.isNull(null);
	}

}
