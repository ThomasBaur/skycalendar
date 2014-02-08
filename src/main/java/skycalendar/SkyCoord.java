package skycalendar;

/**
 * The class representing an earth base sky coordinate of a celestial object.
 */
public class SkyCoord {
	private double rightAscension;
	private double declination;

	public SkyCoord() {
		rightAscension = 0;
		declination = 0;
	}

	public SkyCoord(double rightAscension, double declination) {
		this.rightAscension = rightAscension;
		this.declination = declination;
	}

	public void setRightAscension(double rightAscension) {
		this.rightAscension = rightAscension;
	}

	public void setDeclination(double declination) {
		this.declination = declination;
	}

	public double getRightAscension() {
		return rightAscension;
	}

	public double getDeclination() {
		return declination;
	}

	public void setRightAscension(int hour, int min, double sec) {
		rightAscension = Math.abs(hour) + ((double) min) / 60.0 + sec / 3600.0;
		if (hour < 0) {
			rightAscension *= -1;
		}
	}

	public void setDeclination(int deg, int min, double sec) {
		declination = Math.abs(deg) + ((double) min) / 60.0 + sec / 3600.0;
		if (deg < 0) {
			declination *= -1;
		}
	}

	public String toString() {
		return "Ra: " + rightAscension + ", Dec: " + declination;
	}
}
