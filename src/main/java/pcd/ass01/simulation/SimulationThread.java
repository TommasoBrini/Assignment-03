package pcd.ass01.simulation;

public class SimulationThread extends Thread {

        private final AbstractSimulation simulation;
        private int step;

        public SimulationThread(AbstractSimulation simulation) {
            this.simulation = simulation;
        }

        public void setStep(int step){
            this.step = step;
        }

        @Override
        public void run() {
            simulation.run(step);
        }

        public void resumeSimulation() {
            simulation.resume();
        }

        public void pauseSimulation() {
            simulation.pause();
        }

        public void stopSimulation() {
            simulation.stop();
        }
}
