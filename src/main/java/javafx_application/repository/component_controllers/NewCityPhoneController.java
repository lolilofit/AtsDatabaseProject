package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;


@Component
@ComponentScan(basePackageClasses = DBManager.class)

public class NewCityPhoneController {
    private CRUDRepository crudRepository;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @FXML
    private TextField street;
    @FXML
    private TextField area;
    @FXML
    private TextField home;
    @FXML
    private TextField flat;
    @FXML
    private TextField index;
    @FXML
    private MenuButton atsId;

    private String savedAts;

    private MenuItem menuItemHandler(MenuItem item) {
        savedAts = item.getText();
        atsId.setText(savedAts);
        return item;
    }

    public void getAllAts() throws SQLException {

        ResultSet resultSet = crudRepository.executeQuery("SELECT C##ATS.ATS.atsid from C##ATS.ATS");

        atsId.getItems().clear();

        while(resultSet.next()) {
            MenuItem item = new MenuItem();
            item.setText(resultSet.getString(1));
            item.setOnAction(event -> menuItemHandler(item));
            atsId.getItems().add(item);
        }
    }

    public void addPhone() throws SQLException {
        crudRepository.executeQuery("insert into C##ATS.NUMBERS (atsid, isfree) values (" + savedAts + ", 'N')");
        ResultSet resultSet = crudRepository.executeQuery("select numberid from C##ATS.NUMBERS where atsid = " + savedAts + "and numbr is null");

        resultSet.next();

        crudRepository.executeQuery("insert into C##ATS.ADDRESSNUMBER (area, street, home, apartment, indx, numb, idtypeplace) VALUES ('" + area.getText() + "', '"
                + street.getText() + "', '"
                + home.getText() + "', '"
                + flat.getText() + "', '"
                + index.getText()  + "', '"
                + resultSet.getString(1) + "', 2)"
        );
    }

}
