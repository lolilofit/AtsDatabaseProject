package javafx_application.repository.component_controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx_application.DatabaseBeansConfig;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.tables.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class JavaFXComponents {
    public static void submitInsertButton(Map<String, Class> tableParams, Button submitButton, CRUDRepository crudRepository, Map<String, String> params, String tablename) {
        try {
            crudRepository.insert(tableParams, tablename, params);
        }
        catch (SQLException | NoSuchMethodException e) {
            e.printStackTrace();
            submitButton.setText("Failed, try again");
        }
        submitButton.setText("Thanks!");
    }

    public static Optional<DatabaseBeansConfig.View> openTab(TabPane tabPane, String tabText, String beanName, List<DatabaseBeansConfig.View> viewList) throws NoSuchMethodException{
        Tab viewTab = new Tab();
        viewTab.setText(tabText);
        Optional<DatabaseBeansConfig.View> viewCandidate = viewList.stream().filter(val -> val.getName().equals(beanName)).findFirst();
        if(!viewCandidate.isPresent())
            throw new NoSuchMethodException();

        viewTab.setContent(viewCandidate.get().getView());
        tabPane.getTabs().add(viewTab);
        tabPane.getSelectionModel().selectLast();
        return viewCandidate;
    }

    public static void submitUpdateButton(Map<String, Class> tableParams, Button submitButton, CRUDRepository crudRepository, Map<String, String> params, String tablename) {

        /*
        try {
            crudRepository.update(tableParams, tablename, params);
        }
        catch (SQLException e) {
            e.printStackTrace();
            submitButton.setText("Failed, try again");
        }
        submitButton.setText("Thanks!");

         */
    }

    public static <T> void menuBox(Class<T> classT,
                                   ResultSet result,
                                   MenuButton menuButton ,
                                   //CRUDRepository crudRepository,
                                   Map<String, T> namesParams,
                                   UnaryOperator<MenuItem> operator) {
        menuButton.getItems().clear();
        int id = 0;

        try {
            Constructor<T> constructor = classT.getConstructor(ResultSet.class);
            //find table and cast to field type
            while(result.next()) {
                MenuItem item = new MenuItem();
                item.setId(Integer.toString(id));
                T tableObject = constructor.newInstance(result);
                //????????????
                if(tableObject instanceof Table)
                    item.setText(((Table)tableObject).getMainString());
                namesParams.put(Integer.toString(id), tableObject);
                id++;
                item.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            operator.apply(item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                menuButton.getItems().add(item);
            }
        } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
