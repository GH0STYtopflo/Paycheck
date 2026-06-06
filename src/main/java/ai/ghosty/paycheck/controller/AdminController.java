package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.model.Employee;
import ai.ghosty.paycheck.model.Position;
import ai.ghosty.paycheck.service.EmployeeServices;
import ai.ghosty.paycheck.service.RecordsServices;
import ai.ghosty.paycheck.service.SalaryCalculator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class AdminController extends Controller {
    @FXML private TextField txtfName, txtfLast, txtfChildren, txtfWork, txtfOT,
            txtfLoan, txtfPosition, txtfIncome, txtfDeduction;
    @FXML private Label lblWarning;
    @FXML private RadioButton radioMarried, radioNotMarried, radioFemale, radioMale;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox rent;
    @FXML private Button register;
    @FXML private ToggleButton exists;
    @FXML private TextField txtfID;

    private boolean married;
    private String gender;

    // ATTENTION! arguments order: 1. own stage !ATTENTION
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];
        lblWarning.setVisible(false);
        show();
    }

    private void show() {
        ownstage.setResizable(false);
        ownstage.initStyle(StageStyle.TRANSPARENT);
        ownstage.setTitle("Admin Dashboard");
        setMovable();
        ownstage.show();
    }

    @FXML
    private void onRegister() {
        if (!checkFields()) return;
        if (!exists.isSelected()) register();
        else updateEmpStatus();
    }

    @FXML
    private void onExists() {
        if (exists.isSelected()) {
            register.setText("Update");
            txtfID.setDisable(false);
            txtfName.setDisable(true);
            txtfLast.setDisable(true);
            datePicker.setDisable(true);
            radioMale.setDisable(true);
            radioFemale.setDisable(true);
        }
        else {
            register.setText("Register Employee");
            txtfID.setDisable(true);
            txtfName.setDisable(false);
            txtfLast.setDisable(false);
            datePicker.setDisable(false);
            radioMale.setDisable(false);
            radioFemale.setDisable(false);
        }
    }

    private void openConfirm(Employee emp) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/app/ui/confirm.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            ConfirmController controller = loader.getController();
            controller.initialize(stage, emp, lblWarning);
        } catch (IOException e) {
            System.err.println("[error] failed to load confirmation view");
        }
    }

    @FXML
    private void onMarried() {
        radioNotMarried.setSelected(false);
        married = true;
    }
    @FXML
    private void onNotMarried() {
        radioMarried.setSelected(false);
        married = false;
    }
    @FXML
    private void onMale() {
        radioFemale.setSelected(false);
        gender = "m";
    }
    @FXML
    private void onFemale() {
        radioMale.setSelected(false);
        gender = "f";
    }

    @FXML
    private void onClose(){close();}
    @FXML
    private void onMinimized(){minimize();}



    private void register() {
        Employee emp = new Employee(txtfName.getText().trim(), txtfLast.getText().trim(), gender, Integer.parseInt(txtfOT.getText().trim()),
                Integer.parseInt(txtfChildren.getText().trim()), Integer.parseInt(txtfWork.getText().trim()),
                Integer.parseInt(txtfDeduction.getText().trim()), new BigDecimal(txtfLoan.getText().trim()),
                rent.isSelected(), datePicker.getValue(), new Position(txtfPosition.getText(), new BigDecimal(txtfIncome.getText().trim())));

        openConfirm(emp);
    }

    private void updateEmpStatus() {
        Employee emp = EmployeeServices.getById(Integer.parseInt(txtfID.getText().trim()));
        if (emp == null) {
            updateWarning("Employee doesn't exist");
            return;
        }

        emp.setChildren(Integer.parseInt(txtfChildren.getText().trim()));
        emp.setWorkHours(Integer.parseInt(txtfWork.getText().trim()));
        emp.setRent(rent.isSelected());
        emp.setExtraHours(Integer.parseInt(txtfOT.getText().trim()));
        emp.setDeductionHours(Integer.parseInt(txtfDeduction.getText().trim()));
        emp.setLoan(new BigDecimal(txtfLoan.getText().trim()));
        emp.setPosition(new Position(txtfPosition.getText().trim(), new BigDecimal(txtfIncome.getText().trim())));

        SalaryCalculator calc = new SalaryCalculator(emp);
        RecordsServices.create(calc, emp.getId());
        EmployeeServices.updateUserState(emp);

        lblWarning.setText("Created new record successfully");
        lblWarning.setTextFill(Color.GREEN);
        lblWarning.setVisible(true);
    }

    private boolean checkFields() {
        TextField[] fields = new TextField[]{txtfName, txtfLast, txtfChildren, txtfWork, txtfOT,
                txtfLoan, txtfPosition, txtfIncome, txtfDeduction};
        if (exists.isSelected()) {
            fields = Arrays.copyOfRange(fields, 2, 9);
        }

        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                updateWarning("Please fill in all required fields");
                return false;
            }
        }

        if (!checkRadio()) {
            updateWarning("Please select your marital status and gender");
            return false;
        }

        if (!validateNumericFields()) {
            updateWarning("Please enter valid inputs");
            return false;
        }


        return true;
    }

    private boolean validateNumericFields() {
        TextField[] fields = new TextField[]{txtfChildren, txtfWork, txtfOT,
                txtfLoan, txtfIncome, txtfDeduction};

        for (TextField field : fields) {
            try {
                Double.parseDouble(field.getText().trim());
            }
            catch (Exception e) {
                return false;
            }
        }

        return true;
    }

    private boolean checkRadio() {
        if (exists.isSelected()) return radioMarried.isSelected() || radioNotMarried.isSelected();

    return (radioFemale.isSelected() || radioMale.isSelected()) &&
                (radioMarried.isSelected() || radioNotMarried.isSelected());
    }

    private void updateWarning(String warning) {
        lblWarning.setTextFill(Color.RED);
        lblWarning.setText(warning);
        lblWarning.setVisible(true);
    }
}
