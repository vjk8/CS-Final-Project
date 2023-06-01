package components;

/**
 * Holds Time data in a way that is intuitively displayed and compatible with
 * system times.
 */
public class TimeFormat implements Comparable {
    private int hours;
    private int minutes;
    private int seconds;
    private int millis;

    /**
     * Constructs a zero-valued TimeFormat
     */
    public TimeFormat() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        millis = 0;
    }

    /**
     * Constructs a TimeFormat when the time in question is system time
     * @param timeMillis
     */
    public TimeFormat(int timeMillis) {
        hours = timeMillis / 3600000;
        timeMillis -= 3600000 * hours;
        minutes = timeMillis / 60000;
        timeMillis -= 60000 * minutes;
        seconds = timeMillis / 1000;
        timeMillis -= 1000 * seconds;
        millis = timeMillis / 10;
    }

    /**
     * Constructs a TimeFormat when the time in question is in the order of hours
     * @param h hours
     * @param m minutes
     * @param s seconds
     * @param ms milliseconds
     */
    public TimeFormat(int h, int m, int s, int ms) {
        hours = h;
        minutes = m;
        seconds = s;
        millis = ms;
    }

    /**
     * Constructs a TimeFormat when the time in question is in the order of minutes
     * @param m minutes
     * @param s seconds
     * @param ms milliseconds
     */
    public TimeFormat(int m, int s, int ms) {
        hours = 0;
        minutes = m;
        seconds = s;
        millis = ms;
    }

    /**
     * Constructs a TimeFormat when the time in question is in the order of hours
     * @param s seconds
     * @param ms milliseconds
     */
    public TimeFormat(int s, int ms) {
        hours = 0;
        minutes = 0;
        seconds = s;
        millis = ms;
    }

    /**
     * Constructs a TimeFormat from an already-formatted String
     * @param originalTime A preformatted time String such as 1:23.45
     */
    public TimeFormat(String originalTime) {
        double numberSeconds = 0;
        String Minutes = "";
        int firstColonIndex = 0;
        for (int i = 0; i < 3; i++) {
            if (originalTime.charAt(i) == ':') {
                firstColonIndex = i;
                break;
            }
            Minutes += originalTime.charAt(i);
        }
        numberSeconds += Double.parseDouble(Minutes) * 60;
        numberSeconds += Double.parseDouble(originalTime.substring(firstColonIndex + 1, firstColonIndex + 3));
        double mils = Double.parseDouble(originalTime.substring(firstColonIndex + 4,
                                                                Math.min(firstColonIndex + 6, originalTime.length()))) /
                      100.0;
        if (mils < .1) mils *= 10;
        numberSeconds += mils;

        int timeMillis = (int)(numberSeconds * 1000);

        hours = timeMillis / 3600000;
        timeMillis -= 3600000 * hours;
        minutes = timeMillis / 60000;
        timeMillis -= 60000 * minutes;
        seconds = timeMillis / 1000;
        timeMillis -= 1000 * seconds;
        millis = timeMillis / 10;
    }

    /**
     * Comparator for TimeFormats used to satisfy Comparable interface requirements and sort TimeFormats in results
     * @param other the other TimeFormat (represented as an Object) to compare to
     * @return a value denoting the comparison between the TimeFormats
     */
    public int compareTo(Object other) {
        TimeFormat ot = (TimeFormat)other;
        return (intValue() - ot.intValue());
    }

    /**
     * Gives the TimeFormat as an intuitively formatted String
     * @return the String value of the TimeFormat
     */
    @Override
    public String toString() {
        String res = "";
        if (hours != 0) res += String.valueOf(hours) + ":";
        if (res.length() != 0 || minutes != 0) {
            if (minutes < 10 && hours != 0)
                res += "0" + String.valueOf(minutes) + ":";
            else
                res += String.valueOf(minutes) + ":";
        }
        if (res.length() != 0 || seconds != 0) {
            if (seconds < 10 && minutes != 0)
                res += "0" + String.valueOf(seconds);
            else
                res += String.valueOf(seconds);
        }
        if (res.length() != 0 || millis != 0) {
            if (millis < 10)
                res += ".0";
            else
                res += "." + String.valueOf(millis / 10);
        }
        return res;
    }

    /**
     * Getter for the hours
     * @return the number of hours
     */
    public int getHours() {
        return hours;
    }

    /**
     * Getter for the minutes
     * @return the number of minutes
     */
    public int getMinutes() {
        return minutes;
    }

    /**
     * Getter for the seconds
     * @return thenumber of seconds
     */
    public int getSeconds() {
        return seconds;
    }

    /**
     * Getter for the milliseconds
     * @return the number of milliseconds
     */
    public int getMilliseconds() {
        return millis;
    }

    /**
     * An objective way of looking at the time; represents the TimeFormat as a single int denoting the total
     * milliseconds
     * @return an integer representing the TimeFormat as a count of milliseconds
     */
    public int intValue() {
        return getHours() * 3600000 + getMinutes() * 60000 + getSeconds() * 1000 + getMilliseconds() * 10;
    }
}
