package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.authentication.QuickLogin;
import ai.ghosty.paycheck.model.Role;
import ai.ghosty.paycheck.model.User;
import ai.ghosty.paycheck.service.EmployeeServices;
import ai.ghosty.paycheck.service.RecordsServices;
import ai.ghosty.paycheck.service.UserServices;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import static ai.ghosty.paycheck.util.Encryption.stringToSHA256;


public class LoginController extends Controller {
    @FXML private TextField txtfUsername;
    @FXML private PasswordField txtfPassword;
    @FXML private Button btnLogin, btnClose, btnMinimize, btnConfirm;
    @FXML private Label lblPhrase, lblMethod, lblWarning;
    @FXML private HBox buttons, phraseBox;

    private String loginMethod = "pass", code;

    // ATTENTION! arguments order: 1. Own stage !ATTENTION
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];
        animationSetup();
        show();
    }

    private void animationSetup() {
        btnLogin.setOnMouseEntered(event -> {scaleUp(btnLogin);});
        btnLogin.setOnMouseExited(event -> {scaleDown(btnLogin);});
        btnConfirm.setOnMouseEntered(event -> {scaleUp(btnConfirm);});
        btnConfirm.setOnMouseExited(event -> {scaleDown(btnConfirm);});
    }


    private void show() {
        ownstage.setResizable(false);
        ownstage.initStyle(StageStyle.TRANSPARENT);
        ownstage.setTitle("Login");
        setMovable();
        buttons.getChildren().remove(btnConfirm);
        ownstage.show();
    }

    @FXML
    private void onClose() {close();}

    @FXML
    private void onMinimize() {minimize();}

    @FXML
    private void onLogin() {login();}

    @FXML
    private void onSwitchMethod() {
        switchLoginMethod();
    }

    @FXML
    private void onSendCode() {
        if (!checkRequiredFields() && userExists() == null) return;
        code = QuickLogin.generateCode();
    }

    private void switchLoginMethod() {
        if (loginMethod.equals("pass")) {
            loginMethod = "code";
            buttons.getChildren().add(btnConfirm);
            translate(btnConfirm);
            lblPhrase.setText("Code");
            lblMethod.setText("Use Password");
            btnConfirm.setVisible(true);
            HBox.setMargin(lblPhrase, new Insets(0, 32, 0, 0));

        }
        else {
            loginMethod = "pass";
            lblPhrase.setText("Password");
            lblMethod.setText("Quick Login");
            buttons.getChildren().remove(btnConfirm);
            HBox.setMargin(lblPhrase, new Insets(0, 0, 0, 0));
        }
    }

    private void login() {
        User usr = userExists();
        if (usr == null) return;
        if (loginMethod.equals("pass")) {
            if (isAuthenticated(txtfPassword.getText(), usr.getPasswordHash())) {
                lblWarning.setTextFill(Color.GREEN);
                lblWarning.setText("Login successful");
                lblWarning.setVisible(true);
                openNextView(usr);
            }
            else {
                lblWarning.setText("Invalid username or password");
                lblWarning.setTextFill(Color.RED);
                lblWarning.setVisible(true);
            }
        }
        else if (loginMethod.equals("code")) {
            if (isAuthenticated(txtfPassword.getText())) {
                lblWarning.setTextFill(Color.GREEN);
                lblWarning.setText("Login successful");
                lblWarning.setVisible(true);
                openNextView(usr);
            }
            else {
                lblWarning.setText("Please provide the code correctly");
                lblWarning.setTextFill(Color.RED);
                lblWarning.setVisible(true);
            }
        }
    }

    private User userExists() {
        User usr = UserServices.getUserByUsername(txtfUsername.getText());
        if (usr == null) {
            lblWarning.setTextFill(Color.RED);
            lblWarning.setText("User doesn't exist");
            lblWarning.setVisible(true);
        }

        return usr;
    }

    private void openNextView(User usr) {
        switch (usr.getRole()) {
            case Role.ADMIN: {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/admin.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    Controller controller = loader.getController();
                    controller.initialize(stage);
                } catch (IOException e) {
                    System.err.println("failed to load admin view " + e.getMessage());
                    e.printStackTrace();
                }
                break;
            }
            case Role.USER: {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/details.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(loader.load()));
                    Controller controller = loader.getController();
                    controller.initialize(stage, EmployeeServices.getById(usr.getId()), RecordsServices.getById(usr.getId()));
                } catch (IOException e) {
                    System.err.println("failed to load admin view " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isAuthenticated(String inpass, String userpasshash) {
        return stringToSHA256(inpass).equals(userpasshash);
    }
    private boolean isAuthenticated(String code) {
        return code.equals(this.code);
    }


    private boolean checkRequiredFields() {
        if (loginMethod.equals("code")) {
            if (txtfUsername.getText().isEmpty()) {
                lblWarning.setText("Please fill all the fields");
                lblWarning.setVisible(true);
                return false;
            }
            else return true;
        }
        if (txtfUsername.getText().trim().isEmpty() || txtfPassword.getText().trim().isEmpty()) {
            lblWarning.setText("Please fill all the fields");
            lblWarning.setVisible(true);
            return false;
        }

        return true;
    }



    // animation stuff
    private void scaleUp(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), btn);
        st.setToX(1.02);
        st.setToY(1.02);
        st.play();
    }

    private void scaleDown(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), btn);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }

    private void translate(Button btn) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(100), btn);
        tt.setFromX(-50);
        tt.setToX(0);
        tt.play();
    }
}
