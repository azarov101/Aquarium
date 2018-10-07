
public class MarineAnimalDecorator implements MarineAnimal
{
	private MarineAnimal animal;
	
	public MarineAnimalDecorator(MarineAnimal animalToDecorate) { animal = animalToDecorate; }
	
	@Override
	public void PaintFish() { animal.PaintFish(); }
	
}
