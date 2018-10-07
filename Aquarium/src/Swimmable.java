import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import javax.swing.JColorChooser;

public abstract class Swimmable extends Thread implements SeaCreature,Cloneable,Observable,MarineAnimal
{	
	protected ArrayList<Observer> observers;
	protected Color col;
	protected int horSpeed, verSpeed;
	protected int size;
	protected int frequency;
	protected int counter;
	protected int x_front, y_front;
	protected int x_dir, y_dir;
	protected AquaPanel panel;
	protected CyclicBarrier barrier;
	protected Boolean isSuspend;
	protected int movements;
	protected HungerState state;
	protected static long firstToGetWorm = 0;
	protected Hungry hungryState;
	protected Satiated satiatedState;
	protected static int ID = 0;
	protected int idForMemento;
	
	public Swimmable()
	{
		observers = new ArrayList<Observer>();
		col = null;
		horSpeed = verSpeed = size = counter = frequency = movements = 0;
		barrier = null;
		x_dir = y_dir = 1 ; // down and right //
		isSuspend = false;
		// initialize hunger states //
		state = null;
		hungryState = new Hungry();
		satiatedState = new Satiated();
		idForMemento = ++ID;
	}
	
	public Swimmable(Swimmable other)
	{
		this();
		setFields(other.panel, other.size, other.horSpeed, other.verSpeed, other.frequency, other.col);
		idForMemento = other.idForMemento;
	}
	
	public void setFields(AquaPanel panel, int size, int hor, int ver, int frequency, Color color)
	{
		setPanel(panel);
		setSizeOfCreature(size);
		setHorSpeed(hor);
		setVerSpeed(ver);
		setFrequency(frequency);
		setColor(color);
		addObserver(panel);
		
		// start randomly in the Aquarium //
		Random rand = new Random();
		x_front = rand.nextInt(panel.getWidth() -1) +1; 
		y_front = rand.nextInt(panel.getHeight() -1) +1;		
	}
	
	 
	 abstract public void drawCreature(Graphics g);
	 abstract public Swimmable makeCopy();
	 abstract public Object clone();
	 
	 public void setPanel(AquaPanel panel) { this.panel = panel; }
	 public void setSizeOfCreature(int size) { this.size = size; }
	 public void setHorSpeed(int horSpeed) { this.horSpeed = horSpeed; }
	 public void setVerSpeed(int verSpeed) { this.verSpeed = verSpeed; }
	 public void setFrequency(int frequency) { this.frequency = frequency; }
	 public void setColor(Color col) { this.col = col; }
	 public void setSuspend() { isSuspend = true; }
	 public synchronized void setResume() { isSuspend = false;	notify();}
	 public void setBarrier(CyclicBarrier b){ barrier = b; }
	 public void setXLocation(int x) { x_front = x; }
	 public void setYLocation(int y) { y_front = y; }
	 public void setAnimalState(HungerState state) { this.state = state; }
	 public int getIdForMemento() { return idForMemento; }
	 public void setHungryState() { hungryState.changeState(this); }
	 public void setX_dir(int x_dir) {this.x_dir = x_dir; }
	 public void setY_dir(int y_dir) {this.y_dir = y_dir; }

	 
	 public int getHorSpeed() { return horSpeed; }
	 public int getVerSpeed() { return verSpeed; }
	 public String getAnimalName() { return getClass().getName(); }
	 public int getSize() { return size; }
	 public int getFrequency() { return frequency; }
	 public void eatInc() { ++counter; }
	 public int getEatCount() { return counter; }
	 public int getXLocation() { return x_front; }
	 public int getYLocation() { return y_front; }
	 public HungerState getAnimalState() { return state; }
	 public int getX_dir() { return x_dir; }
	 public int getY_dir() {return y_dir; }

	 public Color getColor() { return col; }
	 
	 public String getColorString()
	 {
		 return "("+col.getRed()+","+col.getGreen()+","+col.getBlue()+")";
	 }
	 
	 @Override
	 public void run()
	 {
		 satiatedState.changeState(this); // first animal is not hungry //
		 while(true)
		 {
			 try 
			 {	
				if (movements <= frequency) // meant to prevent overflow //
					++movements;
				if (movements == frequency)
				{
//					hungryState.changeState(this); // change the state to hungry //
					notifyObservers();					
				}
//				System.out.println(movements);
				Thread.sleep(60);
				if (isSuspend)
					synchronized(this){
						wait();
					}		
				else
				{					
					if (panel.CheckWormInstance() != null && getAnimalState().toString().equals("Hungry"))
						promoteAnimal_Eat();
					else
						promoteAnimal_Regular();
				}
				panel.repaint(); // REPAINT //
			 }
			 catch (InterruptedException e) 
			 {
//				e.printStackTrace();
				 System.out.println("Animal " + this.getId() + " interupt!");
				 return;
			 }		
			 catch (BrokenBarrierException e) 
			 {
				e.printStackTrace();
			 }				 
		 }
	 }
	 
	 public void promoteAnimal_Regular()
	 {
		 // Check if animal in the edge of the Aquarium //
		 if (x_front <= 0)
			 x_dir = 1; // go right //
		 else if (x_front >= panel.getWidth())
			 x_dir = -1; // go left //
		
		 if (y_front >= panel.getHeight())
			 y_dir = -1; // go up //		 
		 else if(y_front <= 0)
			 y_dir = 1; // go down //
		
		 // Now promote //
		 x_front += horSpeed*x_dir;
		 y_front += verSpeed*y_dir;		

	 }
	 
	 public void promoteAnimal_Eat() throws InterruptedException, BrokenBarrierException
	 {
		 if (barrier != null)
			 barrier.await(); // animal wait here until all animals are here //
		 
		 
		 // when all animals are here - change barrier to null to avoid waiting again //
		 barrier = null;
		 
		 //////////////////////////// Calculations ////////////////////////////	
		 double oldSpeed = Math.sqrt(horSpeed*horSpeed + verSpeed*verSpeed);
		 double k = 0;
		 
		 try
		 {
			 k = Math.abs((y_front - panel.getHeight()/2) / (x_front - panel.getWidth()/2));
		 } 
		 catch (ArithmeticException e) // division by zero //
		 { 
			 k = Math.abs((y_front - panel.getHeight()/2) / ((x_front +1) - panel.getWidth()/2));
		 }
		 
		 double newHorSpeed = oldSpeed / Math.sqrt(k*k + 1);
		 double newVerSpeed = newHorSpeed * k;
		 //////////////////////////// Done Calculations ////////////////////////////	
		 
		 // change the direction (x_front & y_front) to the middle of the screen //		 
		 if(x_front > panel.getWidth()/2)
			 x_dir = -1; // go left //
		 else
			 x_dir = 1; // go right //
		 
		 if(y_front > panel.getHeight()/2)
			 y_dir = -1; // go up //
		 else
			 y_dir = 1; // go down //
		 
		 // TO FIX A BUG //
		 // Description of the BUG: Animal stack in two frames for ever and can't eat the warm because
		 //	each time (Width < 5 but Hight > 5) or (Width > 5 but Hight < 5).
		 if(newVerSpeed > 10)
			 newVerSpeed = 10;
		 else if(newVerSpeed < 1)
		 {
			 if(y_front != panel.getHeight()/2)
				 newVerSpeed = 1;
			 else
				 newVerSpeed = 0;
		 }

		 if(newHorSpeed > 10)
			 newHorSpeed = 10; 
		 else if(newHorSpeed < 1)
		 {
			 if(x_front != panel.getWidth()/2)
				 newHorSpeed = 1;    
			 else
				 newHorSpeed = 0;    
		 }
		 //////////////////////////// Done FIXING BUG ////////////////////////////	
		 
		 // Now promote //
		 x_front += (int)newHorSpeed*x_dir;
		 y_front += (int)newVerSpeed*y_dir;
		 
		 // Check if the animal close enough to the worm //
		 if ((Math.abs(x_front-panel.getWidth()/2) <= 5) && (Math.abs(y_front-panel.getHeight()/2) <= 5))
		 {
			 synchronized (this) 
			 {
				 if ((Math.abs(x_front-panel.getWidth()/2) <= 5) && (Math.abs(y_front-panel.getHeight()/2) <= 5))
					 if (firstToGetWorm == 0)
						 firstToGetWorm = this.getId();
			 } 
		 }

		 if (firstToGetWorm == this.getId())
		 {
			 panel.receiveResponse_CALLBACK(this); // callback	 
			 satiatedState.changeState(this); // change state to satiated //
			 movements = 0;
			 firstToGetWorm = 0;
		 }
	 }
	 
		@Override
		public void addObserver(Observer observerToAdd) { observers.add(observerToAdd); }
		
		@Override
		public void removeObserver(Observer observerToRemove) 
		{
			// find the class name of the observer we want to remove //
			String observerClassName = String.valueOf(observerToRemove.getClass()); 
			
			for (int i = 0;i<observers.size();++i)
				if (String.valueOf(observers.get(i).getClass()).equals(observerClassName))
					observers.remove(i); // remove the observer from the list //
			System.out.println(observerClassName + " has been removed from the observers list.");
		}
		
		@Override
		public void notifyObservers() 
		{	
			for (Observer observer : observers)
				observer.update(this);
		}
		
		@Override
		public void PaintFish() 
		{
			Color animalColor = null;
  		    animalColor = JColorChooser.showDialog(null, "Pick a color:", animalColor);
  		    if (animalColor == null) // no color was selected //
  		    	return;
			this.setColor(animalColor);
			
			// Check if the color already exists in the color array //
			for (int i = 0; i < AddCreatureDialog.COLORS.length; ++i)
				if (AddCreatureDialog.COLORS[i].equals(getColorString()))
					return;
			
			// Add the new color to AddCreatureDialog //
			String[] temp = new String[AddCreatureDialog.COLORS.length+1]; // define new string array with size +1 //
			for (int i = 0; i < AddCreatureDialog.COLORS.length; ++i)
				temp[i] = AddCreatureDialog.COLORS[i]; // copy all colors from original color array to the temp array //
			temp[AddCreatureDialog.COLORS.length] = getColorString(); // final index of the temp array will have the new color //
			AddCreatureDialog.COLORS = temp; // save the temp array //
		}
}	

