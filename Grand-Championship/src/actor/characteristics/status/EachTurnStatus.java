package actor.characteristics.status;

import java.util.Collection;
import java.util.Iterator;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.Stat;

public class EachTurnStatus implements IStatus {

	private String name;
	private String description;
	private Collection<ITraitModifier> traitModifiers;
	private final int nbTurn;

	public EachTurnStatus(String name, String description, Collection<ITraitModifier> traitModifiers,
			final int nbTurns) {
		super();
		this.name = name;
		this.description = description;
		this.traitModifiers = traitModifiers;
		this.nbTurn = nbTurns;
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
	public String applyEffect(final Actor target) {
		Iterator<ITraitModifier> traitModifierIter = this.traitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			Iterator<ITrait> traitsIter = target.currentCharacteristics().iterator();
			
			while (traitsIter.hasNext()) {
			
				ITrait currentActorTrait = traitsIter.next();
				
				if (currentActorTrait.getTraitType() == currentModifiedTrait.getTraitType()) {
					
					currentActorTrait.setValue(currentActorTrait.getValue() + currentModifiedTrait.getValue());
				}
			}
			
			Iterator<Stat> statsIter = target.getStats().iterator();
			
			while (statsIter.hasNext()) {
				
				ITrait currentActorStat = statsIter.next();
				
				if (currentActorStat.getTraitType() == currentModifiedTrait.getTraitType()) {
					
					currentActorStat.setValue(currentActorStat.getValue() + currentModifiedTrait.getValue());
				}
			}
		}
		return description;
	}
	
	@Override
	public String removeEffect(final Actor target) {
		
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
			
			Iterator<Stat> statsIter = target.getStats().iterator();
			
			while (statsIter.hasNext()) {
				
				ITrait currentActorStat = statsIter.next();
				
				if (currentActorStat.getTraitType() == currentModifiedTrait.getTraitType()) {
					
					currentActorStat.setValue(currentActorStat.getValue() - currentModifiedTrait.getValue());
				}
			}
		}
		return name + " doesn't applie any more";
	}
	
	public int getNbTurns() {
		return nbTurn;
	}
}
