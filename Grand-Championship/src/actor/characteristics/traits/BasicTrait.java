package actor.characteristics.traits;

public class BasicTrait implements ITrait {

	private final String name;
	private final TraitType type;
	private int    value;
	
	public BasicTrait (final String name, final TraitType type, final int value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public TraitType getTraitType() {
		return type;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public void setValue(final int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return name + " = " + value;
	}
}
