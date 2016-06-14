package objects.usables.potions;

import actor.Actor;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;
import objects.RandomObject;

public class HealingPotion implements IPotion {
	private final String name;
	private final String description;
	private final int weight;
	private final int value;
	private final int healing;
	private int useTime;
	
	public HealingPotion(final String name, final String description, final int weight, final int value, final int healing) {
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
		this.healing = healing;
		useTime = 1;
	}

	@Override
	public String use(Actor user, Actor target) {

		final int max = target.getBasicTrait(TraitType.VITALITY).getValue();
		final int vitality = target.getCurrentTrait(TraitType.VITALITY).getValue();
		int realHealing = healing;
		if (max - vitality < healing) {
			realHealing -= healing - (max - vitality);
			target.getCurrentTrait(TraitType.VITALITY).setValue(target.getCurrentTrait(TraitType.VITALITY).getValue() + 
					realHealing);
		}
		else {
			target.getCurrentTrait(TraitType.VITALITY).setValue(target.getCurrentTrait(TraitType.VITALITY).getValue() +
					realHealing);
		}
		user.throwObject(this);
		try {
			user.pick(new RandomObject(
					"Empty bottle",
					"It's just waiting to be filled",
					getWeight(),
					2));
		} 
		catch (GameException e) {
			e.printStackTrace();
		}
		return target.getName() + " has been healed for " + realHealing + " PV";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String potionStr = getName() + System.lineSeparator() +
				healing + " PV" + System.lineSeparator() +
				getDescription() + System.lineSeparator() +
				getWeight() + " Kg, " + getValue() + " $";
		return potionStr;
	}

	@Override
	public int getUseTime() {
		return useTime;
	}
}
