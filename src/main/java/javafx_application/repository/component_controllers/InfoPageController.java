package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import javafx_application.repository.tables.ats.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;


@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class InfoPageController {
    private CRUDRepository crudRepository;
    @Autowired
    private List<DatabaseBeansConfig.View> viewList;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @FXML
    private TextField areal;
    @FXML
    private TextField street;
    @FXML
    private TextField home;
    @FXML
    private TextField flat;
    @FXML
    private TextField index;
    @FXML
    private CheckBox lgot;
    @FXML
    private MenuButton telMenu;
    @FXML
    private TextField name;
    @FXML
    private TextField second_name;

    private TabPane tabPane;

    @Autowired private LoginInfo loginInfo;

    private Map<String, Teltype> namesParams = new HashMap<>();

    private int telType;


    public void setPane(TabPane pane) {
        //namesParams = Addressnumber.getTableParams();
        this.tabPane = pane; }


    MenuItem buttonHandler(MenuItem item) {
        telMenu.setText(item.getText());
        Teltype el = namesParams.get(item.getId());
        telType = el.getTeltypeid();
        return item;
    }

    public void submitName() throws SQLException, NoSuchMethodException {
        Map<String, String> setThis = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        setThis.put("NAME", "'" + name.getText() + "'");
        setThis.put("SECOND_NAME", "'" +second_name.getText() + "'");

        params.put("ID", loginInfo.getSubId().toString());

        crudRepository.update(Subscriber.getRepresentativeString(), setThis, params);
    }

    public void menuBoxClick() {
        UnaryOperator<MenuItem> operation = this::buttonHandler;
        ResultSet result;
        try {
            result = crudRepository.selectAllInColumn(Teltype.getRepresentativeString(), "*");
            JavaFXComponents.menuBox(Teltype.class, result, telMenu, namesParams, operation);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void submitButton() {
        Map<String, String> params = new HashMap<>();
        params.put(Logins.getRepresentativeString() + ".id", loginInfo.getLoginId().toString());
        try {
            Addressnumber addressnumber = new Addressnumber(telType, 1, index.getText(), street.getText(), flat.getText(), home.getText(), areal.getText());
            crudRepository.insert(Addressnumber.getTableParams(), Addressnumber.getRepresentativeString(), addressnumber.getParams());

            params.clear();
            params.put("INDX", index.getText());
            params.put("STREET", "'" + street.getText() + "'");
            params.put("APARTMENT", flat.getText());
            params.put("HOME", "'" + home.getText() + "'");
            ResultSet resultSet = crudRepository.selectWithWhere(Addressnumber.getRepresentativeString(), params);
            resultSet.next();
            Integer adrnum = resultSet.getInt("ID");
            loginInfo.setAddrId(adrnum);

            params.clear();
            params.put("LOGIN", "'" + loginInfo.getLoginId().toString() + "'");
            resultSet = crudRepository.selectWithWhere(SubscLogin.getRepresentativeString(), params);
            resultSet.next();
            Integer subid = resultSet.getInt("SUBSCID");
            loginInfo.setSubId(subid);
            params.clear();
            params.put("ID", "'" +  subid.toString() + "'");
            Map<String, String> setThis = new HashMap<>();
            setThis.put("ADRESSNUMBER", adrnum.toString());

            if(lgot.isSelected())
                setThis.put("SUBTYPEID", "2");
            else
                setThis.put("SUBTYPEID", "1");
           
            crudRepository.update(Subscriber.getRepresentativeString(), setThis, params);

            submitName();

            Optional<DatabaseBeansConfig.View> view = JavaFXComponents.openTab(tabPane, "Main Page", "usualMainPage", viewList);
            view.ifPresent(value -> {
                UsualMainController usualMainController = (UsualMainController) (value.getController());
                try {
                    usualMainController.updateInfo();
                    usualMainController.setPane(tabPane);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            JavaFXComponents.openTab(tabPane, "City Phones", "cityPhones", viewList);
            JavaFXComponents.openTab(tabPane, "Get Debt Info", "debtInfo", viewList);
            JavaFXComponents.openTab(tabPane, "Get Abonents", "abonentsInfo", viewList);
            JavaFXComponents.openTab(tabPane, "Info By Number", "infoByNumber", viewList);
            JavaFXComponents.openTab(tabPane, "Search", "searchView", viewList);

        } catch (SQLException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
