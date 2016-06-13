package actor.characteristics.status;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;

public interface IStatus {
	
	public enum StatusType {
		ONE_TIME, EACH_TURN
	}
	
	public StatusType type();
	
	public String name();
	
	public String description();
	
	public Collection<ITraitModifier> traitModifiers();
	
	public String applyEffect(final Actor target);

	public String removeEffect(final Actor target);
	
	public Boolean isDiplayable();
}
