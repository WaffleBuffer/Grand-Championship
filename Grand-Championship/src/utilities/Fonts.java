package utilities;

import actor.Actor;
import objects.IObject;
import objects.equipables.IEquipable.OccupiedPlace;

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
	//TODO : make previous one similar to DAMAGE_PHYS
	private final static String C_OBJECT = "red";
	private final static String C_CRITICAL = "rgb(204,0,0)";
	private final static String C_TEST = "green";
	private final static String C_STATUS = "rgb(0,128,255)";
	private final static String DAMAGE_PHYS = "color: rgb(255,21,21); font: bold;";
	private final static String ABSORBTION_PHYS = "color: rgb(255,21,21);";
	private final static String ABSORBTION_MAG = "color: rgb(0, 204, 204);";
	private final static String ITRAIT = "color: rgb(255, 153, 153);";
	private final static String OCCUPIED_PLACE = "color: rgb(204, 0, 204);";
	private final static String MONEY = "color: rgb(204, 204, 0)";
	
	/**
	 * The CSS used in the display of the logs
	 */
	public final static String CSS = "<style type=\"text/css\">" +
								     "	.actor {color: " + C_ACTOR + ";}" +
								     "	.object {color: " + C_OBJECT + ";}" +
								     "	.critical {color: " + C_CRITICAL + ";}" +
								     "	.test {color: " + C_TEST + ";}" +
								     "	.status {color: " + C_STATUS + ";}" +
								     "	.damageValuePhys {" + DAMAGE_PHYS + "}" +
								     "	.absorbtionPhys {" + ABSORBTION_PHYS + "}" +
								     "	.absorbtionMag {" + ABSORBTION_MAG + "}" +
								     "	.iTrait {" + ITRAIT + "}" +
								     "	.occupiedPlace {" + OCCUPIED_PLACE + "}" + 
								     "	.money {" + MONEY + "}" +
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
		OBJECT, CRITICAL, TEST, STATUS, DAMAGE_PHYS, ABSORBTION_PHYS, ABSORBTION_MAG, ITRAIT, OCCUPIED_PLACE,
		MONEY
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
		case DAMAGE_PHYS:
			html += "damageValuePhys";
			break;
		case ABSORBTION_PHYS:
			html += "absorbtionPhys";
			break;
		case ITRAIT:
			html += "iTrait";
			break;
		case OCCUPIED_PLACE:
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
