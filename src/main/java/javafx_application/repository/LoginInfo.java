package javafx_application.repository;

import javafx.scene.control.Tab;

import java.util.List;

public class LoginInfo {
    private String role;
    private Integer loginId;
    private Integer subId;
    private Integer addrId;
    private List<Tab> loginTabs;

    public List<Tab> getLoginTabs() {
        return loginTabs;
    }

    public void setLoginTabs(List<Tab> loginTabs) {
        this.loginTabs = loginTabs;
    }

    public Integer getAddrId() {
        return addrId;
    }

    public Integer getSubId() {
        return subId;
    }

    public void setAddrId(Integer addrId) {
        this.addrId = addrId;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public Integer getLoginId() {
        return loginId;
    }

    public void setLoginId(Integer loginId) {
        this.loginId = loginId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
