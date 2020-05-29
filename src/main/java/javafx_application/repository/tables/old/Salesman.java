package javafx_application.repository.tables.old;

import javafx_application.repository.tables.Table;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Salesman implements Table {
    private String name;
    private String address;
    private String telephone;
    private static String tablename = "saleman";

    public Salesman(ResultSet resultSet) {
        try {
            this.name = resultSet.getString("name");
            this.address = resultSet.getString("adress");
            this.telephone = resultSet.getString("telephone");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getAddress() { return address; }
    public String getName() { return name; }
    public String getTelephone() { return telephone; }
    public static String getTablename() { return tablename; }

    public static String getRepresentativeString() {
        return "";
    }

    @Override
    public String getMainString() {
        return null;
    }

    public static Map<String, Class> getTableParams() {
        Map<String, Class> table = new HashMap<>();
        table.put("name", String.class);
        table.put("adress", String.class);
        table.put("telephone", String.class);
        return table;
    }
}
