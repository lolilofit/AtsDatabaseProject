package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

public class SubscLogin implements Table {
    private Integer login;
    private Integer subscid;

    @Override
    public String getMainString() {
        return null;
    }
    public static String getRepresentativeString() {
        return "C##ATS.SUBSCLOGIN";
    }
}
