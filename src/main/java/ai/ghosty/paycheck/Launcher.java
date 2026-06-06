package ai.ghosty.paycheck;

import ai.ghosty.paycheck.controller.Controller;
import ai.ghosty.paycheck.db.DBInit;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        if (!(new File("db/ghst.db").exists())) DBInit.init();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            Controller controller = loader.getController();
            controller.initialize(stage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
