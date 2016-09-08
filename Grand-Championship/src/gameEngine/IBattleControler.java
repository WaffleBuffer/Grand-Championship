package gameEngine;

import java.util.Collection;

import actor.Actor;
import actor.characteristics.status.IStatus;
import gameExceptions.GameException;

/**
 * The battle controler to manage stuff like {@link Actor} order to play.
 * @author Thomas MEDARD
 */
public interface IBattleControler {

	/**
	 * Add an {@link Actor} to the battle
	 * @param actor The {@link Actor} to add.
	 * @return The result's log.
	 */
	public String addActor(final Actor actor);
	
	/**
	 * Make the next {@link Actor} play.
	 * @return The result's log.
	 * @throws GameException Thrown by {@link IStatus#applyEffect(Actor)}.
	 */
	public String nextActor() throws GameException;
	
	/**
	 * Get the {@link Collection} of {@link Actor}.
	 * @return The {@link Collection} of {@link Actor}
	 */
	public Collection<Actor> getActors();
	
	/**
	 * Is the battle finished ?
	 * @return Is the battle finished ?
	 */
	public Boolean isBattleOver();
}
