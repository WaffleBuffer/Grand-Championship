package objects.usables;

import actor.Actor;
import objects.IObject;

public interface IUsable extends IObject {

	public String use (final Actor user, final Actor target);
	
	public int getUseTime();
}
