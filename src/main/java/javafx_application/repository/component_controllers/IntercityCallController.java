package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    private TextField name;
    @FXML
    private TextField secondName;
    @FXML
    private TextField when;
    @FXML
    private Label result;


    private Integer findUserId() throws SQLException {
        ResultSet resultSet = crudRepository.executeQuery("SELECT subscriber.id from c##ats.subscriber where name = '"
                + name.getText() + "' and second_name = '" + secondName.getText() + "'");
        resultSet.next();
        return  resultSet.getInt(1);
    }

    public void addCall() {
        try {
            Integer userId = findUserId();

            crudRepository.executeQuery("INSERT INTO c##ats.intercity_talks (CLIENT, CITY, CALLTIME) VALUES (" + userId + ", '"
                    + city.getText() + "', '" + when.getText() + "')");
        } catch (SQLException e) {
            result.setText("Не удалось!");
            return;
        }
        result.setText("Успех!");
    }
}
