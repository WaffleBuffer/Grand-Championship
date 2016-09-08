package gameEngine;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import actor.Actor;
import utilities.Fonts;

/**
 * The default {@link IBattleControler}.
 * @author Thomas MEDARD
 */
public class DefaultBattle implements IBattleControler {

	/**
	 * The {@link ITurnControler} used by this {@link DefaultBattle}.
	 */
	private final ITurnControler turnControler;
	/**
	 * All the {@link Actor} involved in the battle.
	 */
	private final Collection<Actor> actors;
	/**
	 * The {@link Iterator} pointing to the current {@link Actor} playing.
	 */
	private Iterator<Actor> actorIter;
	/**
	 * The current {@link Actor} playing.
	 */
	private Actor currentActor;
	/**
	 * Is the battle finished?
	 */
	private Boolean over;
	
	/**
	 * The constructor.
	 */
	public DefaultBattle() {
		turnControler = new DefaultTurn();
		actors = new LinkedList<Actor>();
		actorIter = actors.iterator();
		over = false;
	}
	
	/**
	 * @see gameEngine.IBattleControler#addActor(actor.Actor)
	 */
	@Override
	public String addActor(final Actor actor) {
		actors.add(actor);
		
		actorIter = actors.iterator();
		
		while (actorIter.hasNext()) {
			if (actorIter.next() == currentActor) {
				break;
			}
		}
		
		return Fonts.wrapHtml(actor.getName(), Fonts.LogType.ACTOR) + " has joined the fight!";
	}
	
	/**
	 * @see gameEngine.IBattleControler#nextActor()
	 */
	@Override
	public String nextActor() throws Exception {
		String log = "";
		if (actorIter.hasNext()) {
			currentActor = actorIter.next();
			if (currentActor.isDead()) {
				return finishBattle();
			}
			log += currentActor.getName() + " to move" + System.lineSeparator();
			log += currentActor.getAi().play(this) + System.lineSeparator();
			return log;
		}
		else {
			actorIter = actors.iterator();
			if (actorIter.hasNext()) {
				log += turnControler.nextTurn(actors) + System.lineSeparator();
				currentActor = actorIter.next();
				if (currentActor.isDead()) {
					return finishBattle();
				}
				log += currentActor.getName() + " to move" + System.lineSeparator();
				log += currentActor.getAi().play(this) + System.lineSeparator();
				return log;
			}
			else {
				return finishBattle();
			}
		}
	}
	
	/**
	 * Called when the battle is over.
	 * @return "Battle over".
	 */
	private String finishBattle() {
		over = true;
		return "<b>Battle over</b>";
	}

	/**
	 * @see gameEngine.IBattleControler#getActors()
	 */
	@Override
	public Collection<Actor> getActors() {
		return actors;
	}

	/**
	 * @see gameEngine.IBattleControler#isBattleOver()
	 */
	@Override
	public Boolean isBattleOver() {
		return over;
	}
}
