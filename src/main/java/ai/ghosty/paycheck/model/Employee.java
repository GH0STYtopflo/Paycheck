package ai.ghosty.paycheck.model;

import ai.ghosty.paycheck.util.IDGen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private int id, children, ot, workHours;
    private boolean isRent, isMarried;
    private String name, lastName, gender;
    private Position position;
    private LocalDate hireDate;
    private BigDecimal loan;
    private int deductionHours;


    public Employee(String name, String lastName, String gender, boolean isMarried, int ot, int children,
                    int workHours, int deductionHours, BigDecimal loan,
                    boolean isRent, LocalDate hireDate, Position position) {

        this.id = IDGen.generateUniqueID(Tables.EMPLOYEES);
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.ot = ot;
        this.children = children;
        this.workHours = workHours;
        this.deductionHours = deductionHours;
        this.loan = loan;
        this.isRent = isRent;
        this.isMarried = isMarried;
        this.position = position;

        this.hireDate = Objects.requireNonNullElseGet(hireDate, LocalDate::now);
    }

    public Employee(int id, String name, String lastName, String gender, boolean isMarried, int ot, int children,
                    int workHours, int deductionHours, BigDecimal loan,
                    boolean isRent, LocalDate hireDate, Position position) {

        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.gender = gender;
        this.ot = ot;
        this.children = children;
        this.workHours = workHours;
        this.deductionHours = deductionHours;
        this.loan = loan;
        this.isRent = isRent;
        this.isMarried = isMarried;
        this.position = position;

        this.hireDate = Objects.requireNonNullElseGet(hireDate, LocalDate::now);
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public int getExtraHours() {return ot;}

    public void setExtraHours(int extraHours) {
        this.ot = extraHours;
    }

    public int getWorkHours() {
        return workHours;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public BigDecimal getLoan() {
        return loan;
    }

    public void setLoan(BigDecimal loan) {
        this.loan = loan;
    }

    public int getDeductionHours() {
        return deductionHours;
    }

    public void setDeductionHours(int deductionHours) {
        this.deductionHours = deductionHours;
    }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {this.children = children;}

    public void setIsRent(boolean isRent) {
        this.isRent = isRent;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean getIsRent() {
        return isRent;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {isMarried = married;}
}
