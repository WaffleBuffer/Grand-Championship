package objects.equipables;

import java.util.Collection;
import java.util.Observer;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import objects.IObject;

public interface IEquipable extends IObject, Observer {
	
	public enum OccupiedPlace {
		ONE_HAND, LEGS, BOTH_HANDS, HEAD, TORSO, LEFT_HAND, RIGHT_HAND;
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
			case LEFT_HAND :
				return "Left hand";
			case RIGHT_HAND : 
				return "Right hand";
			default :
				throw new Exception ("Unsupported place : " + occupiedPlace);
		}
	}

	public Collection<ITrait> getRequiredTraits();
	
	public String applieOnEquipe(Actor target) throws Exception;
	
	public void removeApplieOnEquipe(Actor target) throws Exception;
	
	public OccupiedPlace getOccupiedPlace ();
}
