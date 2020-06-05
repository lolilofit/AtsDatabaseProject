package javafx_application.repository;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class DBManager {
    private Connection connection;
    private String connectionURL;
    private String password;
    private String username;


    public void readConnectionInfo() {
        /*
        InputStream input = new FileInputStream("src/main/resources/db/databaseConnectionConfig.properties");
        Properties prop = new Properties();
        prop.load(input);
        connectionURL = prop.getProperty("connectionURL");
        password = prop.getProperty("password");
        username = prop.getProperty("username");
         */
        connectionURL = "jdbc:oracle:thin:@localhost:1521:XE";
        username = "C##ATS";
        password = "12345";
    }

    @PostConstruct
    public void connectToDB() throws SQLException {
        readConnectionInfo();
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        connection = DriverManager.getConnection(connectionURL, username, password);
    }

    Connection getConnection() {
        return connection;
    }
}
