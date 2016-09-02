package actor.characteristics.traits;

import java.util.Observable;

import actor.Actor;
import gameExceptions.GameException;

public abstract class StatFactory {

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
