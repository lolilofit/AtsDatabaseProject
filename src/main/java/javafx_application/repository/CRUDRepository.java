package javafx_application.repository;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CRUDRepository {
    ResultSet insert(Map<String, Class> tableParams, String tablename, Map<String, String> params) throws SQLException, NoSuchMethodException;
    ResultSet selectAllInColumn(String tableName, String columnName) throws SQLException;
    ResultSet update(String tablename, Map<String, String> setThis, Map<String, String> params) throws SQLException;
    ResultSet selectWithWhere(String tablename, Map<String, String> params) throws SQLException;
    public ResultSet selectWithWhereColumns(String tablename, Map<String, String> params, List<String> colums) throws SQLException;
    public ResultSet executeQuery(String query) throws SQLException;
}
