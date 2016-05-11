package actor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

import actor.characteristics.status.IStatus;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;

public class Actor extends Observable{
	
	public static final int DEFAULT_TRAIT_VALUE = 5;

	private String name;
	
	private Set<ITrait> traits;
	private Collection<IStatus> status;
	
	public Actor (String name) throws Exception {
		super();
		
		this.name = name;
		this.traits = new HashSet<ITrait>();
		this.status = new LinkedList<IStatus>();
		
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.VITALITY, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.STRENGTH, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.DEXTERITY, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.WILL, DEFAULT_TRAIT_VALUE));
	}
	
	public Actor (String name, int Vitality, int strength, int dexterity, int constitution, int will) throws Exception {
		super();
		
		this.name = name;
		this.traits = new HashSet<ITrait>();
		
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.VITALITY, Vitality));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.STRENGTH, strength));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.DEXTERITY, dexterity));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.CONSTITUTION, constitution));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.WILL, will));
	}
	
	public void addIStatus(IStatus status) {
		this.status.add(status);
		
		Iterator<ITraitModifier> traitModifierIter = status.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = this.traits.iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.traitType() == currentModifiedTrait.traitType()) {
					
					currentActorTrait.setValue(currentActorTrait.value() + currentModifiedTrait.value());
					return;
				}
			}
		}
	}
	
	public void removeStatus (IStatus status) {
		
		Iterator<ITraitModifier> traitModifierIter = status.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = this.traits.iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.traitType() == currentModifiedTrait.traitType()) {
					
					currentActorTrait.setValue(currentActorTrait.value() - currentModifiedTrait.value());
					this.status.remove(status);
					return;
				}
			}
		}	
	}
	
	public void removeAllStatus (IStatus status) {
		
		Iterator<ITraitModifier> traitModifierIter = status.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = this.traits.iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.traitType() == currentModifiedTrait.traitType()) {
					
					currentActorTrait.setValue(currentActorTrait.value() - currentModifiedTrait.value());
					this.status.remove(status);
				}
			}
		}	
	}
	
	public String name() {
		return this.name;
	}
	
	/*public Collection<ITraitModifier> traitModifiers() {
		return traitModifiers;
	}*/

	@Override
	public String toString() {
		return "Character [\n"
				+ "name=" + name + ",\n"
				+ "traits=" + traits + "\n"
				+ "status=" + status + "]";
	}
}
