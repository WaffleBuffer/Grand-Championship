package actor.characteristics.status;

import java.util.Collection;

import actor.characteristics.status.traitModifier.ITraitModifier;

public interface IStatus {
	
	public String name();
	
	public String description();
	
	public Collection<ITraitModifier> traitModifiers();
}
