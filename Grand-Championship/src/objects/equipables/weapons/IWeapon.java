package objects.equipables.weapons;

import actor.Actor;
import gameExceptions.GameException;
import objects.equipables.IEquipable;
import utilities.Fonts;

public interface IWeapon extends IEquipable {
	
	public enum DamageType {
		SLASH("Slash"), 
		SMASH("Smash"), 
		PIERCING("Piercing");
		
		private final String name;
		
		private DamageType(final String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return Fonts.wrapHtml(this.name, Fonts.LogType.DAMAGE_PHYS);
		}
	}

	public DamageType damageType();
	
	public int getDamageValue();
	
	public String attack(final Actor target, final Boolean critical, final Actor origin) throws Exception;
	
	public int getCriticalMultiplier();
	public void setCriticalMultiplier(final int criticalMultiplier);
}
