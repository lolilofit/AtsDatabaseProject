package javafx_application.repository.component_controllers;

import javafx_application.repository.CRUDRepository;
import javafx_application.repository.DBManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class ChangeCostController {
    private CRUDRepository crudRepository;
    @Autowired
    public void  setCRUDRepository(CRUDRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

}
