
public class AnimalFactory implements AbstractSeaFactory
{
	@Override
	public SeaCreature produceSeaCreature(String type)
	{       	       
	    if(type.equalsIgnoreCase("FISH"))
	    	return new Fish();
	    if(type.equalsIgnoreCase("JELLYFISH"))
	    	return new Jellyfish();
	        
	    return null;
	}

	
}
