
import java.util.Arrays;

class Day {

    RA primary, secondary;

    boolean weekend;
    String dayOfweek, month;
    int day;

    Day(String dayOfWeek, String month, int day) {
        this.dayOfweek = dayOfWeek;
        this.month = month;
        this.day = day;

        String[] weekends = new String[]{"Thursday", "Friday", "Saturday"};

        if (Arrays.asList(weekends).contains(dayOfWeek)) {
            weekend = true;
        }
    }

    public RA getPrimary() {
        return primary;
    }

    public RA getSecondary() {
        return secondary;
    }

    public void setPrimary(RA primary) {
        this.primary = primary;
    }

    public void setSecondary(RA secondary) {
        this.secondary = secondary;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public String getDayofWeek() {
        return dayOfweek;
    }

    public String getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
