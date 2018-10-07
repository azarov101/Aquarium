import java.util.ArrayList;

public class CareTaker 
{
	ArrayList<Memento> statesList;
	
	public CareTaker() { statesList = new ArrayList<Memento>(); }
	
	public void addMemento(Memento memento) { statesList.add(memento); }
	
	public Memento getMemento(int index) { return statesList.get(index); }
	
	public int getSize() { return statesList.size(); }
	
	public void removeMemento(int index) { statesList.remove(index); }
	
}
