package actor.characteristics.status;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;

public interface IStatus {
	
	public static final int ONE_TIME_STATUS = 0;
	
	public static final int EACH_TURN_STATUS = 1;
	
	public int type();
	
	public String name();
	
	public String description();
	
	public Collection<ITraitModifier> traitModifiers();
	
	public String applyEffect(Actor target);
}
