package objects.equipables.weapons.meleWeapons;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.equipables.weapons.IWeapon;

public class MeleWeapon implements IWeapon {
	
	private Collection<ITrait> requiredTraits;
	private String name;
	private final String description;
	private final int weight;
	private final int value;
	private final DamageType damageType;
	private final int damageValue;
	private Collection<IStatus> statusAplliedOnEquip;
	private Collection<IStatus> statusAplliedOnAttack;
	private final OccupiedPlace occupiedPlace;
	private int criticalMultiplier;
	
	public MeleWeapon(final Collection<ITrait> requiredTraits, final String name, final String description, 
			final int weight, final int value, final DamageType damageType, final int damageValue, 
			final Collection<IStatus> statusApllied, final Collection<IStatus> statusAplliedOnAttack, 
			final OccupiedPlace occupiedPlace) {
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
		this.statusAplliedOnEquip = statusApllied;
		if (statusApllied == null) {
			this.statusAplliedOnEquip = new LinkedList<IStatus>();
		}
		this.statusAplliedOnAttack = statusAplliedOnAttack;
		if (statusAplliedOnAttack == null) {
			this.statusAplliedOnAttack = new LinkedList<IStatus>();
		}
		this.occupiedPlace = occupiedPlace;
		
		// By default, critical attack doubles the damages.
		this.criticalMultiplier = 2;
	}

	@Override
	public Collection<ITrait> getRequiredTraits() {
		return requiredTraits;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public DamageType damageType() {
		return damageType;
	}

	@Override
	public int getDamageValue() {
		return damageValue;
	}

	@Override
	public String applieOnEquipe(Actor target) throws GameException {
		
		Iterator<IStatus> statusIter = statusAplliedOnEquip.iterator();
		
		String log = "";
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			log += target.addIStatus(currentStatus) + System.lineSeparator();
		}
		
		this.name += "(E)";
		return log;
	}

	@Override
	public void removeApplieOnEquipe(Actor target) throws GameException {
		Iterator<IStatus> statusIter = statusAplliedOnEquip.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
		
		this.name = this.name.substring(0, name.length() - 3);
	}

	@Override
	public String toString() {
		try {
			String weaponStr = name + " : " + description + System.lineSeparator();
			
			weaponStr += occupiedPlace + " ";
			weaponStr += System.lineSeparator() +
					 + damageValue + " " + IWeapon.getDamageTypeString(damageType) + " damage" + System.lineSeparator() +
					weight + " Kg, " + value + " $" + System.lineSeparator();
			
			if (!requiredTraits.isEmpty()) {
				weaponStr += "required : " + requiredTraits + System.lineSeparator();
			}
			if (!statusAplliedOnEquip.isEmpty()) {
				weaponStr += "applies " + statusAplliedOnEquip + System.lineSeparator();
			}
			if (!statusAplliedOnAttack.isEmpty()) {
				weaponStr += "on attack " + statusAplliedOnAttack + System.lineSeparator();
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

	@Override
	public String attack(final Actor target, final Boolean critical) throws Exception {
		Iterator<IStatus> statusIter = statusAplliedOnAttack.iterator();
		
		String log = "";
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			int applyChances = currentStatus.getApplyChances();
			
			if (critical) {
				applyChances *= 1.2;
			}
			
			log += target.tryToResist(currentStatus, applyChances) + System.lineSeparator();
		}	
		
		int damageValue = this.getDamageValue();
		
		if (critical) {
			damageValue *= this.criticalMultiplier;
		}
		
		log += target.takeDamage(null, damageValue, this.damageType());
		return log;
	}

	@Override
	public void update(Observable o, Object arg) {
		final Actor actor;
		try {
			actor = (Actor) o;
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		int counter = 0;
		
		Iterator<ITrait> requiredIter = this.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
		
			ITrait currentTargetTrait = null;
			currentTargetTrait = actor.getCurrentTrait(currentRequiredTrait.getTraitType());
			
			if(currentTargetTrait != null) {
				
				if (currentTargetTrait.getValue() < currentRequiredTrait.getValue()) {
					try {
						actor.desequip(this);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					++counter;
				}
			}
		}
		
		if (counter < this.getRequiredTraits().size()) {
			try {
				actor.desequip(this);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static MeleWeapon getFists (final int damage) {
		return new MeleWeapon(
				null,
				"Fists",
				"You know whene they say : \"Do it by yourself\".",
				0,
				0,
				DamageType.SMASH,
				damage,
				null,
				null,
				OccupiedPlace.BOTH_HANDS);
	}

	@Override
	public int getCriticalMultiplier() {
		return this.criticalMultiplier;
	}

	@Override
	public void setCriticalMultiplier(int criticalMultiplier) {
		this.criticalMultiplier = criticalMultiplier;
	}
}
