package skycalendar;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * The representation of a celestial object. Currently this can be either the
 * moon, the sun, the position of the nautical or astronomical twilight or any
 * fixed point given by right ascension and declination.
 * <p>
 * Planets will be provided in a future version.
 */
public class SkyObject {
    public enum Type {MOON, SUN, NAUTICAL_TWILIGHT, ASTRONOMICAL_TWILIGHT, CUSTOM}

    private final Type objectType;
    private final SkyCoord customCoord;

    private static final double COSECLIP = Math.cos(Math.toRadians(23.4));
    private static final double SINECLIP = Math.sin(Math.toRadians(23.4));

    public SkyObject(Type objectType) {
        this.objectType = objectType;
        this.customCoord = null;
    }

    public SkyObject(SkyCoord customCoord) {
        this.objectType = Type.CUSTOM;
        this.customCoord = customCoord;
    }

    public RiseSet calcRiseSet(Calendar givenDate,
                               double longitude, double latitude) {
        // double rads = Math.PI / 180.0;
        // double degs = 180.0 / Math.PI;
        double sinho = 0;
        /* routines use east longitude negative convention */
        longitude = -longitude;
        double date = modifiedJulianDate(givenDate.get(Calendar.YEAR),
                givenDate.get(Calendar.MONTH) + 1,
                givenDate.get(Calendar.DATE), 0);
		/*
		 * define the altitudes for each object treat twilight as a separate
		 * object 3, so sinalt routine falls through to finding Sun altitude
		 * again
		 */
        double sl = sinDeg(latitude);
        double cl = cosDeg(latitude);
        switch (objectType) {
            case MOON:
			/* moonrise - average diameter used */
                sinho = sinDeg(8.0 / 60);
                break;
            case SUN:
			/* sunrise - classic value for refraction */
                sinho = sinDeg(-50.0 / 60.0);
                break;
            case NAUTICAL_TWILIGHT:
			/* nautical twilight */
                sinho = sinDeg(-12.0);
                break;
            case ASTRONOMICAL_TWILIGHT:
			/* astronomical twilight */
                sinho = sinDeg(-18.0);
                break;
            case CUSTOM:
			/* custom object - coordinates given */
                sinho = sinDeg(0.0);
                break;
        }
        RiseSet riseset = new RiseSet();
        double hour = 0.2;

		/* See STEP 1 and 2 of Web page description. */
        double ym = sinOfAltitude(date, hour - 1, longitude, cl, sl) - sinho;
        riseset.setAboveHorizon((ym > 0));

        double utrise = 0;
        double utset = 0;
		/* used later to classify non-risings */
        do {
            QuadStruct quad = new QuadStruct();
			/* STEP 1 and STEP 3 of Web page description */
            double y0 = sinOfAltitude(date, hour, longitude, cl, sl) - sinho;
            double yp = sinOfAltitude(date, hour + 1, longitude, cl, sl) - sinho;

			/* STEP 4 of web page description */
            quad.xe = 0;
            quad.ye = 0;
            quad.z1 = 0;
            quad.z2 = 0;

            int nz = quad(ym, y0, yp, quad);

            switch (nz) {
                case 0:
				/* nothing - go to next time slot */
                    break;

                case 1:
				/* simple rise / set event */
                    if (ym < 0) {
					/* must be a rising event */
                        utrise = hour + quad.z1;
                        riseset.setRises(true);
                    } else {
					/* must be setting */
                        utset = hour + quad.z1;
                        riseset.setSets(true);
                    }
                    break;

                case 2:
				/* rises and sets within interval */
                    if (quad.ye < 0) {
					/* minimum - so set then rise */
                        utrise = hour + quad.z2;
                        utset = hour + quad.z1;
                    } else {
					/* maximum - so rise then set */
                        utrise = hour + quad.z1;
                        utset = hour + quad.z2;
                    }
                    riseset.setRises(true);
                    riseset.setSets(true);
                    break;
            }
			/* reuse the ordinate in the next interval */
            ym = yp;
            hour = hour + 2;
			/*
			 * STEP 5 of Web page description - have we finished for this
			 * object?
			 */
        } while ((hour < 25) && !(riseset.isRises() && riseset.isSets()));
        riseset.setRiseTime(hourMinute(utrise, givenDate));
        riseset.setSetTime(hourMinute(utset, givenDate));
        return riseset;
    }

    private double sinDeg(double x) {
        return Math.sin(Math.toRadians(x));
    }

    private double cosDeg(double x) {
        return Math.cos(Math.toRadians(x));
    }

    /* returns integer part of a number */
    private double ipart(double x) {
        return sgn(x) * Math.floor(Math.abs(x));
    }

    /* returns sign of a number */
    private double sgn(double x) {
        if (x < 0.0) {
            return -1.0;
        } else if (x > 0.0) {
            return 1.0;
        } else {
            return 0.0;
        }
    }

    /* returns fractional part of a number */
    private double fractionalPart(double x) {
        x -= Math.floor(x);
        if (x < 0.0) {
            x += 1;
        }
        return x;
    }

    /**
     * returns number containing the time written in hours and minutes rounded
     * to the nearest minute
     */
    private Calendar hourMinute(double ut, Calendar givenDate) {
        ut = Math.floor(ut * 60 + 0.5) / 60.0;
        int h = (int) ut;
        int m = (int) (60 * (ut - h) + 0.5);
        Calendar cal = (Calendar) givenDate.clone();
        cal.set(Calendar.HOUR_OF_DAY, h);
        cal.set(Calendar.MINUTE, m);
        return cal;
    }

    /**
     * returns modified julian date number of days since 1858 Nov 17 00:00h
     * valid for any date since 4713 BC assumes gregorian calendar after 1582
     * Oct 15, Julian before Years BC assumed in calendar format, i.e. the year
     * before 1 AD is 1 BC
     */
    private double modifiedJulianDate(int year, int month, int date, double hour) {
        double a = 10000.0 * year + 100.0 * month + date;
        if (year < 0) {
            year += 1;
        }
        if (month <= 2) {
            month += 12;
            year -= 1;
        }
        double b;
        if (a <= 15821004.1) {
            b = -2 + (int) (year + 4716) / 4 - 1179;
        } else {
            b = (int) (year / 400) - (int) (year / 100) + (int) (year / 4);
        }
        a = 365.0 * year - 679004.0;

        return a + b + ipart(30.6001 * (month + 1)) + date + hour / 24;
    }

    /**
     * returns the local siderial time for the mjd and longitude specified
     */
    private double localSiderialTime(double modifiedJulianDate, double glong) {
        double mjd0 = ipart(modifiedJulianDate);
        double ut = (modifiedJulianDate - mjd0) * 24.0;
        double t = (mjd0 - 51544.5) / 36525;
        double gmst = 6.697374558 + 1.0027379093 * ut;
        gmst = gmst + (8640184.812866 + (0.093104 - 0.0000062 * t) * t) * t
                / 3600.0;
        return 24.0 * fractionalPart((gmst - glong / 15.0) / 24.0);
    }

    /**
     * Returns RA and DEC of Sun to roughly 1 arcmin for few hundred years
     * either side of J2000.0
     */
    private SkyCoord sun(double time) {
        double p2 = 2 * Math.PI;
		/* Mean anomaly */
        double m = p2 * fractionalPart(0.993133 + 99.997361 * time);
		/* Eq centre */
        double dL = 6893.0 * Math.sin(m) + 72.0 * Math.sin(2 * m);
        double L = p2
                * fractionalPart(0.7859453 + m / p2 + (6191.2 * time + dL) / 1296000.0);
		/* convert to RA and DEC - ecliptic latitude of Sun taken as zero */
        return eclipticToEquatorial(L, 0);
    }

    /**
     * returns ra and dec of Moon to 5 arc min (ra) and 1 arc min (dec) for a
     * few centuries either side of J2000.0 Predicts rise and set times to
     * within minutes for about 500 years in past - TDT and UT time diference
     * may become significant for long times
     */
    private SkyCoord moon(double time) {
        double p2 = 6.283185307;
        double ARC = 206264.8062;
		/* mean long Moon in revs */
        double L0 = fractionalPart(0.606433 + 1336.855225 * time);
		/* mean anomaly of Moon */
        double L = p2 * fractionalPart(0.374897 + 1325.55241 * time);
		/* mean anomaly of Sun */
        double LS = p2 * fractionalPart(0.993133 + 99.997361 * time);
		/* diff longitude sun and moon */
        double d = p2 * fractionalPart(0.827361 + 1236.853086 * time);
		/* mean arg latitude */
        double F = p2 * fractionalPart(0.259086 + 1342.227825 * time);
		/* longitude correction terms */
        double dL = 22640.0 * Math.sin(L) - 4586.0 * Math.sin(L - 2.0 * d);
        dL = dL + 2370.0 * Math.sin(2.0 * d) + 769.0 * Math.sin(2.0 * L);
        dL = dL - 668.0 * Math.sin(LS) - 412.0 * Math.sin(2.0 * F);
        dL = dL - 212.0 * Math.sin(2.0 * L - 2.0 * d) - 206.0
                * Math.sin(L + LS - 2.0 * d);
        dL = dL + 192.0 * Math.sin(L + 2.0 * d) - 165.0
                * Math.sin(LS - 2.0 * d);
        dL = dL - 125.0 * Math.sin(d) - 110.0 * Math.sin(L + LS);
        dL = dL + 148.0 * Math.sin(L - LS) - 55.0 * Math.sin(2.0 * F - 2.0 * d);
		/* latitude arguments */
        double S = F + (dL + 412.0 * Math.sin(2.0 * F) + 541.0 * Math.sin(LS))
                / ARC;
        double h = F - 2.0 * d;
		/* latitude correction terms */
        double N = -526.0 * Math.sin(h) + 44.0 * Math.sin(L + h) - 31.0
                * Math.sin(h - L) - 23.0 * Math.sin(LS + h);
        N = N + 11.0 * Math.sin(h - LS) - 25.0 * Math.sin(F - 2.0 * L) + 21.0
                * Math.sin(F - L);
		/* Lat in rads */
        double lmoon = p2 * fractionalPart(L0 + dL / 1296000.0);
		/* long in rads */
        double bmoon = (18520.0 * Math.sin(S) + N) / ARC;
		/* convert to equatorial coords using a fixed ecliptic */
        return eclipticToEquatorial(lmoon, bmoon);
    }

    /**
     * Convert a longitude/latitude position on the ecliptic plane into an
     * equatorial RightAscension/Declination position
     */
    private SkyCoord eclipticToEquatorial(double longitude, double latitude) {
        double cosLat = Math.cos(latitude);
        double x = Math.cos(longitude) * cosLat;
        double yeclip = Math.sin(longitude) * cosLat;
        double zeclip = Math.sin(latitude);
        double yequat = yeclip * COSECLIP - zeclip * SINECLIP;
        double zequat = yeclip * SINECLIP + zeclip * COSECLIP;
        SkyCoord coord = new SkyCoord((12.0 / Math.PI) * Math.atan2(yequat, x),
                Math.toDegrees(Math.atan2(zequat,
                        Math.sqrt(x * x + yequat * yequat))));
        if (coord.getRightAscension() < 0) {
            coord.setRightAscension(coord.getRightAscension() + 24.0);
        }
        return coord;
    }


	/**
	 * returns calendar date as a string in international format given the
	 * modified julian date BC dates are in calendar format - i.e. no year zero
	 * Gregorian dates are returned after 1582 Oct 10th In English colonies and
	 * Sweeden, this does not reflect historical dates
	 */
//	private Calendar calday(double x) {
//		double jd = x + 2400000.5;
//		double jd0 = ipart(jd + 0.5);
//		double c;
//		if (jd0 < 2299161.0) {
//			c = jd0 + 1524;
//		} else {
//			double b = ipart((jd0 - 1867216.25) / 36524.25);
//			c = jd0 + (b - ipart(b / 4.0)) + 1525.0;
//		}
//		double d = ipart((c - 122.1) / 365.25);
//		double e = 365.0 * d + ipart(d / 4.0);
//		double F = ipart((c - e) / 30.6001);
//		int day = (int) (ipart(c - e + 0.5) - ipart(30.6001 * F));
//		int month = (int) (F - 1.0 - 12.0 * ipart(F / 14.0));
//		int year = (int) (d - 4715.0 - ipart((month + 7.0) / 10.0));
//		GregorianCalendar cal = new GregorianCalendar(year, month - 1, day);
//		return cal;
//	}

    /**
     * returns sine of the altitude of either the sun or the moon given the
     * modified julian day number at midnight UT and the hour of the UT day, the
     * longitude of the observer, and the sine and cosine of the latitude of the
     * observer
     */
    private double sinOfAltitude(double mjd0, double hour, double glong, double cphi,
                                 double sphi) {
        double instant = mjd0 + hour / 24.0;
        double t = (instant - 51544.5) / 36525.0;
        SkyCoord currentCoord;
        switch (objectType) {
            case MOON:
                currentCoord = moon(t);
                break;
            case CUSTOM:
                currentCoord = customCoord;
                break;
            default:
                currentCoord = sun(t);
        }
		/* hour angle of object */
        double tau = 15.0 * (localSiderialTime(instant, glong) - currentCoord.getRightAscension());
        return sphi * sinDeg(currentCoord.getDeclination()) + cphi
                * cosDeg(currentCoord.getDeclination()) * cosDeg(tau);
    }

    /**
     * finds a parabola through three points and returns values of coordinates
     * of extreme value (xe, ye) and zeros if any (z1, z2) assumes that the x
     * values are -1, 0, +1
     */
    private int quad(double ym, double y0, double yp, QuadStruct quad) {
        int nz = 0;
        double a = 0.5 * (ym + yp) - y0;
        double b = 0.5 * (yp - ym);
        double c = y0;
		/* x coord of symmetry line */
        quad.xe = -b / (2.0 * a);
		/* extreme value for y in interval */
        quad.ye = (a * quad.xe + b) * quad.xe + c;
		/* discriminant */
        double dis = b * b - 4.0 * a * c;

        if (dis > 0) {
			/* there are zeros */
            double dx = 0.5 * Math.sqrt(dis) / Math.abs(a);
            quad.z1 = quad.xe - dx;
            quad.z2 = quad.xe + dx;
            if (Math.abs(quad.z1) <= 1) {
				/* this zero is in interval */
                nz++;
            }
            if (Math.abs(quad.z2) <= 1) {
				/* this zero is in interval */
                nz++;
            }
            if (quad.z1 < -1.0) {
				/* this zero is in interval */
                quad.z1 = quad.z2;
            }
        }
        return nz;
    }

    public static void main(String[] args) {
        double longit = 6;
        double latt = 50;

        SkyObject v = new SkyObject(Type.MOON);
        GregorianCalendar cal = new GregorianCalendar(
                TimeZone.getTimeZone("GMT0"));
        cal.set(2012, 9, 19);
//		RiseSet moonRiseSetToday = v.calcRiseSet(cal, timezone, longit, latt);
//		Calendar riseTime = moonRiseSetToday.riseTime;
//		Calendar setTime = moonRiseSetToday.setTime;
//		System.out.println(new StringBuilder().append("Moon:\n")
//				.append("Rise: ").append(riseTime).append("\nSet: ")
//				.append(setTime).append("\n").toString());
        v = new SkyObject(Type.SUN);
        RiseSet sunRiseSetToday = v.calcRiseSet(cal, longit, latt);
        Calendar riseTime = sunRiseSetToday.getRiseTime();
        Calendar setTime = sunRiseSetToday.getSetTime();
        System.out.println("Sun:\n" + sunRiseSetToday.toString() +
                "\nRise: " + riseTime + "\nSet: " +
                setTime + "\n");
    }

    private static class QuadStruct {
        public double xe;
        public double ye;
        public double z1;
        public double z2;
    }
}
