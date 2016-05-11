package actor.characteristics.status;

import java.util.Collection;

import actor.characteristics.status.traitModifier.ITraitModifier;

public class Status implements IStatus {
	
	private String name;
	private String description;
	private Collection<ITraitModifier> traitModifiers;

	public Status(String name, String description, Collection<ITraitModifier> traitModifiers) {
		super();
		this.name = name;
		this.description = description;
		this.traitModifiers = traitModifiers;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public Collection<ITraitModifier> traitModifiers() {
		return traitModifiers;
	}

	@Override
	public String toString() {
		return "\n" + name + " :\n" + description + "\n" + traitModifiers;
	}
}
