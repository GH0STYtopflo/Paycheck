package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.authentication.QuickLogin;
import ai.ghosty.paycheck.model.Role;
import ai.ghosty.paycheck.model.User;
import ai.ghosty.paycheck.service.EmployeeServices;
import ai.ghosty.paycheck.service.RecordsServices;
import ai.ghosty.paycheck.service.UserServices;
import ai.ghosty.paycheck.util.FieldValidation;
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
    @FXML private Button btnLogin, btnClose, btnMinimize, btnSendCode;
    @FXML private Label lblPhrase, lblMethod, lblWarning;
    @FXML private HBox buttons;

    private String code;
    private boolean passLogin;

    // ATTENTION! arguments order: 1. Own stage !ATTENTION
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];
        passLogin = true;
        setMovable();
        show();
    }


    private void show() {
        ownstage.setResizable(false);
        ownstage.initStyle(StageStyle.TRANSPARENT);
        ownstage.setTitle("Login");
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
        if (FieldValidation.validateFields(new TextField[]{txtfUsername} ,(byte) 0, lblWarning)
                && !UserServices.usernameExists(txtfUsername.getText().trim())) return;
        code = QuickLogin.generateCode();
    }

    private void switchLoginMethod() {
        lblMethod.setText(passLogin ? "Login With Password" : "Quick Login");
        lblPhrase.setText(passLogin ? "OTA Code" : "Password");
        btnSendCode.setDisable(!btnSendCode.isDisabled());


        passLogin = !passLogin;
    }

    private void login() {
        User usr = UserServices.getUserByUsername(txtfUsername.getText().trim());
        if (usr == null) {
            FieldValidation.updateWarningText((byte) 1, "Username Doesn't Exist", lblWarning);
            return;
        }

        if (passLogin) {
            if (isAuthenticated(txtfPassword.getText(), usr.getPasswordHash())) {
                FieldValidation.updateWarningText((byte) 0, "Login Successful", lblWarning);
                openNextView(usr);
            }
            else {
                FieldValidation.updateWarningText((byte) 1, "Invalid username or password", lblWarning);
            }
        }
        else {
            if (isAuthenticated(txtfPassword.getText())) {
                FieldValidation.updateWarningText((byte) 0, "Login Successful", lblWarning);
                openNextView(usr);
            }
            else {
                FieldValidation.updateWarningText((byte) 1, "Invalid OTA Code", lblWarning);
            }
        }
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
}
