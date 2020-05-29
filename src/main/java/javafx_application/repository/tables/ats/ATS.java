package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

public class ATS implements Table {
    private Integer atsId;
    private Integer atsType;


    @Override
    public String getMainString() {
        return atsId.toString();
    }

    public static String getRepresentativeString() {
        return "C##ATS.ATS";
    }
}
