package objects.equipables.weapons;

import objects.equipables.IEquipable;

public interface IWeapon extends IEquipable {
	
	public static final int SMASH = 0;
	public static final String[] DAMAGE_TYPES_STR = {"smash"};

	public int damageType();
	
	public int damageValue();
}
