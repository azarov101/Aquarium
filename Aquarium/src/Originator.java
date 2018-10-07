
public class Originator 
{
	private Swimmable animal;
	
	public void setState(Swimmable animal) 
	{ 
		this.animal = animal.makeCopy(); // duplicate animal //
		this.animal.setX_dir(animal.getX_dir());
		this.animal.setY_dir(animal.getY_dir());
		this.animal.setXLocation(animal.getXLocation());
		this.animal.setYLocation(animal.getYLocation());
	} 
	
	public Swimmable getState() { return animal; }
	
	public Memento createMemento() 
	{
		return new Memento(animal);
	}
	
	public void setMemento(Memento memento) { animal = memento.getSavedState(); }
}
