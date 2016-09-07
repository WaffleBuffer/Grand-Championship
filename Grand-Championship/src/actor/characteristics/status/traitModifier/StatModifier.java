package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.Stat;
import actor.characteristics.traits.ITrait.TraitType;

/**
 * An {@link ITraitModifier} for {@link Stat}.
 * @author Thomas MEDARD
 */
public class StatModifier implements ITraitModifier {

	/**
	 * The type of {@link ITrait} modified by this {@link StatModifier}.
	 */
	private final TraitType traitType;
	/**
	 * The modifier value.
	 */
	private final int value;
	
	/**
	 * The constructor.
	 * @param traitType {@link StatModifier#traitType}.
	 * @param value {@link StatModifier#value}.
	 */
	public StatModifier(final TraitType traitType, final int value) {
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

	/**
	 * @see actor.characteristics.status.traitModifier.ITraitModifier#getModifierType()
	 */
	@Override
	public ModifierType getModifierType() {
		return ModifierType.STAT;
	}
}
