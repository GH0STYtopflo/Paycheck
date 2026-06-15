package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.model.*;
import ai.ghosty.paycheck.model.Record;
import ai.ghosty.paycheck.service.*;
import ai.ghosty.paycheck.util.Encryption;
import ai.ghosty.paycheck.util.PolicyConfig;
import ai.ghosty.paycheck.validation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminController extends Controller {
    @FXML
    private void onClose(){close();}
    @FXML
    private void onMinimized(){minimize();}

    // ATTENTION! arguments order: 1. own stage !ATTENTION
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];
        lblWarning.setVisible(false);
        setUpPosCombo();
        setUpRadiobuttons();
        populatePositionsTable();
        populateEmpTable();
        show();
    }

    // ||||||||||||||||||||||||||||||||| 1st tab ||||||||||||||||||||||||||||||||||
    @FXML private TextField txtfName, txtfLast, txtfChildren, txtfWork, txtfOT,
            txtfLoan, txtfDeduction, txtfUsername;
    @FXML private PasswordField txtfPassword;
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

    private void setUpPosCombo() {
        List<String> titles = new ArrayList<>();
        this.positions = PositionsServices.getAllPositions();

        for (Position position : positions) {
            titles.add(position.getTitle() + "-" + position.getId());
        }
        comboPositions.setItems(FXCollections.observableList(titles));
    }

    @FXML
    private void OnExists() {
        txtfID.setDisable(!txtfID.isDisabled());
        txtfName.setDisable(!txtfName.isDisabled());
        txtfLast.setDisable(!txtfLast.isDisabled());
        txtfUsername.setDisable(!txtfUsername.isDisabled());
        txtfPassword.setDisable(!txtfPassword.isDisabled());

        for (int i = 0; i < 2; i++) {
            marriedGroup[i].setDisable(!marriedGroup[i].isDisabled());
            genderGroup[i].setDisable(!genderGroup[i].isDisabled());
        }

        datePicker.setDisable(!datePicker.isDisable());
        register.setText((exists.isSelected()) ? "New Paycheck" : "Register Employee" );
        exists.setText("Employee doesn't exist");
    }

    @FXML
    private void OnUpdate() {
        if (!validateFields()) return;
        register();
    }
    
    private boolean validateFields() {
        if (comboPositions.getSelectionModel().getSelectedIndex() == -1) {
            WarningUpdater.updateWarningText((byte) 1, "Please select a position", lblWarning);
            return false;
        }

        ValidationResult res;
        if (!exists.isSelected()) {
            if (!(res = Validator.validate(new TextField[]{txtfName, txtfLast, txtfUsername, txtfPassword},
                    Rule.NOT_BLANK)).isValid()) {
                WarningUpdater.updateWarningText((byte) 1, res.getError(), lblWarning);
                return false;
            }

            return checkRadioButtons((byte) 2);
        }

        if (!(res = Validator.validate(new TextField[]{txtfChildren, txtfOT,
                        txtfDeduction , txtfWork, txtfLoan},
                Rule.NOT_BLANK)).isValid()) {
            WarningUpdater.updateWarningText((byte) 1, res.getError(), lblWarning);
            return false;
        }

        return checkRadioButtons((byte) 3);
    }

    // op codes: 1 = marital 2 = gender 3 = all
    private boolean checkRadioButtons(byte op) {
        boolean radioSelected = false;
        if (op == 2 || op == 3) {
            for (RadioButton radio : genderGroup) {
                if (radio.isSelected()) radioSelected = true;
            }

            if (!radioSelected) {
                WarningUpdater.updateWarningText((byte) 1, "Please select a gender", lblWarning);
                return false;
            }
        }

        if (op == 1 ||  op == 3) {
            radioSelected = false;
            for (RadioButton radio : marriedGroup) {
                if (radio.isSelected()) radioSelected = true;
            }

            if (!radioSelected) {
                WarningUpdater.updateWarningText((byte) 1, "Please select a marital status", lblWarning);
                return false;
            }
        }

        return true;
    }

    private void register() {
        Record rec;
        if (!exists.isSelected()) {
            if (UserServices.usernameExists(txtfUsername.getText())) return;

            Employee emp = new Employee(txtfName.getText().trim(), txtfLast.getText().trim(),
                    (radioMale.isSelected()) ? "m" : "f", radioMarried.isSelected(),
                    Integer.parseInt(txtfOT.getText().trim()), Integer.parseInt(txtfChildren.getText().trim()),
                    Integer.parseInt(txtfWork.getText().trim()), Integer.parseInt(txtfDeduction.getText().trim()),
                    new BigDecimal(txtfLoan.getText().trim()), rent.isSelected(), datePicker.getValue(),
                    positions.get(comboPositions.getSelectionModel().getSelectedIndex()));
            User user = new User(emp.getId(), txtfUsername.getText().trim(), Encryption.stringToSHA256(txtfPassword.getText().trim()),
                    Role.USER);

            rec = SalaryCalculator.calculate(emp, PolicyConfig.readPolicyConf());
            EmployeeServices.create(emp);
            UserServices.createUser(user);
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

            rec = SalaryCalculator.calculate(emp, PolicyConfig.readPolicyConf());
            EmployeeServices.updateEmpState(emp);
        }

        RecordsServices.create(rec);
        WarningUpdater.updateWarningText((byte) 0, "Paycheck record created successfully", lblWarning);
    }

    // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    // ||||||||||||||||||||||||||||||||| 2nd tab ||||||||||||||||||||||||||||||||||
    private @FXML TextField txtfNewPosTitle, txtfNewPosIncome;
    private @FXML Label lblWarning1;
    private @FXML Button btnCreatePosition;
    private @FXML TableView<Position> tablePositions;
    private @FXML TableColumn<Position, String> colPosTitle;
    private @FXML TableColumn<Position, Integer> colPosId;
    private @FXML TableColumn<Position, BigDecimal> colPosIncome;


    private void populatePositionsTable() {
        colPosId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPosTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colPosIncome.setCellValueFactory(new PropertyValueFactory<>("salaryPerHour"));

        tablePositions.setItems(FXCollections.observableList(PositionsServices.getAllPositions()));
    }

    @FXML
    private void onCreatePositon() {
        ValidationResult res;

        if (!(res = Validator.validate(txtfNewPosTitle, Rule.NOT_BLANK)).isValid()) {
            WarningUpdater.updateWarningText((byte) 1, "Please enter a title", lblWarning);
        }

        if (!(res = Validator.validate(txtfNewPosIncome, Rule.NOT_BLANK, Rule.NUMERIC, Rule.POSITIVE)).isValid()) {
            WarningUpdater.updateWarningText((byte) 1, "invalid Income value", lblWarning);
        }
        createPosition();
        populatePositionsTable();
        setUpPosCombo();
        WarningUpdater.updateWarningText((byte) 0, "Position created successfully", lblWarning1);
    }

    private void createPosition() {
        Position position = new Position(txtfNewPosTitle.getText().trim(),
                new BigDecimal(txtfNewPosIncome.getText().trim()));

        PositionsServices.createPosition(position);
    }

    // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    // ||||||||||||||||||||||||||||||||| 3rd tab ||||||||||||||||||||||||||||||||||
    @FXML private TextField txtfIncomeTax, txtfSocialSecurity, txtfHealthcare, txtfInsurance,
            txtfOvertimeMult, txtfMaxLoanRate, txtfAccomodation, txtfMealAllowance, txtfRecreation,
            txtfChildAllowance, txtfWomenExtra;
    @FXML private Button btnResetPolicies, btnSavePolicies;

    @FXML private Label lblPolicyWarning;

    @FXML
    private void onSave(){
        if (!validatePolicyFields()) return;
        savePolicy();
    }

    @FXML
    private void onReset() {
        PolicyConfig.updatePolicyConf(new Policy());
        WarningUpdater.updateWarningText((byte) 0, "Policy updated successfully", lblPolicyWarning);
    }

    private boolean validatePolicyFields() {
        ValidationResult res;

        if (!(res = Validator.validate(new TextField[]{txtfIncomeTax,
                txtfSocialSecurity, txtfHealthcare, txtfInsurance,
                txtfOvertimeMult, txtfMaxLoanRate, txtfAccomodation,
                txtfMealAllowance, txtfRecreation, txtfChildAllowance, txtfWomenExtra},
                Rule.NUMERIC, Rule.POSITIVE)).isValid()) {
            WarningUpdater.updateWarningText((byte) 1, res.getError(), lblWarning);

            return false;
        }

        return true;
    }

    private void savePolicy() {
        Policy policy = new Policy(
                txtfIncomeTax.getText().isBlank() ? null : new BigDecimal(txtfIncomeTax.getText()),
                txtfSocialSecurity.getText().isBlank() ? null : new BigDecimal(txtfSocialSecurity.getText()),
                txtfHealthcare.getText().isBlank() ? null : new BigDecimal(txtfHealthcare.getText()),
                txtfInsurance.getText().isBlank() ? null : new BigDecimal(txtfInsurance.getText()),
                txtfOvertimeMult.getText().isBlank() ? null : new BigDecimal(txtfOvertimeMult.getText()),
                txtfMaxLoanRate.getText().isBlank() ? null : new BigDecimal(txtfMaxLoanRate.getText()),
                txtfAccomodation.getText().isBlank() ? null : new BigDecimal(txtfAccomodation.getText()),
                txtfMealAllowance.getText().isBlank() ? null : new BigDecimal(txtfMealAllowance.getText()),
                txtfRecreation.getText().isBlank() ? null : new BigDecimal(txtfRecreation.getText()),
                txtfChildAllowance.getText().isBlank() ? null : new BigDecimal(txtfChildAllowance.getText()),
                txtfWomenExtra.getText().isBlank() ? null : new BigDecimal(txtfWomenExtra.getText())
        );

        WarningUpdater.updateWarningText((byte) 0, "Policy saved successfully", lblPolicyWarning);
        PolicyConfig.updatePolicyConf(policy);
    }


    // ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    // ||||||||||||||||||||||||||||||||| 4rd tab ||||||||||||||||||||||||||||||||||
    @FXML private TableView tableEmployees;
    @FXML private TableColumn<Employee, Integer> colEmpId;
    @FXML private TableColumn<Employee, String> colEmpName;
    @FXML private TableColumn<Employee, String> colEmpLastName;
    @FXML private TableColumn<Employee, String> colEmpPosition;

    private void populateEmpTable() {
        ObservableList<Employee> empList = FXCollections.observableArrayList(EmployeeServices.getAllEmployees());

        colEmpId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmpName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmpLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colEmpPosition.setCellValueFactory(cellData -> {
            Employee employee = cellData.getValue();
            return new SimpleStringProperty(employee.getPosition().getTitle());
        });

        tableEmployees.setItems(empList);
    }

    @FXML
    private void onDelete() {deleteEmp();}

    private void deleteEmp() {
        EmployeeServices.deleteEmployee(((Employee) tableEmployees.getSelectionModel().getSelectedItem()).getId());
        populateEmpTable();
    }


}
