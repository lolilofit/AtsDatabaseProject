package javafx_application.repository.component_controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import javafx_application.repository.LoginInfo;
import javafx_application.repository.filtering.FilterTreeOperations;
import javafx_application.repository.filtering.SearchTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class SearchController {

    @Autowired private CRUDRepository crudRepository;
    @Autowired private LoginInfo loginInfo;
    @Autowired private FilterTreeOperations tree;

    @FXML
    private TextField search;
    @FXML
    private TextField selectedFilters;
    @FXML
    private ListView variants;
    @FXML
    private ListView result;

    private List<String> savedFilters = new ArrayList<>();

    private void setNewViewList(ListView listView, List<String> list) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                ObservableList<String> langs;
                List<String> notNullList = list.stream().filter(el -> !el.equals("null ")).collect(Collectors.toList());

                if (notNullList.size() == 0)
                    langs = FXCollections.observableArrayList("");
                else
                    langs = FXCollections.observableArrayList(notNullList);
                listView.setItems(langs);
            }});

    }
    public void inputSearchMethod() {
        setNewViewList(variants, tree.findByString(search.getText()));

        variants.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                    @Override
                    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                        if(newValue == null) return;
                        if(newValue.toString().equals(""))
                            return;

                        String addThis = tree.findAndSetTree(
                                newValue.toString()
                        );
                        if(!addThis.equals("")) {
                            savedFilters.add(addThis);
                            selectedFilters.setText(selectedFilters.getText() + ";" + addThis);
                        }
                        setNewViewList(variants, tree.findByString(search.getText()));
                    }
                });
    }

    public void removeFilters() {
        List<String> newfilters = Arrays.stream(selectedFilters.getText().split(";")).filter(str -> {return !str.equals("");}).collect(Collectors.toList());
        savedFilters = newfilters;
        tree.searchInTree(newfilters);
        setNewViewList(variants, tree.findByString(search.getText()));
    }

    public void doSearch() {
        StringBuilder query = new StringBuilder();
        StringBuilder whereClause = new StringBuilder();
        StringBuilder fromClause = new StringBuilder();
        StringBuilder groupBy = new StringBuilder();

        SearchTree  searchTree = tree.getSearchTree();

        query.append("SELECT ");
        fromClause.append("FROM ");
        whereClause.append("WHERE ");
        groupBy.append("GROUP BY ");

        int savedfiltersSize  = savedFilters.size();
        String connectingTablesStatement = "";

        for(int i = 0; i < savedfiltersSize; i++) {
            for(Pair<String, SearchTree> entry : searchTree.getChildrens()) {
                if(savedFilters.get(i).equals(entry.getValue().getFilter().getName())) {
                    searchTree = entry.getValue();
                    connectingTablesStatement = entry.getKey();
                    break;
                }
            }

            List<String> selectingColumns = searchTree.getFilter().getSelectingColums();
            if(selectingColumns != null) {
                for(int j = 0; j < selectingColumns.size(); j++) {
                    query.append(selectingColumns.get(j));
                    if(j == selectingColumns.size() - 1 && i == savedfiltersSize - 1)
                        query.append(" ");
                    else
                        query.append(", ");
                }
            }
                if(connectingTablesStatement.equals("")) {
                        fromClause.append(searchTree.getFilter().getRelatedTable());
                        fromClause.append(" ");
                }
                else {
                    fromClause.append("LEFT JOIN ");
                    fromClause.append(searchTree.getFilter().getRelatedTable());
                    fromClause.append(" ON ").append(connectingTablesStatement);
                    fromClause.append(" ");
                }

                Map<String, String> filters = searchTree.getFilter().getFilterForWhere();
                if(filters != null) {
                    Iterator<String> iterator = filters.keySet().iterator();
                    while(iterator.hasNext()) {
                            String next = iterator.next();
                            if(iterator != filters.keySet().iterator())
                                whereClause.append(" ");
                            else
                                whereClause.append(", ");
                            whereClause.append(next).append("=").append(filters.get(next)).append(" ");
                    }
                }

                if(searchTree.getFilter().getGroupBy().size() != 0) {
                    List<String> group = searchTree.getFilter().getGroupBy();
                    for(int k = 0; k < group.size(); k++) {
                        if(!group.get(k).equals("")) {
                            if (!groupBy.toString().equals("GROUP BY ")) {
                                groupBy.append(", ").append(group.get(k));
                            } else {
                                groupBy.append(group.get(k));
                            }
                        }
                    }
                }
        }
        query.append(fromClause.toString());
        if(!whereClause.toString().equals("WHERE "))
            query.append(whereClause.toString());
        if(!groupBy.toString().equals("GROUP BY "))
            query.append(groupBy.toString());

        try {
            ResultSet resultSet = crudRepository.executeQuery(query.toString());
            List<String> resultStringList = new ArrayList<>();

            while(resultSet.next()) {
                StringBuilder line = new StringBuilder();
                for(int k = 1; k <= savedfiltersSize; k++) {
                    line.append(resultSet.getString(k)).append(" ");
                }
                resultStringList.add(line.toString());
            }
            setNewViewList(result, resultStringList);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
