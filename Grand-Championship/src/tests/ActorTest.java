package tests;

import java.util.Collection;
import java.util.LinkedList;

import actor.Actor;
import actor.characteristics.status.Status;
import actor.characteristics.status.traitModifier.BasicTraitModifier;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;

public class ActorTest {

	public static void main(String[] args) {
		try {
			Actor bob = new Actor("Bob");
			
			Collection<ITraitModifier> modifiedTraits = new LinkedList<ITraitModifier>();
			modifiedTraits.add(new BasicTraitModifier(ITrait.DEXTERITY, -2));
			
			Status status = new Status("Sleepy", "You are not really awaken (not like the force)", modifiedTraits);
			bob.addIStatusr(status);
			
			System.out.println(bob);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
