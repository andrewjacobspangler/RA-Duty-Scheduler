
public class RA {

    String name;
    int[] weekdayPrefs, weekendPrefs;

    public RA(String name, int[] weekdayPrefs, int[] weekendPrefs) {
        this.name = name;
        this.weekdayPrefs = weekdayPrefs;
        this.weekendPrefs = weekendPrefs;
    }

    int[] getWeekdayPrefs() {
        return weekdayPrefs;
    }

    int[] getWeekendPrefs() {
        return weekendPrefs;
    }

    String getName() {
        return name;
    }
}
