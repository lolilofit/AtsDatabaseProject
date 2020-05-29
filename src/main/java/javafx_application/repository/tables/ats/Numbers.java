package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

public class Numbers implements Table {
    int numberId;
    String isFree;
    String numbr;
    int atsId;


    @Override
    public String getMainString() {
        return "C##ATS.NUMBERS";
    }

    public static String getRepresentativeString() {
        return "C##ATS.NUMBERS";
    }
}
