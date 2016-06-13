package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;

public class StatModifier implements ITraitModifier {

	private final TraitType traitType;
	private final int value;
	
	public StatModifier(final TraitType traitType, final int value) {
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
		String traitModifierString = "";
		if (value > 0) {
			traitModifierString += "+";
		}
		
		try {
			return traitModifierString += value + " " + ITrait.getTraitName(traitType);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
