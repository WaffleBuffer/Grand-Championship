package actor.characteristics.traits;

import actor.Actor;
import gameExceptions.GameException;
import utilities.IObservable;

/**
 * A trait of an {@link Actor}.
 * @author Thomas MEDARD
 */
public interface ITrait extends IObservable {
	
	/**
	 * The type of The {@code ITrait}.
	 * @author Thomas MEDARD
	 */
	public enum TraitType {
		// BasicTrait
		/**
		 * Life, PV
		 */
		VITALITY, 
		/**
		 * Strength, fist fight
		 */
		STRENGTH, 
		/**
		 * Dexterity, speed, agility
		 */
		DEXTERITY, 
		/**
		 * Resistance to physics debuff
		 */
		CONSTITUTION, 
		/**
		 * Resistance to magics debuff, MP
		 */
		WILL,
		
		// Stats
		/**
		 * Physical protection
		 */
		ARMOR,
		/**
		 * Magic protection
		 */
		MAGICAL_PROTECTION,
		/**
		 * Critical chances
		 */
		CRITICAL
	}
	
	/**
	 * Get the name of a {@link TraitType} in String format.
	 * @param type {@link TraitType}.
	 * @return The String corresponding to the name of the {@link TraitType}.
	 * @throws GameException If the {@link TraitType} is unsupported.
	 */
	public static String getTraitName (final TraitType type) throws GameException{
		switch (type) {
			case VITALITY :
				return "Vitality";
			case STRENGTH :
				return "Strength";
			case DEXTERITY : 
				return "Dexterity";
			case CONSTITUTION :
				return "Constitution";
			case WILL :
				return "Will";
			case ARMOR :
				return "Armor";
			case MAGICAL_PROTECTION :
				return "Magical protection";
			case CRITICAL :
				return "Critical chance";
			default :
				throw new GameException ("Unsupported type of BasicTrait", GameException.ExceptionType.UNKNOWN_BASICTRAIT);
		}
	}

	/**
	 * Get the name of the {@link ITrait}.
	 * @return The name of the {@link ITrait}.
	 */
	public String getName();
	
	/**
	 * Get the {@link TraitType} of the {@link ITrait}.
	 * @return The {@link TraitType} of the {@link ITrait}.
	 */
	public TraitType getTraitType();
	
	/**
	 * Get the value of the {@link ITrait}.
	 * @return The value of the {@link ITrait}.
	 */
	public int getValue();
	
	/**
	 * Set the value of the {@link ITrait}.
	 * @param value The value of the {@link ITrait}.
	 */
	public void setValue(final int value);
}
