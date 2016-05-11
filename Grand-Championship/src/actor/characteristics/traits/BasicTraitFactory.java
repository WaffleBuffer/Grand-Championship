package actor.characteristics.traits;

public class BasicTraitFactory {

	public static BasicTrait getBasicTrait (int type, int value) throws Exception{
		switch (type) {
			case ITrait.VITALITY :
					return new BasicTrait(ITrait.TRAITS_STR[ITrait.VITALITY], ITrait.VITALITY, value);
			case ITrait.STRENGTH :
					return new BasicTrait(ITrait.TRAITS_STR[ITrait.STRENGTH], ITrait.STRENGTH, value);
			case ITrait.DEXTERITY : 
					return new BasicTrait(ITrait.TRAITS_STR[ITrait.DEXTERITY], ITrait.DEXTERITY, value);
			case ITrait.CONSTITUTION :
					return new BasicTrait(ITrait.TRAITS_STR[ITrait.CONSTITUTION], ITrait.CONSTITUTION, value);
			case ITrait.WILL :
					return new BasicTrait(ITrait.TRAITS_STR[ITrait.WILL], ITrait.WILL, value);
			default :
				throw new Exception ("Unsupported type of BasicTrait");
		}
	}
}
