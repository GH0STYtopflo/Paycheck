package ai.ghosty.paycheck.model;

import java.math.BigDecimal;

public class Record {
    private final int id; // corresponding users id
    private BigDecimal baseIncome;
    private BigDecimal grossIncome;
    private BigDecimal otPay;
    private BigDecimal accommodationAllowance;
    private BigDecimal mealAllowance;
    private BigDecimal recreationAllowance;
    private BigDecimal childAllowance;
    private BigDecimal womensExtra;
    private BigDecimal tax;
    private BigDecimal socialSecurity;
    private BigDecimal healthcare;
    private BigDecimal insurance;
    private BigDecimal deductionFromHours;
    private BigDecimal loanRepay;
    private BigDecimal totalDeductions;
    private BigDecimal netIncome;
    private String date;

    public Record (int id) {
        this.id = id;
    }
    public Record (int id, BigDecimal baseIncome,
                   BigDecimal grossIncome, BigDecimal otPay,
                   BigDecimal accommodationAllowance, BigDecimal mealAllowance,
                   BigDecimal recreationAllowance, BigDecimal childAllowance,
                   BigDecimal womensExtra, BigDecimal tax,
                   BigDecimal socialSecurity, BigDecimal healthcare,
                   BigDecimal insurance, BigDecimal deductionFromHours,
                   BigDecimal loanRepay, BigDecimal totalDeductions,
                   BigDecimal netIncome, String date) {

        this.id = id;
        this.grossIncome = grossIncome;
        this.otPay = otPay;
        this.baseIncome = baseIncome;
        this.accommodationAllowance = accommodationAllowance;
        this.mealAllowance = mealAllowance;
        this.recreationAllowance = recreationAllowance;
        this.childAllowance = childAllowance;
        this.womensExtra = womensExtra;
        this.tax = tax;
        this.socialSecurity = socialSecurity;
        this.healthcare = healthcare;
        this.insurance = insurance;
        this.deductionFromHours = deductionFromHours;
        this.loanRepay = loanRepay;
        this.totalDeductions = totalDeductions;
        this.netIncome = netIncome;
        this.date = date;
    }

    public BigDecimal getBaseIncome() {
        return baseIncome;
    }

    public void setBaseIncome(BigDecimal baseIncome) {
        this.baseIncome = baseIncome;
    }

    public BigDecimal getGrossIncome() {
        return grossIncome;
    }

    public void setGrossIncome(BigDecimal grossIncome) {
        this.grossIncome = grossIncome;
    }

    public BigDecimal getOtPay() {
        return otPay;
    }

    public void setOtPay(BigDecimal otPay) {
        this.otPay = otPay;
    }

    public BigDecimal getAccommodationAllowance() {
        return accommodationAllowance;
    }

    public void setAccommodationAllowance(BigDecimal accommodationAllowance) {
        this.accommodationAllowance = accommodationAllowance;
    }

    public BigDecimal getMealAllowance() {
        return mealAllowance;
    }

    public void setMealAllowance(BigDecimal mealAllowance) {
        this.mealAllowance = mealAllowance;
    }

    public BigDecimal getRecreationAllowance() {
        return recreationAllowance;
    }

    public void setRecreationAllowance(BigDecimal recreationAllowance) {
        this.recreationAllowance = recreationAllowance;
    }

    public BigDecimal getChildAllowance() {
        return childAllowance;
    }

    public void setChildAllowance(BigDecimal childAllowance) {
        this.childAllowance = childAllowance;
    }

    public BigDecimal getWomensExtra() {
        return womensExtra;
    }

    public void setWomensExtra(BigDecimal womensExtra) {
        this.womensExtra = womensExtra;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getSocialSecurity() {
        return socialSecurity;
    }

    public void setSocialSecurity(BigDecimal socialSecurity) {
        this.socialSecurity = socialSecurity;
    }

    public BigDecimal getHealthcare() {
        return healthcare;
    }

    public void setHealthcare(BigDecimal healthcare) {
        this.healthcare = healthcare;
    }

    public BigDecimal getInsurance() {
        return insurance;
    }

    public void setInsurance(BigDecimal insurance) {
        this.insurance = insurance;
    }

    public BigDecimal getDeductionFromHours() {
        return deductionFromHours;
    }

    public void setDeductionFromHours(BigDecimal deductionFromHours) {
        this.deductionFromHours = deductionFromHours;
    }

    public BigDecimal getLoanRepay() {
        return loanRepay;
    }

    public void setLoanRepay(BigDecimal loanRepay) {
        this.loanRepay = loanRepay;
    }

    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }
}
