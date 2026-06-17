package ai.ghosty.paycheck;

import ai.ghosty.paycheck.db.DBInit;
import ai.ghosty.paycheck.logger.LogLevel;
import ai.ghosty.paycheck.logger.Logger;
import ai.ghosty.paycheck.view.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Launcher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        if (!setUpProjStructure()) return;
        DBInit.init();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            Controller controller = loader.getController();
            controller.initialize(stage);
        } catch (Exception e) {
            Logger.log("failed to load view login.fxml " + e.getMessage(), LogLevel.ERROR);
        }

    }

    private boolean setUpProjStructure() {
        Path path = Path.of("db");

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
                Logger.log("setup project structure successfully", LogLevel.INFO);
            } catch (IOException e) {
                Logger.log("failed to setup project structure", LogLevel.ERROR);
                return false;
            }
        }

        return true;
    }

    public static void launcher(String[] args) {launch(args);}
}
