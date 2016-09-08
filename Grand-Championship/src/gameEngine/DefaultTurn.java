package gameEngine;

import java.util.Collection;
import java.util.Iterator;

import actor.Actor;
import actor.characteristics.status.EachTurnStatus;
import actor.characteristics.status.IStatus.StatusType;
import gameExceptions.GameException;

/**
 * The Default {@link ITurnControler}. Just apply all {@link EachTurnStatus}.
 * @author Thomas MEDARD
 */
public class DefaultTurn implements ITurnControler {

	/**
	 * @see gameEngine.ITurnControler#nextTurn(java.util.Collection)
	 */
	@Override
	public String nextTurn(final Collection<Actor> actors) throws GameException {
		
		String log = "";
		final Iterator<Actor> actorIter = actors.iterator();
		
		while (actorIter.hasNext()) {
			final Actor currentActor = actorIter.next();
			
			final Iterator<EachTurnStatus> statusIter = currentActor.getEachTurnStatus().iterator();
			
			while (statusIter.hasNext()) {
				final EachTurnStatus currentStatus = statusIter.next();
				if (currentStatus.getType() == StatusType.EACH_TURN) {
					log += currentStatus.applyEffect(currentActor) + System.lineSeparator();
				}
				currentStatus.setNbTurns(currentStatus.getNbTurns() - 1);
				if (currentStatus.getNbTurns() <= 0) {
					currentActor.removeStatus(currentStatus);
				}
			}
		}
		return log;
	}

}
