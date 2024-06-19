package pcd.ass01.simulation.implementation;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pcd.ass01.agent.AbstractAgent;
import pcd.ass01.agent.implementation.CarAgentInfo;
import pcd.ass01.environment.AbstractEnvironment;
import pcd.ass01.environment.implementation.Road;
import pcd.ass01.environment.implementation.RoadsEnv;
import pcd.ass01.environment.implementation.TrafficLight;
import pcd.ass01.simulation.SimulationListener;
import pcd.ass01.simulation.SimulationThread;
import pcd.ass01.util.V2d;

import java.awt.*;
import javax.swing.*;

public class RoadSimView extends JFrame implements SimulationListener, ActionListener {

	private RoadSimViewPanel panel;
	private static final int CAR_DRAW_SIZE = 10;
	private final JButton start = new JButton("Start");
	private final JButton stop = new JButton("Stop");
	private final JButton pause = new JButton("Pause");
	private final JSlider slider = new JSlider(100, 100000);
	private final JLabel stepNumLabel = new JLabel("Number of step to execute:  %,d".formatted(slider.getValue()));
	private SimulationThread simulationThread;
	
	public RoadSimView(SimulationThread thread, String name) {
		super("RoadSim View");

		simulationThread = thread;

		setSize(1500,800);
			
		panel = new RoadSimViewPanel(1500,600);
		panel.setSize(1500, 600);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);


		JPanel buttons = new JPanel();
		LayoutManager layout2 = new GridLayout(3,1);
		buttons.setLayout(layout2);
		JLabel simName = new JLabel(name);
		simName.setHorizontalAlignment(SwingConstants.CENTER);
		Font font = simName.getFont();
		simName.setFont(new Font(font.getName(), Font.BOLD, 15)); // Cambia la dimensione del font a 20

		stepNumLabel.setFont(new Font(font.getName(), Font.BOLD, 15));
		stepNumLabel.setHorizontalAlignment(SwingConstants.CENTER);

		start.addActionListener(this);
		stop.addActionListener(this);
		pause.addActionListener(this);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				stepNumLabel.setText("Number of step to execute: %,d".formatted(slider.getValue()));
			}
		});

		pause.setEnabled(false);
		stop.setEnabled(false);

		Dimension buttonSize = new Dimension(100, 50);
		// Imposta le dimensioni dei pulsanti
		start.setPreferredSize(buttonSize);
		stop.setPreferredSize(buttonSize);
		pause.setPreferredSize(buttonSize);

		start.setBackground(Color.green);

		slider.setPreferredSize(new Dimension(1000, 50));

		buttons.add(simName);

		JPanel buttonPanel = new JPanel(new GridLayout(1,3));
		buttonPanel.add(start, BorderLayout.NORTH);
		buttonPanel.add(stop, BorderLayout.NORTH);
		buttonPanel.add(pause, BorderLayout.NORTH);

		buttons.add(buttonPanel);
		
		JPanel sliderPanel = new JPanel(new GridLayout(1, 2));
		sliderPanel.add(stepNumLabel);
		sliderPanel.add(slider);

		buttons.add(sliderPanel);

		cp.add(buttons, BorderLayout.NORTH);
		cp.add(BorderLayout.CENTER,panel);
		setContentPane(cp);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
			
	}
	
	public void display() {
		SwingUtilities.invokeLater(() -> {
			this.setVisible(true);
		});
	}

	@Override
	public void notifyInit(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		// TODO Auto-generated method stub
		var e = ((RoadsEnv) env);
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
	}

	@Override
	public void notifyStepDone(int t, List<AbstractAgent> agents, AbstractEnvironment env) {
		var e = ((RoadsEnv) env);
		panel.update(e.getRoads(), e.getAgentInfo(), e.getTrafficLights());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.start){
			this.start.setBackground(Color.white);
			this.stop.setBackground(Color.red);
			this.start.setEnabled(false);
			this.stop.setEnabled(true);
			this.pause.setEnabled(true);
			simulationThread.setStep(slider.getValue());
			simulationThread.start();
		} else if (e.getSource() == this.stop) {
			this.simulationThread.stopSimulation();
			this.stop.setEnabled(false);
			this.pause.setEnabled(false);
			JOptionPane.showMessageDialog(this, "Simulation closed");
			System.exit(0);
		} else if (e.getSource() == this.pause) {
			this.start.setBackground(Color.white);
			if(this.pause.getText().equals("Resume")){
				this.pause.setText("Pause");
				simulationThread.resumeSimulation();
				this.stop.setBackground(Color.red);
				this.stop.setEnabled(true);
			} else {
				this.pause.setText("Resume");
				simulationThread.pauseSimulation();
				this.stop.setBackground(Color.white);
				this.stop.setEnabled(false);
			}
		}
	}


	class RoadSimViewPanel extends JPanel {
		
		List<CarAgentInfo> cars;
		List<Road> roads;
		List<TrafficLight> sems;
		
		public RoadSimViewPanel(int w, int h){
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);   
	        Graphics2D g2 = (Graphics2D)g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			g2.clearRect(0,0,this.getWidth(),this.getHeight());
			
			if (roads != null) {
				for (var r: roads) {
					g2.drawLine((int)r.getFrom().x(), (int)r.getFrom().y(), (int)r.getTo().x(), (int)r.getTo().y());
				}
			}
			
			if (sems != null) {
				for (var s: sems) {
					if (s.isGreen()) {
						g.setColor(new Color(0, 255, 0, 255));
					} else if (s.isRed()) {
						g.setColor(new Color(255, 0, 0, 255));
					} else {
						g.setColor(new Color(255, 255, 0, 255));
					}
					g2.fillRect((int)(s.getPos().x()-5), (int)(s.getPos().y()-5), 10, 10);
				}
			}
			
			g.setColor(new Color(0, 0, 0, 255));

			if (cars != null) {
				for (var c: cars) {
					double pos = c.getPos();
					Road r = c.getRoad();
					V2d dir = V2d.makeV2d(r.getFrom(), r.getTo()).getNormalized().mul(pos);
					g2.drawOval((int)(r.getFrom().x() + dir.x() - CAR_DRAW_SIZE/2), (int)(r.getFrom().y() + dir.y() - CAR_DRAW_SIZE/2), CAR_DRAW_SIZE , CAR_DRAW_SIZE);
				}
			}
  	   }
	
	   public void update(List<Road> roads, 
			   			  List<CarAgentInfo> cars,
			   			List<TrafficLight> sems) {
		   this.roads = roads;
		   this.cars = cars;
		   this.sems = sems;
		   repaint();
	   }
	}
}
