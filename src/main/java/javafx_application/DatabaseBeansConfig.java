package javafx_application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import javafx_application.repository.component_controllers.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
@ComponentScan(basePackageClasses = DBManager.class)
public class DatabaseBeansConfig {

    @Bean(name = "addIntercityCall")
    public View getAddIntercityCall() throws IOException {
        return loadView("addIntercityCall", "/db/fxml/add_intercity_call.fxml");
    }

    @Bean
    public IntercityCallController getIntercityCallController() throws IOException {
        return (IntercityCallController) getAddIntercityCall().getController();
    }

    @Bean(name = "cityPhones")
    public View getCityPhones() throws  IOException {
        return loadView("cityPhones", "/db/fxml/city_phones.fxml");
    }

    @Bean
    public CityPhonesController getCityPhonesController() throws IOException {
        return (CityPhonesController) getCityPhones().getController();
    }

    @Bean(name = "debtInfo")
    public View getDebtInfo() throws  IOException {
        return loadView("debtInfo", "/db/fxml/get_debt.fxml");
    }

    @Bean
    public DebtInfoController getDebtInfoController() throws IOException {
        return (DebtInfoController) getDebtInfo().getController();
    }

    @Bean(name = "abonentsInfo")
    public View getAbonentsInfo() throws  IOException {
        return loadView("abonentsInfo", "/db/fxml/get_abonents.fxml");
    }

    @Bean
    public AbonentsInfoController getAbonentsInfoController() throws IOException {
        return (AbonentsInfoController) getAbonentsInfo().getController();
    }

        @Bean(name = "infoByNumber")
    public View getInfoByNumber() throws  IOException {
        return loadView("infoByNumber", "/db/fxml/get_info_about.fxml");
    }

    @Bean
    public InfoByNumberController getInfoByNumberController() throws IOException {
        return (InfoByNumberController) getInfoByNumber().getController();
    }


    @Bean(name = "сhangeCost")
    public View getChangeCost() throws  IOException {
        return loadView("сhangeCost", "/db/fxml/change_cost.fxml");
    }

    @Bean
    public ChangeCostController getChangeCostController() throws IOException {
        return (ChangeCostController) getChangeCost().getController();
    }

    @Bean(name = "newCityPhone")
    public View getNewCityPhone() throws  IOException {
        return loadView("newCityPhone", "/db/fxml/new_city_phone.fxml");
    }

    @Bean
    public NewCityPhoneController getNewCityPhoneController() throws IOException {
        return (NewCityPhoneController) getNewCityPhone().getController();
    }

    @Bean(name = "atsWorkerInfo")
    public View getAtsWorkerInfo() throws  IOException {
        return loadView("atsWorkerInfo", "/db/fxml/ats_worker_info.fxml");
    }

    @Bean
    public InfoAtsWorkerController getInfoAtsWorkerController() throws IOException {
        return (InfoAtsWorkerController) getAtsWorkerInfo().getController();
    }

    @Bean(name = "atsWorker")
    public View getAtsWorker() throws  IOException {
        return loadView("atsWorker", "/db/fxml/ats_worker.fxml");
    }

    @Bean
    public AtsWorkerController getAtsWorkerController() throws IOException {
        return (AtsWorkerController) getAtsWorker().getController();
    }

    @Bean(name = "searchView")
    public View getSearchView() throws  IOException {
        return loadView("searchView", "/db/fxml/search.fxml");
    }

    @Bean
    public SearchController getSearchController() throws IOException {
        return (SearchController) getSearchView().getController();
    }

    @Bean(name = "loginPage")
    public View getLoginView() throws  IOException {
        return loadView("loginView", "/db/fxml/login.fxml");
    }

    @Bean
    public LoginPageController getMainLoginController() throws IOException {
        return (LoginPageController) getLoginView().getController();
    }

    @Bean(name = "infoView")
    public View getInfoView() throws  IOException {
        return loadView("infoView", "/db/fxml/sub_info.fxml");
    }
    @Bean
    public InfoPageController getInfoPageController() throws IOException {
        return (InfoPageController) getInfoView().getController();
    }
    @Bean
    public LoginInfo getLoginInfo() {
        return new LoginInfo();
    }

    @Bean(name = "usualMainPage")
    public View getUsualMainPageView() throws  IOException {
        return loadView("usualMainPage", "/db/fxml/usual_user_main.fxml");
    }
    @Bean
    public UsualMainController getUsualMainPageController() throws IOException {
        return (UsualMainController) getUsualMainPageView().getController();
    }

    protected View loadView(String beanName, String filepath) throws IOException {

            FXMLLoader loader = new FXMLLoader(getClass().getResource(filepath));
            Parent root = (Parent) loader.load();
            Object controller = loader.getController();

            return new View(beanName, root, controller);
    }

    public class View {
        private Parent view;
        private Object controller;
        private String name;

        public View(String name, Parent view, Object controller) {
            this.name = name;
            this.view = view;
            this.controller = controller;
        }
        public Parent getView() {
            return view;
        }

        public void setView(Parent view) {
            this.view = view;
        }

        public Object getController() {
            return controller;
        }

        public String getName() {
            return name;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }
}
