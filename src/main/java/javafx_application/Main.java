package javafx_application;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(DatabaseBeansConfig.class);
        DatabaseBeansConfig.View salesmanView = (DatabaseBeansConfig.View) context.getBean("loginPage");

        Parent root = salesmanView.getView();
        VBox layout = new VBox(root);
        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
