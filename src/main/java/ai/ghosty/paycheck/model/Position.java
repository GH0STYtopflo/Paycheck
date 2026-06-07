package ai.ghosty.paycheck.model;

import java.math.BigDecimal;

public class Position {
    private int id;
    private String title;
    private BigDecimal salaryPerHour;

    public Position(int id, String title, BigDecimal salaryPerHour) {
        this.id = id;
        this.title = title;
        this.salaryPerHour = salaryPerHour;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getSalaryPerHour() {
        return salaryPerHour;
    }

    public int getId() {
        return id;
    }
}
