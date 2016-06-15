package actor.characteristics.status;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;
import actor.characteristics.traits.Stat;

public class EachTurnStatus implements IStatus {

	private String name;
	private String description;
	private Collection<ITraitModifier> traitModifiers;
	private final int nbTurn;
	private final Boolean displayable;
	private int applyChances;
	private final ITrait.TraitType resistance;

	public EachTurnStatus(String name, String description, Collection<ITraitModifier> traitModifiers,
			final int nbTurns, final Boolean displayable, final int applyChances, final ITrait.TraitType resistance) {
		super();
		this.name = name;
		this.description = description;
		this.traitModifiers = traitModifiers;
		if (traitModifiers == null) {
			this.traitModifiers = new LinkedList<ITraitModifier>();
		}
		this.nbTurn = nbTurns;
		this.displayable = displayable;
		this.applyChances = applyChances;
		this.resistance = resistance;
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

	@Override
	public Boolean isDiplayable() {
		return displayable;
	}

	@Override
	public int getApplyChances() {
		return this.applyChances;
	}

	@Override
	public TraitType getResistance() {
		return resistance;
	}
}
