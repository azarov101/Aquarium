
public interface Observable 
{
	public void addObserver(Observer observerToAdd);
	public void removeObserver(Observer observerToRemove);
	public void notifyObservers();
}
