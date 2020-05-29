package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.tables.old.Salesman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;


@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class UpdateSalemanController {
    @Autowired private CRUDRepository crudRepository;
    private Map<String, Salesman> namesParams = new HashMap<>();

    @FXML
    private MenuButton menuButton;
    @FXML
    private TextField addressTextField;
    @FXML
    private TextField telephoneTextField;
    @FXML
    public void initialize() {

    }
    @PostConstruct
    public void init() {
        System.out.println("");
    }

     MenuItem buttonHandler(MenuItem item) {
        menuButton.setText(item.getText());
        Salesman salesmabById = namesParams.get(item.getId());
        addressTextField.setText(salesmabById.getAddress());
        telephoneTextField.setText(salesmabById.getTelephone());
        return item;
    }

    @FXML
    public void updateSaleman() throws SQLException {
        UnaryOperator<MenuItem> operation = this::buttonHandler;
        ResultSet result = crudRepository.selectAllInColumn(Salesman.getTablename(), "*");
        JavaFXComponents.menuBox(Salesman.class, result, menuButton, namesParams, operation);
        /*
        menuButton.getItems().clear();
        Integer id = 0;
        ResultSet result = null;
        try {
            result = crudRepository.selectAllInColumn("saleman", "*");
            //find table and cast to field type
            while(result.next()) {
                MenuItem item = new MenuItem();
                item.setId(id.toString());
                Salesman salesman = new Salesman(result);
                item.setText(salesman.getName());
                namesParams.put(id.toString(), salesman);
                id++;
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        menuButton.setText(item.getText());
                        Salesman salesmabById = namesParams.get(item.getId());
                        addressTextField.setText(salesmabById.getAddress());
                        telephoneTextField.setText(salesmabById.getTelephone());
                    }
                });
                menuButton.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

         */
    }
}
