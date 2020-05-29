package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

import java.util.HashMap;
import java.util.Map;

public class Addressnumber implements Table {
    private Integer id;
    private String indx;
    private Integer numb;
    private String street;
    private String home;
    private String apartment;
    private String area;
    private Integer teltype;
    private Integer idtypeplace;
    private Map<String, String> params = new HashMap<>();

    public Addressnumber(Integer teltype, Integer idTypePlace, String indx, String street, String apartment, String home, String area) {
        params.put("idtypeplace", idTypePlace.toString());
        params.put("teltype", teltype.toString());
        params.put("indx", indx);
        params.put("street", street);
        params.put("home", home);
        params.put("apartment", apartment);
        params.put("area", area);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public static Map<String, Class> getTableParams() {
        Map<String, Class> table = new HashMap<>();
        table.put("idtypeplace", Integer.class);
        table.put("teltype", Integer.class);
        table.put("indx", String.class);
        table.put("street", String.class);
        table.put("home", String.class);
        table.put("apartment", String.class);
        table.put("area", String.class);
        return table;
    }

    public static String getRepresentativeString() {
        return "C##ATS.ADDRESSNUMBER";
    }

    @Override
    public String getMainString() {
        return this.id.toString();
    }
}
