/*******************************************************************************
 * Copyright 2013 André Rouél
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
package net.sf.qualitycheck.immutableobject.domain;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

import net.sf.qualitycheck.Check;

@Immutable
public final class Annotation {

	/**
	 * Representation of annotation {@link Immutable}
	 */
	public static final Annotation IMMUTABLE = Annotation.of(Immutable.class);

	/**
	 * Representation of annotation {@link Nonnegative}
	 */
	public static final Annotation NONNEGATIVE = Annotation.of(Nonnegative.class);

	/**
	 * Representation of annotation {@link Nonnull}
	 */
	public static final Annotation NONNULL = Annotation.of(Nonnull.class);

	/**
	 * Representation of annotation {@link NotThreadSafe}
	 */
	public static final Annotation NOT_THREAD_SAFE = Annotation.of(NotThreadSafe.class);

	/**
	 * Representation of annotation {@link Nullable}
	 */
	public static final Annotation NULLABLE = Annotation.of(Nullable.class);

	public static Annotation of(@Nonnull final Class<?> annotationType) {
		Check.notNull(annotationType, "annotationType");
		Check.stateIsTrue(annotationType.isAnnotation(), "requires an annotation");
		return new Annotation(Type.of(annotationType));
	}

	public static Annotation of(@Nonnull final java.lang.annotation.Annotation annotation) {
		Check.notNull(annotation, "annotation");
		return new Annotation(Type.of(annotation.annotationType()));
	}

	public static Annotation of(@Nonnull final String annotationType) {
		Check.notEmpty(annotationType, "annotationType");
		return new Annotation(new Type(annotationType));
	}

	private final Type type;

	public Annotation(final Type annotationType) {
		type = Check.notNull(annotationType, "annotationType");
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Annotation other = (Annotation) obj;
		if (!type.equals(other.type)) {
			return false;
		}
		return true;
	}

	public Type getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + type.hashCode();
		return result;
	}

	public boolean isImmutable() {
		return IMMUTABLE.equals(this);
	}

	public boolean isNonnegative() {
		return NONNEGATIVE.equals(this);
	}

	public boolean isNonnull() {
		return NONNULL.equals(this);
	}

	public boolean isNotThreadSafe() {
		return NOT_THREAD_SAFE.equals(this);
	}

	public boolean isNullable() {
		return NULLABLE.equals(this);
	}

	@Override
	public String toString() {
		return Characters.AT_SIGN + type.getName();
	}

}
