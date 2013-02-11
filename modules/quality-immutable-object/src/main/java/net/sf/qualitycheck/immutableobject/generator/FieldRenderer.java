package net.sf.qualitycheck.immutableobject.generator;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.sf.qualitycheck.Check;
import net.sf.qualitycheck.immutableobject.domain.CollectionVariant;
import net.sf.qualitycheck.immutableobject.domain.Field;
import net.sf.qualitycheck.immutableobject.domain.ImmutableSettings;
import net.sf.qualitycheck.immutableobject.domain.Type;

import org.stringtemplate.v4.AttributeRenderer;

final class FieldRenderer implements AttributeRenderer {

	public enum Option {
		COPY, COPY_FROM_INTERFACE, IMMUTABLE;
		@Nullable
		public static Option evaluate(@Nonnull final String option) {
			Check.notNull(option, "option");
			return valueOf(option.trim().toUpperCase());
		}
	}

	private static final String CHECK_NONNEGATIVE = "Check.notNegative(%s, \"%s\")";

	private static final String CHECK_NONNULL = "Check.notNull(%s, \"%s\")";

	@Nonnull
	public static final String determineGeneric(@Nonnull final Type type) {
		return type.getGenericDeclaration().isUndefined() ? "" : "<" + type.getGenericDeclaration().getDeclaration() + ">";
	}

	@Nonnull
	static String surroundWithCheck(@Nonnull final Field field, @Nonnull final String referenceAccess) {
		Check.notEmpty(referenceAccess, "referenceAccess");
		String result = referenceAccess;
		if (field.isNonnegative()) {
			result = String.format(CHECK_NONNEGATIVE, result, result);
		} else if (field.isNonnull()) {
			result = String.format(CHECK_NONNULL, result, result);
		}
		return result;
	}

	@Nonnull
	private final ImmutableSettings _settings;

	public FieldRenderer(@Nonnull final ImmutableSettings settings) {
		_settings = Check.notNull(settings, "settings");
	}

	@Nonnull
	String copyCollection(@Nonnull final Field field, @Nonnull final String currentResult) {
		String result = currentResult;
		final CollectionVariant variant = CollectionVariant.evaluate(field.getType());
		if (variant != null) {
			if (_settings.hasGuava()) {
				result = String.format(variant.getGuavaCopy(), result);
			} else {
				final String generic = determineGeneric(field.getType());
				result = String.format(variant.getDefaultCopy(), generic, result);
			}
		}
		return result;
	}

	@Nonnull
	String makeCollectionImmutable(@Nonnull final Field field, @Nonnull final String currentResult) {
		String result = currentResult;
		final CollectionVariant variant = CollectionVariant.evaluate(field.getType());
		if (variant != null) {
			if (_settings.hasGuava()) {
				result = String.format(variant.getGuavaImmutable(), result);
			} else {
				final String generic = determineGeneric(field.getType());
				result = String.format(variant.getDefaultImmutable(), generic, result);
			}
		}
		return result;
	}

	@Nonnull
	@Override
	public String toString(final Object o, final String formatOption, final Locale locale) {
		// o will be instanceof CollectionVariant
		final Field field = (Field) o;
		String result = field.getName();
		final Option option = formatOption != null ? Option.evaluate(formatOption) : null;

		if (Option.COPY_FROM_INTERFACE == option) {
			result = _settings.getInterfaceDeclaration().getType().getName().toLowerCase() + "." + field.getAccessorMethodName();
		}

		if (_settings.hasQualityCheck()) {
			result = surroundWithCheck(field, result);
		}

		if (option == Option.IMMUTABLE) {
			result = makeCollectionImmutable(field, result);
		} else if (option == Option.COPY || option == Option.COPY_FROM_INTERFACE) {
			result = copyCollection(field, result);
		}

		return result;
	}

}
