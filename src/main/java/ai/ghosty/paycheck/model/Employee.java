package ai.ghosty.paycheck.model;

import ai.ghosty.paycheck.util.IDGen;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Employee {
    private int id, children, ot, workHours;
    private boolean rent;
    private String name, lastName, gender;
    private Position position;
    private LocalDate hireDate;
    private BigDecimal loan;
    private int deductionHours;


    public Employee(String name, String lastName, String gender, int ot, int children,
                    int workHours, int deductionHours, BigDecimal loan,
                    boolean rent, LocalDate hireDate, Position position) {

        this.id = IDGen.generateUniqueID();
        this.name = name;
        this.lastName = lastName;
        this.gender = gender;
        this.ot = ot;
        this.children = children;
        this.workHours = workHours;
        this.deductionHours = deductionHours;
        this.loan = loan;
        this.rent = rent;

        if (position != null) {this.position = position; }
        else this.position = new Position("NaP", BigDecimal.ZERO);

        this.hireDate = Objects.requireNonNullElseGet(hireDate, LocalDate::now);
    }

    public Employee(int id, String name, String lastName, String gender, int ot, int children,
                    int workHours, int deductionHours, BigDecimal loan,
                    boolean rent, LocalDate hireDate, Position position) {

        this.name = name;
        this.lastName = lastName;
        this.id = id;
        this.gender = gender;
        this.ot = ot;
        this.children = children;
        this.workHours = workHours;
        this.deductionHours = deductionHours;
        this.loan = loan;
        this.rent = rent;

        if (position != null) {this.position = position; }
        else this.position = new Position("NaP", BigDecimal.ZERO);

        this.hireDate = Objects.requireNonNullElseGet(hireDate, LocalDate::now);
    }

    // getter and setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public int getExtraHours() {
        return ot;
    }

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

    public void setRent(boolean rent) {
        this.rent = rent;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isRent() {
        return rent;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
