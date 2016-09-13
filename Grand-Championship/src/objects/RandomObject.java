package objects;

/**
 * An {@link IObject} but without any uses. It can only be picked.
 * @author Thomas MEDARD
 */
public class RandomObject implements IObject{

	/**
	 * The name of the {@link RandomObject}.
	 */
	private final String name;
	/**
	 * The description of the {@link RandomObject}.
	 */
	private final String description;
	/**
	 * The weight of the {@link RandomObject}.
	 */
	private final int weight;
	/**
	 * The value of the {@link RandomObject}.
	 */
	private final int value;
	
	
	/**
	 * The constructor
	 * @param name The name of the {@link RandomObject}.
	 * @param description The description of the {@link RandomObject}.
	 * @param weight The weight of the {@link RandomObject}.
	 * @param value The value of the {@link RandomObject}.
	 */
	public RandomObject(final String name, final String description, final int weight, final int value) {
		super();
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
	}

	/**
	 * @see objects.IObject#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @see objects.IObject#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @see objects.IObject#getWeight()
	 */
	@Override
	public int getWeight() {
		return weight;
	}

	/**
	 * @see objects.IObject#getValue()
	 */
	@Override
	public int getValue() {
		return value;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String objectStr = getName() + System.lineSeparator() +
				getDescription() + System.lineSeparator() +
				getWeight() + " Kg, " + getValue() + " $";
		return objectStr;
	}
}
