package GUI.PropulsionDraw;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JPanel;

import GUI.PropulsionDraw.ComponentMetaFileTypes.TankMetaFile;

public class StatisticsPanel {
	private JPanel panel;
	private Canvas canvas;
	
	static DecimalFormat decf 		  = new DecimalFormat("#.##");
	
	public StatisticsPanel(Canvas canvas) {
		this.canvas = canvas;
	    panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.setSize(300, 30);
		
		panel = createPanel();
		
	}
	
	public void updatePanel() {
		//System.out.println("update statistics panel");
		panel.removeAll();
		panel = createPanel();
		panel.repaint();
		panel.revalidate();
	}
	
	private JPanel createPanel() {
		
		JPanel elementNumber = CreateLine("Number of Elements: ", ""+canvas.getCanvasElements().size());
		panel.add(elementNumber);
		
		JPanel RelNumber = CreateLine("Number of Relations: ", ""+canvas.getRelationships().size());
		panel.add(RelNumber);
		
		double mass = 0 ; 
		for(BoxElement element : canvas.getCanvasElements()) {
			mass = mass + element.getMetaFile().getSystemMass();
			//System.out.println(element.getMetaFile().getSystemMass());
		}
		JPanel systemMass = CreateLine("Total Mass (dry) [kg]: ", ""+decf.format(mass));
		panel.add(systemMass);
		
		double fuelCapacity = 0 ; 
		for(BoxElement element : canvas.getCanvasElements()) {
			if(element.getMetaFile().getElementType()==4)
			fuelCapacity = fuelCapacity + ((TankMetaFile) element.getMetaFile()).getPropellantMassCapacity();
			//System.out.println("capacity: "+((TankMetaFile) element.getMetaFile()).getPropellantMassCapacity());
		}
		JPanel fuelCapacityPanel = CreateLine("Propellant capacity [kg]: ", "" + decf.format(fuelCapacity));
		panel.add(fuelCapacityPanel);
		
		
		return panel;
	}

	
	public  JPanel CreateLine(String description, String value) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setSize(300, 30);
		
		JLabel descriptionLabel = new JLabel(""+description);
		//descriptionLabel.setSize(200, 25);
		descriptionLabel.setHorizontalAlignment(JLabel.LEFT);
		//descriptionLabel.setLocation(0, 1);
		panel.add(descriptionLabel, BorderLayout.WEST);
		
		JLabel valueLabel = new JLabel(""+value);
		panel.add(valueLabel, BorderLayout.EAST);

		return panel;
	}

	public JPanel getPanel() {
		return panel;
	}
	
	
}
