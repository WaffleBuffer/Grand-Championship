package actor.characteristics.status;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;

public interface IStatus {
	
	public enum StatusType {
		ONE_TIME, EACH_TURN, TEMPORARY
	}
	
	public StatusType getType();
	
	public String getName();
	
	public String getDescription();
	
	public Collection<ITraitModifier> getTraitModifiers();
	
	public String applyEffect(final Actor target) throws Exception;

	public String removeEffect(final Actor target);
	
	public Boolean isDiplayable();
	
	public int getApplyChances();
	
	public ITrait.TraitType getResistance();
}
