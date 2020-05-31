package javafx_application.repository.component_controllers;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import javafx_application.repository.tables.ats.Logins;
import javafx_application.repository.tables.ats.Roles;
import javafx_application.repository.tables.ats.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class LoginPageController {
    private CRUDRepository crudRepository;
    @Autowired private List<DatabaseBeansConfig.View> viewList;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }
    @Autowired private LoginInfo loginInfo;

    @FXML
    private TabPane tabPane;
    @FXML
    private TextField loginIn;
    @FXML
    private PasswordField passwordIn;
    @FXML
    private PasswordField passwordUp;
    @FXML
    private TextField loginUp;
    @FXML
    private MenuButton roleMenu;
    @FXML
    private Label result;
    @FXML
    private Label createResult;


    private String savedRole;

    public void setUsualRole() {
        savedRole = "21";
        roleMenu.setText("usual");
    }

    public void setAtsWorkerRole() {
        savedRole = "22";
        roleMenu.setText("ats worker");
    }

    public void setAdminRole() {
        savedRole = "1";
        roleMenu.setText("admin");
    }

    private void login(String login, String password) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("login", "'" + login + "'");
            params.put("password", "'" + password + "'");
            Map<String, String> setThis = new HashMap<>();
            setThis.put("isLoggedIn", "'Y'");
            Integer id;


            crudRepository.update(Logins.getRepresentativeString(), setThis, params);
            ResultSet resultSet = crudRepository.selectWithWhere(Logins.getRepresentativeString(), params);

            if (resultSet.next()) {
                params.clear();
                params.put("id", Integer.toString(resultSet.getInt("ROLE")));
                id = resultSet.getInt("ID");

                ResultSet findNameResult = crudRepository.selectWithWhere(Roles.getRepresentativeString(), params);
                findNameResult.next();
                String role = findNameResult.getString("name");
                loginInfo.setRole(role);
                loginInfo.setLoginId(id);

                if (role.equals("USUAL")) {
                    resultSet = requestPersonalInfo(id, "ADDRESSNUMBER.ID");
                    resultSet.next();
                    loginInfo.setSubId(Integer.parseInt(resultSet.getString(1)));

                    loginInfo.setRole("USUAL");

                    String adr = resultSet.getString(2);

                    if (adr != null) {
                        loginInfo.setAddrId(Integer.parseInt(adr));

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
                    } else {
                        Optional<DatabaseBeansConfig.View> view = JavaFXComponents.openTab(tabPane, "Your Info", "infoView", viewList);
                        view.ifPresent(value -> ((InfoPageController) (value.getController())).setPane(tabPane));
                    }
                    closeLogin();
                }
                if (role.equals("WORKER") || role.equals("ADMIN")) {
                    resultSet = requestPersonalInfo(id, "SUBSCRIBER.NAME");
                    resultSet.next();
                    loginInfo.setSubId(Integer.parseInt(resultSet.getString(1)));

                    if (role.equals("ADMIN"))
                        loginInfo.setRole("ADMIN");
                    else
                        loginInfo.setRole("WORKER");

                    if (resultSet.getString(2) != null) {
                        JavaFXComponents.openTab(tabPane, "ATS Worker", "atsWorker", viewList);
                        JavaFXComponents.openTab(tabPane, "Search", "searchView", viewList);
                        JavaFXComponents.openTab(tabPane, "New City Phone", "newCityPhone", viewList);
                        JavaFXComponents.openTab(tabPane, "City Phones", "cityPhones", viewList);
                        JavaFXComponents.openTab(tabPane, "Get Debt Info", "debtInfo", viewList);
                        JavaFXComponents.openTab(tabPane, "Get Abonents", "abonentsInfo", viewList);
                        JavaFXComponents.openTab(tabPane, "Info By Number", "infoByNumber", viewList);
                        JavaFXComponents.openTab(tabPane, "Add intercity call", "addIntercityCall", viewList);


                        if (role.equals("ADMIN")) {
                            JavaFXComponents.openTab(tabPane, "Change Costs", "changeCost", viewList);
                        }
                    } else {
                        Optional<DatabaseBeansConfig.View> view = JavaFXComponents.openTab(tabPane, "Your Info", "atsWorkerInfo", viewList);
                        view.ifPresent(value -> ((InfoAtsWorkerController) (value.getController())).setPane(tabPane));
                    }
                    closeLogin();
                }
            }
            else {
                result.setText("Не удалось");
                return;
            }
        } catch (SQLException | NoSuchMethodException e) {
            result.setText("Не удалось!");
            return;
        }
        result.setText("");
    }

    public void submitButtonController() {
        Logins logins = new Logins(loginIn.getText(), passwordIn.getText(), "Y", savedRole);
        try {
            crudRepository.insert(Logins.getTableParams(), Logins.getRepresentativeString(), logins.getParams());
        } catch (SQLException | NoSuchMethodException e) {
            e.printStackTrace();
            createResult.setText("Не удалось");
            return;
        }

        login(loginIn.getText(), passwordIn.getText());
        createResult.setText("");
    }

    private void closeLogin() {
        List<Tab> tabsToRemove = tabPane.getTabs().stream().filter(tab -> tab.getText().equals("Sign in") || tab.getText().equals("Sign up")).collect(Collectors.toList());
        loginInfo.setLoginTabs(tabsToRemove);
        tabPane.getTabs().removeAll(tabsToRemove);
    }

    private ResultSet requestPersonalInfo(Integer id, String secondColumn) throws SQLException {
        Map<String, String> params = new HashMap<>();
        params.put(Logins.getRepresentativeString() + ".id", id.toString());

        List<String> columns = new ArrayList<>();
        columns.add("SUBSCID");
        if(!secondColumn.equals(""))
            columns.add(secondColumn);

        return crudRepository.selectWithWhereColumns(
                "((" + Logins.getRepresentativeString() + " INNER JOIN C##ATS.SUBSCLOGIN ON C##ATS.SUBSCLOGIN.LOGIN = " + Logins.getRepresentativeString() + ".ID )"
                        + "INNER JOIN C##ATS.SUBSCRIBER ON C##ATS.SUBSCRIBER.ID = C##ATS.SUBSCLOGIN.SUBSCID) LEFT JOIN C##ATS.ADDRESSNUMBER ON C##ATS.subscriber.ADRESSNUMBER = C##ATS.ADDRESSNUMBER.ID",
                params, columns);
    }

    public void submitButtonUpController() {
        login(loginUp.getText(), passwordUp.getText());
    }
}
