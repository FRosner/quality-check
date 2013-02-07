package net.sf.qualitycheck.immutableobject.domain;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.sf.qualitycheck.Check;

@Immutable
public final class Annotation implements Characters {

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

	final Type _type;

	public Annotation(final Type annotationType) {
		_type = Check.notNull(annotationType, "annotationType");
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
		if (!_type.equals(other._type)) {
			return false;
		}
		return true;
	}

	public Type getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _type.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return Characters.AT_SIGN + _type.getTypeName();
	}

}
