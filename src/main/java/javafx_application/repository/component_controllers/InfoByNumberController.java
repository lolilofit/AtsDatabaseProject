package javafx_application.repository.component_controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class InfoByNumberController {

    @Autowired
    private CRUDRepository crudRepository;

    @FXML
    private Label name;
    @FXML
    private Label secondName;
    @FXML
    private Label age;
    @FXML
    private Label phoneType;
    @FXML
    private Label area;
    @FXML
    private Label street;
    @FXML
    private Label home;
    @FXML
    private Label flat;
    @FXML
    private TextField numberField;


    public void search() throws SQLException {
        ResultSet resultSet = crudRepository.executeQuery("select c##ats.subscriber.name, c##ats.subscriber.second_name, c##ats.subscriber.age, " +
                "c##ats.ADDRESSNUMBER.area, c##ats.ADDRESSNUMBER.street, c##ats.ADDRESSNUMBER.home, c##ats.ADDRESSNUMBER.apartment, c##ats.telephonetype.name" +
                " FROM (C##ATS.NUMBERS \n" +
                " left join (c##ats.ADDRESSNUMBER INNER JOIN c##ats.SUBSCRIBER ON c##ats.subscriber.adressnumber = c##ats.addressnumber.id) ON c##ats.addressnumber.numb = numbers.numberid " +
                " inner join c##ats.telephonetype on c##ats.telephonetype.teltypeid = c##ats.ADDRESSNUMBER.teltype" +
                " INNER JOIN c##ats.PAYMENTS ON c##ats.payments.id = c##ats.subscriber.paymentid)" +
                " where c##ats.numbers.numbr = '" + numberField.getText() + "'");

        if(resultSet.next()) {
            name.setText(resultSet.getString(1));
            secondName.setText(resultSet.getString(2));
            age.setText(Integer.toString(resultSet.getInt(3)));
            area.setText(resultSet.getString(4));
            street.setText(resultSet.getString(5));
            home.setText(resultSet.getString(6));
            flat.setText(Integer.toString(resultSet.getInt(7)));
            phoneType.setText(resultSet.getString(8));
        }
        else {
            name.setText("No such person");
            secondName.setText("");
            age.setText("");
            area.setText("");
            street.setText("");
            home.setText("");
            flat.setText("");
            phoneType.setText("");
        }
    }
}
