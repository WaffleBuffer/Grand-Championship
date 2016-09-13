package gameEngine.ai;

import java.util.Iterator;

import actor.Actor;
import gameEngine.IBattleControler;

/**
 * The default {@link AI}. It's just attacking with weapon for now.
 * @author Thomas MEDARD
 */
public class DefaultAI implements AI {
	
	/**
	 * The {@link Actor} controlled by this {@link AI}.
	 */
	private Actor actor;
	
	/**
	 * The constructor
	 * @param actor The {@link Actor} to link this {@link DefaultAI} to.
	 */
	public DefaultAI (final Actor actor) {
		this.actor = actor;
	}

	/**
	 * @see gameEngine.ai.AI#play(gameEngine.IBattleControler)
	 */
	@Override
	public String play(IBattleControler battle) {
		if (actor.isDead()) {
			return actor.getName() + " is dead and can't do anything";
		}
		Iterator<Actor> actorIter = battle.getActors().iterator();
		
		while (actorIter.hasNext()) {
			final Actor currentActor = actorIter.next();
			
			if (currentActor !=  actor) {
				return actor.weaponAtack(currentActor);
			}
		}
		return "";
	}

}
