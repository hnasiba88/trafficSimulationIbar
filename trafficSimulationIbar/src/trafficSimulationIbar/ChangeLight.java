package trafficSimulationIbar;

public class ChangeLight implements Runnable {
	private int signal;
	private int redLight;
	private int greenLight;
	private Thread simulator;
	private int pause;

	ChangeLight() {
		signal = 1;
		redLight = 6000;
		greenLight = 6000;
	}

	public int getSignal() {
		return signal;
	}

	public void setSignal(int signal) {
		this.signal = signal;
	}

	public int getRedLight() {
		return redLight;
	}

	public void setRedLight(int redLight) {
		this.redLight = redLight;
	}

	public int getGreenLight() {
		return greenLight;
	}

	public void setGreenLight(int greenLight) {
		this.greenLight = greenLight;
	}

	public void run() {
		signal = 1;
		while (true) {
			if (signal == 1) {
				signal = 0;
				pause = greenLight;
			} else {
				signal = 1;
				pause = redLight;
			}
			try {
				Thread.sleep(pause);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void start() {
		simulator = new Thread(this);
		simulator.start();
	}

	public void stop() {
		simulator.interrupt();
	}
}
