package actor.characteristics.traits;

public interface ITrait {
	
	public enum TraitType {
		VITALITY, 
		STRENGTH, 
		DEXTERITY, 
		CONSTITUTION, 
		WILL;
	}
	
	public static String getTraitName (final TraitType type) throws Exception{
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
			default :
				throw new Exception ("Unsupported type of BasicTrait");
		}
	}

	public String getName();
	
	public TraitType getTraitType();
	
	public int getValue();
	
	public void setValue(int value);
}
