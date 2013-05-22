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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.sf.qualitycheck.Check;
import net.sf.qualitycheck.Throws;
import net.sf.qualitytest.exception.IllegalMissingAnnotationOnMethodException;

import org.junit.Test;

public class CheckIntegrationTest {

	private static void publicMethodsAnnotated(@Nonnull final Class<?> clazz, @Nonnull final Class<? extends Annotation> annotation,
			final List<String> ignoreList) {
		final Method[] methods = clazz.getDeclaredMethods();
		for (final Method m : methods) {
			if (!ignoreList.contains(m.getName()) && Modifier.isPublic(m.getModifiers()) && !m.isAnnotationPresent(annotation)) {
				throw new IllegalMissingAnnotationOnMethodException(clazz, annotation, m);
			}
		}
	}

	@Test
	public void publicMethodsMustBeAnnotatedWithThrows() {
		final List<String> ignoreList = new ArrayList<String>();
		ignoreList.add("nothing");

		publicMethodsAnnotated(Check.class, Throws.class, ignoreList);
	}
}
