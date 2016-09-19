package utilities;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.traits.ITrait;
import objects.IObject;
import objects.equipables.ObjectEmplacement;

/**
 * All the fonts for each type of things.
 * @author Thomas MEDARD
 *
 */
public abstract class Fonts {
	/**
	 * CSS for {@link Actor}.
	 */
	public final static String CSS_ACTOR = "blue";
	/**
	 * CSS for {@link IObject}.
	 */
	private final static String CSS_OBJECT = "red";
	/**
	 * CSS for critical hit?
	 */
	private final static String CSS_CRITICAL = "rgb(204,0,0)";
	/**
	 * CSS for test (chances).
	 */
	private final static String CSS_TEST = "green";
	/**
	 * CSS for {@link IStatus}.
	 */
	private final static String CSS_STATUS = "rgb(0,128,255)";
	/**
	 * CSS for physical damages.
	 */
	private final static String CSS_DAMAGE_PHYS = "color: rgb(255,21,21); font: bold;";
	/**
	 * CSS for physical damages absorption.
	 */
	private final static String CSS_ABSORBTION_PHYS = "color: rgb(255,21,21);";
	/**
	 * CSS for magical damages absorption.
	 */
	private final static String CSS_ABSORBTION_MAG = "color: rgb(0, 204, 204);";
	/**
	 * CSS for {@link ITrait}.
	 */
	private final static String CSS_ITRAIT = "color: rgb(255, 153, 153);";
	/**
	 * CSS for {@link ObjectEmplacement}.
	 */
	private final static String CSS_OBJECT_EMPLACEMENT = "color: rgb(204, 0, 204);";
	/**
	 * CSS for money and value (in gold).
	 */
	private final static String CSS_MONEY = "color: rgb(204, 204, 0)";
	
	/**
	 * The CSS used in the display of the logs
	 */
	public final static String CSS = "<style type=\"text/css\">" +
								     "	.actor {color: " + CSS_ACTOR + ";}" +
								     "	.object {color: " + CSS_OBJECT + ";}" +
								     "	.critical {color: " + CSS_CRITICAL + ";}" +
								     "	.test {color: " + CSS_TEST + ";}" +
								     "	.status {color: " + CSS_STATUS + ";}" +
								     "	.damageValuePhys {" + CSS_DAMAGE_PHYS + "}" +
								     "	.absorbtionPhys {" + CSS_ABSORBTION_PHYS + "}" +
								     "	.absorbtionMag {" + CSS_ABSORBTION_MAG + "}" +
								     "	.iTrait {" + CSS_ITRAIT + "}" +
								     "	.occupiedPlace {" + CSS_OBJECT_EMPLACEMENT + "}" + 
								     "	.money {" + CSS_MONEY + "}" +
								     "</style>";
	
	/**
	 * Identification for specific HTML content (such as {@link Actor} or {@link IObject}).
	 * @author Thomas MEDARD
	 * @see Fonts#wrapHtml(String, LogType)
	 */
	public enum LogType {
		/**
		 * {@link Actor}.
		 */
		ACTOR, 
		/**
		 * {@link Object}.
		 */
		OBJECT, 
		/**
		 * Critical hit.
		 */
		CRITICAL, 
		/**
		 * Tests.
		 */
		TEST, 
		/**
		 * {@link IStatus}.
		 */
		STATUS,
		/**
		 * Physical damages.
		 */
		DAMAGE_PHYS, 
		/**
		 * Physical damages absorption.
		 */
		ABSORBTION_PHYS,
		/**
		 * Magical damages absorption.
		 */
		ABSORBTION_MAG, 
		/**
		 * {@link ITrait}.
		 */
		ITRAIT, 
		/**
		 * {@link ObjectEmplacement}.
		 */
		OBJECT_EMPLACEMENT,
		/**
		 * Money and value (in gold).
		 */
		MONEY
	}
	
	/**
	 * Wraps a giver String with HTML tags.
	 * @param text The String to wraps.
	 * @param type The {@link LogType} to use for HTML tags (like {@link LogType#ACTOR}). Can be null for default text.
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
		case DAMAGE_PHYS:
			html += "damageValuePhys";
			break;
		case ABSORBTION_PHYS:
			html += "absorbtionPhys";
			break;
		case ITRAIT:
			html += "iTrait";
			break;
		case OBJECT_EMPLACEMENT:
			html += "occupiedPlace";
			break;
		case ABSORBTION_MAG:
			html += "absorbtionMag";
			break;
		case MONEY:
			html += "money";
			break;
		default:
			html += "default";
		}
		return html += "\">" + text + "</span>";
	}
}
