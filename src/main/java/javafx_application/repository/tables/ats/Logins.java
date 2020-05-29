package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

import java.util.HashMap;
import java.util.Map;

public class Logins implements Table {
    private Integer id;
    private String login;
    private String password;
    private String isLoggedIn;
    private Integer role;
    private Map<String, String> params = new HashMap<>();

    public Logins(String login, String password, String isLoggedIn, String role) {
        params.put("login", login);
        params.put("password", password);
        params.put("isLoggedIn", isLoggedIn);
        params.put("role", role);
    }

    public Map<String, String> getParams() {
        return params;
    }

    public static String getRepresentativeString() {
        return "C##ATS.LOGINS";
    }

    @Override
    public String getMainString() {
        return this.login;
    }

    public static Map<String, Class> getTableParams() {
        Map<String, Class> table = new HashMap<>();
        table.put("login", String.class);
        table.put("password", String.class);
        table.put("isLoggedIn", String.class);
        table.put("role", String.class);
        return table;
    }
}
