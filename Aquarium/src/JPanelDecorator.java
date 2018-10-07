import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JPanelDecorator extends JDialog
{
	private static final long serialVersionUID = 1L;
	private static String[] column = {"ID","Animal","Color","Size","Hor. speed","Ver. speed","Frequency","Eat counter"};

	private JTable table;
	private JScrollPane scrollPane;
	private JLabel l_title;
    private AquaPanel myAquaPanel;
    private String task;
    private Color animalColor;
	
    // Ctor for Duplicate and Decorator //
	public JPanelDecorator(AquaPanel myAquaPanel ,HashSet<Swimmable> animalSet, String task)
	{
		super();
		
		this.myAquaPanel = myAquaPanel;
		this.task = task;
		this.setModal(true); // make that the parent window cannot be accessed whilst this window is active //
	    DefaultTableModel model = new DefaultTableModel(); // helps to add rows dynamically to the table //
	    
	    // define the table and initialize it using the model we created //
	    table = new JTable(model){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column){ // disable cell editing by user //
				return false;
			}
	    };
	    Iterator<Swimmable> iterator_animalSet = animalSet.iterator(); // initialize the iterator //

	    for (int i = 0; i < column.length; ++i) // add the columns to the table //
		    model.addColumn(column[i]);
	    
		while(iterator_animalSet.hasNext())
		{ // add all the animals to the table //
			Swimmable animal = iterator_animalSet.next();
			
			String id = String.valueOf(animal.getId());
			String animalName = animal.getAnimalName();
			String color = animal.getColorString();
			String size = String.valueOf(animal.getSize());
			String horSpeed = String.valueOf(animal.getHorSpeed());
			String verSpeed = String.valueOf(animal.getVerSpeed());
			String counter = String.valueOf(animal.getEatCount());
			String frequency = String.valueOf(animal.getFrequency());
			
			String[] rowData = {id, animalName, color, size, horSpeed, verSpeed, frequency, counter};
			model.addRow(rowData); // add the row to the table //

		}
		if (task.equalsIgnoreCase("DUPLICATE")) // Action listener function //
			MouseListener_Duplicate();
		else if (task.equalsIgnoreCase("DECORATOR")) // Action listener function //
			MouseListener_Decorator();
		
		table.isCellEditable(0, 0); // prevent from editing a row //
		table.setRowSelectionInterval(0, 0); // show whole row selection and not single cell //
	    table.getTableHeader().setReorderingAllowed(false); // user cannot rearrange the columns //
		table.setPreferredScrollableViewportSize(new Dimension(500,96));
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
	    scrollPane = new JScrollPane(table);

		add(scrollPane); // add the table to the panel //
		// add the title to the panel //
		l_title = new JLabel("Click on Animal: ");		
		l_title.setFont(new Font("Serif",Font.BOLD,30)); // bold font //		
		add("North",l_title);
		
		pack();
		setLocationRelativeTo(null); // open the frame in the middle of the screen //
	}
	
    // Ctor for Save Memento //
	public JPanelDecorator(AquaPanel myAquaPanel ,HashSet<Swimmable> animalSet)
	{
		super();
		
		this.myAquaPanel = myAquaPanel;
		this.setModal(true); // make that the parent window cannot be accessed whilst this window is active //
		setTitle("Save state");
	    DefaultTableModel model = new DefaultTableModel(); // helps to add rows dynamically to the table //
	    
	    // define the table and initialize it using the model we created //
	    table = new JTable(model){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column){ // disable cell editing by user //
				return false;
			}
	    };
	    
	    for (int i = 0; i < column.length; ++i) // add the columns to the table //
		    model.addColumn(column[i]);
	    
	    Iterator<Swimmable> iterator_animalSet = animalSet.iterator(); // initialize the iterator //

		while(iterator_animalSet.hasNext())
		{ // add all the animals to the table //
			Swimmable animal = iterator_animalSet.next();
			
			String id = String.valueOf(animal.getId());
			String animalName = animal.getAnimalName();
			String color = animal.getColorString();
			String size = String.valueOf(animal.getSize());
			String horSpeed = String.valueOf(animal.getHorSpeed());
			String verSpeed = String.valueOf(animal.getVerSpeed());
			String counter = String.valueOf(animal.getEatCount());
			String frequency = String.valueOf(animal.getFrequency());
			
			String[] rowData = {id, animalName, color, size, horSpeed, verSpeed, frequency, counter};
			model.addRow(rowData); // add the row to the table //
		}
		
		MouseListener_Memento_Set_State();
		
		table.isCellEditable(0, 0); // prevent from editing a row //
		table.setRowSelectionInterval(0, 0); // show whole row selection and not single cell //
	    table.getTableHeader().setReorderingAllowed(false); // user cannot rearrange the columns //
		table.setPreferredScrollableViewportSize(new Dimension(500,96));
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
	    scrollPane = new JScrollPane(table);

		add(scrollPane); // add the table to the panel //
		// add the title to the panel //
		l_title = new JLabel("Click on Creature: ");		
		l_title.setFont(new Font("Serif",Font.BOLD,30)); // bold font //		
		add("North",l_title);
		
		pack();
		setLocationRelativeTo(null); // open the frame in the middle of the screen //
	}
	
    // Ctor for Restore Memento //
	public JPanelDecorator(AquaPanel myAquaPanel, CareTaker caretaker)
	{
		super();
		
		this.myAquaPanel = myAquaPanel;
		this.setModal(true); // make that the parent window cannot be accessed whilst this window is active //
		setTitle("Restore state");
	    DefaultTableModel model = new DefaultTableModel(); // helps to add rows dynamically to the table //
	    
	    // define the table and initialize it using the model we created //
	    table = new JTable(model){
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column){ // disable cell editing by user //
				return false;
			}
	    };
		 String[] column = {"ID","Animal","Color","Size","Hor. speed","Ver. speed","x","y"};

	    for (int i = 0; i < column.length; ++i) // add the columns to the table //
		    model.addColumn(column[i]);
	    
	    
		for (int i = 0; i < caretaker.getSize(); ++i)
		{ // add all the animals to the table //
			Swimmable animal = caretaker.getMemento(i).getSavedState();
			String id = String.valueOf(animal.getIdForMemento());
			String animalName = animal.getAnimalName();
			String color = animal.getColorString();
			String size = String.valueOf(animal.getSize());
			String horSpeed = String.valueOf(animal.getHorSpeed());
			String verSpeed = String.valueOf(animal.getVerSpeed());
			String x = String.valueOf(animal.getXLocation());
			String y = String.valueOf(animal.getYLocation());
			
			String[] rowData = {id, animalName, color, size, horSpeed, verSpeed, x, y};
			model.addRow(rowData); // add the row to the table //
		}
		
		MouseListener_Memento_Get_State();
		
		table.isCellEditable(0, 0); // prevent from editing a row //
		table.setRowSelectionInterval(0, 0); // show whole row selection and not single cell //
	    table.getTableHeader().setReorderingAllowed(false); // user cannot rearrange the columns //
		table.setPreferredScrollableViewportSize(new Dimension(500,96));
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
	    scrollPane = new JScrollPane(table);

		add(scrollPane); // add the table to the panel //
		// add the title to the panel //
		l_title = new JLabel("Click on animal: ");		
		l_title.setFont(new Font("Serif",Font.BOLD,30)); // bold font //		
		add("North",l_title);
		
		pack();
		setLocationRelativeTo(null); // open the frame in the middle of the screen //
	}
	
	private void MouseListener_Duplicate()
	{
		setTitle("Duplicate Animal");

		////////// Start mouse event when press on a row //////////
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int column = table.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && column >= 0) // if the event is on animal //
                {
                	// show dialog //
                	int choice = JOptionPane.showOptionDialog(null, 
                	      "Do you wish to edit the duplicated animal?", 
                	      "Edit?", 
                	      JOptionPane.YES_NO_OPTION, 
                	      JOptionPane.QUESTION_MESSAGE, 
                	      null, null, null);
                	 
                	  if (choice == JOptionPane.YES_OPTION) 
                	  { // if user want to edit //
                		  String type = (String)table.getValueAt(row, 1);
                		  String color = (String)table.getValueAt(row, 2);
                		  String size = (String)table.getValueAt(row, 3);
                		  String horSpeed = (String)table.getValueAt(row, 4);
                		  String verSpeed = (String)table.getValueAt(row, 5);
                		  String frequency = (String)table.getValueAt(row, 6);
                		  int colorIndex = getColorIndex(color); // get animals color index in the JComboBox //
                		  remove(scrollPane); // remove the table from the screen //
                		  remove(l_title); // remove the title from the screen //
                		  // show edit dialog //
              			DuplicateAnimalDialog duplicate = new DuplicateAnimalDialog(myAquaPanel,type, colorIndex, size, horSpeed, verSpeed, frequency);
            			duplicate.setVisible(true);
            			dispose();
                	  }
                	  else if (choice == JOptionPane.NO_OPTION)
                	  { // if user duplicate animal without edit //
                		  String animalID = (String)table.getValueAt(row, 0);
                		  myAquaPanel.CloneAnimal(animalID); // clone the animal //
                    	  dispose();
                	  } 
                }
            }
	    });
	    ////////// end mouse event when press on a row //////////
	}
	
	private void MouseListener_Decorator()
	{
		setTitle("Decorate Animal");

		////////// Start mouse event when press on a row //////////
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int column = table.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && column >= 0) // if the event is on animal //
                {
                	// show dialog //
                	int choice = JOptionPane.showOptionDialog(null, 
                	      "Would you like to change its color?", 
                	      "Change Color?", 
                	      JOptionPane.YES_NO_OPTION, 
                	      JOptionPane.QUESTION_MESSAGE, 
                	      null, null, null);
                	 
                	  if (choice == JOptionPane.YES_OPTION) 
                	  {                 		  
                		  String animalID = (String)table.getValueAt(row, 0);
                		  Swimmable animal = (Swimmable) myAquaPanel.getCreatureByID(animalID,"Swimmable"); // find animal //
                		  MarineAnimalDecorator mad = new MarineAnimalDecorator(animal);
                		  mad.PaintFish();
                		  table.setValueAt(animal.getColorString(), row, 2); // manually refresh the color cell //
                		  dispose();
                	  }
                }
            }
	    });
	    ////////// end mouse event when press on a row //////////
	}
	
	private void MouseListener_Memento_Set_State()
	{
		////////// Start mouse event when press on a row //////////
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int column = table.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && column >= 0) // if the event is on animal //
                {
                	// show dialog //
                	int choice = JOptionPane.showOptionDialog(null, 
                	      "Do you wish to save this animal's state?", 
                	      "Save state?", 
                	      JOptionPane.YES_NO_OPTION, 
                	      JOptionPane.QUESTION_MESSAGE, 
                	      null, null, null);
                	 
                	  if (choice == JOptionPane.YES_OPTION) 
                	  { // if user want to save state //
                		  
                		  String animalD = (String)table.getValueAt(row, 0);
                		  String animalType = (String)table.getValueAt(row, 1);
                		  animalType = getCreatureType(animalType);
                		  SeaCreature animal = myAquaPanel.getCreatureByID(animalD,animalType); // find animal //
                		  
                		  myAquaPanel.SaveObjectState((Swimmable) animal);

            			dispose();
                	  }
                }
            }
	    });
	    ////////// end mouse event when press on a row //////////
	}
	
	private void MouseListener_Memento_Get_State()
	{
		////////// Start mouse event when press on a row //////////
		table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                int column = table.columnAtPoint(evt.getPoint());
                
                if (row >= 0 && column >= 0) // if the event is on animal //
                {
                	// show dialog //
                	int choice = JOptionPane.showOptionDialog(null, 
                	      "Do you wish to restore this animal's state?", 
                	      "Restore state?", 
                	      JOptionPane.YES_NO_OPTION, 
                	      JOptionPane.QUESTION_MESSAGE, 
                	      null, null, null);
                	 
                	  if (choice == JOptionPane.YES_OPTION) 
                	  { // if user want to restore state //
                		  
                		  String animalD = (String)table.getValueAt(row, 0);
                		  String animalType = (String)table.getValueAt(row, 1);
                		  animalType = getCreatureType(animalType);
                		  SeaCreature animal = myAquaPanel.getAnimalFromCareTaker(animalD); // find animal in caretaker //
                		  
                		  myAquaPanel.RestoreObjectState((Swimmable) animal);

            			dispose();
                	  }
                }
            }
	    });
	    ////////// end mouse event when press on a row //////////
	}
	
	private int getColorIndex(String color)
	{
		for (int i = 0; i < AddCreatureDialog.COLORS.length; ++i)
		{
			if (AddCreatureDialog.COLORS[i].equals(color))
				return i;
		}
		return 0;
	}
	
	private String getCreatureType(String name)
	{
		if (name.equalsIgnoreCase("FISH") || name.equalsIgnoreCase("JELLYFISH"))
			return "Swimmable";
		return "Immobile";
	}
}
