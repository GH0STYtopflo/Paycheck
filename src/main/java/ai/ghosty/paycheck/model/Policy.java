package ai.ghosty.paycheck.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Supplier;

public class Policy {
    private BigDecimal INCOME_TAX_RATE, SOCIAL_SECURITY_RATE, 
            HEALTHCARE_RATE, INSURANCE_RATE, 
            OVERTIME_MULTIPLIER, MAX_LOAN_REPAY_RATE, ACCOMMODATION_FLAT_RATE, 
            MEAL_ALLOWANCE_FLAT_RATE, RECREATION_PER_FAMILY_MEMBER, 
            CHILD_ALLOWANCE_PER_CHILD, WOMEN_EXTRA;
    
    public Policy() {
        this.INCOME_TAX_RATE = new BigDecimal("0.15");
        this.SOCIAL_SECURITY_RATE = new BigDecimal("0.062");
        this. HEALTHCARE_RATE= new BigDecimal("0.0145");
        this.INSURANCE_RATE = new BigDecimal("0.02");
        this.OVERTIME_MULTIPLIER = new BigDecimal("1.5");
        this.MAX_LOAN_REPAY_RATE = new BigDecimal("0.10");
        this.ACCOMMODATION_FLAT_RATE = new BigDecimal("500.00");
        this.MEAL_ALLOWANCE_FLAT_RATE = new BigDecimal("100.00");
        this.RECREATION_PER_FAMILY_MEMBER = new BigDecimal("25.00");
        this.CHILD_ALLOWANCE_PER_CHILD = new BigDecimal("75.00");
        this.WOMEN_EXTRA = new BigDecimal("100.00");
    }
    
    public Policy(BigDecimal INCOME_TAX_RATE, BigDecimal SOCIAL_SECURITY_RATE,
                  BigDecimal HEALTHCARE_RATE, BigDecimal INSURANCE_RATE,
                  BigDecimal OVERTIME_MULTIPLIER, BigDecimal MAX_LOAN_REPAY_RATE,
                  BigDecimal ACCOMMODATION_FLAT_RATE, BigDecimal MEAL_ALLOWANCE_FLAT_RATE,
                  BigDecimal RECREATION_PER_FAMILY_MEMBER, BigDecimal CHILD_ALLOWANCE_PER_CHILD,
                  BigDecimal WOMEN_EXTRA) {
        
        this.INCOME_TAX_RATE = Objects.requireNonNullElseGet(INCOME_TAX_RATE, () -> (new BigDecimal("0.15")));
        this.SOCIAL_SECURITY_RATE = Objects.requireNonNullElseGet(SOCIAL_SECURITY_RATE, () -> new BigDecimal("0.062"));
        this.HEALTHCARE_RATE = Objects.requireNonNullElseGet(HEALTHCARE_RATE, () -> new BigDecimal("0.0145"));
        this.INSURANCE_RATE =  Objects.requireNonNullElseGet(INSURANCE_RATE, () -> new BigDecimal("0.02"));
        this.OVERTIME_MULTIPLIER = Objects.requireNonNullElseGet(OVERTIME_MULTIPLIER, () -> new BigDecimal("1.5"));
        this.MAX_LOAN_REPAY_RATE = Objects.requireNonNullElseGet(MAX_LOAN_REPAY_RATE, () -> new BigDecimal("0.10"));
        this.ACCOMMODATION_FLAT_RATE = Objects.requireNonNullElseGet(ACCOMMODATION_FLAT_RATE, () -> new BigDecimal("500.00"));
        this.MEAL_ALLOWANCE_FLAT_RATE = Objects.requireNonNullElseGet(MEAL_ALLOWANCE_FLAT_RATE, () -> new BigDecimal("100.00"));
        this.RECREATION_PER_FAMILY_MEMBER = Objects.requireNonNullElseGet(RECREATION_PER_FAMILY_MEMBER, () -> new BigDecimal("25.00"));
        this.CHILD_ALLOWANCE_PER_CHILD = Objects.requireNonNullElseGet(CHILD_ALLOWANCE_PER_CHILD, () -> new BigDecimal("75"));
        this.WOMEN_EXTRA = Objects.requireNonNullElseGet(WOMEN_EXTRA, () -> new BigDecimal("100"));
    }

    public BigDecimal getINCOME_TAX_RATE() {
        return INCOME_TAX_RATE;
    }

    public BigDecimal getSOCIAL_SECURITY_RATE() {
        return SOCIAL_SECURITY_RATE;
    }

    public BigDecimal getHEALTHCARE_RATE() {
        return HEALTHCARE_RATE;
    }

    public BigDecimal getINSURANCE_RATE() {
        return INSURANCE_RATE;
    }

    public BigDecimal getOVERTIME_MULTIPLIER() {
        return OVERTIME_MULTIPLIER;
    }

    public BigDecimal getMAX_LOAN_REPAY_RATE() {
        return MAX_LOAN_REPAY_RATE;
    }

    public BigDecimal getACCOMMODATION_FLAT_RATE() {
        return ACCOMMODATION_FLAT_RATE;
    }

    public BigDecimal getMEAL_ALLOWANCE_FLAT_RATE() {
        return MEAL_ALLOWANCE_FLAT_RATE;
    }

    public BigDecimal getRECREATION_PER_FAMILY_MEMBER() {
        return RECREATION_PER_FAMILY_MEMBER;
    }

    public BigDecimal getCHILD_ALLOWANCE_PER_CHILD() {
        return CHILD_ALLOWANCE_PER_CHILD;
    }

    public BigDecimal getWOMEN_EXTRA() {
        return WOMEN_EXTRA;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\n");
        sb.append("INCOME TAX RATE: " + INCOME_TAX_RATE);
        sb.append(",\n");
        sb.append("SOCIAL SECURITY: " + SOCIAL_SECURITY_RATE);
        sb.append(",\n");
        sb.append("HEALTHCARE: " + HEALTHCARE_RATE);
        sb.append(",\n");
        sb.append("INSURANCE: " + INSURANCE_RATE);
        sb.append(",\n");
        sb.append("OT: " + OVERTIME_MULTIPLIER);
        sb.append(",\n");
        sb.append("MAX LOAN: " + MAX_LOAN_REPAY_RATE);
        sb.append(",\n");
        sb.append("ACCOMMODATION: "+ ACCOMMODATION_FLAT_RATE);
        sb.append(",\n");
        sb.append("MEAL: " + MEAL_ALLOWANCE_FLAT_RATE);
        sb.append(",\n");
        sb.append("RECREATION: " + RECREATION_PER_FAMILY_MEMBER);
        sb.append(",\n");
        sb.append("CHILD ALLOWANCE: " + CHILD_ALLOWANCE_PER_CHILD);
        sb.append(",\n");
        sb.append("WOMEN EXTRA: " + WOMEN_EXTRA + "\n}");


        return sb.toString();
    }
}
