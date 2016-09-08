package gameEngine;

import java.util.Collection;

import actor.Actor;

public interface IBattleControler {

	public String addActor(final Actor actor);
	
	public String nextActor() throws Exception;
	
	public Collection<Actor> getActors();
	
	public Boolean isBattleOver();
}
