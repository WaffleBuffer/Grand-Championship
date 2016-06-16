package utilities;

import java.util.Observer;

public interface IObservable {

	public void addObserver(Observer o); 
    public void deleteObserver(Observer o);
    public void notifyObservers();
}
