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
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;
import objects.equipables.wearables.armors.IArmor;
import objects.equipables.wearables.armors.MetalArmor;
import objects.usables.potions.HealingPotion;

public class ActorTest {

	public static void main(String[] args) {
		try {
			final Actor bob = new Actor("Bob");
			
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.DEXTERITY, -2));
			
			OneTimeStatus sleepy = new OneTimeStatus("Sleepy", "This cannot be so boring right?", modifiedTraits, true, 80, TraitType.DEXTERITY);
			
			System.out.println(bob.tryToResist(sleepy, sleepy.getApplyChances()));
			
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", 
					weaponModifiedTraits, true, 100, null);
			
			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
			Collection<IStatus> attackStatus = new LinkedList<IStatus>();
			modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.WILL, -2));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, -1));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.CONSTITUTION, -1));
			attackStatus.add(new EachTurnStatus("Spoon curse", "You have been cursed by a spoon, seriously?", modifiedTraits,
					4, true, 70, null, IStatus.StatusType.TEMPORARY));
			
			weaponStatuss.add(weaponStatus);
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
					IEquipable.OccupiedPlace.ONE_HAND);
			
			System.out.println(bob.pick(spoon));
			System.out.println(bob.equip(spoon));
			
			Collection<ITrait> required = new LinkedList<ITrait>();;
			required.add(BasicTraitFactory.getBasicTrait(TraitType.STRENGTH, 8));
			
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
					IEquipable.OccupiedPlace.BOTH_HANDS);
			
			try {
				System.out.println(bob.pick(bigHighDoubleHandedSword));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			required = new LinkedList<ITrait>();
			required.add(BasicTraitFactory.getBasicTrait(TraitType.STRENGTH, 6));
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
					IEquipable.OccupiedPlace.BOTH_HANDS);
			
			System.out.println(bob.pick(theBigPoint));
			try {
				System.out.println(bob.equip(theBigPoint));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			MetalArmor metalPlates = new MetalArmor(
					null,
					"Metal plates", 
					"Some good old metal plates",
					30,
					60,
					IArmor.ArmorType.PHYSICAL, 
					20, 
					null, 
					IEquipable.OccupiedPlace.TORSO);
			
			System.out.println(bob.pick(metalPlates));
			
			System.out.println(bob.equip(metalPlates));
			
			//System.out.println(bob.desequip(IEquipable.OccupiedPlace.TORSO));
			
			HealingPotion potion = new HealingPotion(
					"Healing potion", 
					"Get you back on your feets",
					1,
					50,
					50);
			
			System.out.println(bob.takeDamage(bob, 40, IWeapon.DamageType.SMASH));
			
			System.out.println(bob.pick(potion));
			
			System.out.println(potion.use(bob, bob));
			
			System.out.println(bob.equip(spoon));
			System.out.println(bob.weaponAtack(bob));
			
			try {
				System.out.println(bob.equip(theBigPoint));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			System.out.println(bob);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
