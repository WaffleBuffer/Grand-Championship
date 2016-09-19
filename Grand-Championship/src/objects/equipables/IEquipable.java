package objects.equipables;

import java.util.Collection;
import java.util.Observer;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.IObject;
import objects.equipables.ObjectEmplacement.PlaceType;

/**
 * TODO : put equip function in this class.
 * TODO : create a OccupiedPlace class with equipped object.
 * An {@link IObject} which can be equipped.
 * @author Thomas MEDARD
 */
public interface IEquipable extends IObject, Observer {
	
	/**
	 * The type of this {@link IEquipable}
	 * @author Thomas MEDARD
	 */
	public enum EquipableType {
		/**
		 * A weapon that can attack
		 */
		WEAPON, 
		/**
		 * An armor
		 */
		ARMOR
	}

	/**
	 * Get the required {@link ITrait} to equip this.
	 * @return The required {@link ITrait} to equip this.
	 */
	public Collection<ITrait> getRequiredTraits();
	
	/**
	 * Applies all effects when equipping this on target.
	 * @param target The {@link Actor} to apply the effects.
	 * @return The result's log.
	 * @throws GameException If there is type problem.
	 */
	public String applieOnEquipe(Actor target) throws GameException;
	
	/**
	 * When desequipping this, remove all {@link IStatus} on equippe.
	 * @param target The {@link Actor} to remove from.
	 * @throws GameException If there is type problem.
	 */
	public void removeApplieOnEquipe(Actor target) throws GameException;
	
	/**
	 * Get the {@link ObjectEmplacement} of this.
	 * @return The {@link ObjectEmplacement} of this.
	 */
	public Collection<PlaceType> getObjectEmplacements ();
	
	/**
	 * Get the {@link EquipableType} of this {@link IEquipable}.
	 * @return The {@link EquipableType} of this {@link IEquipable}.
	 */
	public EquipableType getType();
}
