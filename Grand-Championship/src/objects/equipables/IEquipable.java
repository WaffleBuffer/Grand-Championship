package objects.equipables;

import java.util.Collection;
import java.util.Observer;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.IObject;
import utilities.Fonts;

public interface IEquipable extends IObject, Observer {
	
	public enum OccupiedPlace {
		ONE_HAND("One hand"), 
		LEGS("Legs"),
		BOTH_HANDS("Both hands"),
		HEAD("Head"),
		TORSO("Torso"),
		LEFT_HAND("Left hand"),
		RIGHT_HAND("Right hand");
		
		private final String name;
		
		private OccupiedPlace(final String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return Fonts.wrapHtml(this.name, Fonts.LogType.OCCUPIED_PLACE);
		}
	}

	public Collection<ITrait> getRequiredTraits();
	
	public String applieOnEquipe(Actor target) throws GameException;
	
	public void removeApplieOnEquipe(Actor target) throws GameException;
	
	public OccupiedPlace getOccupiedPlace ();
}
