package actor.characteristics.traits;

import gameExceptions.GameException;
import utilities.IObservable;

public interface ITrait extends IObservable {
	
	public enum TraitType {
		VITALITY, 
		STRENGTH, 
		DEXTERITY, 
		CONSTITUTION, 
		WILL,
		
		ARMOR,
		MAGICAL_PROTECTION,
		CRITICAL
	}
	
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

	public String getName();
	
	public TraitType getTraitType();
	
	public int getValue();
	
	public void setValue(final int value);
}
