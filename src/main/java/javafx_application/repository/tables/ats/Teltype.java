package javafx_application.repository.tables.ats;

import javafx_application.repository.tables.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Teltype implements Table {
    private Integer teltypeid;
    private String name;
    private Integer mounthcost;

    public Teltype(ResultSet resultSet) {
        try {
            teltypeid = resultSet.getInt("teltypeid");
            name = resultSet.getString("name");
            mounthcost = resultSet.getInt("mounthcost");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getMainString() {
        return this.name;
    }

    public Integer getTeltypeid() {
        return teltypeid;
    }

    public static String getRepresentativeString() {
        return "C##ATS.TELEPHONETYPE";
    }
}
