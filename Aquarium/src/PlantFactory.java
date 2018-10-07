
public class PlantFactory implements AbstractSeaFactory
{

	@Override
	public SeaCreature produceSeaCreature(String type)
	{
	    if(type.equalsIgnoreCase("ZOSTERA"))
	    	return new Zostera();
	    if(type.equalsIgnoreCase("LAMINARIA"))
	    	return new Laminaria();
	    
		return null;
	}	
}
