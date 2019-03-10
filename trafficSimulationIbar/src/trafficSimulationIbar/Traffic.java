package trafficSimulationIbar;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Event;
import java.awt.Panel;

public class Traffic extends Applet {

	private static final long serialVersionUID = 1L;
	private Road road;
	private int carNum;
	private Thread lightThrd[] = new Thread[3];

	public void init() {
		setLayout(new BorderLayout());
		road = new Road(this);
		add("Center", road);
		carNum = 50;
		carNum = Math.min(carNum, 70);
		for (int k = 0; k < carNum; k++)
			road.findCar(Integer.toString(k));
		road.setLghtOrStp(1);
		for (int k = 0; k < 3; k++) {
			ChangeLight[] light = road.getLight();
			lightThrd[k] = new Thread(light[k]);
			light[k].setRedLight((k + 1) * 1000 + 3000);
			light[k].setGreenLight(light[k].getRedLight());
			lightThrd[k].start();
		}
		CarFlow[] carPerMin = road.getCarPerMin();
		carPerMin[0].setTime0(System.currentTimeMillis());
		carPerMin[0].setCarCount(0);
		Panel btpnl = new Panel();
		add("South", btpnl);
		btpnl.add(new Button("Start"));
		btpnl.add(new Button("Stop"));
	}

	public boolean action(Event evt, Object arg) {
		if (((Button) evt.target).getLabel().equals("Stop")) {
			for (int k = 0; k < 3; k++) {
				if (lightThrd[k].isAlive())
					lightThrd[k].interrupt();
			}
			road.stop();
		} else if (((Button) evt.target).getLabel().equals("Start")) {
			if (road.getLghtOrStp() == 1)
				for (int k = 0; k < 3; k++) {
					if (!lightThrd[k].isAlive()) {
						lightThrd[k] = new Thread(road.getLight()[k]);
						lightThrd[k].start();
					}
				}
			if (!road.relaxer.isAlive())
				road.start();
		}
		return true;
	}

	public void start() {
		road.start();
	}

	public void stop() {
		road.stop();
	}
}