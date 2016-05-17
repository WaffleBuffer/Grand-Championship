package objects.equipables;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import objects.IObject;

public interface IEquipable extends IObject {
	
	public static final int LEFT_HAND  = 0;
	public static final int RIGHT_HAND = 1;
	public static final int BOTH_HAND  = 2;
	public static final int HEAD       = 3;
	public static final int TORSO      = 4;
	public static final int LEGS       = 5;
	public static final String[] OCCUPIED_PLACE_STR = {"Left hand", "Right hand", "Both hands", "Head", "Torso", "Legs"};

	public Collection<ITrait> requiredTraits();
	
	public void equipe(Actor target) throws Exception;
	
	public void desequip(Actor target) throws Exception;
}
