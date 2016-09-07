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
import objects.equipables.weapons.IWeapon;
import utilities.Fonts;

/**
 * An {@link IStatus} that apply its effect without time limits.<br>
 * Like {@link IWeapon} {@link IStatus}.
 * @author Thomas Medard
 *
 */
public class OneTimeStatus implements IStatus {
	
	/**
	 * The {@link OneTimeStatus}' name.
	 */
	private final String name;
	/**
	 * The {@link OneTimeStatus}' description.
	 */
	private final String description;
	/**
	 * The {@link Collection} of {@link ITraitModifier} of this {@link OneTimeStatus}.
	 */
	private Collection<ITraitModifier> traitModifiers;
	/**
	 * Should it be displayed in the list of {@link Actor}'s {@link IStatus}.
	 */
	private final Boolean displayable;
	/**
	 * The raw apply chancing of this {@link OneTimeStatus}.
	 */
	private int applyChances;
	/**
	 * The {@link actor.characteristics.traits.ITrait.TraitType} that is used to calculate the resistance. Can be null.
	 */
	private final ITrait.TraitType resistance;

	/**
	 * The constructor
	 * @param name The name of the {@link OneTimeStatus}.
	 * @param description The description of the {@link OneTimeStatus}.
	 * @param traitModifiers The {@link Collection} of {@link ITraitModifier} of this {@link OneTimeStatus}.
	 * @param displayable Should it be displayed in the list of {@link Actor}'s {@link IStatus}.
	 * @param applyChances The {@link actor.characteristics.traits.ITrait.TraitType} that is used to calculate the resistance.
	 * @param resistance The {@link actor.characteristics.traits.ITrait.TraitType} that is used to calculate the resistance. Can be null.
	 */
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
	
	/**
	 * The copy constructor
	 * @param status The {@link IStatus} to copy.
	 */
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
		return Fonts.wrapHtml(name, Fonts.LogType.STATUS) + " :" + description + " " +
				Fonts.wrapHtml(traitModifiers.toString(), Fonts.LogType.STATUS);
	}

	/**
	 * @see actor.characteristics.status.IStatus#getType()
	 */
	@Override
	public StatusType getType() {
		return StatusType.ONE_TIME;
	}

	/**
	 * @see actor.characteristics.status.IStatus#applyEffect(actor.Actor)
	 */
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

	/**
	 * @see actor.characteristics.status.IStatus#removeEffect(actor.Actor)
	 */
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
		return applyChances;
	}

	/**
	 * @see actor.characteristics.status.IStatus#getResistance()
	 */
	@Override
	public TraitType getResistance() {
		return resistance;
	}
}
