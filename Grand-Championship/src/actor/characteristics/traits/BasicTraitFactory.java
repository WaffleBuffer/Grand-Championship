package actor.characteristics.traits;

import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;

/**
 * A factory to create {@link BasicTrait}.
 * @author Thomas MEDARD
 */
public abstract class BasicTraitFactory {

	/**
	 * The factory of {@link BasicTrait}.
	 * @param type The {@link TraitType}.
	 * @param value The value of the {@link BasicTrait}.
	 * @return The {@link BasicTrait}.
	 * @throws GameException If the {@link TraitType} isn't supported.
	 */
	public static BasicTrait createBasicTrait (final TraitType type, final int value) throws GameException{
		switch (type) {
			case VITALITY :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.VITALITY, value);
			case STRENGTH :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.STRENGTH, value);
			case DEXTERITY : 
					return new BasicTrait(ITrait.getTraitName(type), TraitType.DEXTERITY, value);
			case CONSTITUTION :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.CONSTITUTION, value);
			case WILL :
					return new BasicTrait(ITrait.getTraitName(type), TraitType.WILL, value);
			default :
				throw new GameException ("Unsupported type of BasicTrait", GameException.ExceptionType.UNKNOWN_BASICTRAIT);
		}
	}
}