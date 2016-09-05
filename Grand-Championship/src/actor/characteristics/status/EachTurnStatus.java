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
import gameExceptions.GameException;

public class EachTurnStatus implements IStatus {

	private String name;
	private final String description;
	private Collection<ITraitModifier> traitModifiers;
	private int nbTurn;
	private final Boolean displayable;
	private int applyChances;
	private final ITrait.TraitType resistance;
	private final IStatus.StatusType type;

	public EachTurnStatus(String name, String description, Collection<ITraitModifier> traitModifiers,
			final int nbTurns, final Boolean displayable, final int applyChances, final ITrait.TraitType resistance,
			final IStatus.StatusType type) {
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
		this.type = type;
	}
	
	public EachTurnStatus(final IStatus status) {
		super();
		this.name = status.getName();
		this.description = status.getDescription();
		this.traitModifiers = new LinkedList<ITraitModifier>(status.getTraitModifiers());
		if (traitModifiers == null) {
			this.traitModifiers = new LinkedList<ITraitModifier>();
		}
		if (status.getType() == IStatus.StatusType.EACH_TURN || status.getType() == IStatus.StatusType.TEMPORARY) {
			this.nbTurn = ((EachTurnStatus) status).getNbTurns();
		}
		this.displayable = status.isDiplayable();
		this.applyChances = status.getApplyChances();
		this.resistance = status.getResistance();
		this.type = status.getType();
	}

	/** (non-Javadoc)
	 * @see actor.characteristics.status.IStatus#getName()
	 */
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
		return name + " : " + description + " " + traitModifiers + " " + nbTurn + " turns";
	}

	@Override
	public StatusType getType() {
		return this.type;
	}
	
	@Override
	public String applyEffect(final Actor target) throws GameException {
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
	public String removeEffect(final Actor target) {
		
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
	
	public void setNbTurns(final int nbTurns) {
		this.nbTurn = nbTurns;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		EachTurnStatus other = (EachTurnStatus) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} 
		else if (!name.equals(other.name)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
	
}
