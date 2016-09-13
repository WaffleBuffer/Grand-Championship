package objects.equipables;

import java.util.Collection;
import java.util.Observer;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.IObject;
import utilities.Fonts;

/**
 * TODO : put equip function in this class.
 * An {@link IObject} which can be equiped.
 * @author Thomas MEDARD
 */
public interface IEquipable extends IObject, Observer {
	
	/**
	 * @author Thomas MEDARD
	 */
	public enum OccupiedPlace {
		/**
		 * One hand location.
		 */
		ONE_HAND("One hand"), 
		/**
		 * Legs location.
		 */
		LEGS("Legs"),
		/**
		 * Both hands location.
		 */
		BOTH_HANDS("Both hands"),
		/**
		 * Head location.
		 */
		HEAD("Head"),
		/**
		 * Torso location.
		 */
		TORSO("Torso"),
		/**
		 * Left hand location.
		 */
		LEFT_HAND("Left hand"),
		/**
		 * Right hand location.
		 */
		RIGHT_HAND("Right hand");
		
		/**
		 * The displayed name of this {@link OccupiedPlace}'s name.
		 */
		private final String name;
		
		/**
		 * The constructor
		 * @param name The name (displayed) of the place.
		 */
		private OccupiedPlace(final String name) {
			this.name = name;
		}
		
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return Fonts.wrapHtml(this.name, Fonts.LogType.OCCUPIED_PLACE);
		}
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
	
	public void removeApplieOnEquipe(Actor target) throws GameException;
	
	public OccupiedPlace getOccupiedPlace ();
	
	// TODO : create equip(Actor).
}
