# Paycheck

A desktop payroll management application built with JavaFX and SQLite. Paycheck handles employee registration, salary calculation, and paycheck record management with a clean, emerald-themed UI.

## Features

### Authentication
- **Password Login** — standard username/password authentication with SHA-256 hashed passwords
- **Quick Login** — one-time code (OTA) based login for fast access
- **Role-based access** — Admin and User roles with separate dashboards

### Admin Dashboard
- **Employee Management** — register new employees with personal details (name, gender, marital status, children, hire date, position)
- **Position Management** — create and manage job positions with custom hourly rates
- **Paycheck Generation** — generate paycheck records for new or existing employees, including work hours, overtime, deductions, and loans
- **Policy Configuration** — customize all payroll parameters:
  - Income tax rate
  - Social security rate
  - Healthcare rate
  - Insurance rate
  - Overtime multiplier
  - Max loan repayment rate
  - Accommodation, meal, recreation, and child allowances
  - Women's extra allowance
- **Employee Directory** — view and delete employees from a searchable table

### Employee Details View
- **Personal Info** — view employee ID, name, gender, position, and calculated tenure
- **Paycheck Breakdown** — detailed salary view with:
  - **Earnings**: base income, overtime pay, accommodation, meal, recreation, child allowances, women's extra
  - **Deductions**: income tax, social security, healthcare, insurance, hour deductions, loan repayment
  - **Net income** with formatted currency display
- **Pay History** — browse all paycheck records by period via a dropdown selector

### Salary Calculation Engine
- **Tenure-based raises** — automatic 2% loyalty bonus per year of service (capped at 30%)
- **Configurable allowances** — flat-rate and per-member allowances
- **Loan repayment** — capped at a configurable percentage of gross income
- **Overtime** — calculated with a configurable multiplier (default 1.5x)

### Other
- **Input validation** — field-level validation (not blank, numeric, non-negative) with user-friendly warnings
- **Application logging** — custom logger with log levels (INFO, ERROR)
- **Cross-platform** — builds for Linux and Windows via Maven with platform-specific JavaFX classifiers
- **Auto DB initialization** — SQLite database and tables are created automatically on first run

## Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Java 21 |
| UI | JavaFX 21 (FXML + CSS) |
| Database | SQLite (via sqlite-jdbc) |
| Build | Maven (with shade plugin for uber JAR) |
| Architecture | MVC (Model-View-Controller) |

## Project Structure

```
src/main/java/ai/ghosty/paycheck/
├── Launcher.java              # Application entry point
├── authentication/
│   └── QuickLogin.java        # OTA code generation
├── db/
│   ├── DBConnect.java         # SQLite connection manager
│   └── DBInit.java            # Schema creation & admin seeding
├── logger/
│   ├── Logger.java            # Application-wide logger
│   └── LogLevel.java          # Log level enum
├── model/
│   ├── Employee.java          # Employee entity
│   ├── Position.java          # Job position entity
│   ├── Record.java            # Paycheck record entity
│   ├── Policy.java            # Payroll policy parameters
│   ├── User.java              # User account entity
│   ├── Role.java              # User role enum
│   └── Tables.java            # Table name enum
├── service/
│   ├── EmployeeServices.java  # Employee CRUD
│   ├── UserServices.java      # User CRUD & auth queries
│   ├── RecordsServices.java   # Paycheck record CRUD
│   ├── PositionsServices.java # Position CRUD
│   └── SalaryCalculator.java  # Salary calculation engine
├── util/
│   ├── Encryption.java        # SHA-256 hashing
│   ├── IDGen.java             # Unique ID generator
│   └── PolicyConfig.java      # Policy persistence (JSON)
├── validation/
│   ├── Rule.java              # Validation rule enum
│   ├── Validator.java         # Field validation logic
│   └── ValidationResult.java  # Validation result holder
└── view/
    ├── Controller.java        # Base controller (window drag, close, minimize)
    ├── LoginController.java   # Login screen
    ├── AdminController.java   # Admin dashboard
    ├── DetailsController.java # Employee paycheck details
    └── WarningUpdater.java    # Warning label helper

src/main/resources/app/ui/
├── login.fxml / loginStyle.css
├── admin.fxml / adminStyle.css
└── details.fxml / detailsStyle.css
```

## Getting Started

### Prerequisites
- Java 21 or later
- Maven 3.6+

#### Clone the repository

```bash
# Clone the repository
git clone https://github.com/GH0STYtopflo/Paycheck
cd Paycheck
```

#### Run with Maven JavaFX plugin
```bash
mvn javafx:run
```

#### Or build the uber JAR and run it
```bash
mvn clean package
mv target/UberJarCrossPlatform.jar ../
java -jar UberJarCrossPlatform.jar
```

#### Alternatively you can use the run scripts
```bash
# For GNU/Linux Systems 
chmod 700 run.sh 
./run.sh
# For Windows Systems
.\run.ps1
```
> Please note that you might need to make Windows let PowerShell run scipts.
> to do so run `Set-ExecutionPolicy -Scope CurrentUser RemoteSigned` in PowerShell
> then answer `[Y] Yes`. After this you can run PowerShell scripts normally.
> For reverting changes run `Set-ExecutionPolicy -Scope CurrentUser Undefined`

The SQLite database (`db/ghst.db`) is created automatically on the first launch.

### Default Admin Credentials

| Field | Value |
|-------|-------|
| Username | `GHOSTY` |
| Password | `ghostywontmiss` |


## Database Schema

The application uses four tables:

- **positions** — job titles with hourly rates
- **employees** — personal details, position, work hours, and benefits info
- **users** — login credentials linked to employees (one-to-one)
- **records** — paycheck records with full earnings/deduction breakdown per period

## License

This project is for educational purposes.
