package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

public class SubscriberType implements Table {
    private Integer id;
    private String name;
    private Integer sale;

    @Override
    public String getMainString() {
        return name;
    }

    public static String getRepresentativeString() {
        return "C##ATS.SUBSCRIBERTYPE";
    }
}
