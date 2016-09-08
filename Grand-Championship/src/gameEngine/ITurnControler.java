package gameEngine;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.IStatus;
import gameExceptions.GameException;

/**
 * Controls the beginning of the turn like the effects that should apply to an {@link Actor}.
 * @author Thomas MEDARD
 */
public interface ITurnControler {

	/**
	 * Called at the beginning of the {@link Actor}'s turn.
	 * @param actors The {@link Actor}.
	 * @return The result's log.
	 * @throws GameException Thrown by {@link IStatus#applyEffect(Actor)}.
	 */
	public String nextTurn(final Collection<Actor> actors) throws GameException;
}
