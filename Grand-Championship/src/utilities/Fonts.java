package utilities;

/**
 * All the fonts for each type of things.<br>
 * C = color
 * @author Thomas
 *
 */
public abstract class Fonts {
	public final static String C_ACTOR = "blue";
	public final static String C_OBJECT = "red";
	
	public final static String CSS = "<style type=\"text/css\">" +
								     "	.actor {color: " + C_ACTOR + "}" +
								     "	.object {color: " + C_OBJECT + "}" +
								     "</style>";
}
