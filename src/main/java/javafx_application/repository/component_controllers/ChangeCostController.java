package javafx_application.repository.component_controllers;

import javafx_application.repository.DBManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackageClasses = DBManager.class)
public class ChangeCostController {}
