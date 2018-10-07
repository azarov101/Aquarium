
public class Hungry implements HungerState
{

	@Override
	public void changeState(Swimmable animal) 
	{
		animal.setAnimalState(this);
	}
   public String toString() { return "Hungry"; }
}
