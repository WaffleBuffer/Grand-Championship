/**
 * 
 */
package objects.equipables.wearables.armors;

import gameExceptions.GameException;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.IWeapon.DamageType;

/**
 * Description of any armor
 * @author tmedard
 *
 */
public interface IArmor extends IEquipable{
	/**
	 * All the armor types possibles.<br>
	 * - {@link ArmorType#PHYSICAL}
	 * - {@link ArmorType#MAGIC}
	 * @author tmedard
	 */
	public enum ArmorType {
		/**
		 * Resist to :
		 *  - {@link DamageType#PIERCING} (less efficient);
		 *  - {@link DamageType#SLASH};
		 *  - {@link DamageType#SMASH}.
		 *  
		 *  @see DamageType
		 */
		PHYSICAL {
			@Override
			public String toString(){
                return "Physical";
            }
			
			@Override
			public int getArmorReduction (final IWeapon.DamageType damageType, final int damageValue, 
					final int armorValue)
					throws GameException {
				switch (damageType) {
				case PIERCING :
					return (int) (armorValue - armorValue * 0.2);
				case SLASH :
					return armorValue;
				case SMASH :
					return armorValue;
				default :
					throw new GameException ("Unknown armor type", GameException.ExceptionType.UNKNOWN_ARMORTYPE);
				}
			}
		},
		/**
		 * Resisti to :
		 * - Nothing for now since there is no magic yet.
		 */
		MAGIC {
			@Override
			public String toString(){
                return "Magical";
            }
			
			@Override
			public int getArmorReduction (final IWeapon.DamageType damageType, final int damageValue, 
					final int armorValue) throws GameException {
				switch (damageType) {
				case PIERCING :
					return 0;
				case SLASH :
					return 0;
				case SMASH :
					return 0;
				default :
					throw new GameException ("Unknown armor type", GameException.ExceptionType.UNKNOWN_ARMORTYPE);
				}
			}
		};
		
		/**
		 * Calculate the armor reduction depending on the damage type and the armor type
		 * @param damageType The {@link DamageType}
		 * @param damageValue The damage value
		 * @param armorValue The armor value
		 * @return The armor reduction
		 * @throws GameException When unknown armor type
		 */
		public abstract int getArmorReduction (final IWeapon.DamageType damageType, final int damageValue, 
				final int armorValue)
				throws GameException;
	}
	
	/**
	 * Get the armor value
	 * @return The armor value
	 */
	public int getArmorValue();
	
	/**
	 * Get the {@link ArmorType}
	 * @return the {@link ArmorType}
	 * @see ArmorType
	 */
	public ArmorType getArmorType();

}
