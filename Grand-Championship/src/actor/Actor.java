package actor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
import objects.equipables.ObjectEmplacement;
import objects.equipables.ObjectEmplacement.PlaceType;
import objects.equipables.weapons.IWeapon;
import objects.equipables.wearables.armors.IArmor;
import utilities.Fonts;
import utilities.Fonts.LogType;

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
	 * All the emplacements for equipping objects
	 * @see ObjectEmplacement
	 */
	private final Collection<ObjectEmplacement> objectEmplacements;
	
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
	public Actor (final String name) throws GameException {
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
		this.objectEmplacements = new LinkedList<ObjectEmplacement>();
		objectEmplacements.add(new ObjectEmplacement(PlaceType.ONE_HAND));
		objectEmplacements.add(new ObjectEmplacement(PlaceType.ONE_HAND));
		objectEmplacements.add(new ObjectEmplacement(PlaceType.HEAD));
		objectEmplacements.add(new ObjectEmplacement(PlaceType.TORSO));
		objectEmplacements.add(new ObjectEmplacement(PlaceType.LEGS));
		
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
	public String addIStatus(final IStatus status) throws GameException {
		
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
	public String removeStatus (final IStatus status) throws GameException {
		
		String log = "";
		switch (status.getType()) {
		case ONE_TIME :
			this.oneTimestatus.remove((OneTimeStatus) status);
			return ((OneTimeStatus) status).removeEffect(this);
		case TEMPORARY :
			log += ((EachTurnStatus) status).removeEffect(this);
		case EACH_TURN :
			this.eachTurnStatus.remove((EachTurnStatus) status);
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
					actorString += currentEachTurnStatus + "<br>";
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
		for (ObjectEmplacement emplacement : objectEmplacements) {
			
			if (emplacement.getEquipped() != null) {
				actorString += emplacement + " : " + emplacement.getEquipped() + "<br>";
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
	public String pick (final IObject object) throws GameException {
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
	public String drop (final IObject object) throws GameException {
		if (this.inventory.contains(object)) {
			this.inventory.remove(object);
			this.currentWeight -= object.getWeight();
			return object.getName() + " has been droped";
		}
		throw new GameException("Object not in inventory", GameException.ExceptionType.UNKNOWN_OBJECT);
	}
	
	/**
	 * Equip an {@link IObject} in the first free {@link ObjectEmplacement}.
	 * @param equipObject The {@link IObject} to equip.
	 * @return The result of the action.
	 * @throws GameException If you can't equip the new {@link IObject}.
	 */
	public String equip(IEquipable equipObject) throws GameException{
		
		// Verifying that this Actor can equip the object
		if (!canEquip(equipObject)) {
			throw new GameException(Fonts.wrapHtml(this.getName(), Fonts.LogType.ACTOR) + " can't equip " + 
					Fonts.wrapHtml(equipObject.getName(), Fonts.LogType.OBJECT), GameException.ExceptionType.REQUIRED_TRAIT);
		}
		
		String log = "";
		
		Collection<ObjectEmplacement> neededEmplacement = new LinkedList<ObjectEmplacement>();
		int nbCorrespondingEmplacements = 0;
		
		// Searching for all the required emplacements
		for (ObjectEmplacement emplacement : this.objectEmplacements) {
			
			if (equipObject.getObjectEmplacements().contains(emplacement.getType())) {
				neededEmplacement.add(emplacement);
				++nbCorrespondingEmplacements;
				break;
			}
		}
		
		// If this actor doesn't have the required emplacements
		if (nbCorrespondingEmplacements < equipObject.getObjectEmplacements().size()) {
			return Fonts.wrapHtml(this.getName(), LogType.ACTOR) + " doesn't have the needed emplacements to equip " +
					Fonts.wrapHtml(equipObject.getName(), LogType.OBJECT);
		}
		
		// If the required emplacements are already occupied then we desequip what's inside
		for (ObjectEmplacement neededEmp : neededEmplacement) {
			if (neededEmp.getEquipped() != null) {
				this.desequip(neededEmp.getEquipped());
			}
			
			neededEmp.setEquippedObject(equipObject);
		}
		
		log += equipObject.applieOnEquipe(this) + "<br>";
		log += Fonts.wrapHtml(this.getName(), LogType.ACTOR)  + " is equiped with " + 
				Fonts.wrapHtml(equipObject.getName(), LogType.OBJECT);
		return log;
	}
	
	/**
	 * Verify is this {@link Actor} can equip an {@link IEquipable}.
	 * @param object The {@link IEquipable} to verify.
	 * @return true if possible or false otherwise.
	 */
	private Boolean canEquip (final IEquipable object){
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
		
		// Finding and the object.
		ObjectEmplacement currentObjEmp;
		Iterator<ObjectEmplacement> empsIter = objectEmplacements.iterator();
		int nbEmpFound = 0;
		
		while (empsIter.hasNext()) {
			
			currentObjEmp = empsIter.next();
			
			if (currentObjEmp.getEquipped() == object) {
				currentObjEmp.setEquippedObject(null);
				++nbEmpFound;
				
				// If all are found then no need to continue research
				if (nbEmpFound == object.getObjectEmplacements().size()) {
					break;
				}
			}
		}
		
		// If the object hadn't been found then this function shouldn't have been called
		if (nbEmpFound == 0) {
			throw new GameException("Object not equipped", GameException.ExceptionType.UNKNOWN_OBJECT);
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
	 * TODO : rework this : too many responsabilitys
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
	
	/**
	 * Test if a random generated number is below this {@link Actor} critical chances
	 * @return If a random generated number is below this {@link Actor} critical chances
	 */
	private boolean isAttackCritical() {
		if (this.getStat(ITrait.TraitType.CRITICAL) == null) {
			return false;
		}
		
		// Get a random number for critical hit chances.
		final int critical = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		return this.getStat(ITrait.TraitType.CRITICAL).getValue() >= critical ? true : false;
	}
	
	/**
	 * Get all equipped weapons
	 * @return All equipped weapons
	 */
	private Collection<IWeapon> getEquippedWeapons() {
		
		Collection<IWeapon> equippedWeapons = new LinkedList<IWeapon>();
		
		for (ObjectEmplacement objEmp : this.objectEmplacements) {
			if (objEmp.getEquipped() == null) {
				continue;
			}
			else if (objEmp.getEquipped().getType() != IEquipable.EquipableType.WEAPON) {
				continue;
			}
			else if (equippedWeapons.contains(objEmp.getEquipped())) {
				continue;
			}
			else {
				equippedWeapons.add(((IWeapon)objEmp.getEquipped()));
			}
		}
		
		return equippedWeapons;
	}
	
	// TODO: Create an Attack object
	// TODO: Create an Observer Manager
	/**
	 * Make this {@link Actor} attack with his equipped weapon(s).
	 * @param target The target of the attack.
	 * @return The result's log.
	 * @throws GameException If an error occurs during the attack
	 */
	public String weaponAtack(final Actor target) throws GameException {
		String log = "";

		final Boolean isCritical = this.isAttackCritical();
		
		if (isCritical) {
			log += Fonts.wrapHtml("Critical attack !",Fonts.LogType.CRITICAL) + "<br>";
		}
		
		for (IWeapon equippedWeapon : getEquippedWeapons()) {
			log += equippedWeapon.attack(target, isCritical, this);
		}

		return log;
	}
	
	/**
	 * Make a test to resist to an {@link IStatus}. If it failed, it will be applied.
	 * @param status The {@link IStatus} to resist.
	 * @param applyChances The chances of appliance.
	 * @return The result's log.
	 * @throws GameException If copying the status (to affect this Actor) failed.
	 */
	public String tryToResist (final IStatus status, final float applyChances) throws GameException {
		
		if (applyChances > 100.0) {
			throw new GameException("Apply chances must be < to 100", GameException.ExceptionType.INVALID_VALUE);
		}
		// Try to get the resistance trait corresponding to the IStatus to resist.
		ITrait resistanceTrait;// = this.getBasicTrait(status.getResistance());
		
		// No more resistance based on basic traits, only stats.
		//if (resistanceTrait == null) {
		resistanceTrait = this.getStat(status.getResistance());
		//}
		// Creating a random number.
		final float resistanceResult = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		final float threshold;
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
	 * Get this {@link Actor}'s inventory.
	 * @return This {@link Actor}'s inventory.
	 */
	public Collection<IObject> getInventory() {
		return this.inventory;
	}
}
