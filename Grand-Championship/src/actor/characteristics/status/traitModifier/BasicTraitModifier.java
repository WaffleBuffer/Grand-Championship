package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait;

public class BasicTraitModifier implements ITraitModifier {

	private int    traitType;
	private int    value;
	
	public BasicTraitModifier(int traitType, int value) {
		this.traitType = traitType;
		this.value     = value;
	}
	
	@Override
	public int traitType() {
		return traitType;
	}

	@Override
	public int value() {
		return value;
	}

	@Override
	public String toString() {
		return value + " " + ITrait.TRAITS_STR[traitType];
	}
}
