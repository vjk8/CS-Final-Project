package components;

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
        numberSeconds += Double.parseDouble(originalTime.substring(firstColonIndex+1, firstColonIndex+3));
        double mils = Double.parseDouble(originalTime.substring(firstColonIndex+4, Math.min(firstColonIndex+6, originalTime.length()))) / 100.0;
        if (mils < .1) mils *= 10;
        numberSeconds += mils;
        
        int timeMillis = (int) (numberSeconds * 1000);

        hours = timeMillis / 3600000;
        timeMillis -= 3600000 * hours;
        minutes = timeMillis / 60000;
        timeMillis -= 60000 * minutes;
        seconds = timeMillis / 1000;
        timeMillis -= 1000 * seconds;
        millis = timeMillis / 10;
    }

    public int compareTo(Object other) {
        TimeFormat ot = (TimeFormat)other;
        return (intValue() - ot.intValue());
    }

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

    public int intValue() {
        return getHours() * 3600000 + getMinutes() * 60000 + getSeconds() * 1000 + getMilliseconds() * 10;
    }
}
