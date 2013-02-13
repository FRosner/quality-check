package net.sf.qualitycheck.immutableobject.util;

import net.sf.qualitycheck.Check;
import net.sf.qualitycheck.immutableobject.domain.ReservedWord;

public final class ReservedWordConverter {

	public static String convertIfNecessary(final String name) {
		Check.notNull(name, "name");
		return ReservedWord.isReserved(name) ? name + 1 : name;
	}

	/**
	 * <strong>Attention:</strong> This class is not intended to create objects from it.
	 */
	private ReservedWordConverter() {
		// This class is not intended to create objects from it.
	}

}
