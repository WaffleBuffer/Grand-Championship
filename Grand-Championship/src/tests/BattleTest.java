package tests;

import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import actor.Actor;
import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus;
import actor.characteristics.status.OneTimeStatus;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import gameEngine.DefaultBattle;
import gameEngine.IBattleControler;
import hmi.LogScreen;
import objects.equipables.IEquipable;
import objects.equipables.weapons.IWeapon;
import objects.equipables.weapons.meleWeapons.MeleWeapon;
import objects.equipables.wearables.armors.IArmor;
import utilities.Fonts;
import objects.equipables.wearables.armors.Armor;

/**
 * Test the battle system
 * @author tmedard
 *
 */
public class BattleTest {
	
	/**
	 * The main function
	 * @param args Arguments passed to the main function (unused)
	 */
	public static void main(String[] args) {
		try {
			// Creating the log's screen.
			final LogScreen log = new LogScreen();
						
			// The champion !
			final Actor bob = new Actor("Bob");
			
			// Preparation of his main weapon
			Collection<ITraitModifier> weaponModifiedTraits = new LinkedList<ITraitModifier>();
			weaponModifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, +1));
			OneTimeStatus weaponStatus = new OneTimeStatus("Better strength", "Equiping this makes you feel stronger", 
					weaponModifiedTraits, true, 100, null);

			// On equip
			Collection<IStatus> weaponStatuss = new LinkedList<IStatus>();
			
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.WILL, -2));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.STRENGTH, -1));
			modifiedTraits.add(new BasicTraitModifier(ITrait.TraitType.CONSTITUTION, -1));
			weaponStatuss.add(weaponStatus);
			
			// On hit
			Collection<IStatus> attackStatus = new LinkedList<IStatus>();
			attackStatus.add(new EachTurnStatus("Spoon curse", "You have been cursed by a spoon, seriously?", modifiedTraits,
					4, true, 70, null, IStatus.StatusType.TEMPORARY));
			
			
			// Creating his main weapon
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
			
			// Picking and equipping his main weapon
			log.displayLog(bob.equip(spoon));
			
			// Creation of some Armor (yeah, bob is overpowered)
			Armor metalPlates = new Armor(
					null,
					"Metal plates", 
					"Some good old metal plates",
					30,
					60,
					IArmor.ArmorType.PHYSICAL, 
					20, 
					null, 
					IEquipable.OccupiedPlace.TORSO);
			
			// Picking then equipping the Armor
			System.out.println(bob.pick(metalPlates));
			System.out.println(bob.equip(metalPlates));
			
			// Creating his challenger (poor guy)
			final Actor pop = new Actor("Pop");
			
			// Bob is cheating! He attacks before the actual fight!
			System.out.println(bob.weaponAtack(pop));
			// So now bob will desequip and drop his weapon
			System.out.println(bob.desequip(spoon));
			System.out.println(bob.drop(spoon));

			// Preparation of the battle
			final IBattleControler battleControler = new DefaultBattle();
			battleControler.addActor(bob);
			battleControler.addActor(pop);
			
			// Now let them fight for 10 turns
			for (int i = 0; i < 10; ++i) {
				System.out.println(battleControler.nextActor());
				System.out.println(bob);
				System.out.println(pop);
			}
			
			// And now fight to the death
			while(!battleControler.isBattleOver()) {
				System.out.println(battleControler.nextActor());
			}
			
			// Who won? Who's next? You decide! Epic Battle of the History!!!!!!!!!
			System.out.println(bob);
			System.out.println(pop);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
