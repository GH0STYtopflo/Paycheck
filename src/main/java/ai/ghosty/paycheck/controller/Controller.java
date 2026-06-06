package ai.ghosty.paycheck.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public abstract class Controller {
    Stage ownstage;
    Controller parentController;
    @FXML HBox barDeco;
    double xOffset, yOffset;

    public abstract void initialize(Object... args);

    public void close() {ownstage.close();}
    public void minimize(){ownstage.setIconified(true);};

    public void setMovable() {
        barDeco.setOnMousePressed(event -> {
            xOffset = event.getX();
            yOffset = event.getY();
        });

        barDeco.setOnMouseDragged(event -> {
            Stage stage = (Stage) barDeco.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
