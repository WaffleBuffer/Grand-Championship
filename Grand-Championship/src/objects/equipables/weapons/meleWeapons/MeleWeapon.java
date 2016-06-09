package objects.equipables.weapons.meleWeapons;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;

public class MeleWeapon implements IWeapon {
	
	private Collection<ITrait> requiredTraits;
	private String name;
	private String description;
	private int weight;
	private int value;
	private DamageType damageType;
	private int damageValue;
	private Collection<IStatus> statusApllied;
	private OccupiedPlace occupiedPlace;
	
	public MeleWeapon(Collection<ITrait> requiredTraits, String name, String description, int weight, int value,
			DamageType damageType, int damageValue, Collection<IStatus> statusApllied, OccupiedPlace occupiedPlace) {
		super();
		
		this.requiredTraits = requiredTraits;
		if (requiredTraits == null) {
			this.requiredTraits = new LinkedList<ITrait>();
		}
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.damageType = damageType;
		this.damageValue = damageValue;
		this.statusApllied = statusApllied;
		if (statusApllied == null) {
			this.statusApllied = new LinkedList<IStatus>();
		}
		this.occupiedPlace = occupiedPlace;
	}

	@Override
	public Collection<ITrait> getRequiredTraits() {
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
	public DamageType damageType() {
		return damageType;
	}

	@Override
	public int damageValue() {
		return damageValue;
	}

	@Override
	public void applieOnEquipe(Actor target) throws GameException, Exception {
		
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.addIStatus(currentStatus);
		}
		
		this.name += "(E)";
	}

	@Override
	public void removeApplieOnEquipe(Actor target) throws Exception {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
		
		this.name = this.name.substring(0, name.length() - 3);
	}

	@Override
	public String toString() {
		try {
			String weaponStr = name + " : " + description + System.lineSeparator() +
					IEquipable.getOccupiedPlaceString(occupiedPlace) + System.lineSeparator() +
					 + damageValue + " " + IWeapon.getDamageTypeString(damageType) + " damage" + System.lineSeparator() +
					weight + " Kg, " + value + " $" + System.lineSeparator();
			
			if (!requiredTraits.isEmpty()) {
				weaponStr += "required : " + requiredTraits + System.lineSeparator();
			}
			if (!statusApllied.isEmpty()) {
				weaponStr += "applies" + statusApllied + System.lineSeparator();
			}
			return  weaponStr;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public OccupiedPlace getOccupiedPlace() {
		return occupiedPlace;
	}
}
