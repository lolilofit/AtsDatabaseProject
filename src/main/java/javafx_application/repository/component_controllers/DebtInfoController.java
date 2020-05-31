package javafx_application.repository.component_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.xml.soap.Text;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class DebtInfoController {
    private CRUDRepository crudRepository;
    @Autowired
    private List<DatabaseBeansConfig.View> viewList;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }
    @Autowired private LoginInfo loginInfo;

    @FXML
    private TextField atsNumber;
    @FXML
    private TextField area;
    @FXML
    private CheckBox debtWeek;
    @FXML
    private TextField minDebt;
    @FXML
    private TextField maxDebt;
    @FXML
    private ListView res;
    @FXML
    private MenuButton menu;

    private String selectingDebt;

    public void selectADebt() {
        selectingDebt = "c##ats.payments.debt";
        menu.setText("Абон. плата");
    }
    public void selectIDebt() {
        selectingDebt = "c##ats.payments.INTERCITYDEBT";
        menu.setText("Межгород");
    }

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
        StringBuilder whereClause = new StringBuilder();

        if(!minDebt.getText().equals("")) {
            whereClause.append(selectingDebt + " > " + minDebt.getText() );
        }
        else {
            whereClause.append(selectingDebt + " > " + "0");
        }

        if(!maxDebt.getText().equals("")) {
            whereClause.append(" and " + selectingDebt + " < " + maxDebt.getText() );
        }

        if(!atsNumber.getText().equals("")) {
            whereClause.append(" and ");
            whereClause.append("c##ats.numbers.atsid = " + atsNumber.getText());
        }
        if(!area.getText().equals("")) {
            whereClause.append(" and ");
            whereClause.append("c##ats.ADDRESSNUMBER.area = '" + area.getText()+ "'");
        }
        if(debtWeek.isSelected()) {
            whereClause.append(" and ");
            whereClause.append("sysdate - 37 > c##ats.LASTPAYMENT");
        }

        ResultSet resultSet = crudRepository.executeQuery("SELECT c##ats.subscriber.name, c##ats.subscriber.second_name FROM (C##ATS.NUMBERS " +
                "left join (c##ats.ADDRESSNUMBER INNER JOIN c##ats.SUBSCRIBER ON c##ats.subscriber.adressnumber = c##ats.addressnumber.id) ON addressnumber.numb = numbers.numberid " +
                "INNER JOIN c##ats.PAYMENTS ON c##ats.payments.id = c##ats.subscriber.paymentid)" +
                " where " + whereClause.toString());

        List<String> result = new ArrayList<>();
        while(resultSet.next())  {
            result.add(resultSet.getString(1) + " " + resultSet.getString(2));
        }
        setNewViewList(res, result);
    }
}
