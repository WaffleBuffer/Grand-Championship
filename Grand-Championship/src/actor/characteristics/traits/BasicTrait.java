package actor.characteristics.traits;

public class BasicTrait implements ITrait {

	private String name;
	private int    type;
	private int    value;
	
	public BasicTrait (String name, int type, int value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	@Override
	public String name() {
		return name;
	}

	@Override
	public int traitType() {
		return type;
	}

	@Override
	public int value() {
		return value;
	}
	
	@Override
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return name + " = " + value;
	}
}
