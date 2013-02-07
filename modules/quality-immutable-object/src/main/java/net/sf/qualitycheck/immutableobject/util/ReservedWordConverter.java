package net.sf.qualitycheck.immutableobject.util;

import net.sf.qualitycheck.Check;

public final class ReservedWordConverter {

	public static String convertIfNecessary(final String name) {
		Check.notNull(name, "name");
		return ReservedWord.isReserved(name) ? name + 1 : name;
	}

	private ReservedWordConverter() {
	}

}
