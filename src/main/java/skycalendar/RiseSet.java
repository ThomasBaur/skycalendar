package skycalendar;

import java.util.Calendar;

/**
 * This class keeps the rise and set information for a given day in the
 * calculation.
 */
public class RiseSet {

	private boolean aboveHorizon;

	private boolean rises;

	private boolean sets;

	private Calendar riseTime;

	private Calendar setTime;

	private String dayTime(Calendar cal) {
		return new StringBuilder().append(cal.get(Calendar.HOUR_OF_DAY))
				.append(':').append(cal.get(Calendar.MINUTE)).toString();
	}

	public String toString() {
		String out = "";
		// logic to sort the various rise and set states
		if (isRises() || isSets()) {
			// current object rises and sets today
			if (isRises()) {
				out += dayTime(getRiseTime()) + "\n";
			} else {
				out += "	----\n";
			}
			if (isSets()) {
				out += dayTime(getSetTime()) + "\n";
			} else {
				out += "	----\n";
			}
		} else {
			// current object not so simple
			if (isAboveHorizon()) {
				out += "always above horizon\n";
			} else {
				out += "always below horizon\n";
			}
		}
		return out;
	}

	public boolean isAboveHorizon() {
		return aboveHorizon;
	}

	public void setAboveHorizon(boolean aboveHorizon) {
		this.aboveHorizon = aboveHorizon;
	}

	public boolean isRises() {
		return rises;
	}

	public void setRises(boolean rises) {
		this.rises = rises;
	}

	public boolean isSets() {
		return sets;
	}

	public void setSets(boolean sets) {
		this.sets = sets;
	}

	public Calendar getRiseTime() {
		return riseTime;
	}

	public void setRiseTime(Calendar riseTime) {
		this.riseTime = riseTime;
	}

	public Calendar getSetTime() {
		return setTime;
	}

	public void setSetTime(Calendar setTime) {
		this.setTime = setTime;
	}

}
