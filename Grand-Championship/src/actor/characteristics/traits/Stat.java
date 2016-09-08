package actor.characteristics.traits;

import java.util.Observable;
import java.util.Observer;

import actor.Actor;
import utilities.Fonts;

/**
 * A {@link Stat} is an {@link ITrait} that not all {@link Actor} have,<br>
 * and/or that is not in the same magnitude than a {@link BasicTrait}.
 * @author Thomas MEDARD
 */
public abstract class Stat extends Observable implements ITrait, Observer {

	/**
	 * The name of this {@link Stat}.
	 */
	private final String name;
	/**
	 * The {@link actor.characteristics.traits.ITrait.TraitType} of this {@link Stat}.
	 */
	private final TraitType type;
	/**
	 * The modification value of this {@link Stat}.
	 */
	private int value;
	
	/**
	 * The constructor.
	 * @param name {@link Stat#name}
	 * @param type {@link Stat#type}
	 * @param value {@link Stat#value}
	 */
	public Stat (final String name, final TraitType type, final int value) {
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
		return Fonts.wrapHtml(name, Fonts.LogType.ITRAIT) + " = " + Fonts.wrapHtml(Integer.toString(value), Fonts.LogType.ITRAIT);
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public abstract void update(Observable o, Object arg);
}
