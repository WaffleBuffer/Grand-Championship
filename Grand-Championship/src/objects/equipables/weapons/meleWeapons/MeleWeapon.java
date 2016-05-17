package objects.equipables.weapons.meleWeapons;

import java.util.Collection;
import java.util.Iterator;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.traits.ITrait;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;

public class MeleWeapon implements IWeapon {
	
	private Collection<ITrait> requiredTraits;
	private String name;
	private String description;
	private int weight;
	private int value;
	private int damageType;
	private int damageValue;
	private Collection<IStatus> statusApllied;
	private int occupiedPlace;
	
	public MeleWeapon(Collection<ITrait> requiredTraits, String name, String description, int weight, int value,
			int damageType, int damageValue, Collection<IStatus> statusApllied, int occupiedPlace) {
		super();
		this.requiredTraits = requiredTraits;
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.damageType = damageType;
		this.damageValue = damageValue;
		this.statusApllied = statusApllied;
		this.occupiedPlace = occupiedPlace;
	}

	@Override
	public Collection<ITrait> requiredTraits() {
		return requiredTraits;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public int weight() {
		return weight;
	}

	@Override
	public int value() {
		return value;
	}

	@Override
	public int damageType() {
		return damageType;
	}

	@Override
	public int damageValue() {
		return damageValue;
	}

	@Override
	public void equipe(Actor target) throws Exception {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.addIStatus(currentStatus);
		}
	}

	@Override
	public void desequipe(Actor target) throws Exception {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
	}

	@Override
	public String toString() {
		return "MeleWeapon [requiredTraits=" + requiredTraits + ", name=" + name + ", description=" + description
				+ ", weight=" + weight + ", value=" + value + ", damageType=" + damageType + ", damageValue="
				+ damageValue + ", emplacement=" + IEquipable.OCCUPIED_PLACE_STR[occupiedPlace] + "\n" +
				"statusApllied=" + statusApllied + "]";
	}

	@Override
	public int occupiedPlace() {
		return occupiedPlace;
	}
}
