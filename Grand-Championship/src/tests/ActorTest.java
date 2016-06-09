package tests;

import java.util.Collection;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;

public class ActorTest {

	public static void main(String[] args) {
		try {
			Actor bob = new Actor("Bob");
			
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.DEXTERITY, -2));
			
			OneTimeStatus sleepy = new OneTimeStatus("Sleepy", "This cannot be so boring right?", modifiedTraits);
			bob.addIStatus(sleepy);
			
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", weaponModifiedTraits);
			
			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
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
					IEquipable.OccupiedPlace.ONE_HAND);
			
			bob.pick(spoon);
			
			spoon.equipe(bob);
			spoon.desequipe(bob);
			
			MeleWeapon bigHighDoubleHandedSword = new MeleWeapon(
					null, 
					"big High Double Handed Sword",
					"It's a very (too) heavy double handed sword",
					50,
					10, 
					IWeapon.DamageType.SLASH,
					30, 
					null, 
					IEquipable.OccupiedPlace.BOTH_HANDS);
			
			try {
				bob.pick(bigHighDoubleHandedSword);
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
