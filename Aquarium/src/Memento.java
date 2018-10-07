
public class Memento 
{
	private Swimmable animal;
	
	public Memento(Swimmable animal)
	{
		super();
		this.animal = animal;
	}
	
	public Swimmable getSavedState() { return animal; }
}
