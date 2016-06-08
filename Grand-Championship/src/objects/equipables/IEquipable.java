package objects.equipables;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import objects.IObject;

public interface IEquipable extends IObject {
	
	public enum OccupiedPlace {
		ONE_HAND, LEGS, BOTH_HANDS, HEAD, TORSO
	}
	
	public static String getOccupiedPlaceString (final OccupiedPlace occupiedPlace) throws Exception {
		switch (occupiedPlace) {
		case ONE_HAND :
			return "One hand";
		case LEGS :
			return "Legs";
		case BOTH_HANDS :
			return "Both Hands";
		case HEAD :
			return "Head";
		case TORSO :
			return "Torso";
		default :
			throw new Exception ("Unsupported place");
		}
	}

	public Collection<ITrait> requiredTraits();
	
	public void equipe(Actor target) throws Exception;
	
	public void desequipe(Actor target) throws Exception;
	
	public OccupiedPlace occupiedPlace ();
}
