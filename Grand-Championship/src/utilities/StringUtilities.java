package utilities;

import java.nio.CharBuffer;

/**
 * Some utlitys function for operations on {@link String}
 * @author tmedard
 *
 */
public abstract class StringUtilities {

	/**
	 * Create a {@link String} filled with the {@code char c}, {@code count} times 
	 * @param c The {@code char} wich will fill the {@link String}
	 * @param count The number of {@code char c} that will be in the {@link String}
	 * @return The filled {@link String}
	 */
	public static String filledString (final char c, final int count) {
		return CharBuffer.allocate(count).toString().replace('\0', c);
	}
}
