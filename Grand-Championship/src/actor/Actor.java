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
import utilities.Fonts;

/**
 * TODO : Create IActor
 * A basic character.
 * @author Thomas Medard
 *
 */
public class Actor extends Observable{
	
	/**
	 * The default value for the {@link Actor}.
	 */
	public static final int DEFAULT_TRAIT_VALUE = 5;

	/**
	 * The name of the {@link Actor}.
	 */
	private final String name;
	
	/**
	 * The {@link Set} of {@link ITrait} of the {@link Actor} without modification.
	 */
	private final Set<ITrait> basicCharacteristics;
	/**
	 * The {@link Set} of {@link ITrait} of the {@link Actor} with modification.
	 */
	private final Set<ITrait> currentCharacteristics;
	
	/**
	 * The {@link Set} of {@link Stat} of the {@link Actor}.
	 */
	private final Set<Stat> stats;
	
	/**
	 * The {@link Collection} of {@link OneTimeStatus} affecting this {@link Actor}.
	 */
	private final Collection<OneTimeStatus> oneTimestatus;
	/**
	 * The {@link Set} of {@link EachTurnStatus} affecting this {@link Actor}.<br>
	 * All {@link EachTurnStatus} should only be once in the {@link Set}.
	 */
	private final Set<EachTurnStatus> eachTurnStatus;
	
	/**
	 * The {@link Map} of each {@link IEquipable} in each {@link objects.equipables.IEquipable.OccupiedPlace} of this {@link Actor}.
	 */
	private final Map<IEquipable.OccupiedPlace, IEquipable> equippedObjects;
	
	/**
	 * The max weight of this {@link Actor}.
	 */
	private final int maxWeight;
	/**
	 * The current weight of this {@link Actor}.
	 */
	private int currentWeight;
	/**
	 * The inventory of this {@link Actor}.
	 */
	private final Collection<IObject> inventory;
	
	/**
	 * The {@link AI} controlling this {@link Actor}.
	 */
	private AI ai;
	
	/**
	 * The constructor of {@link Actor} with {@link Actor#DEFAULT_TRAIT_VALUE} as default value for all {@link ITrait}.
	 * @param name The {@link Actor#name} of this {@link Actor}.
	 * @throws GameException If {@link ITrait} are not supported.
	 */
	public Actor (String name) throws GameException {
		super();
		
		// Initialization of all attributes.
		this.name = name;
		this.basicCharacteristics = new HashSet<ITrait>();
		this.stats = new HashSet<Stat>();
		this.oneTimestatus = new LinkedList<OneTimeStatus>();
		this.eachTurnStatus = new HashSet<EachTurnStatus>();
		this.maxWeight = 100;
		this.currentWeight = 0;
		this.inventory = new LinkedList<IObject>();
		this.setAi(new DefaultAI(this));
		
		// Initialization of equipable slots.
		this.equippedObjects = new HashMap<IEquipable.OccupiedPlace, IEquipable>();
		equippedObjects.put(OccupiedPlace.BOTH_HANDS, null);
		equippedObjects.put(OccupiedPlace.HEAD, null);
		equippedObjects.put(OccupiedPlace.LEGS, null);
		equippedObjects.put(OccupiedPlace.LEFT_HAND, null);
		equippedObjects.put(OccupiedPlace.RIGHT_HAND, null);
		equippedObjects.put(OccupiedPlace.TORSO, null);
		
		// Creation of BasicTrait with BasicTraitFactory for the basic characteristics
		basicCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.VITALITY, 200));
		basicCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.STRENGTH, DEFAULT_TRAIT_VALUE));
		BasicTrait dexterity = BasicTraitFactory.createBasicTrait(ITrait.TraitType.DEXTERITY, DEFAULT_TRAIT_VALUE);
		basicCharacteristics.add(dexterity);
		basicCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.WILL, DEFAULT_TRAIT_VALUE));
		
		// Creation of BasicTrait with BasicTraitFactory for the current characteristics
		currentCharacteristics = new HashSet<ITrait>();
		currentCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.VITALITY, 200));
		currentCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.STRENGTH, DEFAULT_TRAIT_VALUE));
		dexterity = BasicTraitFactory.createBasicTrait(ITrait.TraitType.DEXTERITY, DEFAULT_TRAIT_VALUE);
		currentCharacteristics.add(dexterity);
		currentCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		currentCharacteristics.add(BasicTraitFactory.createBasicTrait(ITrait.TraitType.WILL, DEFAULT_TRAIT_VALUE));
		
		//Creating Stat
		final Stat critical = StatFactory.createState(ITrait.TraitType.CRITICAL, (10 + 2 * dexterity.getValue()), this);
		stats.add(critical);
		final Stat armor = StatFactory.createState(ITrait.TraitType.ARMOR, 0, this);
		stats.add(armor);
	}
	
	/**
	 * Add an {@link IStatus} to this {@link Actor}.
	 * @param status The {@link IStatus} to add.
	 * @return The result of the application of the {@link IStatus}.
	 * @throws GameException If {@link ITrait} are not supported.
	 */
	public String addIStatus(IStatus status) throws GameException {
		
		switch (status.getType()) {
		case ONE_TIME :
			this.oneTimestatus.add((OneTimeStatus) status);
			return (status.applyEffect(this));
		case EACH_TURN :
		case TEMPORARY :
			final EachTurnStatus eachTurnStatu = (EachTurnStatus) status;
			this.eachTurnStatus.add(eachTurnStatu);
			return (eachTurnStatu.applyEffect(this));
		default :
			throw new GameException("Unknown status type", GameException.ExceptionType.UNKNOWN_STATUS);
		}
	}
	
	/**
	 * Remove an {@link IStatus} from this {@link Actor}.
	 * @param status The {@link IStatus} to remove.
	 * @return The result of the application of the {@link IStatus}.
	 * @throws GameException If unknown {@link IStatus} is unknown.
	 */
	public String removeStatus (IStatus status) throws GameException {
		
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
			throw new GameException("Unknown trait", GameException.ExceptionType.UNKNOWN_TRAIT);
		}	
		log += this.getName() + " is no longer affected by " + status.getName();
		return log;
	}
	
	/**
	 * Return the name of this {@link Actor}.
	 * @return the Name of this {@link Actor}.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Make a recap of the {@link Actor}.
	 */
	@Override
	public String toString() {
		String actorString = "";
		actorString = "=============== " + Fonts.wrapHtml(name, Fonts.LogType.ACTOR) + " ===============<br>" +
		getCurrentTrait(TraitType.VITALITY) + "/" + Fonts.wrapHtml(Integer.toString(getBasicTrait(TraitType.VITALITY).getValue()), Fonts.LogType.ITRAIT) + ", ";
		
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
		
		actorString += "<br>" +
		stats + "<br>" +
		currentWeight + "/" + maxWeight + " Kg<br>"; 
		
		if (!oneTimestatus.isEmpty()) {
			actorString += "<br>";
			actorString += "<b>Status : </b>" + "<br>";
			for (OneTimeStatus currentOneTimeStatus : oneTimestatus) {
				if (currentOneTimeStatus.isDiplayable()) {
					actorString += currentOneTimeStatus + "<br>";
				}
			}
			actorString += "<br>";
		}
		
		if (!eachTurnStatus.isEmpty()) {
			actorString += "<b>Each turn status : </b><br>";
			for (EachTurnStatus currentEachTurnStatus : eachTurnStatus) {
				if (currentEachTurnStatus.isDiplayable()) {
					actorString += currentEachTurnStatus + " (" + currentEachTurnStatus.getNbTurns() + " turns left)<br>";
				}
			}
			actorString += "<br>";
		}
		
		if (!inventory.isEmpty()) {
			actorString += "<b>inventory : </b><br>";
			for (IObject currentObject : inventory) {
				
				actorString += currentObject + "<br>";
			}
			actorString += "<br>";
		}
		
		actorString += "<b>Equiped Objects : </b><br>";
		for (Map.Entry<OccupiedPlace, IEquipable> currentEntry : equippedObjects.entrySet()) {
			
			if (currentEntry.getValue() != null) {
				try {
					actorString += currentEntry.getKey() + " : " + currentEntry.getValue() + "<br>";
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		actorString += "==============================";
		
		return actorString;
	}

	/**
	 * Return the current characteristics of this {@link Actor}.
	 * @return The current characteristics of this {@link Actor}.
	 */
	public Set<ITrait> currentCharacteristics() {
		return currentCharacteristics;
	}

	/**
	 * Return the current max weight of this {@link Actor}.
	 * @return The current max weight of this {@link Actor}.
	 */
	public int maxWeight() {
		return maxWeight;
	}
	
	/**
	 * Return the current weight of this {@link Actor}.
	 * @return The current weight of this {@link Actor}.
	 */
	public int currentWeight() {
		return currentWeight;
	}
	
	/**
	 * Pick an {@link IObject} and put it into this {@link Actor}'s {@link Actor#inventory}.
	 * @param object The {@link IObject} to pick up.
	 * @return A String saying the result of the action.
	 * @throws GameException If the {@link IObject} is too heavy.
	 */
	public String pick (IObject object) throws GameException {
		if (object.getWeight() + this.currentWeight <= this.maxWeight) {
			this.inventory.add(object);
			this.currentWeight += object.getWeight();
			return  Fonts.wrapHtml(object.getName(), Fonts.LogType.OBJECT) + " picked";
		}
		else {
			throw new GameException("Object is too heavy", GameException.ExceptionType.OBJECT_WEIGHT);
		}
	}
	
	/**
	 * Drop the {@link IObject} from this {@link Actor}'s {@link Actor#inventory}.
	 * @param object The {@link IObject} to drop.
	 * @return A string explaining the result of the action.
	 * @throws GameException If the object is not in the inventory then an exception is raised.
	 */
	public String drop (IObject object) throws GameException {
		if (this.inventory.contains(object)) {
			this.inventory.remove(object);
			this.currentWeight -= object.getWeight();
			return object.getName() + " has been droped";
		}
		throw new GameException("Object not in inventory", GameException.ExceptionType.UNKNOWN_OBJECT);
	}
	
	/**
	 * TODO: Use equip in IEquipable
	 * Equip an {@link IObject} in the first free {@link OccupiedPlace}.
	 * @param equipObject The {@link IObject} to equip.
	 * @return The result of the action.
	 * @throws GameException If you can't equip the new {@link IObject}.
	 */
	public String equip(IEquipable equipObject) throws GameException{
		
		// Some basics verifications
		if (equippedObjects.containsValue(equipObject)) {
			return Fonts.wrapHtml(this.getName(), Fonts.LogType.ACTOR) + "is already equiped with " + Fonts.wrapHtml(equipObject.getName(), Fonts.LogType.OBJECT);
		}
		if (!canEquip(equipObject)) {
			throw new GameException(Fonts.wrapHtml(this.getName(), Fonts.LogType.ACTOR) + " can't equip " + 
					Fonts.wrapHtml(equipObject.getName(), Fonts.LogType.OBJECT), GameException.ExceptionType.REQUIRED_TRAIT);
		}
		
		// Preparing result's logs.
		String log = "";
		if (!inventory.contains(equipObject)) {
			log += this.pick(equipObject) + "<br>";
		}
		
		// If an object was already equipped.
		IEquipable lastObject;
		final OccupiedPlace occupiedPlace = equipObject.getOccupiedPlace(); 
		// If the object is both handed
		if (occupiedPlace == OccupiedPlace.BOTH_HANDS) {
			// Verifying if an object is equipped in right or left hand.
			if (equippedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
				lastObject = equippedObjects.get(OccupiedPlace.LEFT_HAND);
				log += desequip(lastObject) + System.lineSeparator();
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			if (equippedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
				lastObject = equippedObjects.get(OccupiedPlace.RIGHT_HAND);
				log += desequip(lastObject) + System.lineSeparator();
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			
			equippedObjects.put(OccupiedPlace.BOTH_HANDS, equipObject);
		}
		// If the object is one handed
		else if (occupiedPlace == OccupiedPlace.ONE_HAND) {
			// Verifying if a double handed object is already equipped.
			if (equippedObjects.get(OccupiedPlace.BOTH_HANDS) != null) {
				lastObject = equippedObjects.get(OccupiedPlace.BOTH_HANDS);
				log += desequip(lastObject) + System.lineSeparator();
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			
			// If the right hand is occupied then verifying the left one.
			if (equippedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
				// If both hands are occupied then equipping in the right one.
				if (equippedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
					lastObject = equippedObjects.get(OccupiedPlace.RIGHT_HAND);
					log += desequip(lastObject) + System.lineSeparator();
					if (!canEquip(equipObject)) {
						equip(lastObject);
						throw new GameException("You can't equip " + equipObject.getName(), 
								GameException.ExceptionType.REQUIRED_TRAIT);
					}
					equippedObjects.put(OccupiedPlace.RIGHT_HAND, equipObject);
				}
				else {
					lastObject = equippedObjects.get(OccupiedPlace.LEFT_HAND);
					log += desequip(lastObject) + System.lineSeparator();
					if (!canEquip(equipObject)) {
						equip(lastObject);
						throw new GameException("You can't equip " + equipObject.getName(), 
								GameException.ExceptionType.REQUIRED_TRAIT);
					}
					equippedObjects.put(OccupiedPlace.LEFT_HAND, equipObject);
				}
			}
			else {
				lastObject = equippedObjects.get(OccupiedPlace.RIGHT_HAND);
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
				equippedObjects.put(OccupiedPlace.RIGHT_HAND, equipObject);
			}
		}
		// If it's not a hand object.
		else {
			lastObject = equippedObjects.get(occupiedPlace);
			if (lastObject != null) {
				log += desequip(lastObject) + System.lineSeparator();
				
				if (!canEquip(equipObject)) {
					equip(lastObject);
					throw new GameException("You can't equip " + equipObject.getName(), 
							GameException.ExceptionType.REQUIRED_TRAIT);
				}
			}
			equippedObjects.put(occupiedPlace, equipObject);
		}
		
		// Making object's required traits to observe the actor's corresponding trait.
		Iterator<ITrait> requiredIter = equipObject.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
			
			ITrait trait = this.getCurrentTrait(currentRequiredTrait.getTraitType());
			trait.addObserver(equipObject);
		}
		
		log += equipObject.applieOnEquipe(this) + "<br>";
		log += "<span class=\"actor\">" + this.getName() + "</span> is equiped with <span class=\"object\">" + 
				equipObject.getName() + "</span>";
		return log;
	}
	
	/**
	 * Verify is this {@link Actor} can equip an {@link IEquipable}.
	 * @param object The {@link IEquipable} to verify.
	 * @return true if possible or false otherwise.
	 */
	private Boolean canEquip (IEquipable object){
		Iterator<ITrait> requiredIter = object.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
			ITrait currentTargetTrait = null;
			
			currentTargetTrait = this.getCurrentTrait(currentRequiredTrait.getTraitType());
			
			if(currentTargetTrait != null) {
				
				if (currentTargetTrait.getValue() < currentRequiredTrait.getValue()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Desequip an {@link IEquipable} from this {@link Actor}.
	 * @param object The {@link IEquipable} to desequip.
	 * @return The action's log.
	 * @throws GameException If an error occur while removing the object's effects,
	 * or if the object is not already equipped.
	 */
	public String desequip(IEquipable object) throws GameException {
		if (!equippedObjects.containsValue(object)) {
			throw new GameException("Object not equipped", GameException.ExceptionType.UNKNOWN_OBJECT);
		}
		
		// Finding and desequipping the object.
		for (Entry<OccupiedPlace, IEquipable> entry : equippedObjects.entrySet())
		{
		    if (entry.getValue() != null && entry.getValue().equals(object)) {
		    	equippedObjects.put(entry.getKey(), null);
		    }
		}
		
		Iterator<ITrait> requiredIter = object.getRequiredTraits().iterator();
		
		// For all required trait, removing observation.
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
			
			ITrait trait = this.getCurrentTrait(currentRequiredTrait.getTraitType());
			trait.deleteObserver(object);
		}
		
		// Removing effects
		object.removeApplieOnEquipe(this);
		return Fonts.wrapHtml(name, Fonts.LogType.ACTOR) + " is no longer equipped with " + 
			Fonts.wrapHtml(object.getName(), Fonts.LogType.OBJECT);
	}
	
	/**
	 * Get the {@link IEquipable} at the {@link OccupiedPlace}.
	 * @param place The {@link OccupiedPlace} of the {@link IEquipable}
	 * @return The {@link IEquipable} at the {@link OccupiedPlace}.
	 */
	public IEquipable getEquipedObject(OccupiedPlace place) {
		return equippedObjects.get(place);
	}
	
	/**
	 * Get the {@link Stat} {@link Collection}.
	 * @return The {@link Stat} {@link Collection}.
	 */
	public Collection<Stat> getStats() {
		return stats;
	}
	
	/**
	 * Get this {@link Actor}'s current {@link ITrait} of a certain {@link actor.characteristics.traits.ITrait.TraitType}.
	 * @param type The {@link actor.characteristics.traits.ITrait.TraitType} of the current {@link ITrait} looked for.
	 * @return The current {@link ITrait} of the {@link actor.characteristics.traits.ITrait.TraitType} looked for. Null if not.
	 */
	public ITrait getCurrentTrait (final ITrait.TraitType type){
		
		final Iterator<ITrait> currentTraitIter = currentCharacteristics.iterator();
		
		while(currentTraitIter.hasNext()) {
			final ITrait currentTrait = currentTraitIter.next();
			
			if (currentTrait.getTraitType() == type) {
				return currentTrait;
			}
		}
		
		return null;
	}
	
	/**
	 * Get this {@link Actor}'s basic {@link ITrait} of a certain {@link actor.characteristics.traits.ITrait.TraitType}.
	 * @param type The {@link actor.characteristics.traits.ITrait.TraitType} of the basic {@link ITrait} looked for.
	 * @return The basic {@link ITrait} of the {@link actor.characteristics.traits.ITrait.TraitType} looked for. Null if not.
	 */
	public ITrait getBasicTrait (final ITrait.TraitType type){
		
		final Iterator<ITrait> basicTraitIter = basicCharacteristics.iterator();
		
		while(basicTraitIter.hasNext()) {
			final ITrait currentTrait = basicTraitIter.next();
			
			if (currentTrait.getTraitType() == type) {
				return currentTrait;
			}
		}
		
		return null;
	}
	
	/**
	 * Get this {@link Actor}'s {@link Stat} of a certain {@link actor.characteristics.traits.ITrait.TraitType}.
	 * @param type The {@link actor.characteristics.traits.ITrait.TraitType} of the {@link Stat} looked for.
	 * @return The {@link Stat} of the {@link actor.characteristics.traits.ITrait.TraitType} looked for. Null if not.
	 */
	public Stat getStat (final ITrait.TraitType type){		
		final Iterator<Stat> statIter = stats.iterator();
		
		while(statIter.hasNext()) {
			final Stat currentStat = statIter.next();
			
			if (currentStat.getTraitType() == type) {
				return currentStat;
			}
		}
		
		return null;
	}
	
	/**
	 * Make this Actor take damage
	 * @param origin The {@link Actor} who is making the damage (can be null).
	 * @param value The raw value of the damage.
	 * @param damageType The {@link objects.equipables.weapons.IWeapon.DamageType} of the attack.
	 * @return The result log of the action.
	 * @throws GameException If unknown stuff appear.
	 */
	public String takeDamage (final Actor origin, final int value, final IWeapon.DamageType damageType) 
			throws GameException {
		int realDamage = value;
		
		// Get the physical damage reduction.
		final Stat armorStat = getStat(ITrait.TraitType.ARMOR);
		// If there is some armor.
		if (armorStat != null) {
			final int armor = armorStat.getValue();
			realDamage -= IArmor.ArmorType.PHYSICAL.getArmorReduction(damageType, value, armor);
		}
		
		// Get the magic damage reduction.
		final Stat magicArmorStat = getStat(ITrait.TraitType.MAGICAL_PROTECTION);
		// If there is some magical protection.
		if (magicArmorStat != null) {
			final int magicArmor = magicArmorStat.getValue();
			realDamage -= IArmor.ArmorType.MAGIC.getArmorReduction(damageType, value, magicArmor);
		}
		
		final int currentVitality = this.getCurrentTrait(TraitType.VITALITY).getValue();
		
		// If damage reduction is superior to damage taken.
		if (realDamage < 0) {
			realDamage = 0;
		}
		// Make the damage
		if (currentVitality - realDamage < 0) {
			this.getCurrentTrait(TraitType.VITALITY).setValue(0);
		}
		else {
			this.getCurrentTrait(TraitType.VITALITY).setValue(currentVitality - realDamage);
		}
		// The result's log.
		return Fonts.wrapHtml(getName(), Fonts.LogType.ACTOR) + " took " + 
		Fonts.wrapHtml(Integer.toString(realDamage) + " " + damageType + " damage", Fonts.LogType.DAMAGE_PHYS) +
		Fonts.wrapHtml(" (" + (value - realDamage) + " absorbed)", Fonts.LogType.ABSORBTION_PHYS) + (origin == null ? "" : " from " + 
		Fonts.wrapHtml(origin.getName(), Fonts.LogType.ACTOR));
	}
	
	// TODO: Create an Attack object
	// TODO: Create an Observer Manager
	/**
	 * TODO : rethinks this function, i think there's a better to do it.
	 * Make this {@link Actor} attack with his equipped weapon(s).
	 * @param target The target of the attack.
	 * @return The result's log.
	 */
	public String weaponAtack(final Actor target) {
		try {
			String log = "";
			
			// Get a random number for critical hit chances.
			final int critical = ThreadLocalRandom.current().nextInt(0, 100 + 1);
			final Boolean isCritical = this.getStat(ITrait.TraitType.CRITICAL).getValue() >= critical ? true : false;
			
			if (isCritical) {
				log += Fonts.wrapHtml("Critical attack !",Fonts.LogType.CRITICAL) + "<br>";
			}
			
			IWeapon weapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.BOTH_HANDS);
			if (weapon == null) {
				weapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.RIGHT_HAND);
				IWeapon leftWeapon = (IWeapon) this.getEquipedObject(IEquipable.OccupiedPlace.LEFT_HAND);
				if (weapon != null) {
					log += weapon.attack(target, isCritical, this);
				}
				if (leftWeapon != null) {
					log += leftWeapon.attack(target, isCritical, this);
				}
				if (weapon == null && leftWeapon == null) {
					log += MeleWeapon.getFists(this.getCurrentTrait(TraitType.STRENGTH).getValue()).attack(target, isCritical, this);
				}
			}
			else {
				log += weapon.attack(target, isCritical, this);
			}
			return log;
		}
		catch (Exception e) {
			e.printStackTrace();
			return Fonts.wrapHtml(this.name, Fonts.LogType.ACTOR) + " couldn't attack.";
		}
	}
	
	/**
	 * Make a test to resist to an {@link IStatus}. If it failed, it will be applied.
	 * @param status The {@link IStatus} to resist.
	 * @param applyChances The chances of appliance.
	 * @return The result's log.
	 * @throws GameException If copying the status (to affect this Actor) failed.
	 */
	public String tryToResist (final IStatus status, final int applyChances) throws GameException {
		// Try to get the resistance trait corresponding to the IStatus to resist.
		ITrait resistanceTrait;// = this.getBasicTrait(status.getResistance());
		
		// No more resistance based on basic traits, only stats.
		//if (resistanceTrait == null) {
		resistanceTrait = this.getStat(status.getResistance());
		//}
		// Creating a random number.
		final int resistanceResult = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		final int threshold;
		if (resistanceTrait == null) {
			threshold = applyChances;	
		}
		else {
			/*
			 * Because the resistanceTrait can be a BasicTrait, it should be multiplied (for now).
			 * Indeed, a basic trait is a low value (5 by default) so it's not very high.
			 * TODO : create some resistance status depending on the actual basic trait, or never 
			 * use basic traits as resistance traits to get rid of the multiplier.
			 */
			threshold = applyChances - resistanceTrait.getValue()/* * 5*/;
		}
		if (resistanceResult < threshold) {
			if (this.eachTurnStatus.contains(status)) {
				Iterator<EachTurnStatus> statusIter = this.eachTurnStatus.iterator();
				
				while (statusIter.hasNext()) {
					EachTurnStatus currentStatus = statusIter.next();
					if (currentStatus.equals(status)) {
						currentStatus.setNbTurns(((EachTurnStatus) status).getNbTurns());
						return Fonts.wrapHtml(this.getName(), Fonts.LogType.ACTOR) + " is affected again by " + 
								Fonts.wrapHtml(status.toString(), Fonts.LogType.STATUS) + 
								Fonts.wrapHtml(" (" + resistanceResult + "/" + threshold + ")", Fonts.LogType.TEST);
					}
				}
			}
			else {
				this.addIStatus(IStatus.copy(status));
				return Fonts.wrapHtml(this.getName(), Fonts.LogType.ACTOR) + " is now affected by " + 
					status.toString() + 
					Fonts.wrapHtml(" (" + resistanceResult + "/" + threshold + ")", Fonts.LogType.TEST);
			}
		}
		else {
			return Fonts.wrapHtml(this.getName(), Fonts.LogType.ACTOR) + " has resisted to " + 
					Fonts.wrapHtml(status.toString(), Fonts.LogType.STATUS) + 
					Fonts.wrapHtml(" (" + resistanceResult + "/" + threshold + ")", Fonts.LogType.TEST);
		}
		return "There's has been an error";
	}
	
	/**
	 * Add a {@link Stat} to this {@link Actor}.
	 * @param stat The {@link Stat} to add to this {@link Actor}.
	 */
	public void addStat (final Stat stat) {
		this.stats.add(stat);
	}
	
	/**
	 * Get the {@link Collection} of all {@link EachTurnStatus}.
	 * @return The {@link Collection} of all {@link EachTurnStatus}.
	 */
	public Collection<EachTurnStatus> getEachTurnStatus() {
		return eachTurnStatus;
	}

	/**
	 * Get the {@link AI} of this {@link Actor}.
	 * @return The {@link AI} of this {@link Actor}.
	 */
	public AI getAi() {
		return ai;
	}

	/**
	 * Set the {@link AI} of this {@link Actor}.
	 * @param ai The {@link AI} to set for this {@link Actor}.
	 */
	public void setAi(AI ai) {
		this.ai = ai;
	}
	
	/**
	 * Check if this {@link Actor} is dead.
	 * @return True if dead, false if alive.
	 */
	public Boolean isDead() {
		return this.getCurrentTrait(ITrait.TraitType.VITALITY).getValue() <= 0;
	}
	
	/**
	 * Check if a {@link Collection} of {@link ITrait} contains a certain {@link actor.characteristics.traits.ITrait.TraitType}.
	 * @param collection The {@link Collection} to search into.
	 * @param type The {@link actor.characteristics.traits.ITrait.TraitType} to look for.
	 * @return True if found, false if not.
	 */
	public boolean containsType (final Collection<ITrait> collection, final ITrait.TraitType type) {
		for (ITrait trait : collection) {
			if (trait.getTraitType() == type) {
				return true;
			}
		}	
		return false;
	}
}
