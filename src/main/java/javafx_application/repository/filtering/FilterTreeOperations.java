package javafx_application.repository.filtering;

import javafx.util.Pair;
import javafx_application.repository.tables.ats.ATS;
import javafx_application.repository.tables.ats.Numbers;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class FilterTreeOperations {
    private SearchTree searchTree;
    private SearchTree currentNode;

    public SearchTree getSearchTree() { return searchTree; }

    List<List<String>> path = new ArrayList<>();
    List<Map<String, String>> params = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> tableNames = new ArrayList<>();
    List<List<String>> columns = new ArrayList<>();
    List<String> connectionWithParent = new ArrayList<>();
    List<List<String>> groupBy = new ArrayList<>();

    private void fillTreeNode(String partPath, Map<String, String> partParams, String name, String tableName, String column, String connection, String group) {
        path.add(Arrays.asList(partPath.split(";")));
        params.add(partParams);
        names.add(name);
        tableNames.add(tableName);
        columns.add(Arrays.asList(column.split(";")));
        connectionWithParent.add(connection);
        groupBy.add(Arrays.asList(group.split(";")));
    }

    public FilterTreeOperations() {
        Map<String, String> atsNodeParams = new HashMap<>();
        fillTreeNode("", atsNodeParams, "Поиск по АТС", ATS.getRepresentativeString(),
                ATS.getRepresentativeString() + ".ATSID", "", "ATS.ATSID");

        Map<String, String> atsNodeParams1 = new HashMap<>();
        fillTreeNode("Поиск по АТС", atsNodeParams1, "Посчитать число номеров", Numbers.getRepresentativeString(),
                "COUNT(NUMBERS.NUMBERID)", "ATS.ATSID = NUMBERS.ATSID", "");

        Map<String, String> atsNodeParams2 = new HashMap<>();
        fillTreeNode("", atsNodeParams2, "Поиск по номерам", Numbers.getRepresentativeString(),
                Numbers.getRepresentativeString() + ".NUMBR", "", "");

        Map<String, String> atsNodeParam3 = new HashMap<>();
        atsNodeParam3.put("rownum", "1");
        fillTreeNode("", atsNodeParam3, "АТС с наибольшей суммой долга", "(SELECT sum(payments.debt) as sm, ats.atsid as atd\n" +
                "FROM c##ats.ats left join (C##ATS.NUMBERS left join (c##ats.ADDRESSNUMBER inner JOIN c##ats.SUBSCRIBER ON c##ats.subscriber.adressnumber = c##ats.addressnumber.id)\n" +
                "ON addressnumber.numb = numbers.numberid INNER JOIN c##ats.PAYMENTS ON c##ats.payments.id = c##ats.subscriber.paymentid) on c##ats.ats.atsid = c##ats.numbers.atsid\n" +
                "group by ats.atsid) p ",
                "p.atd", "", "p.sm, p.atd");

        fillTreeNode("", atsNodeParam3, "АТС с самым большим числом должников", "(SELECT count(payments.debt) as sm, ats.atsid as atd\n" +
                        "FROM c##ats.ats left join (C##ATS.NUMBERS left join (c##ats.ADDRESSNUMBER inner JOIN c##ats.SUBSCRIBER ON c##ats.subscriber.adressnumber = c##ats.addressnumber.id)\n" +
                        "ON addressnumber.numb = numbers.numberid INNER JOIN c##ats.PAYMENTS ON c##ats.payments.id = c##ats.subscriber.paymentid) on c##ats.ats.atsid = c##ats.numbers.atsid\n" +
                        "where payments.debt > 0 \n" +
                        "group by ats.atsid) p ",
                "p.atd", "", "p.sm, p.atd");
        fillTreeNode("", new HashMap<>(), "Проц. соотношение льготников", "(select count(c##ats.subscriber.id) as lgot from c##ats.subscriber where c##ats.subscriber.subtypeid = 2) p, " +
                "(select count(c##ats.subscriber.id) as total from c##ats.subscriber) k", "p.lgot/k.total", "", "");
        fillTreeNode("", new HashMap<>(), "Город с наиб.числом междуг.разговоров", "((SELECT count(*), intercity_talks.city as c\n" +
                "FROM c##ats.intercity_talks\n" +
                "group by intercity_talks.city) t)", "t.c", "", "");
    }

    @PostConstruct
    public void initSearchTree() {

        List<Pair<String, SearchTree>> rootChildrens = new ArrayList<>();
        SearchTree rootNode = new SearchTree(new SearchFilter("", null, null, null, null), rootChildrens);
        searchTree = rootNode;

        for(int i = 0; i < path.size(); i++) {
            List<String> curPath = path.get(i);
            SearchTree cur = searchTree;

            for(int j = 0; j < curPath.size(); j++) {
                assert cur != null;
                for(Pair<String, SearchTree> child : cur.getChildrens()) {
                    if(child.getValue().getFilter().getName().equals(curPath.get(j))) {
                        cur = child.getValue();
                        break;
                    }
                }
                SearchTree atsNode = new SearchTree(new SearchFilter(names.get(i), tableNames.get(i), params.get(i), columns.get(i), groupBy.get(i)), new ArrayList<>());
                cur.addChildren(connectionWithParent.get(i), atsNode);
            }
        }
        currentNode = searchTree;
    }

    public List<String> findByString(String query) {
        List<String> result = new ArrayList<>();
        if(query.equals("")) {
            if(currentNode != null) {
                if(currentNode.getChildrens() != null) {
                    for(Pair<String, SearchTree> entry : currentNode.getChildrens()) {
                        result.add(entry.getValue().getFilter().getName());
                    }
                }
            }
        }
        else {
            if(currentNode != null) {
                if(currentNode.getChildrens() != null) {
                    for(Pair<String, SearchTree> entry : currentNode.getChildrens()) {
                        if (entry.getValue().getFilter().getName().contains(query))
                            result.add(entry.getValue().getFilter().getName());
                    }
                }
            }
        }
        return result;
    }

    public String findAndSetTree(String name) {
        if(currentNode == null)
            return "";
        if(currentNode.getChildrens() == null)
            return "";
        for(Pair<String, SearchTree> entry : currentNode.getChildrens()) {
            if(entry.getValue().getFilter().getName().equals(name)) {
                currentNode = entry.getValue();
                return name;
            }
        }
        return "";
    }

    public void searchInTree(List<String> filters) {
        SearchTree cur = searchTree;
        if(filters.isEmpty()) {
            currentNode = searchTree;
            return;
        }

        for(int i = 0; i < filters.size(); i++) {
            if(cur == null) {
                currentNode = null;
                return;
            }
            if(cur.getChildrens() == null) {
                if(i == (filters.size() - 1))
                    currentNode = cur;
                else currentNode = null;
                return;
            }
            if(currentNode != null) {
                for (Pair<String, SearchTree> entry : currentNode.getChildrens()) {
                    if (entry.getValue().getFilter().getName().equals(filters.get(i))) {
                        continue;
                    }
                }
            }
            currentNode = null;
        }
    }
}
