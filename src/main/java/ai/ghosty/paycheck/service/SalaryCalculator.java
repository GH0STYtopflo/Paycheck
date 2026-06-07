package ai.ghosty.paycheck.service;

import ai.ghosty.paycheck.model.Employee;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import ai.ghosty.paycheck.model.Policy;
import ai.ghosty.paycheck.model.Record;

public class SalaryCalculator {
    public static Record calculate(Employee employee, Policy policy) {
        Record record = new Record(employee.getId());
        
        BigDecimal hourlyRate = employee.getPosition().getSalaryPerHour()
                .multiply(calculateTenureMultiplier(employee));


        calculatePays(record, employee, hourlyRate, policy);
        calculateDeductions(record, employee, hourlyRate, policy);

        record.setNetIncome(record.getGrossIncome().subtract(record.getTotalDeductions()));
        record.setDate(LocalDate.now() + "-" + LocalTime.now().toString());

        return record;
    }

    private static void calculatePays(Record record, Employee employee, BigDecimal hourlyRate, Policy policy) {
        record.setBaseIncome(hourlyRate.multiply(BigDecimal.valueOf(employee.getWorkHours()))
                .setScale(2, RoundingMode.HALF_UP));

        record.setOtPay(hourlyRate.multiply(BigDecimal.valueOf(employee.getExtraHours()))
                .multiply(policy.getOVERTIME_MULTIPLIER())
                .setScale(2, RoundingMode.HALF_UP));

        record.setAccommodationAllowance(employee.getIsRent() ? policy.getACCOMMODATION_FLAT_RATE() : BigDecimal.ZERO);
        record.setMealAllowance(policy.getMEAL_ALLOWANCE_FLAT_RATE());
        record.setRecreationAllowance(policy.getRECREATION_PER_FAMILY_MEMBER().multiply(BigDecimal.valueOf(employee.getChildren() + (employee.isMarried() ? 2 : 0))));
        record.setChildAllowance(policy.getCHILD_ALLOWANCE_PER_CHILD().multiply(BigDecimal.valueOf(employee.getChildren())));
        record.setWomensExtra("f".equalsIgnoreCase(employee.getGender()) ? policy.getWOMEN_EXTRA() : BigDecimal.ZERO);

        record.setGrossIncome(record.getBaseIncome()
                .add(record.getOtPay())
                .add(record.getAccommodationAllowance())
                .add(record.getMealAllowance())
                .add(record.getRecreationAllowance())
                .add(record.getChildAllowance())
                .add(record.getWomensExtra()).setScale(2, RoundingMode.HALF_UP));
    }

    private static void calculateDeductions(Record record, Employee employee, BigDecimal hourlyRate, Policy policy) {
        record.setTax(record.getGrossIncome().multiply(policy.getINCOME_TAX_RATE()).setScale(2, RoundingMode.HALF_UP));
        record.setSocialSecurity(record.getGrossIncome().multiply(policy.getSOCIAL_SECURITY_RATE()).setScale(2, RoundingMode.HALF_UP));
        record.setHealthcare(record.getGrossIncome().multiply(policy.getHEALTHCARE_RATE()).setScale(2, RoundingMode.HALF_UP));
        record.setInsurance(record.getGrossIncome().multiply(policy.getINSURANCE_RATE()).setScale(2, RoundingMode.HALF_UP));

        record.setDeductionFromHours(hourlyRate.multiply(BigDecimal.valueOf(employee.getDeductionHours()))
                .setScale(2, RoundingMode.HALF_UP));

        BigDecimal calculatedRepayment = record.getGrossIncome().multiply(policy.getMAX_LOAN_REPAY_RATE());
        record.setLoanRepay((calculatedRepayment.compareTo(employee.getLoan()) > 0)
                ? employee.getLoan()
                : calculatedRepayment.setScale(2, RoundingMode.HALF_UP));

        record.setTotalDeductions(record.getTax()
                .add(record.getSocialSecurity())
                .add(record.getHealthcare())
                .add(record.getInsurance())
                .add(record.getDeductionFromHours())
                .add(record.getLoanRepay()));
    }


    private static BigDecimal calculateTenureMultiplier(Employee emp) {
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
}
