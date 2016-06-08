package objects.equipables.weapons;

import objects.equipables.IEquipable;

public interface IWeapon extends IEquipable {
	
	public enum DamageType {
		SLASH, SMASH, PIERCING
	}
	
	public static String getDamageTypeString(final DamageType damageType) throws Exception {
		switch (damageType) {
			case SLASH :
				return "Slash";
			case SMASH :
				return "Smash";
			case PIERCING :
				return "Piercing";
			default :
				throw new Exception("Unsupported damage type");
		}
	}

	public DamageType damageType();
	
	public int damageValue();
}
