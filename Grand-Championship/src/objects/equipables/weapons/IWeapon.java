package objects.equipables.weapons;

import actor.Actor;
import gameExceptions.GameException;
import objects.equipables.IEquipable;
import utilities.Fonts;

/**
 * A weapon that can attack.
 * @author Thomas MEDARD
 */
public interface IWeapon extends IEquipable {
	
	/**
	 * The type of damage of this.
	 * @author Thomas MEDARD
	 */
	public enum DamageType {
		/**
		 * Slash damage.
		 */
		SLASH("Slash"), 
		/**
		 * Smash damage
		 */
		SMASH("Smash"), 
		/**
		 * Piercing damage. Has a bonus against armors.
		 */
		PIERCING("Piercing");
		
		/**
		 * The displayed name of this.
		 */
		private final String name;
		
		/**
		 * The constructor.
		 * @param name The displayed name of this.
		 */
		private DamageType(final String name) {
			this.name = name;
		}
		
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return Fonts.wrapHtml(this.name, Fonts.LogType.DAMAGE_PHYS);
		}
	}

	/**
	 * Get the {@link DamageType} of this.
	 * @return The {@link DamageType} of this.
	 */
	public DamageType getDamageType();
	
	/**
	 * Get the raw damage value.
	 * @return The raw damage value.
	 */
	public int getDamageValue();
	
	/**
	 * Attack an {@link Actor}.
	 * @param target the targeted {@link Actor}.
	 * @param critical If the attack is critical.
	 * @param origin The {@link Actor} who attack.
	 * @return The result's log.
	 * @throws GameException If there is a type problem.
	 */
	public String attack(final Actor target, final Boolean critical, final Actor origin) throws GameException;
	
	/**
	 * Get the damage multiplier when there is a critical attack.
	 * @return The damage multiplier.
	 */
	public int getCriticalMultiplier();
	
	/**
	 * Set the damage multiplier when there is a critical attack.
	 * @param criticalMultiplier The damage multiplier.
	 */
	public void setCriticalMultiplier(final int criticalMultiplier);
}
