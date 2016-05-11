package tests;

import actor.Actor;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traitsModifier.BasicTraitModifier;

public class ActorTest {

	public static void main(String[] args) {
		try {
			Actor bob = new Actor("Bob");
			
			bob.addTraitModifier(new BasicTraitModifier("Sleepy", ITrait.DEXTERITY, -2));
			
			System.out.println(bob);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
