package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.model.Employee;
import ai.ghosty.paycheck.model.Position;
import ai.ghosty.paycheck.model.Record;
import ai.ghosty.paycheck.service.EmployeeServices;
import ai.ghosty.paycheck.service.PositionsServices;
import ai.ghosty.paycheck.service.RecordsServices;
import ai.ghosty.paycheck.service.SalaryCalculator;
import ai.ghosty.paycheck.util.FieldValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminController extends Controller {
    @FXML private TextField txtfName, txtfLast, txtfChildren, txtfWork, txtfOT,
            txtfLoan, txtfDeduction;
    @FXML private Label lblWarning;
    @FXML private RadioButton radioMarried, radioNotMarried, radioFemale, radioMale;
    @FXML private DatePicker datePicker;
    @FXML private CheckBox rent;
    @FXML private Button register;
    @FXML private ToggleButton exists;
    @FXML private TextField txtfID;
    @FXML private ComboBox<String> comboPositions;

    private RadioButton[] genderGroup, marriedGroup;
    private List<Position> positions;


    @FXML
    private void onClose(){close();}
    @FXML
    private void onMinimized(){minimize();}

    // ATTENTION! arguments order: 1. own stage !ATTENTION
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];
        lblWarning.setVisible(false);
        this.positions = PositionsServices.getAllPositions();
        setUpPosCombo();
        setUpRadiobuttons();
        show();
    }

    // ||||||||||||||||||||||||||||||||| 1st tab ||||||||||||||||||||||||||||||||||
    private void show() {
        ownstage.setResizable(false);
        ownstage.initStyle(StageStyle.TRANSPARENT);
        ownstage.setTitle("Admin Dashboard");
        setMovable();
        ownstage.show();
    }

    private void setUpRadiobuttons() {
        setUpRadioGroups();
        radioFemale.setOnAction(e-> {radioMale.setSelected(false);});
        radioMale.setOnAction(e-> {radioFemale.setSelected(false);});
        radioMarried.setOnAction(e-> {radioNotMarried.setSelected(false);});
        radioNotMarried.setOnAction(e-> {radioMarried.setSelected(false);});
    }

    private void setUpRadioGroups() {
         marriedGroup = new RadioButton[]{radioMarried, radioNotMarried};
         genderGroup = new RadioButton[]{radioMale, radioFemale};
    }

    private ObservableList<String> setUpPosCombo() {
        List<String> titles = new ArrayList<>();

        for (Position position : positions) {
            titles.add(position.getTitle() + "-" + position.getId());
        }

        return FXCollections.observableList(titles);
    }

    @FXML
    private void OnExists() {
        txtfID.setDisable(!txtfID.isDisabled());
        txtfName.setDisable(!txtfName.isDisabled());
        txtfLast.setDisable(!txtfLast.isDisabled());

        for (int i = 0; i < 20; i++) {
            marriedGroup[i].setDisable(!marriedGroup[i].isDisabled());
            genderGroup[i].setDisable(!genderGroup[i].isDisabled());
        }

        datePicker.setDisable(!datePicker.isDisable());
        register.setText((exists.isSelected()) ? "New Paycheck" : "Register Employee" );
        exists.setText("Employee doesn't exist");
    }

    @FXML
    private void OnRegister() {
        if (!exists.isSelected()) {
            if (!FieldValidation.validateFields(new TextField[]{txtfName, txtfLast}, (byte) 1, lblWarning)) return;
            if (!FieldValidation.validateFields(new TextField[]{txtfChildren, txtfOT,
                    txtfDeduction , txtfWork, txtfLoan}, (byte) 3, lblWarning)) return;

            if (!checkRadioButtons((byte) 3)) return;
        }
        else {
            if (!FieldValidation.validateFields(new TextField[]{txtfID, txtfChildren, txtfOT,
                    txtfDeduction , txtfWork, txtfLoan}, (byte) 3, lblWarning)) return;

            if (!checkRadioButtons((byte) 2)) return;
        }

        register();
    }

    // op codes: 1 = marital 2 = gender 3 = all
    private boolean checkRadioButtons(byte op) {
        boolean radioSelected = false;
        if (op == 2 || op == 3) {
            for (RadioButton radio : genderGroup) {
                if (radio.isSelected()) radioSelected = true;
            }

            if (!radioSelected) {
                lblWarning.setTextFill(Color.RED);
                lblWarning.setText("Please select a gender");
                lblWarning.setVisible(true);
                return false;
            }
        }

        if (op == 1 ||  op == 3) {
            radioSelected = false;
            for (RadioButton radio : marriedGroup) {
                if (radio.isSelected()) radioSelected = true;
            }

            if (!radioSelected) {
                lblWarning.setTextFill(Color.RED);
                lblWarning.setText("Please select a marital status");
                lblWarning.setVisible(true);
                return false;
            }
        }

        return true;
    }

    private void register() {
        if (!exists.isSelected()) {
            Employee emp = new Employee(txtfName.getText().trim(), txtfLast.getText().trim(),
                    (radioMale.isSelected()) ? "m" : "f", radioMarried.isSelected(),
                    Integer.parseInt(txtfOT.getText().trim()), Integer.parseInt(txtfChildren.getText().trim()),
                    Integer.parseInt(txtfWork.getText().trim()), Integer.parseInt(txtfDeduction.getText().trim()),
                    new BigDecimal(txtfLoan.getText().trim()), rent.isSelected(), datePicker.getValue(),
                    positions.get(comboPositions.getSelectionModel().getSelectedIndex()));

            // TODO Record rec = SalaryCalculator.calculate(emp, ) **handle policy bullsh
        }
        else {
            Employee emp = EmployeeServices.getById(Integer.parseInt(txtfID.getText()));
            emp.setMarried(radioMarried.isSelected());
            emp.setIsRent(rent.isSelected());
            emp.setLoan(new BigDecimal(txtfLoan.getText().trim()));
            emp.setChildren(Integer.parseInt(txtfChildren.getText().trim()));
            emp.setWorkHours(Integer.parseInt(txtfWork.getText().trim()));
            emp.setExtraHours(Integer.parseInt(txtfOT.getText().trim()));
            emp.setDeductionHours(Integer.parseInt(txtfDeduction.getText().trim()));

            // TODO Record rec = SalaryCalculator.calculate(emp, ) **handle policy bullsh
        }

        // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    }




}
