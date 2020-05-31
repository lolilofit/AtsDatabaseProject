package javafx_application.repository;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Component
public class DBManager {
    private Connection connection;
    private String connectionURL;
    private String password;
    private String username;


    public void readConnectionInfo() throws IOException {
        //InputStream input = new FileInputStream("src/main/resources/db/databaseConnectionConfig.properties");
        //Properties prop = new Properties();
        //prop.load(input);
        //connectionURL = prop.getProperty("connectionURL");
        //password = prop.getProperty("password");
        //username = prop.getProperty("username");
        connectionURL = "jdbc:oracle:thin:@localhost:1521:XE";
        username = "C##ATS";
        password = "12345";
    }
/*
    void initDatabaseInfo() {
        //load from db
      //  databaseInfo = new HashMap<>();
        Map<String, Class> table1 = new HashMap<>();
        table1.put("name", String.class);
        table1.put("adress", String.class);
        table1.put("telephone", String.class);
        databaseInfo.put("saleman", table1);
    }
*/

//    Map<String, Class> getTableParams(String tablename) {
//        return databaseInfo.get(tablename);
//    }

    @PostConstruct
    public void connectToDB() throws SQLException, IOException {
        readConnectionInfo();
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        connection = DriverManager.getConnection(connectionURL, username, password);
  //      initDatabaseInfo();
    }

    public Connection getConnection() {
        return connection;
    }

    /*
    @Override
    public void run() {
        try {
            connectToDB();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

}
