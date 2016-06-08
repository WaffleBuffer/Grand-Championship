package actor.characteristics.traits;

import actor.characteristics.traits.ITrait.TraitType;

public class BasicTraitFactory {

	public static BasicTrait getBasicTrait (final TraitType type, final int value) throws Exception{
		switch (type) {
			case VITALITY :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.VITALITY, value);
			case STRENGTH :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.STRENGTH, value);
			case DEXTERITY : 
					return new BasicTrait(ITrait.getTraitName(type), TraitType.DEXTERITY, value);
			case CONSTITUTION :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.CONSTITUTION, value);
			case WILL :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.WILL, value);
			default :
				throw new Exception ("Unsupported type of BasicTrait");
		}
	}
}