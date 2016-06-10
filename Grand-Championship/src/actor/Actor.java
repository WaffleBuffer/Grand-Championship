package actor;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.IObject;
import objects.equipables.IEquipable;
import objects.equipables.IEquipable.OccupiedPlace;

/**
 * A basic character
 * @author tmedard
 *
 */
public class Actor extends Observable{
	
	public static final int DEFAULT_TRAIT_VALUE = 5;

	private String name;
	
	private Set<ITrait> basicCharacteristics;
	private Set<ITrait> currentCharacteristics;
	
	private Collection<OneTimeStatus> oneTimestatus;
	private Collection<EachTurnStatus> eachTurnStatus;
	
	private Map<IEquipable.OccupiedPlace, IEquipable> equipedObjects;
	
	private final int maxWeight;
	private int currentWeight;
	private Collection<IObject> inventory;
	
	public Actor (String name) throws Exception {
		super();
		
		this.name = name;
		this.basicCharacteristics = new HashSet<ITrait>();
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
		
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.VITALITY, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.STRENGTH, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.DEXTERITY, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.WILL, DEFAULT_TRAIT_VALUE));
		
		currentCharacteristics = new HashSet<ITrait>(basicCharacteristics);
	}
	
	public Actor (String name, int Vitality, int strength, int dexterity, int constitution, int will) throws Exception {
		super();

		this.name = name;
		this.basicCharacteristics = new HashSet<ITrait>();
		this.eachTurnStatus = new LinkedList<EachTurnStatus>();
		this.maxWeight = 100;
		this.currentWeight = 0;
		this.inventory = new LinkedList<IObject>();
		
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.VITALITY, Vitality));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.STRENGTH, strength));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.DEXTERITY, dexterity));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.CONSTITUTION, constitution));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.TraitType.WILL, will));
		
		currentCharacteristics = new HashSet<ITrait>(basicCharacteristics);
	}
	
	public void addIStatus(IStatus status) throws Exception {
		
		switch (status.type()) {
		case ONE_TIME :
			this.oneTimestatus.add((OneTimeStatus) status);
			status.applyEffect(this);
			break;
		case EACH_TURN :
			this.eachTurnStatus.add((EachTurnStatus) status);
			break;
		default :
			throw new Exception("Unknown status type");
		}
		
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
	
	public void removeStatus (IStatus status) throws Exception {
		
		switch (status.type()) {
		case ONE_TIME :
			this.oneTimestatus.remove((OneTimeStatus) status);
			((OneTimeStatus) status).removeEffect(this);
			break;
		case EACH_TURN :
			this.eachTurnStatus.remove((EachTurnStatus) status);
			break;
		default :
			throw new Exception("Unknown status type");
		}	
	}
	
	public String name() {
		return this.name;
	}

	@Override
	public String toString() {
		String actorString = "=============== " + name + " ===============" + System.lineSeparator() +
		currentCharacteristics + System.lineSeparator() +
		currentWeight + "/" + maxWeight + " Kg" + System.lineSeparator(); 
		
		if (!oneTimestatus.isEmpty()) {
			actorString += "Status : " + System.lineSeparator();
			for (OneTimeStatus currentOneTimeStatus : oneTimestatus) {
				
				actorString += currentOneTimeStatus + System.lineSeparator();
			}
		}
		
		if (!eachTurnStatus.isEmpty()) {
			actorString += "Each turn status : " + System.lineSeparator();
			for (EachTurnStatus currentEachTurnStatus : eachTurnStatus) {
				
				actorString += currentEachTurnStatus + System.lineSeparator();
			}
		}
		
		if (!inventory.isEmpty()) {
			actorString += "inventory : " + System.lineSeparator();
			for (IObject currentObject : inventory) {
				
				actorString += currentObject + System.lineSeparator();
			}
		}
		
		if (!equipedObjects.isEmpty()) {
			actorString += "Euiped Objects : " + System.lineSeparator();
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
		if (object.weight() + this.currentWeight <= this.maxWeight) {
			this.inventory.add(object);
			this.currentWeight += object.weight();
			return  object.name() + " picked";
		}
		else {
			throw new GameException("Object is too heavy", GameException.ExceptionType.OBJECT_WEIGHT);
		}
	}
	
	public String throwObject (IObject object) {
		this.inventory.remove(object);
		this.currentWeight -= object.weight();
		return object.name() + " has been thrown away";
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
		
		if (equipObject.getOccupiedPlace() == OccupiedPlace.BOTH_HANDS) {
			if (equipedObjects.get(OccupiedPlace.LEFT_HAND) != null) {
				desequip(OccupiedPlace.LEFT_HAND);
			}
			if (equipedObjects.get(OccupiedPlace.RIGHT_HAND) != null) {
				desequip(OccupiedPlace.RIGHT_HAND);
			}
			
			equipedObjects.put(OccupiedPlace.BOTH_HANDS, equipObject);
		}
		else if (equipObject.getOccupiedPlace() == OccupiedPlace.ONE_HAND) {
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
			desequip(equipObject.getOccupiedPlace());
			equipedObjects.put(equipObject.getOccupiedPlace(), equipObject);
		}
		
		equipObject.applieOnEquipe(this);
		return name + " is equiped with " + equipObject;
	}
	
	public String desequip(OccupiedPlace place) throws Exception {
		if (equipedObjects.get(place) != null) {
			final IEquipable object = equipedObjects.get(place);
			object.removeApplieOnEquipe(this);
			equipedObjects.put(place, null);
			
			return name + " is no longer equiped with" + object;
		}
		else {
			return "";
		}
	}
	
	public IEquipable getEquipedObject(OccupiedPlace place) {
		return equipedObjects.get(place);
	}
}
