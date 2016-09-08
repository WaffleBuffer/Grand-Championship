package actor.characteristics.traits;

import java.util.Observable;

import actor.Actor;
import actor.characteristics.traits.ITrait.TraitType;
import gameExceptions.GameException;

/**
 * Factory to create {@link Stat}.
 * @author Thomas MEDARD
 */
public abstract class StatFactory {

	/**
	 * Factory creation function.
	 * @param type The {@link TraitType} of the {@link Stat}.
	 * @param value The modification value of the {@link Stat}.
	 * @param actor The {@link Actor} on which to add the {@link Stat}.
	 * @return The {@link Stat} created.
	 * @throws GameException If the {@link TraitType} is not supported.
	 */
	public static Stat createState (final ITrait.TraitType type, final int value, final Actor actor) 
			throws GameException {
		switch (type) {
		case CRITICAL :
			
			final Stat critical = new Stat(ITrait.getTraitName(type), type, value) {
				
				@Override
				public void update(Observable o, Object arg) {
					if (arg == null) {
						return;
					}
					try {
						final BasicTrait dext = (BasicTrait) o;
						
						if (dext.getTraitType() == TraitType.DEXTERITY) {
							this.setValue(this.getValue() - (int)arg * 2);
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			BasicTrait trait = (BasicTrait) actor.getCurrentTrait(ITrait.TraitType.DEXTERITY);
			trait.addObserver(critical);
			
			return critical;
		case ARMOR :
			return new Stat(ITrait.getTraitName(type), type, value) {
				
				@Override
				public void update(Observable o, Object arg) {
					
				}
			};
		default :
			throw new GameException ("Unknown stat type", GameException.ExceptionType.UNKNOWN_TRAIT);
		}
	}
}
