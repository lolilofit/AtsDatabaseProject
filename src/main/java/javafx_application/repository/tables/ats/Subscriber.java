package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

public class Subscriber implements Table {

    public static String getRepresentativeString() {
        return "C##ATS.SUBSCRIBER";
    }

    @Override
    public String getMainString() {
        return null;
    }
}
