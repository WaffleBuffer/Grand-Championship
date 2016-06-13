package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait.TraitType;

public interface ITraitModifier {
	public enum ModifierType {
		BASIC_TRAIT, STAT
	}

	public TraitType getTraitType();
	
	public int getValue();
	
	public ModifierType getModifierType();
}
