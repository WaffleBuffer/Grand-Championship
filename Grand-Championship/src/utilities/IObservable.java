package utilities;

import java.util.Observer;

/**
 * Interface allowing some objects to be {@link java.util.Observable} via {@code interface} and making implementing classes extending {@link java.util.Observable}
 * @author tmedard
 * @see java.util.Observable
 */
public interface IObservable {

	/**
	 * Add an {@link Observer} to this {@link IObservable}
	 * @param o the {@link Observer} to add
	 * @see java.util.Observable#addObserver(Observer)
	 */
	public void addObserver(Observer o);
	/**
	 * Delete an {@link Observer} of this {@link IObservable}
	 * @param o the {@link Observer} to delete
	 * @see java.util.Observable#deleteObserver(Observer)
	 */
    public void deleteObserver(Observer o);
    /**
	 * 
	 * @see java.util.Observable#notifyObservers(Object)
	 */
    public void notifyObservers();
}
