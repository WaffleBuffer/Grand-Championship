package actor.characteristics.traits;

public interface ITrait {
	
	public static final int VITALITY     = 0;
	public static final int STRENGTH     = 1;
	public static final int DEXTERITY    = 2;
	public static final int CONSTITUTION = 3;
	public static final int WILL         = 4;
	
	public static final String[] TRAITS_STR = {"Vitality", "Strength", "Dexterity", "Constitution", "Will"};

	public String name();
	
	public int traitType();
	
	public int value();
	
	public void setValue(int value);
}
