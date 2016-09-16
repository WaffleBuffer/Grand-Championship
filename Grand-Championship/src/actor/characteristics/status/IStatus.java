package actor.characteristics.status;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.Stat;
import gameExceptions.GameException;
import objects.equipables.weapons.IWeapon;

/**
 * An IStatus determines some {@link ITrait} modifier. This can be buff or debuff.
 * @author Thomas
 *
 */
public interface IStatus {
	
	/**
	 * The StatusType help in the different treatment needed.
	 * @author Thomas
	 *
	 */
	public enum StatusType {
		/**
		 * Status that stay applied an undetermined time (like weapons buff).
		 */
		ONE_TIME, 
		/**
		 * Status that applies its effects each turn (like poison).
		 */
		EACH_TURN, 
		/**
		 * Status that stay applied a determined time (like buff or debuff from spell).
		 */
		TEMPORARY
	}
	
	/**
	 * Used to create the right type of {@link IStatus}. Especially used when applying {@link IWeapon} on hit effects.
	 * @param status The {@link IStatus} to copy.
	 * @return The copied {@link IStatus}.
	 * @throws GameException If the {@link IStatus.StatusType} of the {@link IStatus} to copy is unknown.
	 */
	public static IStatus copy(final IStatus status) throws GameException {
			switch (status.getType()) {
			case EACH_TURN :
			case TEMPORARY :
				return new EachTurnStatus(status);
			case ONE_TIME :
				return new OneTimeStatus(status);
			default : 
				throw new GameException("Unknown status type", GameException.ExceptionType.UNKNOWN_STATUS);
		}
	}
	
	/**
	 * Get the {@link IStatus.StatusType} of the {@link IStatus}.
	 * @return The {@link IStatus.StatusType} of the {@link IStatus}.
	 */
	public StatusType getType();
	
	/**
	 * Get the {@link IStatus}'s name.
	 * @return The {@link IStatus}'s name.
	 */
	public String getName();
	
	/**
	 * Get the {@link IStatus}'s description.
	 * @return The {@link IStatus}'s description.
	 */
	public String getDescription();
	
	/**
	 * Get the {@link IStatus}'s {@link ITraitModifier} {@link Collection}.
	 * @return The {@link IStatus}'s {@link ITraitModifier} {@link Collection}.
	 */
	public Collection<ITraitModifier> getTraitModifiers();
	
	/**
	 * Apply the effects of this {@link IStatus} on an {@link Actor}.
	 * @param target The {@link Actor} to apply the effects on.
	 * @return The result's log.
	 * @throws GameException If the creation of a {@link Stat} failed.
	 */
	public String applyEffect(final Actor target) throws GameException;

	/**
	 * Remove the effects of this {@link IStatus} from an {@link Actor}
	 * @param target The {@link Actor} to remove from.
	 * @return The result's log.
	 */
	public String removeEffect(final Actor target);
	
	/**
	 * Check if this {@link IStatus} should be displayed or not (like armors effects).
	 * @return True if this {@link IStatus} should be displayed, false if not.
	 */
	public Boolean isDiplayable();
	
	/**
	 * Get the apply chances for testing resistance when applied on an {@link Actor}.
	 * @return The apply chances
	 */
	public float getApplyChances();
	
	/**
	 * Get the {@link actor.characteristics.traits.ITrait.TraitType} which is involved on testing the resistance to this {@link IStatus}.
	 * @return The {@link actor.characteristics.traits.ITrait.TraitType} corresponding to this {@link IStatus}.
	 */
	public ITrait.TraitType getResistance();
}
