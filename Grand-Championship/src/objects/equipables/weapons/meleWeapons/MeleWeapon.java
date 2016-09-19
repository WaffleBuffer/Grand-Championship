package objects.equipables.weapons.meleWeapons;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.equipables.ObjectEmplacement.PlaceType;
import objects.equipables.weapons.IWeapon;
import utilities.Fonts;

/**
 * It's an {@link IWeapon} with no range.
 * @author Thomas MEDARD
 */
public class MeleWeapon implements IWeapon {
	
	/**
	 * The {@link objects.equipables.IEquipable.EquipableType} of this {@link MeleWeapon}.
	 */
	private final EquipableType type = EquipableType.WEAPON;
	
	/**
	 * The required {@link ITrait} to equip this.
	 */
	private Collection<ITrait> requiredTraits;
	/**
	 * The name of this.
	 */
	private String name;
	/**
	 * The description of this.
	 */
	private final String description;
	/**
	 * The weight of this.
	 */
	private final int weight;
	/**
	 * The value (in gold) of this.
	 */
	private final int value;
	/**
	 * The damage type of this.
	 */
	private final DamageType damageType;
	/**
	 * The raw damage value of this.
	 */
	private final int damageValue;
	/**
	 * The {@link Collection} of {@link IStatus} to apply on the owner when equipping this.
	 */
	private Collection<IStatus> statusAplliedOnEquip;
	/**
	 * The {@link Collection} of {@link IStatus} to apply on the target when attacking someone.
	 */
	private Collection<IStatus> statusAplliedOnAttack;
	/**
	 * The free locations on the body required to equip this.
	 */
	private final Collection<PlaceType> objectEmplacements;
	/**
	 * The damage multiplier when the attack is critical.
	 */
	private int criticalMultiplier;
	
	/**
	 * The constructor.
	 * @param requiredTraits The required {@link ITrait} to equip this. Can be null.
	 * @param name The name of this.
	 * @param description The description of this.
	 * @param weight The weight of this.
	 * @param value The value (in gold) of this.
	 * @param damageType The damage type of this.
	 * @param damageValue The raw damage value of this.
	 * @param statusApllied The {@link Collection} of {@link IStatus} to apply on the owner when equipping this. Can be null.
	 * @param statusAplliedOnAttack The {@link Collection} of {@link IStatus} to apply on the target when attacking someone. Can be null.
	 * @param objectEmplacements The locations on the body of this.
	 */
	public MeleWeapon(final Collection<ITrait> requiredTraits, final String name, final String description, 
			final int weight, final int value, final DamageType damageType, final int damageValue, 
			final Collection<IStatus> statusApllied, final Collection<IStatus> statusAplliedOnAttack, 
			final Collection<PlaceType> objectEmplacements) {
		super();
		
		this.requiredTraits = requiredTraits;
		if (requiredTraits == null) {
			this.requiredTraits = new LinkedList<ITrait>();
		}
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.damageType = damageType;
		this.damageValue = damageValue;
		this.statusAplliedOnEquip = statusApllied;
		if (statusApllied == null) {
			this.statusAplliedOnEquip = new LinkedList<IStatus>();
		}
		this.statusAplliedOnAttack = statusAplliedOnAttack;
		if (statusAplliedOnAttack == null) {
			this.statusAplliedOnAttack = new LinkedList<IStatus>();
		}
		this.objectEmplacements = objectEmplacements;
		
		// By default, critical attack doubles the damages.
		this.criticalMultiplier = 2;
	}

	/**
	 * @see objects.equipables.IEquipable#getRequiredTraits()
	 */
	@Override
	public Collection<ITrait> getRequiredTraits() {
		return requiredTraits;
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
	 * @see objects.equipables.weapons.IWeapon#getDamageType()
	 */
	@Override
	public DamageType getDamageType() {
		return damageType;
	}

	/**
	 * @see objects.equipables.weapons.IWeapon#getDamageValue()
	 */
	@Override
	public int getDamageValue() {
		return damageValue;
	}

	/**
	 * @see objects.equipables.IEquipable#applieOnEquipe(actor.Actor)
	 */
	@Override
	public String applieOnEquipe(Actor target) throws GameException {
		
		Iterator<IStatus> statusIter = statusAplliedOnEquip.iterator();
		
		String log = "";
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			log += target.addIStatus(currentStatus);
			if (statusIter.hasNext()){
				log += "<br>";
			}
		}
		
		this.name += "(E)";
		return log;
	}

	/**
	 * @see objects.equipables.IEquipable#removeApplieOnEquipe(actor.Actor)
	 */
	@Override
	public void removeApplieOnEquipe(Actor target) throws GameException {
		Iterator<IStatus> statusIter = statusAplliedOnEquip.iterator();
		
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			target.removeStatus(currentStatus);
		}
		
		this.name = this.name.substring(0, name.length() - 3);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			String weaponStr = Fonts.wrapHtml(name, Fonts.LogType.OBJECT) + " : " + description + "<br>";
			
			weaponStr += objectEmplacements + "<br>";
			weaponStr += Fonts.wrapHtml(Integer.toString(damageValue), Fonts.LogType.DAMAGE_PHYS) + " " + damageType + " damage<br>" +
					weight + " Kg; " + Fonts.wrapHtml(value + " $", Fonts.LogType.MONEY) + "<br>";
			
			if (!requiredTraits.isEmpty()) {
				weaponStr += "required : " + requiredTraits + "<br>";
			}
			if (!statusAplliedOnEquip.isEmpty()) {
				weaponStr += "applies : " + statusAplliedOnEquip + "<br>";
			}
			if (!statusAplliedOnAttack.isEmpty()) {
				weaponStr += "on hit : " + statusAplliedOnAttack + "<br>";
			}
			return  weaponStr;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @see objects.equipables.IEquipable#getObjectEmplacements()
	 */
	@Override
	public Collection<PlaceType> getObjectEmplacements() {
		return objectEmplacements;
	}

	/**
	 * @see objects.equipables.weapons.IWeapon#attack(actor.Actor, java.lang.Boolean, actor.Actor)
	 */
	@Override
	public String attack(final Actor target, final Boolean critical, final Actor origin) throws GameException {
		Iterator<IStatus> statusIter = statusAplliedOnAttack.iterator();
		
		String log = "";
		while (statusIter.hasNext()) {
			IStatus currentStatus = statusIter.next();
			
			float applyChances = currentStatus.getApplyChances();
			
			if (critical) {
				applyChances *= 1.2;
			}
			
			if (applyChances > 100) {
				applyChances = 100;
			}
			log += target.tryToResist(currentStatus, applyChances) + "<br>";
		}	
		
		int damageValue = this.getDamageValue();
		
		if (critical) {
			damageValue *= this.criticalMultiplier;
		}
		
		log += target.takeDamage(origin, damageValue, this.getDamageType());
		return log;
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
	
	/**
	 * Create an {@link IWeapon} representing the fists. (used when attacking without equipped weapon).
	 * @param damage The damages of the fists.
	 * @return The fists created.
	 */
	public static MeleWeapon getFists (final int damage) {
		Collection<PlaceType> hands = new LinkedList<PlaceType>();
		
		hands.add(PlaceType.ONE_HAND);
		hands.add(PlaceType.ONE_HAND);
		
		return new MeleWeapon(
				null,
				"Fists",
				"You know whene they say : \"Do it by yourself\".",
				0,
				0,
				DamageType.SMASH,
				damage,
				null,
				null,
				hands);
	}

	/**
	 * @see objects.equipables.weapons.IWeapon#getCriticalMultiplier()
	 */
	@Override
	public int getCriticalMultiplier() {
		return this.criticalMultiplier;
	}

	/**
	 * @see objects.equipables.weapons.IWeapon#setCriticalMultiplier(int)
	 */
	@Override
	public void setCriticalMultiplier(int criticalMultiplier) {
		this.criticalMultiplier = criticalMultiplier;
	}

	/**
	 * @see objects.equipables.IEquipable#getType()
	 */
	@Override
	public EquipableType getType() {
		return this.type;
	}
}
