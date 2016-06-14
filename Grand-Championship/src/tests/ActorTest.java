package tests;

import java.util.Collection;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;
import objects.equipables.IEquipable;
import objects.equipables.IEquipable.OccupiedPlace;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;
import objects.equipables.wearables.armors.IArmor;
import objects.equipables.wearables.armors.MetalArmor;
import objects.usables.potions.HealingPotion;

public class ActorTest {

	public static void main(String[] args) {
		try {
			Actor bob = new Actor("Bob");
			
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.DEXTERITY, -2));
			
			OneTimeStatus sleepy = new OneTimeStatus("Sleepy", "This cannot be so boring right?", modifiedTraits, true);
			
			System.out.println(bob.addIStatus(sleepy));
			
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", 
					weaponModifiedTraits, true);
			
			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
			Collection<OccupiedPlace> occupiedPlaces = new LinkedList<OccupiedPlace>();
			occupiedPlaces.add(IEquipable.OccupiedPlace.ONE_HAND);
			weaponStatuss.add(weaponStatus);
			MeleWeapon spoon = new MeleWeapon(
					null, 
					"spoon",
					"It's a spoon",
					1,
					1, 
					IWeapon.DamageType.SLASH,
					3, 
					weaponStatuss, 
					occupiedPlaces);
			
			System.out.println(bob.pick(spoon));
			System.out.println(bob.equip(spoon));
			
			Collection<ITrait> required = new LinkedList<ITrait>();
			occupiedPlaces = new LinkedList<OccupiedPlace>();
			occupiedPlaces.add(IEquipable.OccupiedPlace.BOTH_HANDS);
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
					occupiedPlaces);
			
			try {
				System.out.println(bob.pick(bigHighDoubleHandedSword));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			required = new LinkedList<ITrait>();
			//required.add(BasicTraitFactory.getBasicTrait(TraitType.DEXTERITY, 6));
			MeleWeapon theBigPoint = new MeleWeapon(
					required, 
					"The Big Point",
					"Make your oponent getting to the point",
					15,
					5, 
					IWeapon.DamageType.SMASH,
					20, 
					null, 
					occupiedPlaces);
			
			System.out.println(bob.pick(theBigPoint));
			try {
				System.out.println(bob.equip(theBigPoint));
			}
			catch (GameException e) {
				e.printStackTrace();
			}
			
			occupiedPlaces = new LinkedList<OccupiedPlace>();
			occupiedPlaces.add(IEquipable.OccupiedPlace.TORSO);
			MetalArmor metalPlates = new MetalArmor(
					null,
					"Metal plates", 
					"Some good old metal plates",
					30,
					60,
					IArmor.ArmorType.PHYSICAL, 
					20, 
					null, 
					occupiedPlaces);
			
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
			
			System.out.println(bob);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
