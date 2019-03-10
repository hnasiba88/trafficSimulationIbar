package trafficSimulationIbar;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

public class Road extends Applet implements Runnable {

	private static final long serialVersionUID = 1L;
	Traffic traffic;
	private int nCars;
	private int lghtOrStp;
	private Car cars[] = new Car[100];
	private ChangeLight light[] = new ChangeLight[5];
	private CarFlow carPerMin[] = new CarFlow[5];
	Thread relaxer;
	private Thread flow;
	private int brgFlag[] = new int[5];
	private double speed = 10;
	private int carWidth = 6, carLength = 9;
	private int xPos[] = new int[5];
	private int yPos = 200;
	private int brgRight[] = new int[5];
	private int brgLeft[] = new int[5];
	private int brgTop = yPos + carLength;
	private int brgBottom = yPos - carLength;
	private int roadLeft[] = new int[5];
	private int roadRight[] = new int[5];
	private int roadTop = yPos + carWidth;
	private int roadBottom = yPos - carWidth;
	private Image carImg;
	private Dimension generalViewSize;
	private Graphics generalView;

	private final Color selectColor = Color.pink;
	private final Color carColor = Color.red;

	Road(Traffic traffic) {
		lghtOrStp = 1; // stop :0, light: 1
		this.traffic = traffic;
		for (int i = 0; i < 5; i++) {
			light[i] = new ChangeLight();
			carPerMin[i] = new CarFlow();

			xPos[i] = 150 * (i + 1);
			brgRight[i] = xPos[i] - carLength;
			brgLeft[i] = xPos[i] + carLength;
			brgFlag[i] = 0;
		}
		for (int k = 1; k < 4; k++) {
			roadLeft[k] = xPos[k - 1] - carWidth;
			roadRight[k] = xPos[k - 1] + carWidth;
		}
		roadLeft[0] = 0;
		roadRight[0] = 0;
	}

	int findCar(String lbl) {
		for (int i = 0; i < nCars; i++) {
			if (cars[i].getLbl().equals(lbl)) {
				return i;
			}
		}
		return addCar(lbl);
	}

	int addCar(String lbl) {
		int temp;
		Car car = new Car();
		temp = (int) (5 * Math.random());
		if (temp == 0 || temp == 4) {
			car.setX(480 + 210 * Math.random());
			car.setY(yPos);
			car.setCarWidth(carWidth);
			car.setCarLength(carLength);
		} else {
			car.setX(xPos[temp - 1]);
			car.setY(10 + 100 * Math.random());
			car.setCarWidth(carWidth);
			car.setCarLength(carLength);
		}
		if (temp == 4)
			temp = 0;
		car.setRoad(temp);
		car.setLbl(lbl);
		car.setCarWaiting(-1);
		cars[nCars] = car;
		return nCars++;
	}

	public int getLghtOrStp() {
		return lghtOrStp;
	}

	public void setLghtOrStp(int lghtOrStp) {
		this.lghtOrStp = lghtOrStp;
	}

	public ChangeLight[] getLight() {
		return light;
	}

	public void setLight(ChangeLight[] light) {
		this.light = light;
	}

	public CarFlow[] getCarPerMin() {
		return carPerMin;
	}

	public void setCarPerMin(CarFlow[] carPerMin) {
		this.carPerMin = carPerMin;
	}

	public void run() {
		for (int j = 0; j < 5; j++)
			light[j].setSignal(1);
		flow = new Thread(carPerMin[0]);
		carPerMin[0].setTime0(System.currentTimeMillis());
		carPerMin[0].setCarCount(0);
		flow.start();
		while (true) {
			relax();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	synchronized void relax() {
		for (int i = 0; i < nCars; i++) {
			if (cars[i].getRoad() == 0) {
				cars[i].setDx(-speed * Math.random());
				cars[i].setDy(2 * Math.random() - 1);
			} else {
				cars[i].setDy(speed * Math.random());
				cars[i].setDx(2 * Math.random() - 1);
			}
		}
		for (int i = 0; i < nCars; i++) {
			Car car1 = cars[i];
			for (int j = 0; j < nCars; j++) {
				Car car2 = cars[j];
				if (i == j || car1.getRoad() != car2.getRoad()) {
					continue;
				}
				double vx;
				if (car1.getRoad() == 0)
					vx = car1.getX() - car2.getX();
				else
					vx = car2.getY() - car1.getY();
				if (vx < 0)
					continue;
				double len = vx;

				if (len < (car2.getCarWidth() + car2.getCarLength())) {
					if (car1.getCarWaiting() < 0)
						car1.setCarWaiting(System.currentTimeMillis());

					if (car1.getRoad() == 0)
						car1.setDx(0);
					else
						car1.setDy(0);
				}
			}

		}
//move a car
		Dimension d = getSize();
		double temp;
		for (int i = 0; i < nCars; i++) {
			Car car = cars[i];
			if (car.getRoad() == 0) {
				temp = car.getX();
				car.setX(car.getX() + Math.max(-10, Math.min(10, car.getDx())));
				for (int k = 0; k < 3; k++) {
					if ((car.getX() < brgLeft[k] && car.getX() > brgRight[k]) && brgFlag[k] == 1) {
						if (temp > brgLeft[k] || temp < brgRight[k])
							car.setX(temp);
					} else if ((car.getX() < brgLeft[k] && car.getX() > brgRight[k]) && brgFlag[k] == 0)
						if (lghtOrStp == 0)
							brgFlag[k] = 1;
						else {
							if (light[k].getSignal() == 0)
								brgFlag[k] = 1;
							else
								car.setX(temp);
						}
					else if (temp < brgLeft[k] && temp > brgRight[k])
						brgFlag[k] = 0;
					if (car.getX() < 0) {
						car.setX(d.width - 10 * Math.random());
						carPerMin[0].setCarCount(carPerMin[0].getCarCount() + 1);
					} else if (car.getX() > d.width) {
						car.setX(d.width - 10 * Math.random());
					}
					if (car.getX() != temp && car.getCarWaiting() == -1) {
						carPerMin[0]
								.setCarWt(carPerMin[0].getCarWt() + System.currentTimeMillis() - car.getCarWaiting());
						car.setCarWaiting(-1);
					}
				}
			} else {
				temp = car.getY();
				car.setY(car.getY() + Math.max(-10, Math.min(10, car.getDy())));
				if ((car.getY() < brgTop && car.getY() > brgBottom) && brgFlag[car.getRoad() - 1] == 1) {
					if (temp > brgTop || temp < brgBottom)
						car.setY(temp);
				} else if ((car.getY() < brgTop && car.getY() > brgBottom) && brgFlag[car.getRoad() - 1] == 0)
					if (lghtOrStp == 0)
						brgFlag[car.getRoad() - 1] = 1;
					else {
						if (light[car.getRoad() - 1].getSignal() == 1)
							brgFlag[car.getRoad() - 1] = 1;
						else
							car.setY(temp);
					}
				else if (temp < brgTop && temp > brgBottom)
					brgFlag[car.getRoad() - 1] = 0;

				if (car.getY() > d.height || car.getY() < 0) {
					car.setY(10 * Math.random());
					carPerMin[0].setCarCount(carPerMin[0].getCarCount() + 1);
				}
			}
		}
		repaint();
	}

	public void paintCar(Graphics g, Car car) {
		int x = (int) car.getX();
		int y = (int) car.getY();
		g.setColor((car == null) ? selectColor : carColor);
		int w = car.getCarWidth();
		int h = car.getCarLength();
		g.fillRect(x - w / 2, y - h / 2, w, h);
		g.setColor(Color.black);
		g.drawRect(x - w / 2, y - h / 2, w - 1, h - 1);
	}

	public void paintRoad(Graphics g) {
		Dimension d = getSize();
		g.setColor(Color.gray);
		for (int k = 1; k < 4; k++) {
			g.drawLine(roadLeft[k], 0, roadLeft[k], roadBottom);
			g.drawLine(roadLeft[k], roadTop, roadLeft[k], d.height);
			g.drawLine(roadRight[k], 0, roadRight[k], roadBottom);
			g.drawLine(roadRight[k], roadTop, roadRight[k], d.height);
			g.drawLine(roadRight[k - 1], roadTop, roadLeft[k], roadTop);
			g.drawLine(roadRight[k - 1], roadBottom, roadLeft[k], roadBottom);
		}
		g.drawLine(roadRight[3], roadBottom, d.width, roadBottom);
		g.drawLine(roadRight[3], roadTop, d.width, roadTop);
	}

	public synchronized void update(Graphics g) {
		Dimension d = getSize();
		if ((carImg == null) || (d.width != generalViewSize.width) || (d.height != generalViewSize.height)) {
			carImg = createImage(d.width, d.height);
			generalViewSize = d;
			generalView = carImg.getGraphics();
		}

		generalView.setColor(getBackground());
		generalView.fillRect(0, 0, d.width, d.height);
		paintRoad(generalView);
		for (int i = 0; i < nCars; i++) {
			paintCar(generalView, cars[i]);
		}
		g.drawImage(carImg, 0, 0, null);
	}

	public void start() {
		relaxer = new Thread(this);
		relaxer.start();
	}

	public void stop() {
		relaxer.interrupt();
	}
}
