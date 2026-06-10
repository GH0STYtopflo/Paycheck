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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import ai.ghosty.paycheck.model.Record;

public class DetailsController extends Controller {
    @FXML private Label lblTotalPay;

    @FXML private Label lblId, lblFirstName, lblLastName, lblGender,
            lblHireDate, lblPositionTitle, lblTenure;

    @FXML private ComboBox<String> comboPeriod;

    @FXML private Label lblBaseIncome, lblOt, lblAccommodationAllowance, lblMealAllowance,
            lblRecreationAllowance, lblChildAllowance, lblWomenExtra, lblGrossIncome;

    @FXML private Label lblTax, lblSocialSecurity, lblHealthcare, lblInsurance,
            lblDeductionFromHours, lblLoanRepay, lblTotalDeductions;

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

    private ArrayList<Record> list;

    // ATTENTION! argument pattern:
    // initialize(stage, Employee, Paycheck Record)
    @Override
    public void initialize(Object... args) {
        this.ownstage = (Stage) args[0];

        Employee emp = (Employee) args[1];
        this.list = (ArrayList<Record>) args[2];
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

    private void populateEmployee(Employee emp) {
        lblId.setText(String.valueOf(emp.getId()));
        lblFirstName.setText(emp.getName());
        lblLastName.setText(emp.getLastName());
        lblGender.setText(emp.getGender());
        lblHireDate.setText(emp.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        lblPositionTitle.setText(emp.getPosition().getTitle());
        lblTenure.setText(ChronoUnit.YEARS.between(emp.getHireDate(), LocalDate.now())
        + " Years and " + ChronoUnit.MONTHS.between(emp.getHireDate(), LocalDate.now()) % 12 + " Months");
    }

    private void populateSalary(Record rec) {
        lblBaseIncome.setText(formatCurrency(rec.getBaseIncome()));
        lblOt.setText(formatCurrency(rec.getOtPay()));
        lblAccommodationAllowance.setText(formatCurrency(rec.getAccommodationAllowance()));
        lblMealAllowance.setText(formatCurrency(rec.getMealAllowance()));
        lblRecreationAllowance.setText(formatCurrency(rec.getRecreationAllowance()));
        lblChildAllowance.setText(formatCurrency(rec.getChildAllowance()));
        lblWomenExtra.setText(formatCurrency(rec.getWomensExtra()));

        lblGrossIncome.setText(formatCurrency(rec.getGrossIncome()));

        lblTax.setText(formatCurrency(rec.getTax()));
        lblSocialSecurity.setText(formatCurrency(rec.getSocialSecurity()));
        lblHealthcare.setText(formatCurrency(rec.getHealthcare()));
        lblInsurance.setText(formatCurrency(rec.getInsurance()));
        lblDeductionFromHours.setText(formatCurrency(rec.getDeductionFromHours()));
        lblLoanRepay.setText(formatCurrency(rec.getLoanRepay()));

        lblTotalDeductions.setText(formatCurrency(rec.getTotalDeductions()));
        lblTotalPay.setText(formatCurrency(rec.getNetIncome()));
    }

    private String formatCurrency(BigDecimal val) {
        if (val == null) return "$0.00";
        return "$" + val.setScale(2, RoundingMode.HALF_UP).toString();
    }

    // lil helper
    private ObservableList<String> periodDate() {
        ArrayList<String> dates = new ArrayList<>(this.list.size());

        // TODO we don't need the time stamp to be this accurate
        for (Record rec : list) {
            dates.add(rec.getDate());
        }

        return FXCollections.observableList(dates);
    }
}
