package actor.characteristics.traits;

import java.util.Observable;

import actor.Actor;

/**
 * The basics trait that all {@link Actor} have.
 * @author Thomas MEDARD
 */
public class BasicTrait extends Observable implements ITrait {

	/**
	 * The name of the {@link BasicTrait}.
	 */
	private final String name;
	/**
	 * The type of {@link ITrait.TraitType}.
	 */
	private final TraitType type;
	/**
	 * The value of the {@link BasicTrait}.
	 */
	private int value;
	
	/**
	 * The constructor.
	 * @param name {@link BasicTrait#name}.
	 * @param type {@link BasicTrait#type}.
	 * @param value {@link BasicTrait#value}.
	 */
	public BasicTrait (final String name, final TraitType type, final int value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	/**
	 * @see actor.characteristics.traits.ITrait#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see actor.characteristics.traits.ITrait#getTraitType()
	 */
	@Override
	public TraitType getTraitType() {
		return type;
	}

	/**
	 * @see actor.characteristics.traits.ITrait#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}
	
	/**
	 * @see actor.characteristics.traits.ITrait#setValue(int)
	 */
	@Override
	public void setValue(final int value) {
		final int lastValue = this.getValue();
		
		this.value = value;
		setChanged();
		notifyObservers(lastValue - this.getValue());
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " = " + value;
	}
}
