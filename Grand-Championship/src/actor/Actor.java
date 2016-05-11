package actor;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

import actor.characteristics.traits.BasicTraitFactory;
import actor.characteristics.traits.ITrait;
import actor.characteristics.traitsModifier.ITraitModifier;

public class Actor extends Observable{
	
	public static final int DEFAULT_TRAIT_VALUE = 5;

	private String name;
	
	private Set<ITrait> traits;
	private Collection<ITraitModifier> traitModifiers;
	
	public Actor (String name) throws Exception {
		super();
		
		this.name = name;
		this.traits = new HashSet<ITrait>();
		this.traitModifiers = new LinkedList<ITraitModifier>();
		
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.VITALITY, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.STRENGTH, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.DEXTERITY, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.CONSTITUTION, DEFAULT_TRAIT_VALUE));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.WILL, DEFAULT_TRAIT_VALUE));
	}
	
	public Actor (String name, int Vitality, int strength, int dexterity, int constitution, int will) throws Exception {
		super();
		
		this.name = name;
		this.traits = new HashSet<ITrait>();
		
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.VITALITY, Vitality));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.STRENGTH, strength));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.DEXTERITY, dexterity));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.CONSTITUTION, constitution));
		traits.add(BasicTraitFactory.getBasicTrait(ITrait.WILL, will));
	}
	
	public void addTraitModifier(ITraitModifier traitModifier) {
		traitModifiers.add(traitModifier);
		
		Iterator<ITrait> traitIter = traits.iterator();
		
		while (traitIter.hasNext()) {
			ITrait currentTrait = traitIter.next();
			
			if (currentTrait.traitType() == traitModifier.traitType()) {
				
				currentTrait.setValue(currentTrait.value() + traitModifier.value());
			}
		}
	}
	
	public String name() {
		return this.name;
	}
	
	/*public Collection<ITraitModifier> traitModifiers() {
		return traitModifiers;
	}*/

	@Override
	public String toString() {
		return "Character [\n"
				+ "name=" + name + ",\n"
				+ "traits=" + traits + "\n"
				+ "traits modifiers=" + traitModifiers + "]";
	}
}
