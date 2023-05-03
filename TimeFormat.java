public class TimeFormat implements Comparable {
    private int hours;
    private int minutes;
    private int seconds;
    private int millis;

    public TimeFormat() {
        hours = 0;
        minutes = 0;
        seconds = 0;
        millis = 0;
    }

    public TimeFormat(int timeMillis) {
        hours = timeMillis / 3600000;
        timeMillis -= 3600000 * hours;
        minutes = timeMillis / 60000;
        timeMillis -= 60000 * minutes;
        seconds = timeMillis / 1000;
        timeMillis -= 1000 * seconds;
        millis = timeMillis / 10;
    }

    public TimeFormat(int h, int m, int s, int ms) {
        hours = h;
        minutes = m;
        seconds = s;
        millis = ms;
    }

    public TimeFormat(int m, int s, int ms) {
        hours = 0;
        minutes = m;
        seconds = s;
        millis = ms;
    }

    public TimeFormat(int s, int ms) {
        hours = 0;
        minutes = 0;
        seconds = s;
        millis = ms;
    }

    public TimeFormat(String init)
    {
        // TODO complete constructor
    }

    public int compareTo(Object other) {
        TimeFormat ot = (TimeFormat) other;
        long thisTotal = getHours() * 3600000 + getMinutes() * 60000 + getSeconds() * 1000 + getMilliseconds() * 10;
        long otherTotal = ot.getHours() * 3600000 + ot.getMinutes() * 60000 + ot.getSeconds() * 1000 + ot.getMilliseconds() * 10;
        return (int)(thisTotal - otherTotal);
    }

    @Override
    public String toString() {
        String res = "";
        if (hours != 0) res += String.valueOf(hours) + ":";
        if (res.length() != 0 || minutes != 0) {
            if (minutes < 10 && hours != 0) res += "0" + String.valueOf(minutes) + ":";
            else res += String.valueOf(minutes) + ":";
        }
        if (res.length() != 0 || seconds != 0) {
            if (seconds < 10 && minutes != 0) res += "0" + String.valueOf(seconds) + ":";
            else res += String.valueOf(seconds) + ":";
        }
        if (res.length() != 0 || seconds != 0) {
            if (seconds < 10 && minutes != 0) res += "0" + String.valueOf(seconds) + ".";
            else res += String.valueOf(seconds) + ".";
        }
        if (res.length() != 0 || millis != 0) {
            if (millis < 10 && seconds != 0) res += "0" + String.valueOf(millis);
            else res += String.valueOf(millis);
        }
        return res;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilliseconds() {
        return millis;
    }
}
