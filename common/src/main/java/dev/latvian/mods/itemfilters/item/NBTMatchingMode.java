package dev.latvian.mods.itemfilters.item;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public enum NBTMatchingMode {
	MATCH("match"),
	IGNORE("ignore"),
	CONTAIN("contain");

	public static final NBTMatchingMode[] VALUES = values();
	public static final Map<String, NBTMatchingMode> MAP = new HashMap<>();

	static {
		for (NBTMatchingMode mode : VALUES) {
			MAP.put(mode.id, mode);
		}
	}

	public final String id;

	NBTMatchingMode(String i) {
		id = i;
	}

	public static NBTMatchingMode byName(String n) {
		NBTMatchingMode m = MAP.get(n);
		return m == null ? MATCH : m;
	}
}