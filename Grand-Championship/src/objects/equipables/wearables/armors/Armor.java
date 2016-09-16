package objects.equipables.wearables.armors;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.status.traitModifier.StatModifier;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import utilities.Fonts;

/**
 * Standard metal armor
 * @author Thomas MEDARD
 *
 */
public class Armor implements IArmor {
	
	/**
	 * The required {@link ITrait} to wear this {@link Armor}
	 */
	private Collection<ITrait> requiredTraits;
	/**
	 * The name of the {@link Armor}
	 */
	private String name;
	/**
	 * The description of the {@link Armor}
	 */
	private final String description;
	/**
	 * The weight of the {@link Armor}
	 */
	private final int weight;
	/**
	 * The value (in money) of the {@link Armor}
	 */
	private final int value;
	/**
	 * The {@link objects.equipables.wearables.armors.IArmor.ArmorType} of the {@link Armor}
	 */
	private final ArmorType armorType;
	/**
	 * The armor value of the {@link Armor}
	 */
	private final int armorValue;
	/**
	 * The {@link IStatus} applied when equipping the {@link Armor}.<br>
	 * Will contains at least the {@link Armor#armorValue} provided by the {@link Armor}
	 */
	private Collection<IStatus> statusApllied;
	/**
	 * The {@link objects.equipables.IEquipable.OccupiedPlace} of the {@link Armor}
	 */
	private final OccupiedPlace occupiedPlace;

	/**
	 * The constructor
	 * @param requiredTraits {@link Armor#requiredTraits} of the {@link Armor} (can be null)
	 * @param name {@link Armor#name} of the {@link Armor}
	 * @param description {@link Armor#description} of the {@link Armor}
	 * @param weight {@link Armor#weight} of the {@link Armor}
	 * @param value {@link Armor#value} of the {@link Armor}
	 * @param armorType {@link Armor#armorType} of the {@link Armor}
	 * @param armorValue {@link Armor#armorValue} of the {@link Armor}
	 * @param statusApllied {@link Armor#statusApllied} of the {@link Armor} (can be null)
	 * @param occupiedPlace {@link Armor#occupiedPlace} of the {@link Armor}
	 */
	public Armor(final Collection<ITrait> requiredTraits, final String name, final String description, 
			final int weight, final int value, final ArmorType armorType, final int armorValue, 
			final Collection<IStatus> statusApllied, final OccupiedPlace occupiedPlace) {
		super();
		this.requiredTraits = requiredTraits;
		if (requiredTraits == null) {
			this.requiredTraits = new LinkedList<>();
		}
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.armorType = armorType;
		this.armorValue = armorValue;
		this.statusApllied = statusApllied;
		if (statusApllied == null) {
			this.statusApllied = new LinkedList<>();
		}
		
		Collection<ITraitModifier> armorModifier = new LinkedList<ITraitModifier>();
		armorModifier.add(new StatModifier(ITrait.TraitType.ARMOR, armorValue));
		this.statusApllied.add(new OneTimeStatus(name, "Armor from " + name, armorModifier, false, 100, null));
		
		this.occupiedPlace = occupiedPlace;
	}

	/**
	 * @see objects.equipables.wearables.armors.IArmor#getArmorValue()
	 */
	@Override
	public int getArmorValue() {
		return armorValue;
	}

	/**
	 * @see objects.equipables.wearables.armors.IArmor#getArmorType()
	 */
	@Override
	public ArmorType getArmorType() {
		return armorType;
	}

	/**
	 * @see objects.equipables.IEquipable#getRequiredTraits()
	 */
	@Override
	public Collection<ITrait> getRequiredTraits() {
		return requiredTraits;
	}

	/**
	 * @see objects.equipables.IEquipable#applieOnEquipe(actor.Actor)
	 */
	@Override
	public String applieOnEquipe(Actor target) throws GameException {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		String log = "";
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			log += target.addIStatus(currentStatus) + System.lineSeparator();
		}
		
		this.name += "(E)";
		return log;
	}

	/**
	 * @see objects.equipables.IEquipable#removeApplieOnEquipe(actor.Actor)
	 */
	@Override
	public void removeApplieOnEquipe(Actor target) throws GameException {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
		
		this.name = this.name.substring(0, name.length() - 3);
	}

	/**
	 * @see objects.equipables.IEquipable#getOccupiedPlace()
	 */
	@Override
	public OccupiedPlace getOccupiedPlace() {
		return occupiedPlace;
	}

	/**
	 * @see objects.IObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see objects.IObject#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @see objects.IObject#getWeight()
	 */
	@Override
	public int getWeight() {
		return weight;
	}

	/**
	 * @see objects.IObject#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			String armorStr = Fonts.wrapHtml(name, Fonts.LogType.OBJECT) + " : " + description + "<br>";

			armorStr += occupiedPlace + " ";
			armorStr += Fonts.wrapHtml(Integer.toString(armorValue), (armorType == ArmorType.PHYSICAL ? Fonts.LogType.ABSORBTION_PHYS : Fonts.LogType.ABSORBTION_MAG)) + 
					" " + armorType + " armor" + System.lineSeparator() +
					weight + " Kg, " + Fonts.wrapHtml(value + " $", Fonts.LogType.MONEY)  + "<br>";
			
			if (!requiredTraits.isEmpty()) {
				armorStr += "required : " + requiredTraits + "<br>";
			}
			if (!statusApllied.isEmpty()) {
				armorStr += "applies" + statusApllied + "<br>";
			}
			return  armorStr;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		
		final Actor actor;
		try {
			actor = (Actor) o;
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		int counter = 0;
		
		Iterator<ITrait> requiredIter = this.getRequiredTraits().iterator();
		
		while (requiredIter.hasNext()) {
			ITrait currentRequiredTrait = requiredIter.next();
		
			ITrait currentTargetTrait = null;
			currentTargetTrait = actor.getCurrentTrait(currentRequiredTrait.getTraitType());
			
			if(currentTargetTrait != null) {
				
				if (currentTargetTrait.getValue() < currentRequiredTrait.getValue()) {
					try {
						actor.desequip(this);
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				else {
					++counter;
				}
			}
		}
		
		if (counter < this.getRequiredTraits().size()) {
			try {
				actor.desequip(this);
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
}
