package gameEngine.ai;

import java.util.Iterator;

import actor.Actor;
import gameEngine.IBattleControler;

public class DefaultAI implements AI {
	
	private Actor actor;
	
	public DefaultAI (final Actor actor) {
		this.actor = actor;
	}

	@Override
	public String play(IBattleControler battle) {
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
