package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;

public class BasicTraitModifier implements ITraitModifier {

	private final TraitType traitType;
	private final int value;
	
	public BasicTraitModifier(final TraitType traitType, final int value) {
		this.traitType = traitType;
		this.value     = value;
	}
	
	@Override
	public TraitType getTraitType() {
		return traitType;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		try {
			String traitModifierString = String.format("%+d " + ITrait.getTraitName(traitType), value);
			return traitModifierString;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ModifierType getModifierType() {
		return ModifierType.BASIC_TRAIT;
	}
}
