package javafx_application.repository.component_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
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
    private ListView<Button> variants;
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

    private void setNewViewList(ListView<Button> listView, List<Button> list) {
        Platform.runLater(() -> {
            ObservableList<Button> langs;
            if (list.isEmpty())
                langs = FXCollections.observableArrayList();
            else
                langs = FXCollections.observableArrayList(list);
            listView.setItems(langs);
        });

    }

    public void findDebtors() throws SQLException {
        debtors.clear();

        ResultSet resultSet = crudRepository.executeQuery("select name, second_name, payments.id from C##ATS.payments inner join C##ATS.subscriber on payments.id = subscriber.paymentid where debt > 0");

        List<Button> buttonsList = new ArrayList<>();
        while (resultSet.next()) {
            Button button = new Button(resultSet.getString(1) + " " + resultSet.getString(2));
            button.setStyle("-fx-background-color: WHITE");

            button.setOnMouseClicked(event -> {
                savedPaymentId = debtors.get(button);
                chosenDebtor.setText(button.getText());
            });

            debtors.put(button, resultSet.getInt(3));
            buttonsList.add(button);
        }
        setNewViewList(variants, buttonsList);
    }

    public void sendMessage() throws SQLException {
        Map<String, String> setThis = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        setThis.put("message", "'" + message.getText() + "'");
        params.put("id", savedPaymentId.toString());
        crudRepository.update("C##ATS.PAYMENTS", setThis, params);
    }
}
