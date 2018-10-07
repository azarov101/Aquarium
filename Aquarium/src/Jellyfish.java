import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Jellyfish extends Swimmable
{
	public Jellyfish() { super(); }
	public Jellyfish(Jellyfish other) { super(other); }
	
	@Override
	public void drawCreature(Graphics g)
	{
		int numLegs;
		
		if (size < 40)
			numLegs = 5;
		else if (size < 80)
			numLegs = 9;
		else
			numLegs = 12;
		
		g.setColor(col);
		g.fillArc(x_front - size/2, y_front - size/4, size, size/2, 0, 180);
		
		for(int i=0; i<numLegs; i++)
			g.drawLine(x_front - size/2 + size/numLegs + size*i/(numLegs+1),
				y_front, x_front - size/2 + size/numLegs + size*i/(numLegs+1),
					y_front+size/3);
		
		if (getAnimalState().toString().equals("Hungry"))
		{ // paint that jellyfish is hungry //
			g.setFont(new Font("Serif",Font.BOLD,20));
		    g.setColor(Color.RED);
	    	g.drawString("Hungry",x_front - size/4,y_front - size/4);
		}
	}	
	
	@Override
	public Object clone() {return new Jellyfish(this);}
	
	@Override
	public Swimmable makeCopy()
	{
		Jellyfish copy = null;
		copy = (Jellyfish)this.clone();

		return copy;
	}
}
