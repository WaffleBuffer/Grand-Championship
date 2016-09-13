package objects;

/**
 * 
 * @author Thomas MEDARD
 */
public interface IObject {

	/**
	 * Get the name of the {@link IObject}.
	 * @return The name of the {@link IObject}.
	 */
	public String getName();
	
	/**
	 * Get the description of the {@link IObject}.
	 * @return The description of the {@link IObject}.
	 */
	public String getDescription();
	
	/**
	 * Get the weight the description of the {@link IObject}.
	 * @return The description of the {@link IObject}.
	 */
	public int getWeight();
	
	/**
	 * Get the value of the {@link IObject}.
	 * @return The value of the {@link IObject}.
	 */
	public int getValue();
}
