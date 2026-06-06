package ai.ghosty.paycheck.model;

import java.math.BigDecimal;

public class Position {
    private String posName;
    private BigDecimal baseSalaryPerHour;

    public Position(String posName, BigDecimal baseSalaryPerHour) {
        this.posName = posName;
        this.baseSalaryPerHour = baseSalaryPerHour;
    }

    public String getPosName() {
        return posName;
    }

    public BigDecimal getBaseSalaryPerHour() {
        return baseSalaryPerHour;
    }
}
