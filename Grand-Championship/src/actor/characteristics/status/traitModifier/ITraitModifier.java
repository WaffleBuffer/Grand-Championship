package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait.TraitType;

public interface ITraitModifier {

	public TraitType getTraitType();
	
	public int getValue();
}
