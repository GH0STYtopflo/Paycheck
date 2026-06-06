package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.model.Employee;
import ai.ghosty.paycheck.model.User;
import ai.ghosty.paycheck.service.EmployeeServices;
import ai.ghosty.paycheck.service.RecordsServices;
import ai.ghosty.paycheck.service.SalaryCalculator;
import ai.ghosty.paycheck.service.UserServices;
import ai.ghosty.paycheck.util.Encryption;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmController {
    @FXML private TextField txtfUsername, txtfPassword;
    @FXML private Button btnCancel, btnOk;
    @FXML private Label lblUsername, lblPass;


    private Employee employee;
    private Stage ownstage;
    private Label warn;

    public void initialize(Stage ownstage, Employee emp, Label warn) {
        this.employee = emp;
        this.ownstage = ownstage;
        this.warn = warn;
        show();
    }

    private void show() {
        ownstage.setResizable(false);
        ownstage.initStyle(StageStyle.TRANSPARENT);
        ownstage.setTitle("confirm");

        ownstage.show();
    }

    @FXML
    private void onOk() {
        if (!checkFields()) return;

        if (UserServices.usernameExists(txtfUsername.getText().trim())) {
            warn.setText("Username already exists.");
            warn.setTextFill(Color.RED);
            warn.setVisible(true);
                onCancel();
                return;
            }

        EmployeeServices.create(employee);
        UserServices.createUser(new User(employee.getId(), txtfUsername.getText().trim(),
                Encryption.stringToSHA256(txtfPassword.getText().trim()), "user"));
        warn.setText("Employee registered successfully");
        warn.setTextFill(Color.GREEN);
        warn.setVisible(true);


        createPaycheckRecord();
        onCancel();
    }

    private void createPaycheckRecord() {
        SalaryCalculator calculation = new SalaryCalculator(employee);
        RecordsServices.create(calculation, employee.getId());
    }

    private boolean checkFields() {
        if (txtfUsername.getText().isEmpty()) {
            lblUsername.setText("Username cannot be empty");
            lblUsername.setTextFill(Color.RED);
            return false;
        }
        else if (txtfPassword.getText().isEmpty()) {
            lblPass.setText("Password cannot be empty");
            lblPass.setTextFill(Color.RED);
            return false;
        }

        return true;
    }

    @FXML
    private void onCancel() {ownstage.close();}
}