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

import net.sf.qualitycheck.exception.IllegalStateOfArgumentException;

import org.junit.Assert;
import org.junit.Test;

public class RandomEnumCreationStrategyTest {

	enum EnumWithoutValues {
	}

	enum EnumWithValues {
		A, B, C
	}

	@Test
	public void createValue_argIsAnEmptyEnum() {
		Assert.assertNull(new RandomEnumCreationStrategy().createValue(EnumWithoutValues.class));
	}

	@Test
	public void createValue_argIsAnEnum() {
		Assert.assertNotNull(new RandomEnumCreationStrategy().createValue(EnumWithValues.class));
	}

	@Test(expected = IllegalStateOfArgumentException.class)
	public void createValue_argIsNotAnEnum() {
		new RandomEnumCreationStrategy().createValue(Character.class);
	}

}