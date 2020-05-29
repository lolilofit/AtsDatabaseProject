package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx_application.repository.CRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IntercityCallController {
    private CRUDRepository crudRepository;

    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @FXML
    private TextField city;
    @FXML
    private TextField caller;
    @FXML
    private TextField when;

    private Integer findUserId() throws SQLException {
        ResultSet resultSet = crudRepository.executeQuery("SELECT subscriber.id from c##ats.subscriber where name = '" + caller.getText() + "'");
        resultSet.next();
        return  resultSet.getInt(1);
    }

    public void addCall() throws SQLException {
        Integer userId = findUserId();

        crudRepository.executeQuery("INSERT INTO c##ats.intercity_talks (CLIENT, CITY, CALLTIME) VALUES (" + userId + ", '"
                + city.getText() + "', '" +  when.getText() + "')");
    }
}
