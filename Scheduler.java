
import java.util.ArrayList;
import java.util.Arrays;

public class Scheduler {

    public static void main(String[] args) throws Exception {
        ArrayList<Day> calendar = ScheduleTools.createCalendar("Wednesday", "February", 5, "May", 9);

        RA atoryia = new RA("Atoryia", new int[]{3, 1, 4, 2}, new int[]{2, 1, 3});
        RA elijah = new RA("Elijah", new int[]{2, 4, 1, 3}, new int[]{3, 2, 1});
        RA sai = new RA("Sai", new int[]{1, 3, 2, 4}, new int[]{1, 3, 2});
        RA paige = new RA("Paige", new int[]{4, 2, 1, 3}, new int[]{2, 3, 1});
        RA andrew = new RA("Andrew", new int[]{3, 4, 2, 1}, new int[]{1, 2, 3});
        RA jonathan = new RA("Jonathan", new int[]{2, 1, 3, 4}, new int[]{3, 1, 2});
        RA mithras = new RA("Mithras", new int[]{4, 3, 1, 2}, new int[]{2, 1, 3});
        RA chinwe = new RA("Chinwe", new int[]{1, 2, 4, 3}, new int[]{3, 2, 1});
        RA eli = new RA("Eli", new int[]{3, 1, 2, 4}, new int[]{1, 3, 2});
        RA chris = new RA("Chris", new int[]{2, 3, 1, 4}, new int[]{2, 3, 1});
        RA tanurah = new RA("Tanurah", new int[]{4, 1, 3, 2}, new int[]{1, 2, 3});
        RA kofi = new RA("Kofi", new int[]{1, 4, 2, 3}, new int[]{3, 1, 2});
        RA chiara = new RA("Chiara", new int[]{3, 2, 4, 1}, new int[]{2, 3, 1});
        RA afia = new RA("Afia", new int[]{4, 2, 3, 1}, new int[]{1, 2, 3});

        ArrayList<RA> staff = new ArrayList<>(Arrays.asList(atoryia, elijah, sai, paige, andrew, jonathan, mithras, chinwe, eli, chris, tanurah, kofi, chiara, afia));
        ArrayList<Day> results = ScheduleTools.ScheduleDuty(staff, calendar);
        ScheduleTools.printResults(results);
    }
}
