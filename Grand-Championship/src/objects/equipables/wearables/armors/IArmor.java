/**
 * 
 */
package objects.equipables.wearables.armors;

import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;

/**
 * @author tmedard
 *
 */
public interface IArmor extends IEquipable{
	public enum ArmorType {
		PHYSICAL, MAGIC
	}
	
	public static String getArmorTypeString(final ArmorType type) throws Exception {
		switch (type) {
			case PHYSICAL :
				return "Physical";
			case MAGIC :
				return "Magical";
			default :
				throw new Exception("Unknown armor type");
		}
	}
	
	public static int getArmorReduction (final IWeapon.DamageType damageType, final int damageValue, 
			final ArmorType armorType, final int armorValue)
			throws Exception {
		switch (damageType) {
		case PIERCING :
			if (armorType == ArmorType.PHYSICAL) {
				return (int) (armorValue - armorValue * 0.2);
			}
			else {
				return 0;
			}
		case SLASH :
			if (armorType == ArmorType.PHYSICAL) {
				return armorValue;
			}
			else {
				return 0;
			}
		case SMASH :
			if (armorType == ArmorType.PHYSICAL) {
				return armorValue;
			}
			else {
				return 0;
			}
		default :
			throw new Exception ("Unknown armor type");
		}
	}
	
	public int getArmorValue();
	
	public ArmorType getArmorType();

}
