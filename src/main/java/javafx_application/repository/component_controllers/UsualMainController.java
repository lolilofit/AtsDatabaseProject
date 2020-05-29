package javafx_application.repository.component_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import javafx_application.repository.tables.ats.Logins;
import javafx_application.repository.tables.ats.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.xml.transform.Result;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class UsualMainController {

    @Autowired private CRUDRepository crudRepository;
    @Autowired private LoginInfo loginInfo;
    @Autowired private List<DatabaseBeansConfig.View> viewList;
    private TabPane tabPane;
    @FXML
    private CheckBox serviceEnabled;
    @FXML
    private CheckBox intercityEnabled;
    @FXML
    private Label mountCost;
    @FXML
    private Label mountIntercityCost;
    @FXML
    private Label number;

    public void setPane(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public void updateInfo() throws SQLException {
        ResultSet resultSet = crudRepository.executeQuery("select numbr, debt, mounthcost, intercitycallcost, serviceenabled, INTERCITYENABLED" +
                " from c##ats.subscriber inner join c##ats.addressnumber " +
                "on c##ats.subscriber.adressnumber = c##ats.addressnumber.id inner join c##ats.payments on c##ats.payments.id = c##ats.subscriber.paymentid" +
                " left join c##ats.numbers on addressnumber.numb = numbers.numberid" +
                " where c##ats.addressnumber.id = " + loginInfo.getAddrId());
        resultSet.next();
        number.setText(resultSet.getString(1));
        mountCost.setText(resultSet.getString(3));
        mountIntercityCost.setText(resultSet.getString(4));
        String se = resultSet.getString(5);

        if(se.equals("Y"))
            serviceEnabled.fire();

        String ise = resultSet.getString(6);
        if(ise.equals("Y"))
            serviceEnabled.fire();
    }

    public void changeServiceEnabled() throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("ID", loginInfo.getSubId().toString());
        Map<String, String> setThis = new HashMap<>();
        if(serviceEnabled.isSelected())
            setThis.put("SERVICEENABLED", "'Y'");
        else
            setThis.put("SERVICEENABLED", "'N'");
        try {
            crudRepository.update(Subscriber.getRepresentativeString(), setThis, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateInfo();
    }

    public void changeIntercityEnabled() throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put("ID", loginInfo.getSubId().toString());
        Map<String, String> setThis = new HashMap<>();
        if(serviceEnabled.isSelected())
            setThis.put("INTERCITYENABLED", "'Y'");
        else
            setThis.put("INTERCITYENABLED", "'N'");
        try {
            crudRepository.update(Subscriber.getRepresentativeString(), setThis, params);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        updateInfo();
    }

    public void changePersonalInfo() throws NoSuchMethodException {
        JavaFXComponents.openTab(tabPane, "Your Info", "infoView", viewList);
    }

    public void logout() throws SQLException {
        crudRepository.executeQuery("update " + Logins.getRepresentativeString() + " SET isLoggedIn = 'N' WHERE id = " + loginInfo.getLoginId());
        tabPane.getTabs().clear();
        tabPane.getTabs().setAll(loginInfo.getLoginTabs());
    }
}
