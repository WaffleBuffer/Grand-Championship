package actor.characteristics.traits;

import java.util.Observable;

public abstract class StatFactory {

	public static Stat createState (final ITrait.TraitType type, final int value, final Observable[] dependencys) throws Exception {
		switch (type) {
		case CRITICAL :
			
			if (dependencys.length <= 0) {
				throw new Exception ("need at least one dependency");
			}
			final Stat critical = new Stat(ITrait.getTraitName(type), type, value) {
				
				@Override
				public void update(Observable o, Object arg) {
					if (arg == null) {
						return;
					}
					
					this.setValue(this.getValue() - (int)arg * 2);
				}
			};
			
			for (Observable depdence : dependencys) {
				depdence.addObserver(critical);
			}
			
			return critical;
		case ARMOR :
			return new Stat(ITrait.getTraitName(type), type, value) {
				
				@Override
				public void update(Observable o, Object arg) {
					
				}
			};
		default :
			throw new Exception ("Unknown stat type");
		}
	}
}
