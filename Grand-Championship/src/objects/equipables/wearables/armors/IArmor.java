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
	
	public int getArmorReduction (final IWeapon.DamageType damageType, final int damageValue) throws Exception;
	
	public int getArmorValue();
	
	public ArmorType getArmorType();

}
