package net.sf.qualitycheck.immutableobject.domain;

import java.util.List;

import javax.annotation.Nonnull;

import net.sf.qualitycheck.Check;

public final class Field implements Characters {

	/**
	 * Represents an undefined default value of a field
	 */
	public static final String WITHOUT_VALUE = "";

	@Nonnull
	private final List<Annotation> _annotations;

	@Nonnull
	private final String _content;

	@Nonnull
	private final Final _final;

	@Nonnull
	private final String _name;

	@Nonnull
	private final Static _static;

	@Nonnull
	private final Type _type;

	@Nonnull
	private final Visibility _visibility;

	public Field(@Nonnull final String name, @Nonnull final Type type, @Nonnull final Visibility visibility,
			@Nonnull final Final finalModifier, final Static staticModifier, @Nonnull final List<Annotation> annotations,
			@Nonnull final String content) {
		_name = Check.notNull(name, "name");
		_type = Check.notNull(type, "type");
		_visibility = Check.notNull(visibility, "visibility");
		_final = Check.notNull(finalModifier, "finalModifier");
		_static = Check.notNull(staticModifier, "staticModifier");
		_annotations = Check.notNull(annotations, "annotations");
		_content = Check.notNull(content, "content");
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
		final Field other = (Field) obj;
		if (!_annotations.equals(other._annotations)) {
			return false;
		}
		if (_final != other._final) {
			return false;
		}
		if (!_name.equals(other._name)) {
			return false;
		}
		if (!_type.equals(other._type)) {
			return false;
		}
		if (_visibility != other._visibility) {
			return false;
		}
		return true;
	}

	@Nonnull
	public List<Annotation> getAnnotations() {
		return _annotations;
	}

	@Nonnull
	public Final getFinal() {
		return _final;
	}

	@Nonnull
	public String getName() {
		return _name;
	}

	public Static getStatic() {
		return _static;
	}

	@Nonnull
	public Type getType() {
		return _type;
	}

	@Nonnull
	public Visibility getVisibility() {
		return _visibility;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _annotations.hashCode();
		result = prime * result + _final.hashCode();
		result = prime * result + _name.hashCode();
		result = prime * result + _type.hashCode();
		result = prime * result + _visibility.hashCode();
		return result;
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder();
		if (!_annotations.isEmpty()) {
			for (final Annotation annotation : _annotations) {
				b.append(annotation.toString());
				b.append(NEWLINE);
			}
		}
		if (_visibility != Visibility.UNDEFINED) {
			b.append(_visibility.getName());
			b.append(SPACE);
		}
		if (_static != Static.UNDEFINED) {
			b.append(_static.getName());
			b.append(SPACE);
		}
		if (_final != Final.UNDEFINED) {
			b.append(_final.getName());
			b.append(SPACE);
		}
		b.append(_type.getTypeName());
		if (_type.getGenericDeclaration() != GenericDeclaration.UNDEFINED) {
			b.append(BRACKET_OPEN);
			b.append(_type.getGenericDeclaration());
			b.append(BRACKET_CLOSE);
		}
		b.append(SPACE);
		b.append(_name);
		if (!_content.isEmpty()) {
			b.append(EQUALS_SIGN);
			b.append(_content);
		}
		b.append(SEMICOLON);
		return b.toString();
	}

}
