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

/**
 * Standard metal armor
 * @author tmedard
 *
 */
public class MetalArmor implements IArmor {
	
	/**
	 * The required {@link ITrait} to wear this {@link MetalArmor}
	 */
	private Collection<ITrait> requiredTraits;
	/**
	 * The name of the {@link MetalArmor}
	 */
	private String name;
	/**
	 * The description of the {@link MetalArmor}
	 */
	private final String description;
	/**
	 * The weight of the {@link MetalArmor}
	 */
	private final int weight;
	/**
	 * The value (in money) of the {@link MetalArmor}
	 */
	private final int value;
	/**
	 * The {@link ArmorType} of the {@link MetalArmor}
	 */
	private final ArmorType armorType;
	/**
	 * The armor value of the {@link MetalArmor}
	 */
	private final int armorValue;
	/**
	 * The {@link IStatus} applied when equiping the {@link MetalArmor}.<br>
	 * Will contains at least the {@link MetalArmor#armorValue} provided by the {@link MetalArmor}
	 */
	private Collection<IStatus> statusApllied;
	/**
	 * The {@link OccupiedPlace} of the {@link MetalArmor}
	 */
	private final OccupiedPlace occupiedPlace;

	/**
	 * The constructor
	 * @param requiredTraits {@link MetalArmor#requiredTraits} of the {@link MetalArmor} (can be null)
	 * @param name {@link MetalArmor#name} of the {@link MetalArmor}
	 * @param description {@link MetalArmor#description} of the {@link MetalArmor}
	 * @param weight {@link MetalArmor#weight} of the {@link MetalArmor}
	 * @param value {@link MetalArmor#value} of the {@link MetalArmor}
	 * @param armorType {@link MetalArmor#armorType} of the {@link MetalArmor}
	 * @param armorValue {@link MetalArmor#armorValue} of the {@link MetalArmor}
	 * @param statusApllied {@link MetalArmor#statusApllied} of the {@link MetalArmor} (can be null)
	 * @param occupiedPlace {@link MetalArmor#occupiedPlace} of the {@link MetalArmor}
	 */
	public MetalArmor(final Collection<ITrait> requiredTraits, final String name, final String description, 
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

	@Override
	public int getArmorValue() {
		return armorValue;
	}

	@Override
	public ArmorType getArmorType() {
		return armorType;
	}

	@Override
	public Collection<ITrait> getRequiredTraits() {
		return requiredTraits;
	}

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

	@Override
	public void removeApplieOnEquipe(Actor target) throws GameException {
		Iterator<IStatus> statusIter = statusApllied.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
		
		this.name = this.name.substring(0, name.length() - 3);
	}

	@Override
	public OccupiedPlace getOccupiedPlace() {
		return occupiedPlace;
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
	public int getWeight() {
		return weight;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		try {
			String armorStr = name + " : " + description + System.lineSeparator();

			armorStr += occupiedPlace + " ";
			armorStr += System.lineSeparator() +
					 + armorValue + " " + armorType + " armor" + System.lineSeparator() +
					weight + " Kg, " + value + " $" + System.lineSeparator();
			
			if (!requiredTraits.isEmpty()) {
				armorStr += "required : " + requiredTraits + System.lineSeparator();
			}
			if (!statusApllied.isEmpty()) {
				armorStr += "applies" + statusApllied + System.lineSeparator();
			}
			return  armorStr;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

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
		
			final ITrait currentTargetTrait = actor.getCurrentTrait(currentRequiredTrait.getTraitType());
			
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
