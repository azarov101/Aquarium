import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AddCreatureDialog extends JDialog implements ActionListener
{
	private static final long serialVersionUID = 1L;
    public static String[] COLORS = {"(0,0,255)", "(255,0,0)", "(0,255,0)", "(255,255,0)", "(255,0,255)"}; 
    private AquaPanel myAquaPanel;
	private JPanel panel_RadioButton, panel, panel_save;
	private JRadioButton fish, jellyfish, laminaria, zostera;
	private ButtonGroup radioButtons; // to group the radio buttons //
	private JLabel l_animalSelect, l_animalSize, l_horSpeed, l_verSpeed, l_animalcolor, l_frequency;
	private JTextField t_sizeOfAnimal, t_horSpeed, t_verSpeed, t_frequency;
	private JComboBox<String> animalColor;
	private JButton b_Add;
	private AbstractSeaFactory factory;
	private String creatureAllowed;
	
	public AddCreatureDialog(AquaPanel myAquaPanel, String creatureAllowed)
	{
		super();
		this.setModal(true); // make that the parent window cannot be accessed whilst this window is active //
		this.myAquaPanel = myAquaPanel;
		this.creatureAllowed = creatureAllowed;
		
		factory = null;
		setTitle("Add Creature");
		
		// Define and Initialize Panels: //
		panel_RadioButton = new JPanel();
		panel = new JPanel();
		panel_save = new JPanel();		
		panel_RadioButton.setLayout(new FlowLayout(0,0,0));
		panel_save.setLayout(new FlowLayout());
		panel.setLayout(new GridLayout(5,3)); // 5 rows //
		
		// Initialize Labels: //
		l_animalSelect = new JLabel("Select Animal: ");
		l_animalSelect.setPreferredSize(new Dimension(140, 10)); // set space between the label and the RadioButton //
		l_animalSize = new JLabel("Select Animal Size: ");
		l_horSpeed = new JLabel("Enter Horizontal Speed: ");
		l_verSpeed = new JLabel("Enter Vertical Speed: ");
		l_animalcolor = new JLabel("Select Color: ");
		l_frequency = new JLabel("Frequency of Eating: ");
		
		// Initialize TextFields: //
		t_sizeOfAnimal = new JTextField(6);
		t_horSpeed = new JTextField(6);		
		t_verSpeed = new JTextField(6);
		t_frequency = new JTextField(6);
		
		animalColor = new JComboBox<String>(COLORS);		
		// Initialize RadioButtons: //
		setRadioButtons();
		
		// Initialize 'Add' Button: //
		b_Add = new JButton("Add");
		b_Add.addActionListener(this); // defines the action when press the Add Button //
		
		// Add To Panels: //
		panel_RadioButton.add(l_animalSelect);
		panel_RadioButton.add(fish);
		panel_RadioButton.add(jellyfish);	
		panel_RadioButton.add(laminaria);
		panel_RadioButton.add(zostera);

		panel.add(l_animalSize);
		panel.add(t_sizeOfAnimal);	
		panel.add(l_horSpeed);
		panel.add(t_horSpeed);
		panel.add(l_verSpeed);
		panel.add(t_verSpeed);
		panel.add(l_frequency);
		panel.add(t_frequency);
		panel.add(l_animalcolor);
		panel.add(animalColor);
		panel_save.add(b_Add);

		// Organize the Panels on the screen //
		add("North",panel_RadioButton);
		add(panel);
		add("South",panel_save);
		pack();
		setLocationRelativeTo(null); // open the frame in the middle of the screen //
	}
	
	private void setRadioButtons()
	{
		ItemListener radioButton = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) 
			{
				// because itemStateChanged called twice (first for DESELECTED and then for SELECTED) //
			    if (e.getStateChange() == ItemEvent.SELECTED)  
			    {
					if (((JRadioButton)e.getItem()).equals(fish) || ((JRadioButton)e.getItem()).equals(jellyfish))
					{
						l_frequency.setEnabled(true);						
						t_frequency.setEnabled(true);						
						l_horSpeed.setText("Enter Horizontal Speed: ");
						l_verSpeed.setText("Enter Vertical Speed: ");
						l_animalcolor.setEnabled(true);
						animalColor.setEnabled(true);
					}
					else if (((JRadioButton)e.getItem()).equals(laminaria) || ((JRadioButton)e.getItem()).equals(zostera))
					{
						l_frequency.setEnabled(false);						
						t_frequency.setEnabled(false);						
						l_horSpeed.setText("Enter Coordinate X: ");
						l_verSpeed.setText("Enter Coordinate Y: ");
						l_animalcolor.setEnabled(false);
						animalColor.setEnabled(false);
					}
			    }			
			}
		};
		
		// initialize radiobutton //
		if (creatureAllowed == "Immobile")
		{
			// set where the radio button will start //
			fish = new JRadioButton("Fish", false);
			jellyfish = new JRadioButton("Jellyfish", false);
			laminaria = new JRadioButton("Laminaria", true);
			zostera = new JRadioButton("Zostera", false);
			
			// disable all swimmable features //
			fish.setEnabled(false);
			jellyfish.setEnabled(false);
			l_frequency.setEnabled(false);
			t_frequency.setEnabled(false);		
			l_animalcolor.setEnabled(false);
			animalColor.setEnabled(false);
			l_horSpeed.setText("Enter Coordinate X: ");
			l_verSpeed.setText("Enter Coordinate Y: ");
		}
		else if (creatureAllowed == "Swimmable")
		{
			fish = new JRadioButton("Fish", true);
			jellyfish = new JRadioButton("Jellyfish", false);
			laminaria = new JRadioButton("Laminaria", false);
			zostera = new JRadioButton("Zostera", false);
			
			laminaria.setEnabled(false);
			zostera.setEnabled(false);
		}		
		else // All creature allowed //
		{
			fish = new JRadioButton("Fish", true);
			jellyfish = new JRadioButton("Jellyfish", false);
			laminaria = new JRadioButton("Laminaria", false);
			zostera = new JRadioButton("Zostera", false);
		}
		
		// set action when radiobutton pressed //
		fish.addItemListener(radioButton);
		jellyfish.addItemListener(radioButton);
		laminaria.addItemListener(radioButton);
		zostera.addItemListener(radioButton);
		radioButtons = new ButtonGroup();
		
		// set the radio buttons as a group //
		radioButtons.add(fish); 
		radioButtons.add(jellyfish); 
		radioButtons.add(laminaria);
		radioButtons.add(zostera);
	}
	
	private void CheckFields() throws Exception
	{
		// t_sizeOfAnimal TextField //
		if (t_sizeOfAnimal.getText().equals(""))
			throw new Exception("Enter the size of Animal");
		int sizeOfAnimal = Integer.parseInt(t_sizeOfAnimal.getText());
		if (sizeOfAnimal < 20 || sizeOfAnimal > 320)
			throw new Exception("Size of Animal has to be between 20 to 320!");

		if (t_frequency.isEnabled()) // if its a Swimmable //
		{
			// t_HorizontalSpeed and t_VerticalSpeed TextField //
			if (t_horSpeed.getText().equals(""))
				throw new Exception("Enter the Horizontal Speed");		
			if (t_verSpeed.getText().equals(""))
				throw new Exception("Enter the Vertical Speed");	

			int horizontalSpeed = Integer.parseInt(t_horSpeed.getText());
			int verticalSpeed = Integer.parseInt(t_verSpeed.getText());
			if ((horizontalSpeed < 1 || horizontalSpeed > 10) || (verticalSpeed < 1 || verticalSpeed > 10))
				throw new Exception("Speed has to be between 1 to 10!");
			
			if (t_frequency.getText().equals(""))
				throw new Exception("Enter the Frequency of Eating");
			
			int frequency = Integer.parseInt(t_frequency.getText());
			if (frequency < 100 || frequency > 10000)
				throw new Exception("Frequency of Eating has to be between 100 to 10,000!");
		}
		else
		{
			// t_HorizontalSpeed and t_VerticalSpeed TextField //
			if (t_horSpeed.getText().equals(""))
				throw new Exception("Enter Coordinate X");		
			if (t_verSpeed.getText().equals(""))
				throw new Exception("Enter Coordinate Y");	

			int x = Integer.parseInt(t_horSpeed.getText());
			int y = Integer.parseInt(t_verSpeed.getText());
			if (x < 0 || x > AquaPanel.PANEL_WIDTH)
				throw new Exception("Coordinate X has to be between 0 to " + AquaPanel.PANEL_WIDTH + "!");
			if (y < 0 || y > AquaPanel.PANEL_HEIGHT)
				throw new Exception("Coordinate Y has to be between 0 to " + AquaPanel.PANEL_HEIGHT + "!");
		}
	}
	
	private Color getSelectedColor()
	{
		String color = animalColor.getSelectedItem().toString(); // get string of the color that was chosen - Example "(0,0,255)" //
		char[] color_without_brackets = new char[color.length()-2]; // create char array in size of color string -2 (no brackets) //
		color.getChars(1, color.length()-1, color_without_brackets, 0); // copy the color to char array without brackets - Example '0,0,255' //
		int r = 0, g = 0, b = 0;
		int index = color_without_brackets.length-1; // start from the end //
		int RGB_index = 3;
		int tempNumber = 0;
		int multiplicationByTen = 1;
		while (RGB_index > 0) // this loop will run 3 times //
		{
			while (index >= 0 && color_without_brackets[index] != ',') // while not comma //
			{
				tempNumber += Integer.parseInt(String.valueOf(color_without_brackets[index]))*multiplicationByTen;
				multiplicationByTen *= 10;
				--index;
			}
			--index;
			multiplicationByTen = 1;
			if (RGB_index == 3)
				b = tempNumber;
			else if (RGB_index == 2)
				g = tempNumber;
			else if (RGB_index == 1)
				r = tempNumber;
			tempNumber = 0;
			--RGB_index;
		}	
		return new Color(r,g,b);		
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{			
		if (e.getSource() == b_Add) // if button was pressed //
		{
			try{
				CheckFields(); // 
				String type = SelectedRadioButton(); // find which factoty is selected //
				
				// create the creature using factoty //
				SeaCreature creature = factory.produceSeaCreature(type);
				
				// get the size and the color from the fields //
				int sizeOfAnimal = Integer.parseInt(t_sizeOfAnimal.getText()); 
				int horSpeed = Integer.parseInt(t_horSpeed.getText());
				int verSpeed = Integer.parseInt(t_verSpeed.getText());	

				if (type == "Fish" || type == "Jellyfish")
				{
					int frequency = Integer.parseInt(t_frequency.getText());	
					Color color = getSelectedColor();
					((Swimmable)creature).setFields(myAquaPanel, sizeOfAnimal, horSpeed, verSpeed, frequency, color);
				}
				else
					((Immobile)creature).setFields(myAquaPanel, sizeOfAnimal, horSpeed, verSpeed);
				
				myAquaPanel.AddCreature(creature); // add the creature to the panel //

				dispose(); // close the screen in the end of the addition //
			}
		    catch (NumberFormatException nfe)
		    {
		    	JOptionPane.showMessageDialog(null,"Text defined only for integer numbers!");
		    }
			catch(Exception message)
			{
				JOptionPane.showMessageDialog(null,message.getMessage());
			}
		}
	}
	
	private String SelectedRadioButton()
	{
		if (fish.isSelected())
		{
			factory = new AnimalFactory(); // cast AbstractSeaFactory to AnimalFactory //
			return "Fish";
		}
		else if (jellyfish.isSelected())
		{
			factory = new AnimalFactory(); // cast AbstractSeaFactory to AnimalFactory //
			return "Jellyfish";
		}
		else if (laminaria.isSelected())
		{
			factory = new PlantFactory(); // cast AbstractSeaFactory to AnimalFactory //
			return "Laminaria";
		}
		else if (zostera.isSelected())
		{
			factory = new PlantFactory(); // cast AbstractSeaFactory to AnimalFactory //
			return "Zostera";
		}
		return null;
	}
	
	public void addColor(String colorToAdd)
	{
		animalColor.addItem(colorToAdd);
	}

}
