package ai.ghosty.paycheck.controller;

import ai.ghosty.paycheck.model.Employee;
import ai.ghosty.paycheck.service.SalaryCalculator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DetailsController extends Controller {
    @FXML private Label lblTotalPay;

    // employee info
    @FXML private Label lblId, lblFirstName, lblLastName, lblGender, lblHireDate, lblPositionTitle;

    // period info (idk really i hate clean code)
    @FXML private ComboBox<String> comboPeriod;

    // payments
    @FXML private Label lblBaseIncome, lblOt, lblAccommodationAllowance, lblMealAllowance,
            lblRecreationAllowance, lblChildAllowance, lblWomenExtra, lblGrossIncome;

    // deductions
    @FXML private Label lblTax, lblSocialSecurity, lblHealthcare, lblInsurance,
            lblDeductionFromHours, lblLoanRepay, lblTotalDeductions;

    private ArrayList<SalaryCalculator> list;

    // ATTENTION! argument pattern:
    // initialize(stage, Employee, SalaryCalculator)
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];

        Employee emp = (Employee) args[1];
        this.list = (ArrayList<SalaryCalculator>) args[2];
        comboPeriod.setItems(periodDate());
        comboPeriod.setValue(comboPeriod.getItems().getFirst());
        comboPeriod.setOnAction((event) -> {
            String selected = comboPeriod.getValue();
            populateSalary(list.get(comboPeriod.getItems().indexOf(selected)));
        });
        show();
        if (emp != null) populateEmployee(emp);
        if (!list.isEmpty() && list.getFirst() != null) populateSalary(list.getFirst());
    }

    @FXML
    private void onClose() { close(); }

    @FXML
    private void onMinimize() { minimize(); }

    private void show() {
        ownstage.setResizable(false);
        ownstage.initStyle(StageStyle.TRANSPARENT);
        ownstage.setTitle("Details");
        setMovable();
        ownstage.show();
    }

    private void populateEmployee(Employee emp) {
        lblId.setText(String.valueOf(emp.getId()));
        lblFirstName.setText(emp.getName());
        lblLastName.setText(emp.getLastName());
        lblGender.setText(emp.getGender());
        lblHireDate.setText(emp.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        lblPositionTitle.setText(emp.getPosition().getPosName());
    }

    private void populateSalary(SalaryCalculator sc) {
        lblBaseIncome.setText(formatCurrency(sc.getBaseIncome()));
        lblOt.setText(formatCurrency(sc.getOt()));
        lblAccommodationAllowance.setText(formatCurrency(sc.getAccommodationAllowance()));
        lblMealAllowance.setText(formatCurrency(sc.getMealAllowance()));
        lblRecreationAllowance.setText(formatCurrency(sc.getRecreationAllowance()));
        lblChildAllowance.setText(formatCurrency(sc.getChildAllowance()));
        lblWomenExtra.setText(formatCurrency(sc.getWomenExtra()));

        lblGrossIncome.setText(formatCurrency(sc.getGrossIncome()));

        lblTax.setText(formatCurrency(sc.getTax()));
        lblSocialSecurity.setText(formatCurrency(sc.getSocialSecurity()));
        lblHealthcare.setText(formatCurrency(sc.getHealthcare()));
        lblInsurance.setText(formatCurrency(sc.getInsurance()));
        lblDeductionFromHours.setText(formatCurrency(sc.getDeductionFromHours()));
        lblLoanRepay.setText(formatCurrency(sc.getLoanRepay()));

        lblTotalDeductions.setText(formatCurrency(sc.getTotalDeductions()));
        lblTotalPay.setText(formatCurrency(sc.getNetIncome()));
    }

    private String formatCurrency(BigDecimal val) {
        if (val == null) return "$0.00";
        return "$" + val.setScale(2, RoundingMode.HALF_UP).toString();
    }

    // lil helper
    private ObservableList<String> periodDate() {
        ArrayList<String> dates = new ArrayList<>(this.list.size());

        for (SalaryCalculator cal : list) {
            dates.add(cal.getDate());
        }

        return FXCollections.observableList(dates);
    }
}
