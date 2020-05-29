package javafx_application.repository.component_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx_application.repository.CRUDRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtsWorkerController {
    private CRUDRepository crudRepository;

    @FXML
    private ListView variants;
    @FXML
    private Label chosenDebtor;
    @FXML
    private TextArea message;

    private Integer savedPaymentId;

    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    Map<Button, Integer> debtors = new HashMap<>();

    private void setNewViewList(ListView listView, List<Button> list) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                ObservableList<Button> langs;
                if (list.size() == 0)
                    langs = FXCollections.observableArrayList();
                else
                    langs = FXCollections.observableArrayList(list);
                listView.setItems(langs);
            }});

    }

    public void findDebtors() throws SQLException {
        debtors.clear();

        ResultSet resultSet = crudRepository.executeQuery("select name, second_name, payments.id from C##ATS.payments inner join C##ATS.subscriber on payments.id = subscriber.paymentid where debt > 0");

        List<Button> buttonsList = new ArrayList<>();
        while (resultSet.next()) {
            Button button = new Button(resultSet.getString(1) + " " + resultSet.getString(2));
            button.setStyle("-fx-background-color: WHITE");

            button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    savedPaymentId = debtors.get(button);
                    chosenDebtor.setText(button.getText());
                }
            });

            debtors.put(button, resultSet.getInt(3));
            buttonsList.add(button);
        }
        setNewViewList(variants, buttonsList);
    }

    public void sendMessage() throws SQLException {
        //crudRepository.executeQuery("UPDATE C##ATS.payments SET payments.MESSAGE = '" + message.getText() + "' WHERE payments.id = " + savedPaymentId.toString());
       // crudRepository.executeQuery("UPDATE C##ATS.PAYMENTS SET C##ATS.PAYMENTS.MESSAGE = 'upd' WHERE C##ATS.PAYMENTS.id = 27");
        Map<String, String> setThis = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        setThis.put("message", "'" + message.getText() + "'");
        params.put("id", savedPaymentId.toString());
        crudRepository.update("C##ATS.PAYMENTS", setThis, params);
    }
}
