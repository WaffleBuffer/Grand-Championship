package actor.characteristics.traitsModifier;

import actor.characteristics.traits.ITrait;

public class BasicTraitModifier implements ITraitModifier {

	private String name;
	private int    traitType;
	private int    value;
	
	public BasicTraitModifier(String name, int traitType, int value) {
		this.name      = name;
		this.traitType = traitType;
		this.value     = value;
	}
	
	@Override
	public int traitType() {
		return traitType;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public int value() {
		return value;
	}

	@Override
	public String toString() {
		return name + " : " + value + " " + ITrait.TRAITS_STR[traitType];
	}
}
