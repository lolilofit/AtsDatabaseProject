package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.tables.old.Salesman;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class SubmitArealController implements SubmitController {
    @Autowired private CRUDRepository crudRepository;
    //@Autowired
    //public void  setCRUDRepository(CRUDRepository crudRepository) {
    //    this.crudRepository = crudRepository;
    //}


    @FXML
    private Button submitSalesman;
    @FXML
    private TextField nameField;
    @FXML
    private TextField adressField;
    @FXML
    private TextField telephoneField;

    @FXML
    public void initialize() {

    }
    @PostConstruct
    public void init() {
        System.out.println("");
    }

    @FXML
    public void submit() {
        //System.out.println("Current values:" + nameField.getText() + " " + adressField.getText() + " " + telephoneField.getText());
        //связать названия репозитория и id в программе?
        Map<String, String> params = new HashMap<>();
        params.put("name", nameField.getText());
        params.put("adress", adressField.getText());
        params.put("telephone", telephoneField.getText());
        JavaFXComponents.submitInsertButton(Salesman.getTableParams(), submitSalesman, crudRepository, params, Salesman.getTablename());
    }
}
