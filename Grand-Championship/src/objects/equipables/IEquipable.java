package objects.equipables;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import objects.IObject;

public interface IEquipable extends IObject {
	
	public static final int ONE_HANDE  = 0;
	public static final int LEGS       = 1;
	public static final int BOTH_HANDE  = 2;
	public static final int HEAD       = 3;
	public static final int TORSO      = 4;
	public static final String[] OCCUPIED_PLACE_STR = {"One hande", "Legs", "Both handes", "Head", "Torso"};

	public Collection<ITrait> requiredTraits();
	
	public void equipe(Actor target) throws Exception;
	
	public void desequipe(Actor target) throws Exception;
	
	public int occupiedPlace ();
}
