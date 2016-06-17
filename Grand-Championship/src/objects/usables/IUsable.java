package objects.usables;

import actor.Actor;
import objects.IObject;

/**
 * Usable object
 * @author tmedard
 * @see IObject
 */
public interface IUsable extends IObject {

	/**
	 * The function to use the {@link IUsable}
	 * @param user The {@link Actor} using the {@link IUsable}
	 * @param target the {@link Actor} on which to use the {@link IUsable}
	 * @return The log of the action
	 */
	public String use (final Actor user, final Actor target);
	
	/**
	 * The number of time left that the {@link IUsable} can be used
	 * @return The number of time left
	 */
	public int getUseTime();
}
