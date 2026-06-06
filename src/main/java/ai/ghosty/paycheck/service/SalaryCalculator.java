package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.model.Employee;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class SalaryCalculator {
    private static final BigDecimal INCOME_TAX_RATE = new BigDecimal("0.15");
    private static final BigDecimal SOCIAL_SECURITY_RATE = new BigDecimal("0.062");
    private static final BigDecimal HEALTHCARE_RATE = new BigDecimal("0.0145");
    private static final BigDecimal INSURANCE_RATE = new BigDecimal("0.02");
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");
    private static final BigDecimal MAX_LOAN_REPAY_RATE = new BigDecimal("0.10");
    private static final BigDecimal ACCOMMODATION_FLAT_RATE = new BigDecimal("500.00");
    private static final BigDecimal MEAL_ALLOWANCE_FLAT_RATE = new BigDecimal("100.00");
    private static final BigDecimal RECREATION_PER_CHILD = new BigDecimal("25.00");
    private static final BigDecimal CHILD_ALLOWANCE_PER_CHILD = new BigDecimal("75.00");
    private static final BigDecimal WOMEN_EXTRA = new BigDecimal("100.00");

    private final BigDecimal baseIncome;
    private final BigDecimal grossIncome;
    private final BigDecimal otPay;
    private final BigDecimal accommodationAllowance;
    private final BigDecimal mealAllowance;
    private final BigDecimal recreationAllowance;
    private final BigDecimal childAllowance;
    private final BigDecimal womensExtra;
    private final BigDecimal tax;
    private final BigDecimal socialSecurity;
    private final BigDecimal healthcare;
    private final BigDecimal insurance;
    private final BigDecimal deductionFromHours;
    private final BigDecimal loanRepay;
    private final BigDecimal totalDeductions;
    private final BigDecimal netIncome;
    private final String date;

    public SalaryCalculator(Employee employee) {
        BigDecimal hourlyRate = employee.getPosition().getBaseSalaryPerHour()
                .multiply(calculateTenureMultiplier(employee));

        this.baseIncome = hourlyRate.multiply(BigDecimal.valueOf(employee.getWorkHours()))
                .setScale(2, RoundingMode.HALF_UP);

        this.otPay = hourlyRate.multiply(BigDecimal.valueOf(employee.getExtraHours()))
                .multiply(OVERTIME_MULTIPLIER)
                .setScale(2, RoundingMode.HALF_UP);

        this.accommodationAllowance = employee.isRent() ? ACCOMMODATION_FLAT_RATE : BigDecimal.ZERO;
        this.mealAllowance = MEAL_ALLOWANCE_FLAT_RATE;
        this.recreationAllowance = RECREATION_PER_CHILD.multiply(BigDecimal.valueOf(employee.getChildren()));
        this.childAllowance = CHILD_ALLOWANCE_PER_CHILD.multiply(BigDecimal.valueOf(employee.getChildren()));
        this.womensExtra = "f".equalsIgnoreCase(employee.getGender()) ? WOMEN_EXTRA : BigDecimal.ZERO;

        this.grossIncome = this.baseIncome
                .add(this.otPay)
                .add(this.accommodationAllowance)
                .add(this.mealAllowance)
                .add(this.recreationAllowance)
                .add(this.childAllowance)
                .add(this.womensExtra).setScale(2, RoundingMode.HALF_UP);

        this.tax = this.grossIncome.multiply(INCOME_TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        this.socialSecurity = this.grossIncome.multiply(SOCIAL_SECURITY_RATE).setScale(2, RoundingMode.HALF_UP);
        this.healthcare = this.grossIncome.multiply(HEALTHCARE_RATE).setScale(2, RoundingMode.HALF_UP);
        this.insurance = this.grossIncome.multiply(INSURANCE_RATE).setScale(2, RoundingMode.HALF_UP);

        this.deductionFromHours = hourlyRate.multiply(BigDecimal.valueOf(employee.getDeductionHours()))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal calculatedRepayment = this.grossIncome.multiply(MAX_LOAN_REPAY_RATE);
        this.loanRepay = (calculatedRepayment.compareTo(employee.getLoan()) > 0)
                ? employee.getLoan()
                : calculatedRepayment.setScale(2, RoundingMode.HALF_UP);

        this.totalDeductions = this.tax
                .add(this.socialSecurity)
                .add(this.healthcare)
                .add(this.insurance)
                .add(this.deductionFromHours)
                .add(this.loanRepay);

        this.netIncome = this.grossIncome.subtract(this.totalDeductions);
        this.date = LocalDate.now() + "-" + LocalTime.now().toString();
    }

    // Overloaded constructor to build from stored record values
    public SalaryCalculator(java.math.BigDecimal baseIncome,
                            java.math.BigDecimal otPay,
                            java.math.BigDecimal childAllowance,
                            java.math.BigDecimal accommodationAllowance,
                            java.math.BigDecimal mealAllowance,
                            java.math.BigDecimal recreationAllowance,
                            java.math.BigDecimal womensExtra,
                            java.math.BigDecimal tax,
                            java.math.BigDecimal loanRepay,
                            java.math.BigDecimal insurance,
                            java.math.BigDecimal healthcare,
                            java.math.BigDecimal socialSecurity,
                            java.math.BigDecimal deductionFromHours,
                            java.math.BigDecimal grossIncome,
                            java.math.BigDecimal totalDeductions,
                            java.math.BigDecimal netIncome,
                            String date) {
        this.baseIncome = baseIncome.setScale(2, RoundingMode.HALF_UP);
        this.otPay = otPay.setScale(2, RoundingMode.HALF_UP);
        this.accommodationAllowance = accommodationAllowance.setScale(2, RoundingMode.HALF_UP);
        this.mealAllowance = mealAllowance.setScale(2, RoundingMode.HALF_UP);
        this.recreationAllowance = recreationAllowance.setScale(2, RoundingMode.HALF_UP);
        this.childAllowance = childAllowance.setScale(2, RoundingMode.HALF_UP);
        this.womensExtra = womensExtra.setScale(2, RoundingMode.HALF_UP);
        this.grossIncome = grossIncome.setScale(2, RoundingMode.HALF_UP);
        this.tax = tax.setScale(2, RoundingMode.HALF_UP);
        this.socialSecurity = socialSecurity.setScale(2, RoundingMode.HALF_UP);
        this.healthcare = healthcare.setScale(2, RoundingMode.HALF_UP);
        this.insurance = insurance.setScale(2, RoundingMode.HALF_UP);
        this.deductionFromHours = deductionFromHours.setScale(2, RoundingMode.HALF_UP);
        this.loanRepay = loanRepay.setScale(2, RoundingMode.HALF_UP);
        this.totalDeductions = totalDeductions.setScale(2, RoundingMode.HALF_UP);
        this.netIncome = netIncome.setScale(2, RoundingMode.HALF_UP);
        this.date = date;
    }

    private BigDecimal calculateTenureMultiplier(Employee emp) {
        long yearsOfService = ChronoUnit.YEARS.between(emp.getHireDate(), LocalDate.now());
        if (yearsOfService <= 0) {
            return BigDecimal.ONE;
        }
        BigDecimal loyaltyBonus = BigDecimal.valueOf(yearsOfService).multiply(new BigDecimal("0.02"));
        BigDecimal maxBonus = new BigDecimal("0.30");

        if (loyaltyBonus.compareTo(maxBonus) > 0) {
            loyaltyBonus = maxBonus;
        }
        return BigDecimal.ONE.add(loyaltyBonus);
    }

    public BigDecimal getBaseIncome() { return baseIncome; }
    public BigDecimal getTax() { return tax; }
    public BigDecimal getInsurance() { return insurance; }
    public BigDecimal getOt() { return otPay; }
    public BigDecimal getChildAllowance() { return childAllowance; }
    public BigDecimal getAccommodationAllowance() { return accommodationAllowance; }
    public BigDecimal getGrossIncome() { return grossIncome; }
    public BigDecimal getDeductionFromHours() { return deductionFromHours; }
    public BigDecimal getTotalDeductions() { return totalDeductions; }
    public BigDecimal getNetIncome() { return netIncome; }
    public BigDecimal getHealthcare() { return healthcare; }
    public BigDecimal getSocialSecurity() { return socialSecurity; }
    public BigDecimal getMealAllowance() { return mealAllowance; }
    public BigDecimal getRecreationAllowance() { return recreationAllowance; }
    public BigDecimal getWomenExtra() { return womensExtra; }
    public BigDecimal getLoanRepay() { return loanRepay; }

    public String[] statsToString() {
        return new String[]{
                baseIncome.toString(), tax.toString(), insurance.toString(),
                otPay.toString(), childAllowance.toString(), accommodationAllowance.toString(),
                grossIncome.toString(), deductionFromHours.toString(), totalDeductions.toString(),
                netIncome.toString(), healthcare.toString(), socialSecurity.toString(),
                mealAllowance.toString(), recreationAllowance.toString()
        };
    }

    public String getDate() {
        return date;
    }
}
