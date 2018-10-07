import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DuplicateAnimalDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
//	private JTable table;
    private AquaPanel myAquaPanel;
	private JPanel panel, panel_save;
	private JLabel l_animalSize, l_horSpeed, l_verSpeed, l_animalcolor, l_frequency;
	private JTextField t_sizeOfAnimal, t_horSpeed, t_verSpeed, t_frequency;
	private JComboBox<String> animalColor;
	private JButton b_Add;
	private AnimalFactory factory;
	private String type; // type of animal //
	
	public DuplicateAnimalDialog(AquaPanel myAquaPanel ,String type, int colorIndex,String size,String horSpeed,String verSpeed,String frequency)
	{
		super();
		this.myAquaPanel = myAquaPanel;
		factory = new AnimalFactory();
		
		// Define and Initialize Panels: //
		this.setModal(true); // make that the parent window cannot be accessed whilst this window is active //
		panel = new JPanel();
		panel_save = new JPanel();		
		panel_save.setLayout(new FlowLayout());
		panel.setLayout(new GridLayout(5,3)); // 5 rows //
		
		// Initialize Labels: //
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
		animalColor = new JComboBox<String>(AddCreatureDialog.COLORS);		
		
		// Copy Fields from the animal we want to duplicate: //
		t_sizeOfAnimal.setText(size);
		t_horSpeed.setText(horSpeed);
		t_verSpeed.setText(verSpeed);
		t_frequency.setText(frequency);
		animalColor.setSelectedIndex(colorIndex); // change //

		// Initialize 'Add' Button: //
		b_Add = new JButton("Add");
		
		////////// Start mouse event when press the 'Add' button //////////
		b_Add.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (e.getSource() == b_Add) // if button was pressed //
				{
					try{
						CheckFields();
						
						// create the creature using factoty //
						Swimmable animal = (Swimmable)factory.produceSeaCreature(type);
						
						// get size, color, speed and frequency from the fields //
						int sizeOfAnimal = Integer.parseInt(t_sizeOfAnimal.getText()); 
						int horSpeed = Integer.parseInt(t_horSpeed.getText());
						int verSpeed = Integer.parseInt(t_verSpeed.getText());	
						int frequency = Integer.parseInt(t_frequency.getText());	
						Color color = getSelectedColor();
						animal.setFields(myAquaPanel, sizeOfAnimal, horSpeed, verSpeed, frequency, color);
											
						myAquaPanel.AddCreature(animal); // add the animal to the panel //

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
		});
		////////// End mouse event when press the 'Add' button //////////
		
		// Add all components to the panel //
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
		setTitle("Edit Animal");
		
		// Organize the Panels on the screen //
		add(panel);
		add("South",panel_save);
		pack();
		setLocationRelativeTo(null); // open the frame in the middle of the screen //	

	}
	
	
	private void CheckFields() throws Exception
	{
		// t_sizeOfAnimal TextField //
		if (t_sizeOfAnimal.getText().equals(""))
			throw new Exception("Enter the size of Animal");
		int sizeOfAnimal = Integer.parseInt(t_sizeOfAnimal.getText());
		if (sizeOfAnimal < 20 || sizeOfAnimal > 320)
			throw new Exception("Size of Animal has to be between 20 to 320!");

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
}
