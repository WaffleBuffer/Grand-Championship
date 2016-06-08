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
	
	public static final String[] TRAITS_STR = {"Vitality", "Strength", "Dexterity", "Constitution", "Will"};
	
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
			return value + " " + ITrait.getTraitName(traitType);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
