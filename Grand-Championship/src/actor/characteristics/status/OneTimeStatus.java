package actor.characteristics.status;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;
import actor.characteristics.traits.Stat;
import actor.characteristics.traits.StatFactory;

public class OneTimeStatus implements IStatus {
	
	private final String name;
	private final String description;
	private Collection<ITraitModifier> traitModifiers;
	private final Boolean displayable;
	private int applyChances;
	private final ITrait.TraitType resistance;

	public OneTimeStatus(final String name, final String description, 
			final Collection<ITraitModifier> traitModifiers,
			final Boolean displayable, final int applyChances, final ITrait.TraitType resistance) {
		super();
		this.name = name;
		this.description = description;
		this.traitModifiers = traitModifiers;
		if (traitModifiers == null) {
			this.traitModifiers = new LinkedList<ITraitModifier>();
		}
		this.displayable = displayable;
		this.applyChances = applyChances;
		this.resistance = resistance;
	}
	
	public OneTimeStatus(final IStatus status) {
		super();
		this.name = status.getName();
		this.description = status.getDescription();
		this.traitModifiers = new LinkedList<ITraitModifier>(status.getTraitModifiers());
		if (traitModifiers == null) {
			this.traitModifiers = new LinkedList<ITraitModifier>();
		}
		this.displayable = status.isDiplayable();
		this.applyChances = status.getApplyChances();
		this.resistance = status.getResistance();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Collection<ITraitModifier> getTraitModifiers() {
		return traitModifiers;
	}

	@Override
	public String toString() {
		return name + " :" + description + " " + traitModifiers;
	}

	@Override
	public StatusType getType() {
		return StatusType.ONE_TIME;
	}

	@Override
	public String applyEffect(final Actor target) throws Exception {
		Iterator<ITraitModifier> traitModifierIter = this.getTraitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			if (!(currentModifiedTrait.getModifierType() == ITraitModifier.ModifierType.STAT)) {
				Iterator<ITrait> traitsIter = target.currentCharacteristics().iterator();
				
				while (traitsIter.hasNext()) {
				
					ITrait currentActorTrait = traitsIter.next();
					
					if (currentActorTrait.getTraitType() == currentModifiedTrait.getTraitType()) {
						
						currentActorTrait.setValue(currentActorTrait.getValue() + currentModifiedTrait.getValue());
					}
				}
			}
			else {				
				Iterator<Stat> statsIter = target.getStats().iterator();
				
				while (statsIter.hasNext()) {
					
					ITrait currentActorStat = statsIter.next();
					
					if (currentActorStat.getTraitType() == currentModifiedTrait.getTraitType()) {
						
						currentActorStat.setValue(currentActorStat.getValue() + currentModifiedTrait.getValue());
						break;
					}
					else if (!statsIter.hasNext()) {
						target.addStat(StatFactory.createState(currentModifiedTrait.getTraitType(),
								currentModifiedTrait.getValue(), target));
					}
				}
			}
		}
		return description;
	}

	@Override
	public String removeEffect(Actor target) {
		
		Iterator<ITraitModifier> traitModifierIter = this.getTraitModifiers().iterator();
		
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

	@Override
	public Boolean isDiplayable() {
		return displayable;
	}

	@Override
	public int getApplyChances() {
		return applyChances;
	}

	@Override
	public TraitType getResistance() {
		return resistance;
	}
}
