package gameEngine;

import java.util.Collection;

import actor.Actor;

public interface ITurnControler {

	public String nextTurn(final Collection<Actor> actors) throws Exception;
}
