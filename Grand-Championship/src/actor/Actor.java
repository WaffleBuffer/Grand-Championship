package actor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
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
import gameEngine.ai.AI;
import gameEngine.ai.DefaultAI;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;
import objects.IObject;
import objects.equipables.IEquipable;
import objects.equipables.IEquipable.OccupiedPlace;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;
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
	private final Set<EachTurnStatus> eachTurnStatus;
	
	private final Map<IEquipable.OccupiedPlace, IEquipable> equipedObjects;
	
	private final int maxWeight;
	private int currentWeight;
	private final Collection<IObject> inventory;
	
	private AI ai;
	
	public Actor (String name) throws Exception {
		super();
		
		this.name = name;
		this.basicCharacteristics = new HashSet<ITrait>();
		this.stats = new HashSet<Stat>();
		this.oneTimestatus = new LinkedList<OneTimeStatus>();
		this.eachTurnStatus = new HashSet<EachTurnStatus>();
		this.maxWeight = 100;
		this.currentWeight = 0;
		this.inventory = new LinkedList<IObject>();
		this.setAi(new DefaultAI(this));
		
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
		
		final Stat critical = StatFactory.createState(ITrait.TraitType.CRITICAL, (10 + 2 * dexterity.getValue()), this);
		stats.add(critical);
		final Stat armor = StatFactory.createState(ITrait.TraitType.ARMOR, 0, this);
		stats.add(armor);
	}
	
	public String addIStatus(IStatus status) throws Exception {
		
		switch (status.getType()) {
		case ONE_TIME :
			this.oneTimestatus.add((OneTimeStatus) status);
			return (status.applyEffect(this));
		case EACH_TURN :
		case TEMPORARY :
			final EachTurnStatus eachTurnStatu = (EachTurnStatus) status;
			this.eachTurnStatus.add(eachTurnStatu);
			eachTurnStatu.applyEffect(this);
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
		
		String log = "";
		switch (status.getType()) {
		case ONE_TIME :
			this.oneTimestatus.remove((OneTimeStatus) status);
			return ((OneTimeStatus) status).removeEffect(this);
		case TEMPORARY :
			log += ((EachTurnStatus) status).removeEffect(this);
		case EACH_TURN :
			this.eachTurnStatus.remove(status);
			break;
		default :
			throw new Exception("Unknown status type");
		}	
		log += this.getName() + " is no longer affected by " + status.getName();
		return log;
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
			actorString += System.lineSeparator();
			actorString += "Status : " + System.lineSeparator();
			for (OneTimeStatus currentOneTimeStatus : oneTimestatus) {
				if (currentOneTimeStatus.isDiplayable()) {
					actorString += currentOneTimeStatus + System.lineSeparator();
				}
			}
			actorString += System.lineSeparator();
		}
		
		if (!eachTurnStatus.isEmpty()) {
			actorString += "Each turn status : " + System.lineSeparator();
			for (EachTurnStatus currentEachTurnStatus : eachTurnStatus) {
				if (currentEachTurnStatus.isDiplayable()) {
					actorString += currentEachTurnStatus + " (" + currentEachTurnStatus.getNbTurns() + " turns left)" +
							System.lineSeparator();
				}
			}
			actorString += System.lineSeparator();
		}
		
		if (!inventory.isEmpty()) {
			actorString += "inventory : " + System.lineSeparator();
			actorString += System.lineSeparator();
			for (IObject currentObject : inventory) {
				
				actorString += currentObject + System.lineSeparator();
			}
			actorString += System.lineSeparator();
		}
		
		actorString += System.lineSeparator() + "Equiped Objects : " + System.lineSeparator();
		actorString += System.lineSeparator();
		for (Map.Entry<OccupiedPlace, IEquipable> currentEntry : equipedObjects.entrySet()) {
			
			if (currentEntry.getValue() != null) {
				try {
					actorString += currentEntry.getKey() + " : " + currentEntry.getValue() + System.lineSeparator() +
							System.lineSeparator();
				} 
				catch (Exception e) {
					e.printStackTrace();
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
	
	public String drop (IObject object) {
		this.inventory.remove(object);
		this.currentWeight -= object.getWeight();
		return object.getName() + " has been droped";
	}
	
	public String equip(IEquipable equipObject) throws GameException, Exception{
		
		if (equipedObjects.containsValue(equipObject)) {
			return this.getName() + " is already equiped with " + equipObject.getName();
		}
		if (!canEquip(equipObject)) {
			throw new GameException("You can't equip " + equipObject.getName(), 
					GameException.ExceptionType.REQUIRED_TRAIT);
		}
		
		String log = "";
		if (!inventory.contains(equipObject)) {
			log += this.pick(equipObject) + System.lineSeparator();
		}
		
		IEquipable lastObject;
		final OccupiedPlace occupiedPlace = equipObject.getOccupiedPlace(); 
		if (occupiedPlace == OccupiedPlace.BOTH_HANDS) {
			if (equipedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
				lastObject = equipedObjects.get(OccupiedPlace.LEFT_HAND);
				log += desequip(lastObject) + System.lineSeparator();
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			if (equipedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
				lastObject = equipedObjects.get(OccupiedPlace.RIGHT_HAND);
				log += desequip(lastObject) + System.lineSeparator();
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			
			equipedObjects.put(OccupiedPlace.BOTH_HANDS, equipObject);
		}
		else if (occupiedPlace == OccupiedPlace.ONE_HAND) {
			if (equipedObjects.get(OccupiedPlace.BOTH_HANDS) != null) {
				lastObject = equipedObjects.get(OccupiedPlace.BOTH_HANDS);
				log += desequip(lastObject) + System.lineSeparator();
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			
			if (equipedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
				if (equipedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
					lastObject = equipedObjects.get(OccupiedPlace.RIGHT_HAND);
					log += desequip(lastObject) + System.lineSeparator();
					if (!canEquip(equipObject)) {
						equip(lastObject);
						throw new GameException("You can't equip " + equipObject.getName(), 
								GameException.ExceptionType.REQUIRED_TRAIT);
					}
					equipedObjects.put(OccupiedPlace.RIGHT_HAND, equipObject);
				}
				else {
					lastObject = equipedObjects.get(OccupiedPlace.LEFT_HAND);
					log += desequip(lastObject) + System.lineSeparator();
					if (!canEquip(equipObject)) {
						equip(lastObject);
						throw new GameException("You can't equip " + equipObject.getName(), 
								GameException.ExceptionType.REQUIRED_TRAIT);
					}
					equipedObjects.put(OccupiedPlace.LEFT_HAND, equipObject);
				}
			}
			else {
				lastObject = equipedObjects.get(OccupiedPlace.RIGHT_HAND);
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
				equipedObjects.put(OccupiedPlace.RIGHT_HAND, equipObject);
			}
		}
		else {
			lastObject = equipedObjects.get(occupiedPlace);
			if (lastObject != null) {
				log += desequip(lastObject) + System.lineSeparator();
				
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			equipedObjects.put(occupiedPlace, equipObject);
		}
		
		Iterator<ITrait> requiredIter = equipObject.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
			
			ITrait trait = this.getCurrentTrait(currentRequiredTrait.getTraitType());
			trait.addObserver(equipObject);
		}
		
		log += equipObject.applieOnEquipe(this);
		log += name + " is equiped with " + equipObject.getName();
		return log;
	}
	
	private Boolean canEquip (IEquipable object) throws GameException {
		Iterator<ITrait> requiredIter = object.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
		
			final ITrait currentTargetTrait = this.getCurrentTrait(currentRequiredTrait.getTraitType());
			
			if(currentTargetTrait != null) {
				
				if (currentTargetTrait.getValue() < currentRequiredTrait.getValue()) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return true;
	}
	
	public String desequip(IEquipable object) throws Exception {
		
		Set<Entry<OccupiedPlace, IEquipable>> entrySet = equipedObjects.entrySet();
		
		Iterator<Entry<OccupiedPlace, IEquipable>> entryIter =  entrySet.iterator();
		
		while (entryIter.hasNext()) {
			Entry<OccupiedPlace, IEquipable> entry = entryIter.next();
			
			if (entry.getValue() == object) {
				equipedObjects.put(entry.getKey(), null);
				
				Iterator<ITrait> requiredIter = object.getRequiredTraits().iterator();
				
				while (requiredIter.hasNext()) {
					ITrait currentRequiredTrait = requiredIter.next();
					
					ITrait trait = this.getCurrentTrait(currentRequiredTrait.getTraitType());
					trait.deleteObserver(object);
				}
				
				object.removeApplieOnEquipe(this);
				return name + " is no longer equiped with " + object.getName();
			}
		}
		return "";
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
		
		final Stat armorStat = getStat(ITrait.TraitType.ARMOR);
		if (armorStat != null) {
			final int armor = armorStat.getValue();
			realDamage -= IArmor.ArmorType.PHYSICAL.getArmorReduction(damageType, value, armor);
		}
		
		final Stat magicArmorStat = getStat(ITrait.TraitType.MAGICAL_PROTECTION);
		if (magicArmorStat != null) {
			final int magicArmor = magicArmorStat.getValue();
			realDamage -= IArmor.ArmorType.MAGIC.getArmorReduction(damageType, value, magicArmor);
		}
		
		final int currentVitality = this.getCurrentTrait(TraitType.VITALITY).getValue();
		
		if (realDamage < 0) {
			realDamage = 0;
		}
		if (currentVitality - realDamage < 0) {
			this.getCurrentTrait(TraitType.VITALITY).setValue(0);
		}
		else {
			this.getCurrentTrait(TraitType.VITALITY).setValue(currentVitality - realDamage);
		}
		return getName() + " took " + realDamage + " " + IWeapon.getDamageTypeString(damageType) + " damage" +
		" (" + (value - realDamage) + " absorbed)" + (origin == null ? "" : " from " + origin.getName());
	}
	
	public String weaponAtack(final Actor target) {
		try {
			String log = "";
			
			final int critical = ThreadLocalRandom.current().nextInt(0, 100 + 1);
			final Boolean isCritical = this.getStat(ITrait.TraitType.CRITICAL).getValue() >= critical ? true : false;
			
			if (isCritical) {
				log += "Critical attack !" + System.lineSeparator();
			}
			
			IWeapon weapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.BOTH_HANDS);
			if (weapon == null) {
				weapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.RIGHT_HAND);
				IWeapon leftWeapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.LEFT_HAND);
				if (weapon != null) {
					log += weapon.attack(target, isCritical);
				}
				if (leftWeapon != null) {
					log += leftWeapon.attack(target, isCritical);
				}
				if (weapon == null && leftWeapon == null) {
					log += MeleWeapon.getFists(this.getCurrentTrait(TraitType.STRENGTH).getValue()).attack(target, isCritical);
				}
			}
			else {
				log += weapon.attack(target, isCritical);
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
		final int resistanceResult = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		final int threshold;
		if (resistanceTrait == null) {
			threshold = applyChances;	
		}
		else {
			/*
			 * Because the resistanceTrait can be a BasicTrait, it should multiplied (for now)
			 * TODO : create some restistance stats depending on the actual basic trait
			 * to get rid of the multiplier.
			 */
			threshold = applyChances - resistanceTrait.getValue() * 5;
		}
		if (resistanceResult < threshold) {
			if (this.eachTurnStatus.contains(status)) {
				Iterator<EachTurnStatus> statusIter = this.eachTurnStatus.iterator();
				
				while (statusIter.hasNext()) {
					EachTurnStatus currentStatus = statusIter.next();
					if (currentStatus.equals(status)) {
						currentStatus.setNbTurns(((EachTurnStatus) status).getNbTurns());
						return this.getName() + " is affected again by " + status + " (" + resistanceResult + "/" + threshold + ")";
					}
				}
			}
			this.addIStatus(IStatus.copy(status));
			return this.getName() + " is now affected by " + status + " (" + resistanceResult + "/" + threshold + ")";
		}
		else {
			return this.getName() + " has resisted to " + status.getName() + " (" + resistanceResult + "/" + threshold + ")";
		}
	}
	
	public void addStat (final Stat stat) {
		this.stats.add(stat);
	}
	
	public Collection<EachTurnStatus> getEachTurnStatus() {
		return eachTurnStatus;
	}

	public AI getAi() {
		return ai;
	}

	public void setAi(AI ai) {
		this.ai = ai;
	}
	
	public Boolean isDead() {
		return this.getCurrentTrait(ITrait.TraitType.VITALITY).getValue() <= 0;
	}
}
