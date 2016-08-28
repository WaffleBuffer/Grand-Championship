package actor.characteristics.status;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.traitModifier.ITraitModifier;
import actor.characteristics.traits.ITrait;
import gameExceptions.GameException;

public interface IStatus {
	
	public enum StatusType {
		ONE_TIME, EACH_TURN, TEMPORARY
	}
	
	public static IStatus copy(final IStatus status) throws Exception {
			switch (status.getType()) {
			case EACH_TURN :
			case TEMPORARY :
				return new EachTurnStatus(status);
			case ONE_TIME :
				return new OneTimeStatus(status);
			default : 
				throw new Exception("Unknown status type");
		}
	}
	
	public StatusType getType();
	
	public String getName();
	
	public String getDescription();
	
	public Collection<ITraitModifier> getTraitModifiers();
	
	public String applyEffect(final Actor target) throws GameException;

	public String removeEffect(final Actor target);
	
	public Boolean isDiplayable();
	
	public int getApplyChances();
	
	public ITrait.TraitType getResistance();
}
