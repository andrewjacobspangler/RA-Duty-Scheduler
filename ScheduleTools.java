
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ScheduleTools {

    static public ArrayList<Day> createCalendar(String startDayName, String startMonth, int startDay, String endMonth, int endDay) {
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        int[] daysInMonths = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if ((checkEntry(startMonth, months) || checkEntry(endMonth, months) || checkEntry(startDayName, daysOfWeek)) == false) {
            throw new IllegalArgumentException("Invalid Input");
        }

        ArrayList<Day> Calendar = new ArrayList<>();
        int startMonthIndex = Arrays.asList(months).indexOf(startMonth);
        int endMonthIndex = Arrays.asList(months).indexOf(endMonth);
        int dayOfWeekIndex = Arrays.asList(daysOfWeek).indexOf(startDayName);

        for (int month = startMonthIndex; month <= endMonthIndex; month++) {
            if (month != startMonthIndex) {
                startDay = 1;
            }
            for (int day = startDay; day <= daysInMonths[month]; day++) {
                if ((month >= endMonthIndex) && (day >= endDay + 1)) {
                    break;
                }
                Calendar.add(new Day(daysOfWeek[dayOfWeekIndex], months[month], day));
                if (dayOfWeekIndex >= 6) {
                    dayOfWeekIndex = 0;
                } else {
                    dayOfWeekIndex++;
                }
            }
        }

        return Calendar;
    }

    static boolean checkEntry(String entry, String[] months) {
        for (String month : months) {
            if (month.equals(entry)) {
                return true;
            }
        }
        return false;
    }

    static ArrayList<Day> ScheduleDuty(ArrayList<RA> staff, ArrayList<Day> calendar) {
        List<String> weekendDays = Arrays.asList("Thursday", "Friday", "Saturday");
        Map<RA, int[]> dutyCounts = new HashMap<>();
        Map<RA, ArrayList<Integer>> scheduledDays = new HashMap<>();

        for (RA ra : staff) {
            dutyCounts.put(ra, new int[4]);
            scheduledDays.put(ra, new ArrayList<>());
        }

        for (int i = 0; i < calendar.size(); i++) {
            Day day = calendar.get(i);
            boolean isWeekend = weekendDays.contains(day.getDayofWeek());
            int shiftType = isWeekend ? 0 : 2;

            RA primary = getNextRA(staff, dutyCounts, shiftType, i, scheduledDays, calendar);
            if (primary != null) {
                day.setPrimary(primary);
                dutyCounts.get(primary)[shiftType]++;
                scheduledDays.get(primary).add(i);
            }

            shiftType = isWeekend ? 1 : 3;
            RA secondary = getNextRAForSecondary(staff, dutyCounts, shiftType, i, primary, scheduledDays, calendar);
            if (secondary != null) {
                day.setSecondary(secondary);
                dutyCounts.get(secondary)[shiftType]++;
                scheduledDays.get(secondary).add(i);
            }
        }

        return calendar;
    }

    static RA getNextRA(ArrayList<RA> staff, Map<RA, int[]> dutyCounts, int shiftType, int dayIndex, Map<RA, ArrayList<Integer>> scheduledDays, ArrayList<Day> calendar) {
        return findRA(staff, dutyCounts, shiftType, dayIndex, scheduledDays, calendar, null);
    }

    static RA getNextRAForSecondary(ArrayList<RA> staff, Map<RA, int[]> dutyCounts, int shiftType, int dayIndex, RA primary, Map<RA, ArrayList<Integer>> scheduledDays, ArrayList<Day> calendar) {
        return findRA(staff, dutyCounts, shiftType, dayIndex, scheduledDays, calendar, primary);
    }

    private static RA findRA(ArrayList<RA> staff, Map<RA, int[]> dutyCounts, int shiftType, int dayIndex, Map<RA, ArrayList<Integer>> scheduledDays, ArrayList<Day> calendar, RA excludeRA) {
        RA selectedRA = null;
        int minShifts = Integer.MAX_VALUE;
        Day day = calendar.get(dayIndex);
        boolean isWeekend = day.isWeekend();

        for (RA ra : staff) {
            if ((excludeRA == null || !ra.equals(excludeRA)) && !raHasShiftForDay(ra, day) && isShiftMoreThan3DaysAway(ra, dayIndex, scheduledDays, calendar)) {
                int shifts = dutyCounts.get(ra)[shiftType];

                int preferenceScore;
                if (isWeekend) {
                    preferenceScore = ra.getWeekendPrefs()[shiftType];
                } else {
                    preferenceScore = ra.getWeekdayPrefs()[shiftType];
                }

                if (shifts < minShifts || (shifts == minShifts && preferenceScore < getPreferenceScore(selectedRA, isWeekend, shiftType))) {
                    minShifts = shifts;
                    selectedRA = ra;
                }
            }
        }
        if (selectedRA == null) {
            for (RA ra : staff) {
                if ((excludeRA == null || !ra.equals(excludeRA)) && !raHasShiftForDay(ra, day)) {
                    int shifts = dutyCounts.get(ra)[shiftType];
                    if (shifts < minShifts) {
                        minShifts = shifts;
                        selectedRA = ra;
                    }
                }
            }
        }
        return selectedRA;
    }

    private static int getPreferenceScore(RA ra, boolean isWeekend, int shiftType) {
        if (ra == null) {
            return Integer.MAX_VALUE;
        }
        return isWeekend ? ra.getWeekendPrefs()[shiftType] : ra.getWeekdayPrefs()[shiftType];
    }

    static boolean raHasShiftForDay(RA ra, Day day) {
        return day.getPrimary() != null && day.getPrimary().equals(ra) || day.getSecondary() != null && day.getSecondary().equals(ra);
    }

    static boolean isShiftMoreThan3DaysAway(RA ra, int dayIndex, Map<RA, ArrayList<Integer>> scheduledDays, ArrayList<Day> calendar) {
        if (!scheduledDays.containsKey(ra)) {
            return true;
        }
        for (int scheduledDayIndex : scheduledDays.get(ra)) {
            if (Math.abs(dayIndex - scheduledDayIndex) <= 3) {
                return false;
            }
        }
        return true;
    }

    static void printResults(ArrayList<Day> calendar) {
        Map<String, int[]> dutyCounts = new HashMap<>();

        for (Day day : calendar) {
            System.out.println(day.getDayofWeek() + ", " + day.getMonth() + " " + day.getDay() + ":");

            if (day.getPrimary() != null) {
                System.out.println("  Primary: " + day.getPrimary().getName());
                dutyCounts.putIfAbsent(day.getPrimary().getName(), new int[4]);
                dutyCounts.get(day.getPrimary().getName())[day.isWeekend() ? 0 : 2]++;
            }
            if (day.getSecondary() != null) {
                System.out.println("  Secondary: " + day.getSecondary().getName());
                dutyCounts.putIfAbsent(day.getSecondary().getName(), new int[4]);
                dutyCounts.get(day.getSecondary().getName())[day.isWeekend() ? 1 : 3]++;
            }
        }

        System.out.println("\nDuty Count per RA:");
        for (Map.Entry<String, int[]> entry : dutyCounts.entrySet()) {
            int[] counts = entry.getValue();
            System.out.println(entry.getKey() + " - Weekend Primary: " + counts[0] + ", Weekend Secondary: " + counts[1] + ", Weekday Primary: " + counts[2] + ", Weekday Secondary: " + counts[3]);
        }
    }
}
