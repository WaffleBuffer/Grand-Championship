package objects.equipables.weapons;

import objects.equipables.IEquipable;

public interface IWeapon extends IEquipable {

	public int damageType();
	
	public int damageValue();
}
