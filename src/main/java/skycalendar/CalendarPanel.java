package skycalendar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.swing.JPanel;

/**
 * CalendarPanel.java
 */
public class CalendarPanel extends JPanel {
	private SkyObject moon = new SkyObject(SkyObject.Type.MOON);
	private SkyObject sun = new SkyObject(SkyObject.Type.SUN);
	private SkyObject naut = new SkyObject(SkyObject.Type.NAUTICAL_TWILIGHT);
	private SkyObject astro = new SkyObject(SkyObject.Type.ASTRONOMICAL_TWILIGHT);
	private SkyObject custom = null;

	private double longitude = 6;
	private double latitude = 50;

	private int timeBarWidth = 15;
	private int dayBarHeight = 20;
	private int dayWidth = 20;
	private int majorTick = 10;
	private int minorTick = 5;
	private int hourHeight = 20;

	private int freeSpace = 2;
	private int moonWidth = 2;

	private Calendar calendar;
	private TimeZone desiredTimeZone = TimeZone.getTimeZone("GMT0");
	private int actMaxDays;

	private RiseSet moonEphem[];
	private RiseSet sunEphem[];
	private RiseSet nautEphem[];
	private RiseSet astroEphem[];
	private RiseSet customEphem[];

	private boolean showMoon = true;
	private boolean showSun = true;
	private boolean showNaut = true;
	private boolean showAstro = true;
	private boolean showCustom = false;
	private boolean showGrid = true;

	private Color moonColor = Color.white;
	private Color customColor = Color.cyan;
	private Color sunColor = Color.yellow;
	private Color nautColor = sunColor.darker();
	private Color astroColor = nautColor.darker();
	private Color gridColor = Color.red;

	private static final int FIRST_MINUTE = 0;
	private static final int LAST_MINUTE = 24 * 60;

	private static final float[] dash1 = { 3.0f };
	private static final BasicStroke dashed = new BasicStroke(1.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

	/**
	 * The CalendarPanel offers just a default constructor.
	 * 
	 * This initializes the CalendarPanel to use the current date. If this needs
	 * to be changed, the properties of the panel may be investigated.
	 */
	public CalendarPanel() {
		super();
		TimeZone tz = TimeZone.getTimeZone("GMT0");
		calendar = new GregorianCalendar(tz);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		recalculate();
	}

	private void recalculate() {
		FontMetrics metrics = getFontMetrics(getFont());
		hourHeight = metrics.getHeight() + 1;
		dayWidth = metrics.stringWidth("00") + 2 * freeSpace + 1;
		timeBarWidth = dayWidth + majorTick;
		actMaxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		Dimension d = getPreferredSize();
		d.width = actMaxDays * dayWidth + 2 * freeSpace + timeBarWidth;
		d.height = 25 * hourHeight + dayBarHeight + 2 * freeSpace;
		setPreferredSize(d);
		setMinimumSize(d);

		moonEphem = new RiseSet[actMaxDays];
		sunEphem = new RiseSet[actMaxDays];
		nautEphem = new RiseSet[actMaxDays];
		astroEphem = new RiseSet[actMaxDays];
		customEphem = new RiseSet[actMaxDays];
		for (int i = 0; i < actMaxDays; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, i + 1);
			moonEphem[i] = moon.calcRiseSet(calendar, longitude, latitude);
			sunEphem[i] = sun.calcRiseSet(calendar, longitude, latitude);
			nautEphem[i] = naut.calcRiseSet(calendar, longitude, latitude);
			astroEphem[i] = astro.calcRiseSet(calendar, longitude, latitude);
			if (custom != null) {
				customEphem[i] = custom.calcRiseSet(calendar, longitude,
						latitude);
			}
		}
	}

	private Calendar toDesiredTZ(Calendar gmtCal) {
		long timeInMillis = gmtCal.getTimeInMillis();
		Calendar tzCal = new GregorianCalendar(desiredTimeZone);
		tzCal.setTimeInMillis(timeInMillis);
		return tzCal;
	}

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g.clearRect(0, 0, getWidth(), getHeight());

		paintTimeBar(g2);
		paintDayBar(g2);

		g.setColor(Color.black);

		g.fillRect(timeBarWidth + freeSpace, timeToY(LAST_MINUTE), actMaxDays
				* dayWidth, timeToY(FIRST_MINUTE) - timeToY(LAST_MINUTE));

		int sunBarWidth = dayWidth;
		if (showGrid) {
			sunBarWidth--;
		}
		if (showMoon) {
			sunBarWidth -= moonWidth;
		}
		if (showCustom) {
			sunBarWidth -= moonWidth;
		}

		Stroke solid = g2.getStroke();
		for (int i = 0; i < actMaxDays; i++) {
			int barPos = i * dayWidth + timeBarWidth + freeSpace;
			if (showGrid) {
				g.setColor(gridColor);
				g2.setStroke(dashed);
				g.drawLine(barPos + 1, timeToY(LAST_MINUTE), barPos + 1,
						getHeight());
				g2.setStroke(solid);
				barPos++;
			}
			if (showMoon) {
				paintObject(moonEphem[i], g2, moonColor, barPos, moonWidth);
				barPos += moonWidth;
			}
			if (showCustom) {
				paintObject(customEphem[i], g2, customColor, barPos, moonWidth);
				barPos += moonWidth;
			}
			if (showAstro) {
				paintObject(astroEphem[i], g2, astroColor, barPos, sunBarWidth);
			}
			if (showNaut) {
				paintObject(nautEphem[i], g2, nautColor, barPos, sunBarWidth);
			}
			if (showSun) {
				paintObject(sunEphem[i], g2, sunColor, barPos, sunBarWidth);
			}
			barPos += sunBarWidth;
		}
		if (showGrid) {
			g2.setStroke(dashed);
			for (int i = 0; i <= 24; i++) {
				int y = timeToY(i * 60);
				g.setColor(gridColor);
				g.drawLine(timeBarWidth + freeSpace, y, actMaxDays * dayWidth
						+ timeBarWidth + freeSpace - 1, y);
			}
			g2.setStroke(solid);
		}
	}

	private void paintTimeBar(Graphics2D g) {
		FontMetrics metrics = getFontMetrics(getFont());
		int ascent = metrics.getAscent();
		for (int i = 0; i <= 24; i++) {
			String out = Integer.toString(i);
			int x = metrics.stringWidth(out);
			int y = timeToY(i * 60);
			g.drawString(Integer.toString(i), dayWidth - x - freeSpace, y
					+ ascent / 2);
			if (i == 0 || i == 12 || i == 24) {
				g.drawLine(timeBarWidth - majorTick, y, timeBarWidth, y);
			} else {
				g.drawLine(timeBarWidth - minorTick, y, timeBarWidth, y);
			}
		}
	}

	private void paintDayBar(Graphics2D g) {
		FontMetrics metrics = getFontMetrics(getFont());
		int yPos = (dayBarHeight - metrics.getHeight()) / 2
				+ metrics.getMaxDescent();
		for (int i = 0; i < actMaxDays; i++) {
			int width = metrics.stringWidth(Integer.toString(i + 1));
			int xPos = (dayWidth - width) / 2;
			g.drawString(Integer.toString(i + 1), timeBarWidth + freeSpace
					+ dayWidth * i + xPos + 1, getHeight() - yPos);
		}
	}

	private void paintObject(RiseSet aRiseSet, Graphics2D g, Color sourceColor,
			int offset, int width) {
		int riseMin = getMinOfDay(aRiseSet.getRiseTime());
		int setMin = getMinOfDay(aRiseSet.getSetTime());

		g.setColor(sourceColor);

		if (!aRiseSet.isRises() && !aRiseSet.isSets()) {
			/* no rise or set */
			if (aRiseSet.isAboveHorizon()) {
				/* always visible */
				g.fillRect(offset, timeToY(LAST_MINUTE), width,
						timeToY(FIRST_MINUTE) - timeToY(LAST_MINUTE));
			} else {
				/* not at all visible */
			}
		} else if (!aRiseSet.isRises() && aRiseSet.isSets()) {
			/* only setting */
			g.fillRect(offset, timeToY(setMin), width, timeToY(FIRST_MINUTE)
					- timeToY(setMin));
		} else if (aRiseSet.isRises() && !aRiseSet.isSets()) {
			/* only rising */
			g.fillRect(offset, timeToY(LAST_MINUTE), width, timeToY(riseMin)
					- timeToY(LAST_MINUTE));
		} else {
			/* rising and setting */
			if (riseMin < setMin) {
				/* first rises then sets */
				g.fillRect(offset, timeToY(setMin), width, timeToY(riseMin)
						- timeToY(setMin));
			} else {
				/* first sets then rises again */
				g.fillRect(offset, timeToY(setMin), width,
						timeToY(FIRST_MINUTE) - timeToY(setMin));
				g.fillRect(offset, timeToY(LAST_MINUTE), width,
						timeToY(riseMin) - timeToY(LAST_MINUTE));
			}
		}
	}

	private int getMinOfDay(Calendar date) {
		Calendar tzCal = toDesiredTZ(date);
		return tzCal.get(Calendar.HOUR_OF_DAY) * 60
				+ tzCal.get(Calendar.MINUTE);
	}

	private int timeToY(int minOfDay) {
		return getHeight() - freeSpace - dayBarHeight - hourHeight / 2
				- (hourHeight * minOfDay) / 60;
	}

	public void setDate(Calendar newDate) {
		calendar = (Calendar) newDate.clone();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		recalculate();
		revalidate();
		repaint();
	}

	public Calendar getDate() {
		return (Calendar) calendar.clone();
	}

	public void setShowMoon(boolean value) {
		showMoon = value;
		repaint();
	}

	public boolean isShowMoon() {
		return showMoon;
	}

	public void setShowSun(boolean value) {
		showSun = value;
		repaint();
	}

	public boolean isShowSun() {
		return showSun;
	}

	public void setShowNaut(boolean value) {
		showNaut = value;
		repaint();
	}

	public boolean isShowNaut() {
		return showNaut;
	}

	public void setShowAstro(boolean value) {
		showAstro = value;
		repaint();
	}

	public boolean isShowAstro() {
		return showAstro;
	}

	public void setShowCustom(boolean value) {
		if (custom != null) {
			showCustom = value;
			repaint();
		}
	}

	public boolean isShowCustom() {
		return showCustom;
	}

	public void setShowGrid(boolean value) {
		showGrid = value;
		repaint();
	}

	public boolean isShowGrid() {
		return showGrid;
	}

	public void setLongitude(double value) {
		longitude = value;
		recalculate();
		repaint();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double value) {
		latitude = value;
		recalculate();
		repaint();
	}

	public double getLatitude() {
		return latitude;
	}

	public void setDesiredTimeZone(TimeZone desiredTimeZone) {
		this.desiredTimeZone = desiredTimeZone;
		recalculate();
		repaint();
	}

	public TimeZone getDesiredTimeZone() {
		return desiredTimeZone;
	}

	public void setCustom(SkyObject obj) {
		custom = obj;
		recalculate();
		repaint();
	}

	public SkyObject getCustom() {
		return custom;
	}

}
