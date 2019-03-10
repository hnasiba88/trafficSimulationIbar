package trafficSimulationIbar;

public class CarFlow implements Runnable {
	private int carCount;
	private int count;
	private double carWt;
	private int pause;
	private double time0;
	private double time1;
	private double timeLap;
	private double carFlow[] = new double[40];

	Thread flow;

	CarFlow() {
		carCount = 0;
		carWt = 0;
		pause = 2000;
		time0 = 0;
		time1 = 0;
		for (int k = 0; k < 40; k++)
			carFlow[k] = 0;
		count = 0;
	}

	public int getCarCount() {
		return carCount;
	}

	public void setCarCount(int carCount) {
		this.carCount = carCount;
	}

	public double getTime0() {
		return time0;
	}

	public void setTime0(double time0) {
		this.time0 = time0;
	}

	public double getCarWt() {
		return carWt;
	}

	public void setCarWt(double carWt) {
		this.carWt = carWt;
	}

	public void run() {
		while (true) {
			time1 = System.currentTimeMillis();
			timeLap = time1 - time0;
			if (timeLap > 50)
				carFlow[count] = ((double) (carCount) / timeLap) * 1000;
			count = (count + 1) % 40;

			try {
				Thread.sleep(pause);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	public void start() {
		flow = new Thread(this);
		flow.start();
	}

	public void stop() {
		flow.interrupt();
	}
}
