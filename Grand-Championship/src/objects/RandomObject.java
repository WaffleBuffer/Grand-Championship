package objects;

public class RandomObject implements IObject{

	private final String name;
	private final String description;
	private final int weight;
	private final int value;
	
	
	public RandomObject(final String name, final String description, final int weight, final int value) {
		super();
		this.name = name;
		this.description = description;
		this.weight = weight;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		String objectStr = getName() + System.lineSeparator() +
				getDescription() + System.lineSeparator() +
				getWeight() + " Kg, " + getValue() + " $";
		return objectStr;
	}
}
