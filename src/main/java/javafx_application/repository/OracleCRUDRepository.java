package javafx_application.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class OracleCRUDRepository implements  CRUDRepository {
    private DBManager dBManager;

    @Autowired
    public void setDBManager(DBManager manager) {
        this.dBManager = manager;
    }


    @Override
    public ResultSet insert(Map<String, Class> tableParams, String tablename, Map<String, String> params)
            throws SQLException {
        if(params.size() == 0) {
            throw new SQLException();
        }

        ArrayList<String> keys = new ArrayList<>(tableParams.keySet());

        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO ").append(tablename).append(" (");
        query.append(keys.get(0));
        for(int i = 1; i < keys.size(); i++)
            query.append(", ").append(keys.get(i));
        query.append(") VALUES (");
        if(tableParams.get(keys.get(0)) == String.class)
            query.append("'").append(params.get(keys.get(0))).append("'");
        else
            query.append(params.get(0));

        for(int i = 1; i < params.size(); i++) {
            if(tableParams.get(keys.get(0)) == String.class)
                query.append(", '").append(params.get(keys.get(i))).append("'");
            else
                query.append(", " + params.get(keys.get(i)));
        }
        query.append(")");

        Statement statement = dBManager.getConnection().createStatement();

        return statement.executeQuery(query.toString());
    }

    @Override
    public ResultSet selectAllInColumn(String tableName, String columnName) throws SQLException {
        String query = "SELECT " + columnName + " FROM " + tableName;
        Statement statement = dBManager.getConnection().createStatement();
        return statement.executeQuery(query);
    }

    @Override
    public ResultSet selectWithWhereColumns(String tablename, Map<String, String> params, List<String> colums) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT ");
        int colSize = colums.size() - 1;
        for(int i = 0; i <= colSize; i++) {
            if(i != colSize)
                query.append(colums.get(i)).append(", ");
            else
                query.append(colums.get(i)).append(" ");
        }
        query.append("FROM ").append(tablename).append(" WHERE (");

        for(Map.Entry<String,String> entry : params.entrySet()) {
            query.append(entry.getKey()).append(" = ").append(entry.getValue());
            if((params.entrySet().toArray()[params.size() - 1]) != entry)
                query.append(" AND ");
        }
        query.append(")");
        Statement statement = dBManager.getConnection().createStatement();
        return statement.executeQuery(query.toString());
    }


    @Override
    public ResultSet selectWithWhere(String tablename, Map<String, String> params) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT " + "* " + "FROM ").append(tablename).append(" WHERE (");

        for(Map.Entry<String,String> entry : params.entrySet()) {
            query.append(entry.getKey()).append(" = ").append(entry.getValue());
            if((params.entrySet().toArray()[params.size() - 1]) != entry)
                query.append(" AND ");
        }
        query.append(")");
        Statement statement = dBManager.getConnection().createStatement();
        return statement.executeQuery(query.toString());
    }

    @Override
    public ResultSet update(String tablename, Map<String, String> setThis, Map<String, String> params) throws SQLException {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE ").append(tablename).append(" SET ");
        for(Map.Entry<String, String> entry: setThis.entrySet()) {
            query.append(entry.getKey()).append(" = ").append(entry.getValue());
            if((setThis.entrySet().toArray()[setThis.size() - 1]) != entry)
                query.append(" , ");
        }

        if(params.size() > 0) {
            query.append(" WHERE (");
            for(Map.Entry<String,String> entry : params.entrySet()) {
                query.append(entry.getKey()).append(" = ").append(entry.getValue());
                if((params.entrySet().toArray()[params.size() - 1]) != entry)
                    query.append(" AND ");
            }
            query.append(")");
        }
        Statement statement = dBManager.getConnection().createStatement();
        return statement.executeQuery(query.toString());
    }


    @Override
    public ResultSet executeQuery(String query) throws SQLException {
        Statement statement = dBManager.getConnection().createStatement();
        return statement.executeQuery(query);
    }
}
