package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

import java.util.HashMap;
import java.util.Map;

public class Roles implements Table {
    private Integer id;
    private String name;

    public static String getRepresentativeString() {
        return "C##ATS.ROLES";
    }

    @Override
    public String getMainString() {
        return this.name;
    }

    public static Map<String, Class> getTableParams() {
        Map<String, Class> fields = new HashMap<>();
        fields.put("name", String.class);
        return fields;
    }
}
