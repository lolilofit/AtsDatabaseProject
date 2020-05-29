package javafx_application.repository.component_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Configuration
@ComponentScan(basePackageClasses = DBManager.class)
public class CityPhonesController {
    private CRUDRepository crudRepository;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @FXML
    private TextField area;
    @FXML
    private ListView res;

    private void setNewViewList(javafx.scene.control.ListView listView, List<String> list) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                ObservableList<String> langs;
                if (list.size() == 0)
                    langs = FXCollections.observableArrayList("");
                else
                    langs = FXCollections.observableArrayList(list);
                listView.setItems(langs);
            }});

    }


    public void search() throws SQLException {
        ResultSet resultSet = crudRepository.executeQuery("select c##ats.addressnumber.street, c##ats.addressnumber.home, c##ats.addressnumber.apartment " +
                "from c##ats.addressnumber where (idtypeplace = 2 or idtypeplace = 3) and area = '"
                + area.getText() + "'");
        List<String> result = new ArrayList<>();
        while(resultSet.next()) {
            result.add(resultSet.getString(1) + ", " + resultSet.getString(2) + ", " + resultSet.getString(3) );
        }
        setNewViewList(res, result);
    }
}
