package gameEngine.ai;

import actor.Actor;
import gameEngine.IBattleControler;

/**
 * Determines what does the {@link Actor} using this {@link AI}.
 * @author Thomas MEDARD
 */
public interface AI {

	/**
	 * Main function to play the turn.
	 * @param battle The {@link IBattleControler} to have a view of the battlefield.
	 * @return The result's log.
	 */
	public String play(IBattleControler battle);
}
