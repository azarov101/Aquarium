import java.awt.Color;
import java.awt.Graphics;

public abstract class Immobile implements SeaCreature
{
	protected static int ID_COUNTER = 0;
	protected int id;
	protected String name;
	protected int size;
	protected int x, y;
	protected Color col;
	protected AquaPanel panel;
	
	public Immobile()
	{
		id = ++ID_COUNTER;
		name = getClass().getName();
		col = null;
		size = 0;
		x = y = 1 ;
		col = Color.GREEN;
	}
	
	public void setFields(AquaPanel panel, int size, int x, int y)
	{
		this.panel = panel;
		this.size = size;
		this.x = x;
		this.y = y;
	}
	
	 abstract public void drawCreature(Graphics g);
	 
	 public long getId() { return id; }
	 public int getSize() { return size; }
	 public String getPlantName() { return getClass().getName(); }	 
	 public int getXLocation() { return x; }
	 public int getYLocation() { return y; }
	 public String getColor()
	 {
		 return "("+col.getRed()+","+col.getGreen()+","+col.getBlue()+")";
	 }
	 
	 public void setSize(int size) { this.size = size; }
	 public void setXLocation(int x) { this.x = x; }
	 public void setYLocation(int y) { this.y = y; }
}
