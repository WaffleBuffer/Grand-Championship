package objects.equipables;

import utilities.Fonts;

/**
 * An emplacement to equip an {@link IEquipable}.
 * @author Thomas MEDARD
 */
public class ObjectEmplacement {

	/**
	 * The current object occupyng this {@link ObjectEmplacement}. Can be null.
	 */
	private IEquipable equippedObject;
	/**
	 * The {@link PlaceType} of this {@link ObjectEmplacement}.
	 */
	private final PlaceType type;
	
	/**
	 * @author Thomas MEDARD
	 */
	public enum PlaceType {
		/**
		 * One hand location.
		 */
		ONE_HAND("One hand"), 
		/**
		 * Legs location.
		 */
		LEGS("Legs"),
		/**
		 * Head location.
		 */
		HEAD("Head"),
		/**
		 * Torso location.
		 */
		TORSO("Torso");
		
		/**
		 * The displayed name of this {@link ObjectEmplacement}'s name.
		 */
		private final String name;
		
		/**
		 * The constructor
		 * @param name The name (displayed) of the place.
		 */
		private PlaceType(final String name) {
			this.name = name;
		}
		
		/**
		 * @see java.lang.Enum#toString()
		 */
		@Override
		public String toString() {
			return this.name;
		}
	}
	
	/**
	 * The constructor.
	 * @param type The {@link PlaceType} of this {@link ObjectEmplacement}.
	 */
	public ObjectEmplacement(final PlaceType type) {
		this.type = type;
	}
	
	/**
	 * Set the current {@link ObjectEmplacement#equippedObject}.
	 * @param object The {@link IEquipable} to equip.
	 */
	public void setEquippedObject(final IEquipable object) {
		equippedObject = object;
	}
	
	/**
	 * Get the current {@link ObjectEmplacement#equippedObject}.
	 * @return object The current {@link ObjectEmplacement#equippedObject}.
	 */
	public IEquipable getEquipped() {
		return this.equippedObject;
	}
	
	/**
	 * Get the {@link PlaceType} of this {@link ObjectEmplacement}.
	 * @return The {@link PlaceType} of this {@link ObjectEmplacement}.
	 */
	public PlaceType getType() {
		return this.type;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return Fonts.wrapHtml(this.type.toString(), Fonts.LogType.OBJECT_EMPLACEMENT);
	}
}
