package utilities;

import actor.Actor;
import objects.IObject;

/**
 * All the fonts for each type of things.<br>
 * C_ = color
 * @author Thomas
 *
 */
public abstract class Fonts {
	/**
	 * Color for {@link Actor}
	 */
	public final static String C_ACTOR = "blue";
	/**
	 * Color for {@link IObject}
	 */
	public final static String C_OBJECT = "red";
	public final static String C_CRITICAL = "crimson";
	public final static String C_TEST = "green";
	public final static String C_STATUS = "slateblue";
	
	/**
	 * The CSS used in the display of the logs
	 */
	public final static String CSS = "<style type=\"text/css\">" +
								     "	.actor {color: " + C_ACTOR + "}" +
								     "	.object {color: " + C_OBJECT + "}" +
								     "	.critical {color: " + C_CRITICAL + "}" +
								     "	.test {color: " + C_TEST + "}" +
								     "	.status {color: " + C_STATUS + "}" +
								     "</style>";
	
	/**
	 * Identification for specific html content (such as {@link Actor} or {@link IObject}).
	 * @author Thomas
	 * @see Fonts#wrapHtml(String, LogType)
	 */
	public enum LogType {
		/**
		 * {@link Actor}
		 */
		ACTOR, 
		/**
		 * {@link Object}
		 */
		OBJECT, CRITICAL, TEST, STATUS
	}
	
	/**
	 * Wraps a giver String with html tags.
	 * @param text The String to wraps.
	 * @param type The {@link LogType} to use for html tags (like {@link LogType#ACTOR}). Can be null for default text.
	 * @return The wrapped String.
	 */
	public static String wrapHtml(final String text, final LogType type) {
		 String html = "<span class=\"";
		switch(type) {
		case ACTOR:
			html += "actor";
			break;
		case OBJECT:
			html += "object";
			break;
		case CRITICAL:
			html += "critical";
			break;
		case TEST:
			html += "test";
			break;
		case STATUS:
			html += "status";
			break;
		default:
			html += "default";
		}
		return html += "\">" + text + "</span>";
	}
}
