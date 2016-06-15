package actor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.traits.BasicTrait;
import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.Stat;
import actor.characteristics.traits.StatFactory;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;
import objects.IObject;
import objects.equipables.IEquipable;
import objects.equipables.IEquipable.OccupiedPlace;
import objects.equipables.weapons.IWeapon;
import objects.equipables.wearables.armors.IArmor;

/**
 * A basic character
 * @author tmedard
 *
 */
public class Actor extends Observable{
	
	public static final int DEFAULT_TRAIT_VALUE = 5;

	private final String name;
	
	private final Set<ITrait> basicCharacteristics;
	private final Set<ITrait> currentCharacteristics;
	
	private final Set<Stat> stats;
	
	private final Collection<OneTimeStatus> oneTimestatus;
	private final Collection<EachTurnStatus> eachTurnStatus;
	
	private final Map<IEquipable.OccupiedPlace, IEquipable> equipedObjects;
	
	private final int maxWeight;
	private int currentWeight;
	private final Collection<IObject> inventory;
	
	public Actor (String name) throws Exception {
		super();
		
		this.name = name;
		this.basicCharacteristics = new HashSet<ITrait>();
		this.stats = new HashSet<Stat>();
		this.oneTimestatus = new LinkedList<OneTimeStatus>();
		this.eachTurnStatus = new LinkedList<EachTurnStatus>();
		this.maxWeight = 100;
		this.currentWeight = 0;
		this.inventory = new LinkedList<IObject>();
		
		this.equipedObjects = new HashMap<IEquipable.OccupiedPlace, IEquipable>();
		equipedObjects.put(OccupiedPlace.BOTH_HANDS, null);
		equipedObjects.put(OccupiedPlace.HEAD, null);
		equipedObjects.put(OccupiedPlace.LEGS, null);
		equipedObjects.put(OccupiedPlace.LEFT_HAND, null);
		equipedObjects.put(OccupiedPlace.RIGHT_HAND, null);
		equipedObjects.put(OccupiedPlace.TORSO, null);
		
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.VITALITY, 200));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.STRENGTH, DEFAULT_TRAIT_VALUE));
		BasicTrait dexterity = BasicTraitFactory.getBasicTrait(ITrait.TraitType.DEXTERITY, DEFAULT_TRAIT_VALUE);
		basicCharacteristics.add(dexterity);
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.WILL, DEFAULT_TRAIT_VALUE));
		
		currentCharacteristics = new HashSet<ITrait>();
		currentCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.VITALITY, 200));
		currentCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.STRENGTH, DEFAULT_TRAIT_VALUE));
		dexterity = BasicTraitFactory.getBasicTrait(ITrait.TraitType.DEXTERITY, DEFAULT_TRAIT_VALUE);
		currentCharacteristics.add(dexterity);
		currentCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		currentCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.WILL, DEFAULT_TRAIT_VALUE));
		
		Observable [] dependencys = new Observable[1];
		dependencys[0] = dexterity;
		final Stat critical = StatFactory.createState(ITrait.TraitType.CRITICAL, (10 + 2 * dexterity.getValue()), dependencys);
		stats.add(critical);
		final Stat armor = StatFactory.createState(ITrait.TraitType.ARMOR, 0, null);
		stats.add(armor);
	}
	
	public String addIStatus(IStatus status) throws Exception {
		
		switch (status.type()) {
		case ONE_TIME :
			this.oneTimestatus.add((OneTimeStatus) status);
			return (status.applyEffect(this));
		case EACH_TURN :
			this.eachTurnStatus.add((EachTurnStatus) status);
			break;
		default :
			throw new Exception("Unknown status type");
		}
		
		return "";
		/*Iterator<ITraitModifier> traitModifierIter = status.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = this.currentCharacteristics.iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.getTraitType() == currentModifiedTrait.getTraitType()) {
					
					currentActorTrait.setValue(currentActorTrait.getValue() + currentModifiedTrait.getValue());
					return;
				}
			}
		}*/
	}
	
	public String removeStatus (IStatus status) throws Exception {
		
		switch (status.type()) {
		case ONE_TIME :
			this.oneTimestatus.remove((OneTimeStatus) status);
			return ((OneTimeStatus) status).removeEffect(this);
		case EACH_TURN :
			this.eachTurnStatus.remove((EachTurnStatus) status);
			break;
		default :
			throw new Exception("Unknown status type");
		}	
		
		return "";
	}
	
	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		String actorString = "=============== " + name + " ===============" + System.lineSeparator() +
		getCurrentTrait(TraitType.VITALITY) + "/" + getBasicTrait(TraitType.VITALITY).getValue() + ", ";
		
		Iterator<ITrait> traitIter = currentCharacteristics.iterator();

		while(traitIter.hasNext()) {
			final ITrait currentTrait = traitIter.next();
			
			if (currentTrait.getTraitType() == TraitType.VITALITY) {
				continue;
			}
			
			actorString += currentTrait;
			if (traitIter.hasNext()) {
				actorString += ", ";
			}
			
		}
		
		actorString += System.lineSeparator() +
		stats + System.lineSeparator() +
		currentWeight + "/" + maxWeight + " Kg" + System.lineSeparator(); 
		
		if (!oneTimestatus.isEmpty()) {
			actorString += "Status : " + System.lineSeparator();
			for (OneTimeStatus currentOneTimeStatus : oneTimestatus) {
				if (currentOneTimeStatus.isDiplayable()) {
					actorString += currentOneTimeStatus + System.lineSeparator();
				}
			}
		}
		
		if (!eachTurnStatus.isEmpty()) {
			actorString += "Each turn status : " + System.lineSeparator();
			for (EachTurnStatus currentEachTurnStatus : eachTurnStatus) {
				if (currentEachTurnStatus.isDiplayable()) {
					actorString += currentEachTurnStatus + System.lineSeparator();
				}
			}
		}
		
		if (!inventory.isEmpty()) {
			actorString += "inventory : " + System.lineSeparator();
			for (IObject currentObject : inventory) {
				
				actorString += currentObject + System.lineSeparator();
			}
		}
		
		if (!equipedObjects.isEmpty()) {
			actorString += System.lineSeparator() + "Equiped Objects : " + System.lineSeparator();
			for (Map.Entry<OccupiedPlace, IEquipable> currentEntry : equipedObjects.entrySet()) {
				
				if (currentEntry.getValue() != null) {
					try {
						actorString += currentEntry.getKey() + " : " + currentEntry.getValue() + System.lineSeparator();
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		actorString += "==============================";
		
		return actorString;
	}

	public Set<ITrait> currentCharacteristics() {
		return currentCharacteristics;
	}

	public int maxWeight() {
		return maxWeight;
	}
	
	public int currentWeight() {
		return currentWeight;
	}
	
	public String pick (IObject object) throws GameException {
		if (object.getWeight() + this.currentWeight <= this.maxWeight) {
			this.inventory.add(object);
			this.currentWeight += object.getWeight();
			return  object.getName() + " picked";
		}
		else {
			throw new GameException("Object is too heavy", GameException.ExceptionType.OBJECT_WEIGHT);
		}
	}
	
	public String throwObject (IObject object) {
		this.inventory.remove(object);
		this.currentWeight -= object.getWeight();
		return object.getName() + " has been thrown away";
	}
	
	public String equip(IEquipable equipObject) throws GameException, Exception{
		
		int counter = 0;
		
		Iterator<ITrait> requiredIter = equipObject.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
		
			Iterator<ITrait> targetTraitIter = this.currentCharacteristics().iterator();
			
			while(targetTraitIter.hasNext()) {
				ITrait currentTargetTrait = targetTraitIter.next();
				
				if (currentTargetTrait.getTraitType() == currentRequiredTrait.getTraitType()) {
					if (currentTargetTrait.getValue() < currentRequiredTrait.getValue()) {
						throw new GameException("You're level in " + currentTargetTrait.getName() + " is too low", 
								GameException.ExceptionType.REQUIRED_TRAIT);
					}
					else {
						++counter;
					}
				}
			}
		}
		
		if (counter < equipObject.getRequiredTraits().size()) {
			throw new GameException("You're missing traits", 
					GameException.ExceptionType.REQUIRED_TRAIT);
		}
		
		if (!inventory.contains(equipObject)) {
			this.pick(equipObject);
		}
		
		for (OccupiedPlace occupiedPlace : equipObject.getOccupiedPlace()) {
			if (occupiedPlace == OccupiedPlace.BOTH_HANDS) {
				if (equipedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
					desequip(OccupiedPlace.LEFT_HAND);
				}
				if (equipedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
					desequip(OccupiedPlace.RIGHT_HAND);
				}
				
				equipedObjects.put(OccupiedPlace.BOTH_HANDS, equipObject);
			}
			else if (occupiedPlace == OccupiedPlace.ONE_HAND) {
				if (equipedObjects.get(OccupiedPlace.BOTH_HANDS) != null) {
					desequip(OccupiedPlace.BOTH_HANDS);
				}
				
				if (equipedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
					if (equipedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
						desequip(OccupiedPlace.RIGHT_HAND);
						equipedObjects.put(OccupiedPlace.RIGHT_HAND, equipObject);
					}
					else {
						desequip(OccupiedPlace.LEFT_HAND);
						equipedObjects.put(OccupiedPlace.LEFT_HAND, equipObject);
					}
				}
				else {
					desequip(OccupiedPlace.RIGHT_HAND);
					equipedObjects.put(OccupiedPlace.RIGHT_HAND, equipObject);
				}
			}
			else {
				desequip(occupiedPlace);
				equipedObjects.put(occupiedPlace, equipObject);
			}
		}
		
		equipObject.applieOnEquipe(this);
		return name + " is equiped with " + equipObject.getName();
	}
	
	public String desequip(OccupiedPlace place) throws Exception {
		if (equipedObjects.get(place) != null) {
			final IEquipable object = equipedObjects.get(place);
			object.removeApplieOnEquipe(this);
			equipedObjects.put(place, null);
			
			return name + " is no longer equiped with " + object.getName();
		}
		else {
			return "";
		}
	}
	
	public IEquipable getEquipedObject(OccupiedPlace place) {
		return equipedObjects.get(place);
	}
	
	public Collection<Stat> getStats() {
		return stats;
	}
	
	public ITrait getCurrentTrait (final ITrait.TraitType type) {
		final Iterator<ITrait> currentTraitIter = currentCharacteristics.iterator();
		
		while(currentTraitIter.hasNext()) {
			final ITrait currentTrait = currentTraitIter.next();
			
			if (currentTrait.getTraitType() == type) {
				return currentTrait;
			}
		}
		
		return null;
	}
	
	public ITrait getBasicTrait (final ITrait.TraitType type) {
		final Iterator<ITrait> basicTraitIter = basicCharacteristics.iterator();
		
		while(basicTraitIter.hasNext()) {
			final ITrait currentTrait = basicTraitIter.next();
			
			if (currentTrait.getTraitType() == type) {
				return currentTrait;
			}
		}
		
		return null;
	}
	
	public Stat getStat (final ITrait.TraitType type) {
		final Iterator<Stat> statIter = stats.iterator();
		
		while(statIter.hasNext()) {
			final Stat currentStat = statIter.next();
			
			if (currentStat.getTraitType() == type) {
				return currentStat;
			}
		}
		
		return null;
	}
	
	public String takeDamage (final Actor origin, final int value, final IWeapon.DamageType damageType) throws Exception {
		int realDamage = value;
		
		if (getStat(ITrait.TraitType.ARMOR) != null) {
			final int armor = getStat(ITrait.TraitType.ARMOR).getValue();
			realDamage -= IArmor.getArmorReduction(damageType, value, IArmor.ArmorType.PHYSICAL, armor);
		}
		
		if (getStat(ITrait.TraitType.MAGICAL_PROTECTION) != null) {
			final int magicArmor = getStat(ITrait.TraitType.MAGICAL_PROTECTION).getValue();
			realDamage -= IArmor.getArmorReduction(damageType, value, IArmor.ArmorType.MAGIC, magicArmor);
		}
		
		final int currentVitality = this.getCurrentTrait(TraitType.VITALITY).getValue();
		
		if (currentVitality - realDamage < 0) {
			this.getCurrentTrait(TraitType.VITALITY).setValue(0);
		}
		else {
			this.getCurrentTrait(TraitType.VITALITY).setValue(currentVitality - realDamage);
		}
		return getName() + " took " + realDamage + " " + IWeapon.getDamageTypeString(damageType) + " damage" +
		" (" + (value - realDamage) + " absorbed) from " + origin.getName();
	}
	
	public String weaponAtack(final Actor target) {
		try {
			String log = "";
			IWeapon weapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.BOTH_HANDS);
			if (weapon == null) {
				weapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.RIGHT_HAND);
				IWeapon leftWeapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.LEFT_HAND);
				if (weapon != null) {
					log += weapon.attack(target);
				}
				if (leftWeapon != null) {
					log += leftWeapon.attack(target);
				}
			}
			else {
				log += weapon.attack(target);
			}
			return log;
		}
		catch (Exception e) {
			e.printStackTrace();
			return this.name + " don't have any weapon equiped";
		}
	}
	
	public String tryToResist (final IStatus status, final int applyChances) throws Exception {
		ITrait resistanceTrait = this.getBasicTrait(status.getResistance());
		
		if (resistanceTrait == null) {
			resistanceTrait = this.getStat(status.getResistance());
		}
		if (resistanceTrait == null) {
			this.addIStatus(status);
			return this.getName() + " is now affected by " + status;
		}
		else {
			final int resistanceResult = ThreadLocalRandom.current().nextInt(0, 100 + 1);
			if (resistanceResult > resistanceTrait.getValue() - 
					(resistanceTrait.getValue() * (applyChances / 100))) {
				this.addIStatus(status);
				return this.getName() + " is now affected by " + status;
			}
			else {
				return this.getName() + " has resisted to " + status;
			}
		}
	}
}
