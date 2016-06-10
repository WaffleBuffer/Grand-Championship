package actor.characteristics.traits;

import java.util.Observable;

public class BasicTrait extends Observable implements ITrait {

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
		final int lastValue = this.getValue();
		
		this.value = value;
		setChanged();
		notifyObservers(lastValue - this.getValue());
	}
	
	@Override
	public String toString() {
		return name + " = " + value;
	}
}
