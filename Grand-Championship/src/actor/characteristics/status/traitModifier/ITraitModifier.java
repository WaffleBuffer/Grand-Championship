package actor.characteristics.status.traitModifier;

import actor.characteristics.traits.ITrait.TraitType;

/**
 * @author Thomas MEDARD
 *
 */
public interface ITraitModifier {
	/**
	 * The type of modifier.
	 * @author Thomas MEDARD
	 *
	 */
	public enum ModifierType {
		/**
		 * @see {@link BasicTraitModifier}.
		 */
		@SuppressWarnings("javadoc")
		BASIC_TRAIT, 
		/**
		 * @see {@link StatModifier}.
		 */
		@SuppressWarnings("javadoc")
		STAT
	}

	/**
	 * Get the type of trait modified.
	 * @return The {@link TraitType}.
	 */
	public TraitType getTraitType();
	
	/**
	 * Get the modified value.
	 * @return The value.
	 */
	public int getValue();
	
	/**
	 * Get the {@link ModifierType} type.
	 * @return The {@link ModifierType}
	 */
	public ModifierType getModifierType();
}
