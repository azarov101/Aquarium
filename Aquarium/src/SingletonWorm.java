import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class SingletonWorm
{
	private static volatile SingletonWorm instance = null; // thread safe //

    private SingletonWorm() {}

    public static SingletonWorm GetInstance()
    {
       if (instance == null)
          synchronized(SingletonWorm.class)
          {   
              if (instance == null)
              {
                  instance = new SingletonWorm();
              }
          }
       return instance;
    }
    
    public void SetInstance() { instance = null; }
    
	public void drawWorm(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(new BasicStroke(3));
		g2.setColor(Color.red);
		g2.drawArc(AquaPanel.PANEL_WIDTH/2, (AquaPanel.PANEL_HEIGHT/2)-5, 10, 10, 30, 210);
		g2.drawArc(AquaPanel.PANEL_WIDTH/2, (AquaPanel.PANEL_HEIGHT/2)+5, 10, 10, 180, 270);
		g2.setStroke(new BasicStroke(1));
	}
}

