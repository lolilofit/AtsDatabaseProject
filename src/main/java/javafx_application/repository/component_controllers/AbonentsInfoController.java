package javafx_application.repository.component_controllers;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class AbonentsInfoController {
    private CRUDRepository crudRepository;
    @Autowired
    private List<DatabaseBeansConfig.View> viewList;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }
    @Autowired private LoginInfo loginInfo;

    @FXML
    private TextField secondName;
    @FXML
    private TextField atsNumber;
    @FXML
    private TextField minAge;
    @FXML
    private TextField maxAge;
    @FXML
    private CheckBox clientType;
    @FXML
    private ListView res;
    @FXML
    private Label resCount;
    @FXML
    private CheckBox parPhone;

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

        int notNullParam = 0;
        if(!secondName.getText().equals("")) {
            notNullParam++;
            whereClause.append("c##ats.subscriber.second_name = '").append(secondName.getText()).append("'");
        }
        if(!atsNumber.getText().equals("")) {
            if(notNullParam > 0)
                whereClause.append(" and ");
            whereClause.append("C##ATS.NUMBERS.atsid = ").append(atsNumber.getText());
            notNullParam++;
        }
        if(!minAge.getText().equals("")) {
            if(notNullParam > 0)
                whereClause.append(" and ");
            whereClause.append("c##ats.subscriber.age >").append(minAge.getText());
            notNullParam++;
        }
        if(!maxAge.getText().equals("")) {
            if(notNullParam > 0)
                whereClause.append(" and ");
            whereClause.append("c##ats.subscriber.age <").append(maxAge.getText());
            notNullParam++;
        }
        if(parPhone.isSelected()) {
            if(notNullParam > 0)
                whereClause.append(" and ");
            whereClause.append("c##ats.teltype.name = parallel");
        }
        String clType;
        if(clientType.isSelected())
             clType = "'beneficiary'";
        else
            clType = "'usual'";
        if(notNullParam > 0)
            whereClause.append(" and ");
        whereClause.append("c##ats.subscribertype.name = ").append(clType);

        ResultSet resultSet = crudRepository.executeQuery("select c##ats.subscriber.name, c##ats.subscriber.second_name" +
                " FROM (C##ATS.NUMBERS \n" +
                " left join (c##ats.ADDRESSNUMBER INNER JOIN c##ats.SUBSCRIBER ON c##ats.subscriber.adressnumber = c##ats.addressnumber.id) ON c##ats.addressnumber.numb = numbers.numberid " +
                " inner join c##ats.telephonetype on c##ats.telephonetype.teltypeid = c##ats.ADDRESSNUMBER.teltype" +
                " INNER JOIN c##ats.PAYMENTS ON c##ats.payments.id = c##ats.subscriber.paymentid " +
                " inner join c##ats.subscribertype on c##ats.subscribertype.id = c##ats.subscriber.SUBTYPEID)" +
                " where " + whereClause.toString());

        List<String> result = new ArrayList<>();
        Integer cnt = 0;

        while(resultSet.next()) {
            result.add(resultSet.getString(1) + " " + resultSet.getString(2));
            cnt++;
        }
        setNewViewList(res, result);
        resCount.setText(cnt.toString());
    }
}
