package actor.characteristics.status;

import java.util.Collection;
import java.util.Iterator;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;

public class EachTurnStatus implements IStatus {

	private String name;
	private String description;
	private Collection<ITraitModifier> traitModifiers;

	public EachTurnStatus(String name, String description, Collection<ITraitModifier> traitModifiers) {
		super();
		this.name = name;
		this.description = description;
		this.traitModifiers = traitModifiers;
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
	public Collection<ITraitModifier> traitModifiers() {
		return traitModifiers;
	}

	@Override
	public String toString() {
		return name + " : " + description + " " + traitModifiers;
	}

	@Override
	public StatusType type() {
		return IStatus.StatusType.EACH_TURN;
	}
	
	@Override
	public String applyEffect(Actor target) {
		Iterator<ITraitModifier> traitModifierIter = this.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = target.currentCharacteristics().iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.getTraitType() == currentModifiedTrait.getTraitType()) {
					
					currentActorTrait.setValue(currentActorTrait.getValue() - currentModifiedTrait.getValue());
				}
			}
		}
		return description;
	}

}
