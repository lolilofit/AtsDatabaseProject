package javafx_application.repository.filtering;

import javafx.util.Pair;

import java.util.List;

public class SearchTree {
    private SearchFilter filter;
    //string is connection btw tables
    private List<Pair<String, SearchTree>> childrens;

    public SearchTree(SearchFilter searchFilter, List<Pair<String, SearchTree>> childrens) {
        this.childrens = childrens;
        this.filter = searchFilter;
    }

    public List<Pair<String, SearchTree>> getChildrens() { return childrens; }
    public SearchFilter getFilter() { return filter; }
    public void setChildrens(List<Pair<String, SearchTree>> childrens) { this.childrens = childrens; }
    public void setFilter(SearchFilter filter) { this.filter = filter; }
    public void addChildren(String connection, SearchTree child) {this.childrens.add(new Pair<String, SearchTree>(connection, child));}
}
