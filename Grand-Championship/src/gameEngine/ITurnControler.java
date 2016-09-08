package gameEngine;

import java.util.Collection;

import actor.Actor;
import gameExceptions.GameException;

public interface ITurnControler {

	public String nextTurn(final Collection<Actor> actors) throws GameException;
}
