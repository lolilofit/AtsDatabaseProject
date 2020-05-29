package javafx_application.repository.filtering;

import java.util.List;
import java.util.Map;

public class SearchFilter {
    private String name;
    private String relatedTable;
    private List<String> selectingColums;
    private Map<String, String> filterForWhere;
    private List<String> groupBy;

    public SearchFilter(String name, String relatedTable, Map<String, String> filter, List<String> selectingColums, List<String> groupBy) {
        this.name = name;
        this.filterForWhere = filter;
        this.relatedTable = relatedTable;
        this.selectingColums = selectingColums;
        this.groupBy = groupBy;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Map<String, String> getFilterForWhere() { return filterForWhere; }
    public String getRelatedTable() { return relatedTable; }
    public void setFilterForWhere(Map<String, String> filterForWhere) { this.filterForWhere = filterForWhere; }
    public void setRelatedTable(String relatedTable) { this.relatedTable = relatedTable; }
    public List<String> getSelectingColums() { return selectingColums; }

    public List<String> getGroupBy() {
        return groupBy;
    }
}
