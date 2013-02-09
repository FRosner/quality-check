package net.sf.qualitycheck.immutableobject.domain;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import net.sf.qualitycheck.Check;

@Immutable
public final class AccessorPrefix {

	private static final String METHOD_GET_PREFIX = "get";
	private static final String METHOD_HAS_PREFIX = "has";
	private static final String METHOD_IS_PREFIX = "is";

	public static final AccessorPrefix NOT_NEEDED = new AccessorPrefix();
	public static final AccessorPrefix GET = new AccessorPrefix(METHOD_GET_PREFIX);
	public static final AccessorPrefix HAS = new AccessorPrefix(METHOD_HAS_PREFIX);
	public static final AccessorPrefix IS = new AccessorPrefix(METHOD_IS_PREFIX);

	@Nonnull
	private final String _prefix;

	private AccessorPrefix() {
		_prefix = "";
	}

	public AccessorPrefix(@Nonnull final String prefix) {
		Check.notNull(prefix, "prefix");
		_prefix = Check.notEmpty(prefix.trim(), "prefix");
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
		final AccessorPrefix other = (AccessorPrefix) obj;
		if (!_prefix.equals(other._prefix)) {
			return false;
		}
		return true;
	}

	public String getPrefix() {
		return _prefix;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _prefix.hashCode();
		return result;
	}

}
