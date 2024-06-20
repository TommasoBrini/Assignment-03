package simengine_conc.gui;

/**
 * 
 * View designed as a monitor.
 * 
 * @author aricci
 *
 */
public class SimulationGUI {

	private SimulationGUIFrame gui;
	
	public SimulationGUI(int initialValue){	
		gui = new SimulationGUIFrame(initialValue);
	}
	
	public void setController(SimulationController contr) {
		gui.setController(contr);
	}

	public  void display() {
		gui.display();
    }
	
	public void reset() {
		gui.reset();
	}
}
