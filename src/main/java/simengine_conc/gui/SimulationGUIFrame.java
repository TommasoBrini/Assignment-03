package simengine_conc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SimulationGUIFrame extends JFrame implements ActionListener {

	private JButton start;
	private JButton stop;
	private JTextField nSteps;
	
	private SimulationController controller;
	
	public SimulationGUIFrame(int initialValue){
		setTitle("Simulation Dashboard");
		setSize(300,100);		
		nSteps = new JTextField(5);
		nSteps.setText(""+ initialValue);
		start = new JButton("start");
		stop  = new JButton("stop");
		stop.setEnabled(false);
		
		Container cp = getContentPane();
		JPanel panel = new JPanel();
		
		Box p0 = new Box(BoxLayout.X_AXIS);
		p0.add(new JLabel("Num Steps: "));
		p0.add(nSteps);
		Box p1 = new Box(BoxLayout.X_AXIS);
		p1.add(start);
		p1.add(stop);
		Box p2 = new Box(BoxLayout.Y_AXIS);
		p2.add(p0);
		p2.add(Box.createVerticalStrut(10));
		p2.add(p1);
		
		panel.add(p2);
		cp.add(panel);
		
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent ev){
				System.exit(-1);
			}
			public void windowClosed(WindowEvent ev){
				System.exit(-1);
			}
		});

		start.addActionListener(this);
		stop.addActionListener(this);
	}
	
	public void setController(SimulationController contr) {
		this.controller = contr;
	}
	
	public void actionPerformed(ActionEvent ev){
		Object src = ev.getSource();
		if (src==start){	
			try {
				int nSt = Integer.parseInt(nSteps.getText());
				start.setEnabled(false);
				stop.setEnabled(true);
				controller.notifyStarted(nSt);
			} catch (Exception ex) {}
		} else if (src == stop){
			start.setEnabled(true);
			stop.setEnabled(false);
			controller.notifyStopped();
		}
	}
	
	public void reset() {
		SwingUtilities.invokeLater(()-> {
			start.setEnabled(true);
			stop.setEnabled(false);
		});
	}


	public void display() {
        javax.swing.SwingUtilities.invokeLater(() -> {
        	this.setVisible(true);
        });
    }
	
	
}
