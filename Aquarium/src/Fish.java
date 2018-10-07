import java.awt.*;

public class Fish extends Swimmable
{
	public Fish() { super(); }
	public Fish(Fish other) { super(other); }
		
	@Override
	public void drawCreature(Graphics g)
	{
		g.setFont(new Font("Serif",Font.BOLD,20)); // font for hunger state //
		g.setColor(col);

		if(x_dir==1) // fish swims to right side
		{
			// Body of fish
			g.fillOval(x_front - size, y_front - size/4, size, size/2);
			
			// Tail of fish
			int[] x_t={x_front-size-size/4,x_front-size-size/4,x_front-size};
			int [] y_t = {y_front - size/4, y_front + size/4, y_front};
			Polygon t = new Polygon(x_t,y_t,3);
			g.fillPolygon(t);
			
			// Eye of fish
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(255-col.getRed(),255-col.getGreen(),255-col.getBlue()));
			g2.fillOval(x_front-size/5, y_front-size/10, size/10, size/10);
			
			// Mouth of fish
			if(size>70)
				g2.setStroke(new BasicStroke(3));
			else if(size>30)
				g2.setStroke(new BasicStroke(2));
			else
				g2.setStroke(new BasicStroke(1));
			g2.drawLine(x_front, y_front, x_front-size/10, y_front+size/10);
			g2.setStroke(new BasicStroke(1));
			
			// Hunger State of Fish //
			if (getAnimalState().toString().equals("Hungry"))
			{
			    g.setColor(Color.RED);
		    	g.drawString("Hungry",x_front - size,y_front - size/4);
			}
		}
		else // fish swims to left side
		{	
			// Body of fish
			g.fillOval(x_front, y_front - size/4, size, size/2);
			
			// Tail of fish
			int[] x_t={x_front+size+size/4,x_front+size+size/4,x_front+size};
			int [] y_t = {y_front - size/4, y_front + size/4, y_front};
			Polygon t = new Polygon(x_t,y_t,3);
			g.fillPolygon(t);
			
			// Eye of fish
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(255-col.getRed(),255-col.getGreen(),255-col.getBlue()));
			g2.fillOval(x_front+size/10, y_front-size/10, size/10, size/10);
			
			// Mouth of fish
			if(size>70)
				g2.setStroke(new BasicStroke(3));
			else if(size>30)
				g2.setStroke(new BasicStroke(2));
			else
				g2.setStroke(new BasicStroke(1));
			g2.drawLine(x_front, y_front, x_front+size/10, y_front+size/10);
			g2.setStroke(new BasicStroke(1));
			
			// Hunger State of Fish //
			if (getAnimalState().toString().equals("Hungry"))
			{
			    g.setColor(Color.RED);
		    	g.drawString("Hungry",x_front,y_front - size/4);			    
			}
		}
	}
	@Override
	public Object clone() { return new Fish(this); }
	 
	@Override
	public Swimmable makeCopy()
	{
		Fish copy = null;
		copy = (Fish)this.clone();

		return copy;
	}
}
