package utilities;

import java.nio.CharBuffer;

public abstract class StringUtilities {

	public static String filledString (final char c, final int count) {
		return CharBuffer.allocate(count).toString().replace('\0', c);
	}
}
