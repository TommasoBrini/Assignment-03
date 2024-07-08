package org.ass03.part1.concreteSimulation;

import akka.actor.ActorSystem;
import org.ass03.part1.car.CarAgentInfo;
import org.ass03.part1.util.Message;
import org.ass03.part1.util.V2d;
import org.ass03.part1.environment.Road;
import org.ass03.part1.environment.SimulationListener;
import org.ass03.part1.environment.TrafficLight;
import scala.concurrent.Await;
import scala.concurrent.Future;
import akka.pattern.Patterns;
import scala.concurrent.duration.Duration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RoadSimView extends JFrame implements SimulationListener {

	private RoadSimViewPanel panel;
	private static final int CAR_DRAW_SIZE = 10;
	private final JButton start = new JButton("Start");
	private final JButton stop = new JButton("Stop");
	private final JButton pause = new JButton("Pause");
	private final JSlider slider = new JSlider(100, 100000);
	private final JLabel stepNumLabel = new JLabel("Number of step to execute:  %,d".formatted(slider.getValue()));

	public RoadSimView() {
		super("RoadSim View");

		setSize(1500,800);

		panel = new RoadSimViewPanel(1500,600);
		panel.setSize(1500, 600);

		JPanel cp = new JPanel();
		LayoutManager layout = new BorderLayout();
		cp.setLayout(layout);

		JPanel buttons = new JPanel();
		LayoutManager layout2 = new GridLayout(3,1);
		buttons.setLayout(layout2);
		JLabel simName = new JLabel("Simulation");
		simName.setHorizontalAlignment(SwingConstants.CENTER);
		Font font = simName.getFont();
		simName.setFont(new Font(font.getName(), Font.BOLD, 15)); // Cambia la dimensione del font a 20

		stepNumLabel.setFont(new Font(font.getName(), Font.BOLD, 15));
		stepNumLabel.setHorizontalAlignment(SwingConstants.CENTER);

		slider.addChangeListener(e -> stepNumLabel.setText("Number of step to execute: %,d".formatted(slider.getValue())));

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

	public void addStartListener(ActionListener l) {
		start.addActionListener(l);
	}

	public void addStopListener(ActionListener l) {
		stop.addActionListener(l);
	}

	public void addPauseListener(ActionListener l) {
		pause.addActionListener(l);
	}

	public void display() {
		SwingUtilities.invokeLater(this::run);
	}

	@Override
	public void notifyInit(int t) {
	}

	@Override
	public void notifyStepDone(int td, ActorSystem system) {

		List<Road> roads;
		List<CarAgentInfo> info;
		List<TrafficLight> sems;
		Future<Object> future = Patterns.ask(system.actorSelection("/user/env"), new Message("get-roads", null), 1000);
		try {
			roads = (List<Road>) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		future = Patterns.ask(system.actorSelection("/user/env"), new Message("get-agent-info", null), 1000);
		try {
			info = (List<CarAgentInfo>) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		future = Patterns.ask(system.actorSelection("/user/env"), new Message("get-traffic-lights", null), 1000);
		try {
			sems = (List<TrafficLight>) Await.result(future, Duration.create(10, TimeUnit.SECONDS));
		} catch (TimeoutException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		panel.update(roads, info, sems);
	}

	private void run() {
		this.setVisible(true);
	}

	public JButton getStartButton() {
		return start;
	}

	public JButton getStopButton() {
		return stop;
	}

	public JButton getPauseButton() {
		return pause;
	}

	public JSlider getSlider() {
		return slider;
	}

	static class RoadSimViewPanel extends JPanel {
		
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
