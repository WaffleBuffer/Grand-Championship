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

/**
 * This is an {@link IStatus} applying its effects each turn or temporary for certain number of turn.
 * @author Medar
 *
 */
public class EachTurnStatus implements IStatus {
	/**
	 * The name of the {@link EachTurnStatus}.
	 */
	private String name;
	/**
	 * The description of the {@link EachTurnStatus}.
	 */
	private final String description;
	/**
	 * The {@link Collection} of {@link ITraitModifier} of this {@link EachTurnStatus}.
	 */
	private Collection<ITraitModifier> traitModifiers;
	/**
	 * The number of turn left this {@link EachTurnStatus} has before stopping.
	 */
	private int nbTurn;
	/**
	 * Should it be displayed in the list of {@link Actor}'s {@link IStatus}.
	 */
	private final Boolean displayable;
	/**
	 * The raw apply chancing of this {@link EachTurnStatus}.
	 */
	private int applyChances;
	/**
	 * The {@link actor.characteristics.traits.ITrait.TraitType} that is used to calculate the resistance. Can be null.
	 */
	private final ITrait.TraitType resistance;
	/**
	 * The {@link IStatus.StatusType} of this {@link EachTurnStatus}.<br>
	 * For {@link EachTurnStatus}, it's {@code EACH_TURN} (each turn apply) or 
	 * {@code TEMPORARY} (effects applies one time but last some turn).
	 */
	private final IStatus.StatusType type;

	/**
	 * The constructor
	 * @param name The name of the {@link EachTurnStatus}.
	 * @param description The description of the {@link EachTurnStatus}.
	 * @param traitModifiers The {@link Collection} of {@link ITraitModifier} of this {@link EachTurnStatus}.
	 * @param nbTurns The number of turn left this {@link EachTurnStatus} has before stopping.
	 * @param displayable Should it be displayed in the list of {@link Actor}'s {@link IStatus}.
	 * @param applyChances The {@link actor.characteristics.traits.ITrait.TraitType} that is used to calculate the resistance.
	 * @param resistance The {@link actor.characteristics.traits.ITrait.TraitType} that is used to calculate the resistance. Can be null
	 * @param type The {@link IStatus.StatusType} of this {@link EachTurnStatus}.<br>
	 * For {@link EachTurnStatus}, it's {@code EACH_TURN} (each turn apply) or 
	 * {@code TEMPORARY} (effects applies one time but last some turn).
	 */
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
	
	/**
	 * The copy constructor
	 * @param status The {@link IStatus} to copy.
	 */
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

	/**
	 * @see actor.characteristics.status.IStatus#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see actor.characteristics.status.IStatus#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @see actor.characteristics.status.IStatus#getTraitModifiers()
	 */
	@Override
	public Collection<ITraitModifier> getTraitModifiers() {
		return traitModifiers;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name + " : " + description + " " + traitModifiers + " " + nbTurn + " turns";
	}

	/**
	 * @see actor.characteristics.status.IStatus#getType()
	 */
	@Override
	public StatusType getType() {
		return this.type;
	}
	
	/**
	 * @see actor.characteristics.status.IStatus#applyEffect(actor.Actor)
	 */
	@Override
	public String applyEffect(final Actor target) throws GameException {
		Iterator<ITraitModifier> traitModifierIter = this.getTraitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			// Stats modifier are in a different Collection that standard ITrait
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
	
	/**
	 * @see actor.characteristics.status.IStatus#removeEffect(actor.Actor)
	 */
	@Override
	public String removeEffect(final Actor target) {
		
		Iterator<ITraitModifier> traitModifierIter = this.getTraitModifiers().iterator();
		
		while (traitModifierIter.hasNext()) {
			
			ITraitModifier currentModifiedTrait = traitModifierIter.next();
			
			// Stats modifier are in a different Collection that standard ITrait
			if (currentModifiedTrait.getModifierType() != ITraitModifier.ModifierType.STAT) {
				Iterator<ITrait> traitsIter = target.currentCharacteristics().iterator();
				
				while (traitsIter.hasNext()) {
				
					ITrait currentActorTrait = traitsIter.next();
					
					if (currentActorTrait.getTraitType() == currentModifiedTrait.getTraitType()) {
						
						currentActorTrait.setValue(currentActorTrait.getValue() - currentModifiedTrait.getValue());
					}
				}
			}
			else {
				Iterator<Stat> statsIter = target.getStats().iterator();
				
				while (statsIter.hasNext()) {
					
					ITrait currentActorStat = statsIter.next();
					
					if (currentActorStat.getTraitType() == currentModifiedTrait.getTraitType()) {
						
						currentActorStat.setValue(currentActorStat.getValue() - currentModifiedTrait.getValue());
					}
				}
			}
		}
		return name + " doesn't applie any more";
	}
	
	/**
	 * Get {@link EachTurnStatus#nbTurn}.
	 * @return {@link EachTurnStatus#nbTurn}
	 */
	public int getNbTurns() {
		return nbTurn;
	}

	/**
	 * @see actor.characteristics.status.IStatus#isDiplayable()
	 */
	@Override
	public Boolean isDiplayable() {
		return displayable;
	}

	/**
	 * @see actor.characteristics.status.IStatus#getApplyChances()
	 */
	@Override
	public int getApplyChances() {
		return this.applyChances;
	}

	/**
	 * @see actor.characteristics.status.IStatus#getResistance()
	 */
	@Override
	public TraitType getResistance() {
		return resistance;
	}
	
	/**
	 * Set {@link EachTurnStatus#nbTurn}.
	 * @param nbTurns The {@link EachTurnStatus#nbTurn} to set.
	 */
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
		result = prime * result + applyChances;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((displayable == null) ? 0 : displayable.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + nbTurn;
		result = prime * result + ((resistance == null) ? 0 : resistance.hashCode());
		result = prime * result + ((traitModifiers == null) ? 0 : traitModifiers.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EachTurnStatus other = (EachTurnStatus) obj;
		if (applyChances != other.applyChances)
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (displayable == null) {
			if (other.displayable != null)
				return false;
		} else if (!displayable.equals(other.displayable))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (nbTurn != other.nbTurn)
			return false;
		if (resistance != other.resistance)
			return false;
		if (traitModifiers == null) {
			if (other.traitModifiers != null)
				return false;
		} else if (!traitModifiers.equals(other.traitModifiers))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
	
	
}
