package trafficSimulationIbar;

public class Car {
	private double x;
	private double y;
	private double dx;
	private double dy;
	private int road;
	private String lbl;
	private int carWidth;
	private int carLength;
	private double carWaiting;

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public int getRoad() {
		return road;
	}

	public void setRoad(int road) {
		this.road = road;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public String getLbl() {
		return lbl;
	}

	public void setLbl(String lbl) {
		this.lbl = lbl;
	}

	public int getCarWidth() {
		return carWidth;
	}

	public void setCarWidth(int carWidth) {
		this.carWidth = carWidth;
	}

	public int getCarLength() {
		return carLength;
	}

	public void setCarLength(int carLength) {
		this.carLength = carLength;
	}

	public double getCarWaiting() {
		return carWaiting;
	}

	public void setCarWaiting(double carWaiting) {
		this.carWaiting = carWaiting;
	}

}
