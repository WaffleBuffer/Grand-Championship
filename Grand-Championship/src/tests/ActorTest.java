package tests;

import java.util.Collection;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;
import objects.equipables.ObjectEmplacement;
import objects.equipables.ObjectEmplacement.PlaceType;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;
import objects.equipables.wearables.armors.IArmor;
import objects.equipables.wearables.armors.Armor;
import objects.usables.potions.HealingPotion;

/**
 * Test class to see if {@link Actor} class funtions as intended
 * @author tmedard
 *
 */
public class ActorTest {

	/**
	 * The main function
	 * @param args args passed when calling this function (unused)
	 */
	public static void main(String[] args) {
		try {
			// Creating the Actor
			final Actor bob = new Actor("Bob");
			
			// Creation of an IStatus
			// Preparation of the effects
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.DEXTERITY, -2));
			
			// Creation
			OneTimeStatus sleepy = new OneTimeStatus("Sleepy", "This cannot be so boring right?", modifiedTraits, true, 80, TraitType.DEXTERITY);
			
			// Trying to applie the IStatus on bob
			System.out.println(bob.tryToResist(sleepy, sleepy.getApplyChances()));
			
			// Preparation of the creation of a special Weapon
			// Creation of the IStatus applied on equiping the Weapon
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", 
					weaponModifiedTraits, true, 100, null);
			
			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
			weaponStatuss.add(weaponStatus);
			
			// Status applied on target on hit
			Collection<IStatus> attackStatus = new LinkedList<IStatus>();
			modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.WILL, -2));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, -1));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.CONSTITUTION, -1));
			attackStatus.add(new EachTurnStatus("Spoon curse", "You have been cursed by a spoon, seriously?", modifiedTraits,
					4, true, 70, null, IStatus.StatusType.TEMPORARY));
			
			Collection<PlaceType> objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			
			// Creating the actual MeleWeapon
			MeleWeapon spoon = new MeleWeapon(
					null, 
					"cursed spoon",
					"It's a spoon but with some dark spirits trapped inside",
					1,
					1, 
					IWeapon.DamageType.SLASH,
					3, 
					weaponStatuss,
					attackStatus,
					objEmps);
			
			// Picking and equiping the Weapon
			System.out.println(bob.pick(spoon));
			System.out.println(bob.equip(spoon));
			
			// Preparation of a heavy Weapon
			// Creation of the required ITrait to equip the Weapon
			Collection<ITrait> required = new LinkedList<ITrait>();;
			required.add(BasicTraitFactory.createBasicTrait(TraitType.STRENGTH, 8));
			
			objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			
			// Creation of the actual MeleWeapon
			MeleWeapon bigHighDoubleHandedSword = new MeleWeapon(
					null, 
					"big High Double Handed Sword",
					"It's a very (too) heavy double handed sword",
					50,
					10, 
					IWeapon.DamageType.SLASH,
					30, 
					null,
					null,
					objEmps);
			
			try {
				// Trying to picking it
				System.out.println(bob.pick(bigHighDoubleHandedSword));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			// Preparation of an nearly impossible to equipe Weapon
			required = new LinkedList<ITrait>();
			required.add(BasicTraitFactory.createBasicTrait(TraitType.STRENGTH, 5));
			
			objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			objEmps.add(ObjectEmplacement.PlaceType.ONE_HAND);
			
			// Creation of the actual MeleWeapon
			MeleWeapon theBigPoint = new MeleWeapon(
					required, 
					"The Big Point",
					"Make your oponent getting to the point",
					15,
					5, 
					IWeapon.DamageType.SMASH,
					20, 
					null, 
					null,
					objEmps);
			
			// Pickin then trying to equip the Weapon
			System.out.println(bob.pick(theBigPoint));
			try {
				System.out.println(bob.equip(theBigPoint));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			objEmps = new LinkedList<PlaceType>();
			objEmps.add(ObjectEmplacement.PlaceType.TORSO);
			
			// Creation of some Armor
			Armor metalPlates = new Armor(
					null,
					"Metal plates", 
					"Some good old metal plates",
					30,
					60,
					IArmor.ArmorType.PHYSICAL, 
					20, 
					null, 
					objEmps);
			
			// Picking then equiping the Armor
			System.out.println(bob.pick(metalPlates));
			System.out.println(bob.equip(metalPlates));
			
			// Creation of an IUsable object
			HealingPotion potion = new HealingPotion(
					"Healing potion", 
					"Get you back on your feets",
					1,
					50,
					50);
			
			// Make Bob hurting himself
			System.out.println(bob.takeDamage(bob, 40, IWeapon.DamageType.SMASH));
			
			// Pick then use the healing potion
			System.out.println(bob.pick(potion));
			System.out.println(potion.use(bob, bob));
			
			// Trying to attack bob with some cursed Weapon
			System.out.println(bob.equip(spoon));
			System.out.println(bob.weaponAtack(bob));
			
			try {
				// Now that bob is probably cursed, try to equip the nearly impossible to equip Weapon
				System.out.println(bob.equip(theBigPoint));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			// Finally, display bob state
			System.out.println(bob);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
