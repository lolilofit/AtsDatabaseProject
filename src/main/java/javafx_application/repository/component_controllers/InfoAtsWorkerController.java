package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.LoginInfo;
import javafx_application.repository.tables.ats.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoAtsWorkerController {
    private CRUDRepository crudRepository;
    @Autowired private List<DatabaseBeansConfig.View> viewList;
    @Autowired private LoginInfo loginInfo;
    @FXML
    private TextField name;
    @FXML
    private TextField second_name;
    private TabPane tabPane;

    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public void setPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void submitAtsWorkerInfo() throws SQLException, NoSuchMethodException {
        Map<String, String> setThis = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        setThis.put("NAME", "'" + name.getText() + "'");
        setThis.put("SECOND_NAME", "'" +second_name.getText() + "'");

        params.put("ID", loginInfo.getSubId().toString());

        crudRepository.update(Subscriber.getRepresentativeString(), setThis, params);

        JavaFXComponents.openTab(tabPane, "ATS Worker", "atsWorker", viewList);
        JavaFXComponents.openTab(tabPane, "Search", "searchView", viewList);
        JavaFXComponents.openTab(tabPane, "New City Phone", "newCityPhone", viewList);
        JavaFXComponents.openTab(tabPane, "City Phones", "cityPhones", viewList);
        JavaFXComponents.openTab(tabPane, "Get Debt Info", "debtInfo", viewList);
        JavaFXComponents.openTab(tabPane, "Get Abonents", "abonentsInfo", viewList);
        JavaFXComponents.openTab(tabPane, "Info By Number", "infoByNumber", viewList);
        JavaFXComponents.openTab(tabPane, "Add intercity call", "addIntercityCall", viewList);

        if(loginInfo.getRole().equals("Admin")) {
            JavaFXComponents.openTab(tabPane, "Change Costs", "changeCost", viewList);
        }
    }
}
