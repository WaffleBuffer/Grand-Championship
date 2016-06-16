package tests;

import java.util.Collection;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import gameEngine.DefaultBattle;
import gameEngine.IBattleControler;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;

public class BattleTest {
	
	public static void main(String[] args) {
		try {
			final Actor bob = new Actor("Bob");
			
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", 
					weaponModifiedTraits, true, 100, null);

			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
			Collection<IStatus> attackStatus = new LinkedList<IStatus>();
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.WILL, -2));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, -1));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.CONSTITUTION, -1));
			attackStatus.add(new EachTurnStatus("Spoon curse", "You have been cursed by a spoon, seriously?", modifiedTraits,
					4, true, 70, null));
			
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
			
			final Actor pop = new Actor("Pop");

			final IBattleControler battleControler = new DefaultBattle();
			battleControler.addActor(bob);
			battleControler.addActor(pop);
			
			for (int i = 0; i < 10; ++i) {
				System.out.println(battleControler.nextActor());
				System.out.println(bob);
				System.out.println(pop);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
