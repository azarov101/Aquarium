import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.CyclicBarrier;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class AquaPanel extends JPanel implements ActionListener, Observer
{
	private static final long serialVersionUID = 1L;
	public static int PANEL_WIDTH, PANEL_HEIGHT;
	private HashSet<Swimmable> animalSet;
	private HashSet<Immobile> plantSet;
	private Iterator <Swimmable> iterator_animalSet ;
	private Iterator <Immobile> iterator_plantSet ;
	private JPanel panelButtons, panelInfo;
	private JButton[] buttons;
	private String[] names = {"Add Creature","Duplicate Animal","Decorator","Sleep","Wake up","Reset","Food","Info","Exit"};
	private BufferedImage img;
	private SingletonWorm worm;
	private CareTaker caretaker;
	
	public AquaPanel(AquaFrame myFrame)
	{
		super();
		img = null;
		worm = null;
		animalSet = new HashSet<Swimmable>();
		plantSet = new HashSet<Immobile>();
		caretaker = new CareTaker();
		panelButtons = new JPanel();
		panelButtons.setLayout(new GridLayout());

		buttons = new JButton[names.length];
		for(int i = 0; i < names.length; i++)
		{
			buttons[i]=new JButton(names[i]);
			buttons[i].addActionListener(this);
			buttons[i].setBackground(Color.lightGray);
			buttons[i].setPreferredSize(new Dimension(112, 28)); // define all buttons with fixed dimensions //
		    panelButtons.add(buttons[i]);
		}
		
		setLayout(new BorderLayout());
		
		add("South", panelButtons);
		
		panelInfo = new JPanel();
		panelInfo.setLayout(new FlowLayout());
		panelInfo.setVisible(false);
		
		add("North",panelInfo);
		setBackground(Color.WHITE);
	}

	public void setBackgr(int index)
	{
		switch(index)
		{
		   case 0:
			   selectImage();
			   repaint();
			   break;
		   case 1:
			   img = null;
			   setBackground(Color.BLUE);
			   repaint();
			   break;
		   case 2:
			   img = null;
			   setBackground(Color.WHITE);
			   repaint();
		       break;
		}
	}
	
	public void selectImage()
	{
		JFileChooser chooser = new JFileChooser();
		FileFilter imageFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		chooser.setFileFilter(imageFilter);
		
		int search = chooser.showOpenDialog(new JPanel());
		if (search == JFileChooser.APPROVE_OPTION){
			try
			{
			img = ImageIO.read(new File(chooser.getSelectedFile().getAbsolutePath())); 
			}
		    catch (IOException e) 
			{
		    	e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		PANEL_WIDTH = getWidth();
		PANEL_HEIGHT = getHeight();
		
		if (e.getSource() == buttons[0])
		{ // add new animal //

			if (animalSet.size() == 5 && plantSet.size() == 5)
			{
				JOptionPane.showMessageDialog(this, "Cannot create more creatures", "Maximum amount achieved!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			String creatureAllowed = "All";
			AddCreatureDialog addCreature;
			
			if (Math.max(animalSet.size(), plantSet.size()) == 5)
				creatureAllowed = (animalSet.size() < plantSet.size()) ? "Swimmable" : "Immobile";
			
			addCreature = new AddCreatureDialog(this,creatureAllowed);
			addCreature.setVisible(true);
		}
		
		else if (e.getSource() == buttons[1])
		{ // duplicate //
			if (animalSet.size() == 5)
			{
				JOptionPane.showMessageDialog(this, "Cannot create more animals", "Maximum amount achieved!", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (animalSet.size() == 0)
			{
				JOptionPane.showMessageDialog(this, "There are no animals to duplicate!", "Zero animals", JOptionPane.CANCEL_OPTION);
				return;
			}
			JPanelDecorator table = new JPanelDecorator(this,animalSet,"Duplicate");
			table.setVisible(true);	 

			repaint();
		}
		
		else if (e.getSource() == buttons[2])
		{ // decorator //
			if (animalSet.size() == 0)
			{
				JOptionPane.showMessageDialog(this, "There are no animals to decorate!", "Zero animals", JOptionPane.CANCEL_OPTION);
				return;
			}
			JPanelDecorator table = new JPanelDecorator(this,animalSet,"Decorator");
			table.setVisible(true);	
			
			repaint();
		}
		
		else if (e.getSource() == buttons[3])
		{ // suspend //
			iterator_animalSet = animalSet.iterator();
			
			while(iterator_animalSet.hasNext())
			{
				Swimmable animal = iterator_animalSet.next();
				animal.setSuspend();
			}
			repaint();
		}
		
		else if (e.getSource() == buttons[4])
		{ // resume //
			iterator_animalSet = animalSet.iterator();
			
			while(iterator_animalSet.hasNext())
			{
				Swimmable animal = iterator_animalSet.next();
				animal.setResume();
			}
			repaint();
		}
		
		else if (e.getSource() == buttons[5])
		{ // reset //
			iterator_animalSet = animalSet.iterator();
			 
			while(iterator_animalSet.hasNext()) // stop all the threads //
			{
				Swimmable animal = iterator_animalSet.next();
				animal.interrupt();
			}
			animalSet.clear();
			plantSet.clear();
			caretaker = new CareTaker(); // reset caretaker //
			if (worm != null)
				worm.SetInstance(); // remove the food //
			worm = null;
			repaint();
		}
		
		else if (e.getSource() == buttons[6])
		{ // food
			if (worm != null) // if there is already a worm in the Aquarium //
				return;
			try
			{
				// set barrier in the amount of animals that are awake //
				int size = NumOfAwakeAnimals();	
				
				if (size == 0) // if there are animals and they all asleep //
				{
					worm = SingletonWorm.GetInstance(); // flag that animals can eat //
					repaint();
					return;
				}
				
				// else - there are animals that are not asleep //
				CyclicBarrier barrier = new CyclicBarrier(size); // in the amount of awaken animals //

				iterator_animalSet = animalSet.iterator(); // initialize the iterator //

				while(iterator_animalSet.hasNext())
				{
					Swimmable animal = iterator_animalSet.next();
					if (animal.getAnimalState().toString().equals("Hungry"))
						animal.setBarrier(barrier);
				}
				worm = SingletonWorm.GetInstance(); // flag that animals can eat //

				repaint();
			}
			catch (IllegalArgumentException zeroAnimals)
			{
				System.out.println("No animals in the Aquarium!");
				return;
			}
		}
		
		else if (e.getSource() == buttons[7])
		{
			if (panelInfo.isVisible()) // Even click //
			{
				panelInfo.setVisible(false);
				panelInfo.removeAll();
			}
			else
			{ // Odd click //
				panelInfo.setVisible(true);
				AddTable();
			}
		}
		
		else // exit
			System.exit(0);
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); // i have to call super to avoid problems in the frame //
		if(img != null) 
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			
		iterator_animalSet = animalSet.iterator();	
		while(iterator_animalSet.hasNext())
		{
			Swimmable animal = iterator_animalSet.next();
			animal.drawCreature(g);
		}
		
		iterator_plantSet = plantSet.iterator();
		while(iterator_plantSet.hasNext())
		{
			Immobile plant = iterator_plantSet.next();
			plant.drawCreature(g);
		}
		if (worm != null)
			worm.drawWorm(g);
	}
	
	public void AddCreature(SeaCreature creature)
	{
		if (creature instanceof Swimmable)
		{
			Swimmable animal = (Swimmable)creature;
			animalSet.add(animal);
			animal.start();
		}
		else
			plantSet.add((Immobile)creature);
		
		repaint();
	}
	
	/*
	 * function only for Cyclic Barrier 
	 */
	private int NumOfAwakeAnimals()
	{
		int sizeOfAwakeAndHungry = 0;
		boolean emptySet = true;
		
		iterator_animalSet = animalSet.iterator(); // initialize the iterator //

		while(iterator_animalSet.hasNext())
		{
			emptySet = false; // there are animals in the set //

			Swimmable animal = iterator_animalSet.next();
			if (animal.isSuspend == false && animal.getAnimalState().toString().equals("Hungry"))
				sizeOfAwakeAndHungry +=1;
		}
		if (emptySet) // if there are no animals in the set //
			return -1;
		return sizeOfAwakeAndHungry; // if there are animals in the set - return the amount that are awake and HUNGRY //
	}
	
	private void AddTable()
	{
		JTable table;
		iterator_animalSet = animalSet.iterator(); // initialize the iterator //

		String[] column = {"Animal","Color","Size","Hor. speed","Ver. speed","Eat counter"};
		int totalEatCounter = 0;
		
	    DefaultTableModel model = new DefaultTableModel(); // helps to add rows dynamically to the table //
	    table = new JTable(model); // define the table and initialize it using the model we created //
	    for (int i = 0; i < column.length; ++i) // add the columns to the table //
		    model.addColumn(column[i]);
 
		while(iterator_animalSet.hasNext())
		{
			Swimmable animal = iterator_animalSet.next();

			String animalName = animal.getAnimalName();
			String color = animal.getColorString();
			String size = String.valueOf(animal.getSize());
			String horSpeed = String.valueOf(animal.getHorSpeed());
			String verSpeed = String.valueOf(animal.getVerSpeed());
			String counter = String.valueOf(animal.getEatCount());
			totalEatCounter += Integer.parseInt(counter);
			
			String[] rowData = {animalName, color, size, horSpeed, verSpeed, counter};
			model.addRow(rowData); // add the row to the table //
		}
		
		String[] lastRow = {"Total","","","","",String.valueOf(totalEatCounter)};
	    model.addRow(lastRow); // add the last row - Total Eat Counter //
		
		table.setPreferredScrollableViewportSize(new Dimension(500,96));
		table.setFillsViewportHeight(true);
	    JScrollPane scrollPane = new JScrollPane(table);
		panelInfo.add(scrollPane);
	}

	public void receiveResponse_CALLBACK(Swimmable animal)
	{
		if (worm != null)
		{
			animal.eatInc();
			worm.SetInstance();
			worm = null;
		}	
	}
	
	public SingletonWorm CheckWormInstance() { return worm; } // this function help me in Swimmable //
	
	public void CloneAnimal(String animalID)
	{
		Swimmable newAnimal = null;
		
		iterator_animalSet = animalSet.iterator();	
		while(iterator_animalSet.hasNext())
		{
			Swimmable animal = iterator_animalSet.next();
			if (String.valueOf(animal.getId()).equals(animalID)) // clone if it's the same animal //
			{
				newAnimal = animal.makeCopy();
				break;
			}			
		}
		if (newAnimal != null)
		{
			AddCreature((Swimmable)newAnimal); // add the cloned animal //
		}
	}
	
	public String ChangeColor(String animalID, Color colorToChange)
	{	
		iterator_animalSet = animalSet.iterator();	
		while(iterator_animalSet.hasNext())
		{
			Swimmable animal = iterator_animalSet.next();
			if (String.valueOf(animal.getId()).equals(animalID)) // change color if it's the same animal //
			{
				animal.setColor(colorToChange);
				return animal.getColorString();
			}			
		}
		return null; 
	}
	
	public SeaCreature getCreatureByID(String creatureID, String creatureType)
	{
		if (creatureType.equalsIgnoreCase("SWIMMABLE"))
		{
			iterator_animalSet = animalSet.iterator();	
			while(iterator_animalSet.hasNext())
			{
				Swimmable animal = iterator_animalSet.next();
				if (String.valueOf(animal.getId()).equals(creatureID))
					return animal;
			}
		}
		else
		{
			iterator_plantSet = plantSet.iterator();	
			while(iterator_plantSet.hasNext())
			{
				Immobile plant = iterator_plantSet.next();
				if (String.valueOf(plant.getId()).equals(creatureID)) 
					return plant;
			}
		}
		return null; 
	}
	
	public Swimmable getAnimalByMementoID(String creatureID)
	{
			iterator_animalSet = animalSet.iterator();	
			while(iterator_animalSet.hasNext())
			{
				Swimmable animal = iterator_animalSet.next();
				if (String.valueOf(animal.getIdForMemento()).equals(creatureID))
					return animal;
			}
	
		return null; 
	}
	
	public Swimmable getAnimalFromCareTaker(String creatureID)
	{
			for (int i = 0; i < caretaker.getSize(); ++i)
			{
				if (String.valueOf(caretaker.getMemento(i).getSavedState().getIdForMemento()).equals(creatureID))
					return caretaker.getMemento(i).getSavedState();
			}
		return null; 
	}
	
	@Override
	public void update(Swimmable animal) 
	{
		animal.setHungryState();
//		JOptionPane.showMessageDialog(this, "fish is hungry", "i want food", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void SaveObjectState_ShowTable()
	{
		if (animalSet.size() == 0)
		{
			JOptionPane.showMessageDialog(this, "There are no animals in the Aquarium to save", "Zero animals", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		JPanelDecorator table = new JPanelDecorator(this,animalSet);
		table.setVisible(true);		
	}
	
	public void RestoreObjectState_ShowTable()
	{ // need to add constructer to JPanelDialog and send careTaker. need to add mouse listener there //
		// need to make loop for all the creatures in the caretaker list //
		if (caretaker.getSize() == 0)
		{
			JOptionPane.showMessageDialog(this, "Did not find saved animals", "Zero animals", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		JPanelDecorator table = new JPanelDecorator(this,caretaker);
		table.setVisible(true);		
	}
	
	public void SaveObjectState(Swimmable animal)
	{
		for (int i = 0; i < caretaker.getSize(); ++i)
		{
			if (caretaker.getMemento(i).getSavedState().getIdForMemento() == animal.getIdForMemento())
			{
				caretaker.removeMemento(i); // remove the saved memento //
				break;
			}
		}
		
		Originator originator = new Originator();
		originator.setState(animal);
	  
		Memento memento = originator.createMemento();
		caretaker.addMemento(memento);	  
	}
	
	public void RestoreObjectState(SeaCreature creaturePreviousState)
	{
			Swimmable animalPreviousState = (Swimmable)creaturePreviousState;
			String id = String.valueOf(animalPreviousState.getIdForMemento());
			Swimmable animal = (Swimmable)getAnimalByMementoID(id);
			synchronized (animal) 
			{
				animal.setColor(animalPreviousState.getColor());
				animal.setSizeOfCreature(animalPreviousState.getSize());
				animal.setXLocation(animalPreviousState.getXLocation());
				animal.setYLocation(animalPreviousState.getYLocation());
				animal.setHorSpeed(animalPreviousState.getHorSpeed());
				animal.setVerSpeed(animalPreviousState.getVerSpeed());
				animal.setX_dir(animalPreviousState.getX_dir());
				animal.setY_dir(animalPreviousState.getY_dir());
			}
		
		repaint();
	}
}
	