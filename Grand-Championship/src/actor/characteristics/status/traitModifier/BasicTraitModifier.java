package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.BasicTrait;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;

/**
 * An {@link ITraitModifier} for {@link BasicTrait}.
 * @author Thomas MEDARD
 *
 */
public class BasicTraitModifier implements ITraitModifier {

	/**
	 * The {@link ITrait} modified by this {@link BasicTraitModifier}.
	 */
	private final TraitType traitType;
	/**
	 * The modification value of this {@link BasicTraitModifier}. Can be negative.
	 */
	private final int value;
	
	/**
	 * The constructor.
	 * @param traitType The {@link BasicTraitModifier#traitType}.
	 * @param value The {@link BasicTraitModifier#value}.
	 */
	public BasicTraitModifier(final TraitType traitType, final int value) {
		this.traitType = traitType;
		this.value     = value;
	}
	
	/**
	 * @see actor.characteristics.status.traitModifier.ITraitModifier#getTraitType()
	 */
	@Override
	public TraitType getTraitType() {
		return traitType;
	}

	/**
	 * @see actor.characteristics.status.traitModifier.ITraitModifier#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
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

	/**
	 * @see actor.characteristics.status.traitModifier.ITraitModifier#getModifierType()
	 */
	@Override
	public ModifierType getModifierType() {
		return ModifierType.BASIC_TRAIT;
	}
}
