package objects.equipables.weapons;

import actor.Actor;
import gameExceptions.GameException;
import objects.equipables.IEquipable;

public interface IWeapon extends IEquipable {
	
	public enum DamageType {
		SLASH, SMASH, PIERCING
	}
	
	public static String getDamageTypeString(final DamageType damageType) throws GameException {
		switch (damageType) {
			case SLASH :
				return "Slash";
			case SMASH :
				return "Smash";
			case PIERCING :
				return "Piercing";
			default :
				throw new GameException("Unsupported damage type", GameException.ExceptionType.UNKNOWN_DAMAGETYPE);
		}
	}

	public DamageType damageType();
	
	public int getDamageValue();
	
	public String attack(final Actor target, final Boolean critical) throws Exception;
}
