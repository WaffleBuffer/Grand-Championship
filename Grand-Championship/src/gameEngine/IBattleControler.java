package gameEngine;

import java.util.Collection;

import actor.Actor;

public interface IBattleControler {

	public void addActor(final Actor actor);
	
	public String nextActor() throws Exception;
	
	public Collection<Actor> getActors();
	
	public Boolean isBattleOver();
}
