package actor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;
import objects.IObject;

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
	
	private int maxWeight;
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
		
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.VITALITY, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.STRENGTH, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.DEXTERITY, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.WILL, DEFAULT_TRAIT_VALUE));
		
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
		
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.VITALITY, Vitality));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.STRENGTH, strength));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.DEXTERITY, dexterity));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.CONSTITUTION, constitution));
		basicCharacteristics.add(BasicTraitFactory.getBasicTrait(ITrait.WILL, will));
		
		currentCharacteristics = new HashSet<ITrait>(basicCharacteristics);
	}
	
	public void addIStatus(IStatus status) throws Exception {
		
		switch (status.type()) {
		case IStatus.ONE_TIME_STATUS :
			this.oneTimestatus.add((OneTimeStatus) status);
			break;
		case IStatus.EACH_TURN_STATUS :
			this.eachTurnStatus.add((EachTurnStatus) status);
			break;
		default :
			throw new Exception("Unknown status type");
		}
		
		Iterator<ITraitModifier> traitModifierIter = status.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = this.currentCharacteristics.iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.traitType() == currentModifiedTrait.traitType()) {
					
					currentActorTrait.setValue(currentActorTrait.value() + currentModifiedTrait.value());
					return;
				}
			}
		}
	}
	
	public void removeStatus (IStatus status) throws Exception {
		
		switch (status.type()) {
		case IStatus.ONE_TIME_STATUS :
			this.oneTimestatus.remove((OneTimeStatus) status);
			((OneTimeStatus) status).removeEffect(this);
			break;
		case IStatus.EACH_TURN_STATUS :
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
		return "Character [\n"
				+ "name=" + name + ",\n"
				+ "traits=" + currentCharacteristics + "\n"
				+ "status=" + oneTimestatus + "\n" +
				"on each turn status=" + eachTurnStatus + "\n"
				+ ", maxWeight=" + maxWeight + ", currentWeight=" + currentWeight + "\n" + 
				"inventory=" + inventory + "]";
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
	
	public String pick (IObject object) {
		if (this.currentWeight <= object.weight()) {
			this.inventory.add(object);
			this.currentWeight += object.weight();
			return  object.name() + " picked";
		}
		else {
			return object.name() + " is too heavy";
		}
	}
	
	public String throwObject (IObject object) {
		this.inventory.remove(object);
		this.currentWeight -= object.weight();
		return object.name() + " has been thrown away";
	}
}
