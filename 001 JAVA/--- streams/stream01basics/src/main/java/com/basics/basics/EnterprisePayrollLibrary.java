package com.basics.basics;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * ============================================================
 * EnterprisePayrollLibrary - Reusable Data Generation Library
 * ============================================================
 *
 * USAGE:
 *
 *   // 1. Quickstart — 1 million employees, all defaults
 *   PayrollDataset dataset = PayrollLibrary.generate();
 *
 *   // 2. Fluent builder
 *   PayrollDataset dataset = PayrollLibrary.builder()
 *       .employees(500_000)
 *       .seed(42)
 *       .threads(8)
 *       .referenceDate(LocalDate.of(2026, 10, 2))
 *       .validate(true)
 *       .build();
 *
 *   // 3. Access data
 *   List<Employee> all        = dataset.employees();
 *   Organization   org        = dataset.organization();
 *   DatasetStats   stats      = dataset.stats();
 *
 *   // 4. Stream analytics
 *   dataset.employees().parallelStream()
 *       .filter(e -> e.status() == EmploymentStatus.ACTIVE)
 *       .mapToLong(Employee::annualSalary)
 *       .average();
 *
 *   // 5. Incremental / repeated generation (same seed => same employee)
 *   Employee emp = PayrollLibrary.generateOne(42L, 1001, LocalDate.now());
 *
 * All monetary amounts are stored as long PAISE (1 INR = 100 paise).
 * Same (seed, referenceDate, employeeCount) => identical dataset,
 * independent of thread count or JVM version.
 */
public final class EnterprisePayrollLibrary {

    private EnterprisePayrollLibrary() {}

    // ============================================================
    // PUBLIC API  — entry points
    // ============================================================

    /**
     * Generate 1 million employees with default settings.
     */
    public static PayrollDataset generate() {
        return builder().build();
    }

    /**
     * Generate a dataset with a specific employee count.
     */
    public static PayrollDataset generate(int employeeCount) {
        return builder().employees(employeeCount).build();
    }

    /**
     * Generate a single employee deterministically.
     * Useful for incremental generation or unit tests.
     *
     * @param globalSeed    the library-wide seed (must match the seed used
     *                      when the Organization was built)
     * @param employeeId    1-based employee id
     * @param referenceDate payroll reference date
     */
    public static Employee generateOne(long globalSeed, int employeeId, LocalDate referenceDate) {
        Organization org = Organization.build(globalSeed);
        GeneratorConfig cfg = new GeneratorConfig(
                Math.max(employeeId, 1), globalSeed,
                1, referenceDate);
        return new EmployeeFactory(cfg, org).create(employeeId);
    }

    /**
     * Fluent builder.  All parameters are optional; sensible defaults apply.
     */
    public static Builder builder() {
        return new Builder();
    }

    // ============================================================
    // BUILDER
    // ============================================================

    public static final class Builder {
        private int        employeeCount = 1_000_000;
        private long       seed          = 42L;
        private int        threads       = Runtime.getRuntime().availableProcessors();
        private LocalDate  referenceDate = LocalDate.now();
        private boolean    validate      = true;
        private boolean    printProgress = false;

        private Builder() {}

        public Builder employees(int n)              { this.employeeCount = n;       return this; }
        public Builder seed(long s)                  { this.seed = s;                return this; }
        public Builder threads(int t)                { this.threads = t;             return this; }
        public Builder referenceDate(LocalDate d)    { this.referenceDate = d;       return this; }
        /** Run built-in integrity checks after generation (default: true). */
        public Builder validate(boolean v)           { this.validate = v;            return this; }
        /** Print generation progress to stdout (default: false). */
        public Builder printProgress(boolean p)      { this.printProgress = p;       return this; }

        public PayrollDataset build() {
            GeneratorConfig cfg = new GeneratorConfig(employeeCount, seed, threads, referenceDate);
            try {
                Organization    org       = Organization.build(cfg.seed());
                EmployeeFactory factory   = new EmployeeFactory(cfg, org);
                List<Employee>  employees = generateEmployees(cfg, factory, printProgress);

                Map<String, Long> violations = Collections.emptyMap();
                if (validate) {
                    violations = EnterprisePayrollLibrary.validate(employees, org, cfg);
                    if (!violations.isEmpty()) {
                        throw new PayrollGenerationException(
                                "Dataset validation failed: " + violations);
                    }
                }
                return new PayrollDataset(employees, org, cfg, DatasetStats.compute(employees, org));
            } catch (PayrollGenerationException e) {
                throw e;
            } catch (Exception e) {
                throw new PayrollGenerationException("Generation failed", e);
            }
        }
    }

    // ============================================================
    // PUBLIC RESULT TYPE
    // ============================================================

    /**
     * Immutable result object returned by every generation method.
     */
    public static final class PayrollDataset {
        private final List<Employee>     employees;
        private final Organization       organization;
        private final GeneratorConfig    config;
        private final DatasetStats       stats;

        PayrollDataset(List<Employee> employees, Organization organization,
                       GeneratorConfig config, DatasetStats stats) {
            this.employees    = employees;
            this.organization = organization;
            this.config       = config;
            this.stats        = stats;
        }

        /** Unmodifiable, random-access list — safe for parallelStream(). */
        public List<Employee>  employees()    { return employees;    }
        public Organization    organization() { return organization; }
        public GeneratorConfig config()       { return config;       }
        public DatasetStats    stats()        { return stats;        }

        /** Convenience: stream employees in parallel. */
        public java.util.stream.Stream<Employee> parallelStream() {
            return employees.parallelStream();
        }

        /** Convenience: stream employees sequentially. */
        public java.util.stream.Stream<Employee> stream() {
            return employees.stream();
        }

        @Override
        public String toString() {
            return "PayrollDataset[employees=" + employees.size()
                    + ", seed=" + config.seed()
                    + ", referenceDate=" + config.referenceDate() + "]";
        }
    }

    // ============================================================
    // DATASET STATISTICS  (computed once, cached on PayrollDataset)
    // ============================================================

    public static final class DatasetStats {
        public final long totalEmployees;
        public final long totalAttendanceDays;
        public final long totalPresentDays;
        public final long totalLeaveRecords;
        public final long totalBonusRecords;
        public final long totalBonusAmount;        // paise
        public final long totalReimbursements;
        public final long totalLoans;
        public final long totalLoanInstallments;   // lazy; count only
        public final long totalOutstandingLoans;   // paise
        public final long totalSalaryRevisions;
        public final long totalBenefits;
        public final long totalPerformanceRecords;
        public final long totalAnnualPayroll;      // paise
        public final long avgAnnualSalary;         // paise
        public final long minAnnualSalary;         // paise
        public final long maxAnnualSalary;         // paise
        public final Map<EmploymentStatus, Long> headcountByStatus;
        public final Map<Integer, Long> payrollByBusinessUnit; // buId -> paise

        private DatasetStats(Builder b) {
            this.totalEmployees           = b.totalEmployees;
            this.totalAttendanceDays      = b.totalAttendanceDays;
            this.totalPresentDays         = b.totalPresentDays;
            this.totalLeaveRecords        = b.totalLeaveRecords;
            this.totalBonusRecords        = b.totalBonusRecords;
            this.totalBonusAmount         = b.totalBonusAmount;
            this.totalReimbursements      = b.totalReimbursements;
            this.totalLoans               = b.totalLoans;
            this.totalLoanInstallments    = b.totalLoanInstallments;
            this.totalOutstandingLoans    = b.totalOutstandingLoans;
            this.totalSalaryRevisions     = b.totalSalaryRevisions;
            this.totalBenefits            = b.totalBenefits;
            this.totalPerformanceRecords  = b.totalPerformanceRecords;
            this.totalAnnualPayroll       = b.totalAnnualPayroll;
            this.avgAnnualSalary          = b.avgAnnualSalary;
            this.minAnnualSalary          = b.minAnnualSalary;
            this.maxAnnualSalary          = b.maxAnnualSalary;
            this.headcountByStatus        = b.headcountByStatus;
            this.payrollByBusinessUnit    = b.payrollByBusinessUnit;
        }

        static DatasetStats compute(List<Employee> employees, Organization org) {
            Builder b = new Builder();
            b.totalEmployees = employees.size();

            b.totalAttendanceDays   = employees.parallelStream().mapToLong(e -> e.attendance().size()).sum();
            b.totalPresentDays      = employees.parallelStream()
                    .mapToLong(e -> e.attendance().count(AttendanceStatus.PRESENT)).sum();
            b.totalLeaveRecords     = employees.parallelStream().mapToLong(e -> e.leaves().size()).sum();
            b.totalBonusRecords     = employees.parallelStream().mapToLong(e -> e.bonuses().size()).sum();
            b.totalBonusAmount      = employees.parallelStream()
                    .mapToLong(e -> e.bonuses().stream().mapToLong(Bonus::amount).sum()).sum();
            b.totalReimbursements   = employees.parallelStream().mapToLong(e -> e.reimbursements().size()).sum();
            b.totalLoans            = employees.parallelStream().mapToLong(e -> e.loans().size()).sum();
            b.totalLoanInstallments = employees.parallelStream()
                    .mapToLong(e -> e.loans().stream().mapToLong(Loan::tenureMonths).sum()).sum();
            b.totalOutstandingLoans = employees.parallelStream()
                    .mapToLong(e -> e.loans().stream().mapToLong(Loan::outstandingAmount).sum()).sum();
            b.totalSalaryRevisions  = employees.parallelStream().mapToLong(e -> e.salaryRevisions().size()).sum();
            b.totalBenefits         = employees.parallelStream().mapToLong(e -> e.benefits().size()).sum();
            b.totalPerformanceRecords = employees.parallelStream().mapToLong(e -> e.performance().size()).sum();

            LongSummaryStatistics salary = employees.parallelStream()
                    .mapToLong(Employee::annualSalary).summaryStatistics();
            b.totalAnnualPayroll = salary.getSum();
            b.avgAnnualSalary    = Math.round(salary.getAverage());
            b.minAnnualSalary    = salary.getMin();
            b.maxAnnualSalary    = salary.getMax();

            b.headcountByStatus = employees.parallelStream().collect(
                    Collectors.groupingBy(Employee::status,
                            () -> new EnumMap<>(EmploymentStatus.class),
                            Collectors.counting()));

            b.payrollByBusinessUnit = employees.parallelStream().collect(
                    Collectors.groupingBy(Employee::businessUnitId,
                            Collectors.summingLong(Employee::annualSalary)));

            return new DatasetStats(b);
        }

        /** Pretty-print the stats to stdout. */
        public void print(Organization org) {
            System.out.println();
            System.out.println("============================================================");
            System.out.println("  DATASET STATISTICS");
            System.out.println("============================================================");
            System.out.printf(Locale.US, "Employees                : %,d%n",    totalEmployees);
            System.out.printf(Locale.US, "Attendance records       : %,d (present: %,d)%n",
                    totalAttendanceDays, totalPresentDays);
            System.out.printf(Locale.US, "Leave records            : %,d%n",    totalLeaveRecords);
            System.out.printf(Locale.US, "Bonus records            : %,d (total %s)%n",
                    totalBonusRecords, Numbers.formatMoney(totalBonusAmount));
            System.out.printf(Locale.US, "Reimbursements           : %,d%n",    totalReimbursements);
            System.out.printf(Locale.US, "Loans                    : %,d (outstanding %s)%n",
                    totalLoans, Numbers.formatMoney(totalOutstandingLoans));
            System.out.printf(Locale.US, "Loan installments (lazy) : %,d%n",    totalLoanInstallments);
            System.out.printf(Locale.US, "Salary revisions         : %,d%n",    totalSalaryRevisions);
            System.out.printf(Locale.US, "Benefits                 : %,d%n",    totalBenefits);
            System.out.printf(Locale.US, "Performance records      : %,d%n",    totalPerformanceRecords);
            System.out.println("Total annual payroll     : " + Numbers.formatMoney(totalAnnualPayroll));
            System.out.println("Average / Min / Max      : "
                    + Numbers.formatMoney(avgAnnualSalary) + " / "
                    + Numbers.formatMoney(minAnnualSalary) + " / "
                    + Numbers.formatMoney(maxAnnualSalary));
            System.out.println();
            System.out.println("Headcount by status      : " + headcountByStatus);
            System.out.println("Top 5 BUs by payroll:");
            payrollByBusinessUnit.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                    .limit(5)
                    .forEach(en -> System.out.println("  "
                            + org.businessUnits.get(en.getKey() - 1).name()
                            + " : " + Numbers.formatMoney(en.getValue())));
        }

        // internal mutable builder
        private static final class Builder {
            long totalEmployees, totalAttendanceDays, totalPresentDays, totalLeaveRecords;
            long totalBonusRecords, totalBonusAmount, totalReimbursements, totalLoans;
            long totalLoanInstallments, totalOutstandingLoans, totalSalaryRevisions;
            long totalBenefits, totalPerformanceRecords, totalAnnualPayroll;
            long avgAnnualSalary, minAnnualSalary, maxAnnualSalary;
            Map<EmploymentStatus, Long> headcountByStatus;
            Map<Integer, Long> payrollByBusinessUnit;
        }
    }

    // ============================================================
    // EXCEPTION
    // ============================================================

    public static final class PayrollGenerationException extends RuntimeException {
        public PayrollGenerationException(String msg)            { super(msg); }
        public PayrollGenerationException(String msg, Throwable c) { super(msg, c); }
    }

    // ============================================================
    // CONFIGURATION  (public for introspection)
    // ============================================================

    public record GeneratorConfig(int employeeCount, long seed, int threads, LocalDate referenceDate) {
        public GeneratorConfig {
            if (employeeCount < 1) throw new IllegalArgumentException("employees must be >= 1");
            if (threads       < 1) throw new IllegalArgumentException("threads must be >= 1");
        }

        /** Parse CLI args: --employees= --seed= --threads= --date= */
        public static GeneratorConfig parse(String[] args) {
            int       count   = 1_000_000;
            long      seed    = 42L;
            int       threads = Runtime.getRuntime().availableProcessors();
            LocalDate date    = LocalDate.now();
            for (String arg : args) {
                if      (arg.startsWith("--employees=")) count   = Integer.parseInt(val(arg));
                else if (arg.startsWith("--seed="))      seed    = Long.parseLong(val(arg));
                else if (arg.startsWith("--threads="))   threads = Integer.parseInt(val(arg));
                else if (arg.startsWith("--date="))      date    = LocalDate.parse(val(arg));
                else throw new IllegalArgumentException("Unknown argument: " + arg);
            }
            return new GeneratorConfig(count, seed, threads, date);
        }
        private static String val(String arg) { return arg.substring(arg.indexOf('=') + 1); }
    }

    // ============================================================
    // INTERNAL LIMITS
    // ============================================================

    static final class Limits {
        static final int BUSINESS_UNIT_COUNT = 20;
        static final int DEPARTMENT_COUNT    = 200;
        static final int TEAM_COUNT          = 10_000;
        static final int JOB_GRADE_COUNT     = 15;

        static final int MIN_ATTENDANCE_DAYS = 220;
        static final int MAX_ATTENDANCE_DAYS = 260;

        static final int MAX_LEAVE_RECORDS    = 8;
        static final int MAX_BONUSES          = 4;
        static final int MAX_REIMBURSEMENTS   = 8;
        static final int MAX_LOANS            = 2;
        static final int MAX_SALARY_REVISIONS = 5;

        static final int CHUNK_SIZE = 10_000;
        private Limits() {}
    }

    // ============================================================
    // ENUMS  (cached values() arrays)
    // ============================================================

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, INTERN;
        static final EmploymentType[] VALUES = values();
    }

    public enum EmploymentStatus {
        ACTIVE, ON_LEAVE, RESIGNED, TERMINATED, SUSPENDED;
        static final EmploymentStatus[] VALUES = values();
    }

    public enum WorkMode {
        OFFICE, HYBRID, REMOTE;
        static final WorkMode[] VALUES = values();
    }

    public enum LeaveType {
        CASUAL, SICK, EARNED, PRIVILEGE, UNPAID, MATERNITY, PATERNITY, COMP_OFF;
        static final LeaveType[] VALUES = values();
    }

    public enum BonusType {
        PERFORMANCE("Annual performance bonus"),
        PROJECT("Project delivery bonus"),
        RETENTION("Retention bonus"),
        REFERRAL("Employee referral bonus"),
        SPOT("Spot recognition award"),
        FESTIVAL("Festival bonus"),
        JOINING("Joining bonus");

        static final BonusType[] VALUES = values();
        public final String reason;
        BonusType(String reason) { this.reason = reason; }
    }

    public enum ReimbursementType {
        TRAVEL, HOTEL, FOOD, INTERNET, MOBILE, MEDICAL, LEARNING, EQUIPMENT, RELOCATION;
        static final ReimbursementType[] VALUES = values();
    }

    public enum LoanType {
        PERSONAL, SALARY_ADVANCE, EMERGENCY, EQUIPMENT, RELOCATION;
        static final LoanType[] VALUES = values();
    }

    public enum AttendanceStatus {
        PRESENT, ABSENT, HALF_DAY, HOLIDAY, WEEK_OFF, LEAVE;
        static final AttendanceStatus[] VALUES = values();
    }

    public enum TaxRegime {
        OLD, NEW;
        static final TaxRegime[] VALUES = values();
    }

    public enum BenefitType {
        HEALTH_INSURANCE ("Health Insurance",   30_000, false),
        LIFE_INSURANCE   ("Life Insurance",     10_000, false),
        COMPANY_CAR      ("Company Car",       120_000, true),
        LEARNING_ALLOWANCE("Learning Allowance",25_000, false),
        INTERNET_ALLOWANCE("Internet Allowance",18_000, false);

        public final String label;
        public final long   annualValueRupees;
        public final boolean taxable;
        BenefitType(String label, long annualValueRupees, boolean taxable) {
            this.label             = label;
            this.annualValueRupees = annualValueRupees;
            this.taxable           = taxable;
        }
    }

    // ============================================================
    // ORGANIZATION MODELS  (public records)
    // ============================================================

    public record BusinessUnit(int id, String name) {}
    public record Department (int id, String name, int businessUnitId) {}
    public record Team       (int id, String name, int departmentId) {}
    public record Location   (int id, String city, String state) {}

    /** Salary bounds in paise. */
    public record JobGrade(int id, String code, String description,
                           long minimumSalary, long maximumSalary) {}

    // ============================================================
    // EMPLOYEE SUB-MODELS  (public records)
    // ============================================================

    public record SalaryComponent(String name, long annualAmount, boolean taxable) {}

    public record SalaryRevision(LocalDate effectiveDate, long previousSalary,
                                 long newSalary, int increaseBps) {
        public double percentageIncrease() { return increaseBps / 100.0; }
    }

    public record CompensationProfile(
            long basic, long hra, long specialAllowance, long conveyance,
            long medicalAllowance, long employerPf, long gratuity, long annualBonusTarget) {

        public long grossMonthly() {
            return basic + hra + specialAllowance + conveyance + medicalAllowance;
        }

        public List<SalaryComponent> components() {
            return List.of(
                    new SalaryComponent("BASIC",            basic * 12, true),
                    new SalaryComponent("HRA",              hra   * 12, true),
                    new SalaryComponent("SPECIAL_ALLOWANCE",specialAllowance * 12, true),
                    new SalaryComponent("CONVEYANCE",       conveyance * 12, false),
                    new SalaryComponent("MEDICAL",          medicalAllowance * 12, false),
                    new SalaryComponent("EMPLOYER_PF",      employerPf * 12, false),
                    new SalaryComponent("GRATUITY",         gratuity   * 12, false),
                    new SalaryComponent("BONUS_TARGET",     annualBonusTarget, true));
        }
    }

    public record AttendanceRecord(LocalDate date, AttendanceStatus status,
                                   double workingHours, double overtimeHours) {}

    /**
     * Compact attendance storage: 2 bytes per day.
     * bits 0-2  = AttendanceStatus ordinal
     * bits 3-9  = working time in 5-minute units  (max ~10.6 h)
     * bits 10-15= overtime in 5-minute units       (max  ~5.3 h)
     */
    public static final class AttendanceLedger {
        public static final AttendanceLedger EMPTY = new AttendanceLedger(0, new short[0]);

        private final long    startEpochDay;
        private final short[] packed;

        AttendanceLedger(long startEpochDay, short[] packed) {
            this.startEpochDay = startEpochDay;
            this.packed        = packed;
        }

        static short pack(AttendanceStatus status, int workUnits, int overtimeUnits) {
            return (short) (status.ordinal() | (workUnits << 3) | (overtimeUnits << 10));
        }

        public int       size()      { return packed.length; }
        public LocalDate startDate() { return Dates.of(startEpochDay); }
        public LocalDate endDate()   { return Dates.of(startEpochDay + packed.length - 1L); }

        public AttendanceRecord get(int index) {
            int p       = packed[index] & 0xFFFF;
            AttendanceStatus status   = AttendanceStatus.VALUES[p & 0x7];
            double           working  = ((p >>> 3) & 0x7F) * 5 / 60.0;
            double           overtime = ((p >>> 10) & 0x3F) * 5 / 60.0;
            return new AttendanceRecord(Dates.of(startEpochDay + index), status,
                    Numbers.round2(working), Numbers.round2(overtime));
        }

        public java.util.stream.Stream<AttendanceRecord> stream() {
            return IntStream.range(0, packed.length).mapToObj(this::get);
        }

        public int count(AttendanceStatus status) {
            int n = 0;
            for (short s : packed) {
                if ((s & 0x7) == status.ordinal()) n++;
            }
            return n;
        }
    }

    public record LeaveRecord      (LeaveType type, LocalDate startDate, LocalDate endDate,
                                    int days, boolean approved) {}
    public record Bonus            (BonusType type, long amount, LocalDate date, String reason) {}
    public record Reimbursement    (ReimbursementType type, long amount, LocalDate date,
                                    boolean approved) {}
    public record LoanInstallment  (int installmentNumber, LocalDate dueDate, long principal,
                                    long interest, long totalAmount, boolean paid) {}

    public record Loan(LoanType type, long principal, int interestRateBps,
                       int tenureMonths, long monthlyEmi, long outstandingAmount,
                       boolean active, LocalDate disbursementDate, int paidInstallments) {

        public static long computeEmi(long principal, int rateBps, int tenureMonths) {
            double r = rateBps / 120_000.0;
            double f = Math.pow(1 + r, tenureMonths);
            return Math.round(principal * r * f / (f - 1));
        }

        private static long interestOn(long remaining, int rateBps) {
            return (remaining * rateBps + 60_000) / 120_000;
        }

        static long outstandingAfter(long principal, int rateBps, int tenure, long emi, int paid) {
            long remaining = principal;
            for (int i = 1; i <= paid && remaining > 0; i++) {
                long interest = interestOn(remaining, rateBps);
                long part     = (i == tenure) ? remaining
                        : Math.min(Math.max(emi - interest, 0), remaining);
                remaining -= part;
            }
            return remaining;
        }

        /** Amortise the full repayment schedule on demand. */
        public List<LoanInstallment> schedule() {
            List<LoanInstallment> list = new ArrayList<>(tenureMonths);
            long remaining = principal;
            for (int i = 1; i <= tenureMonths; i++) {
                long interest = interestOn(remaining, interestRateBps);
                long part     = (i == tenureMonths) ? remaining
                        : Math.min(Math.max(monthlyEmi - interest, 0), remaining);
                remaining -= part;
                list.add(new LoanInstallment(i, disbursementDate.plusMonths(i),
                        part, interest, part + interest, i <= paidInstallments));
            }
            return list;
        }
    }

    public record TaxProfile       (TaxRegime regime, long investmentDeclaration,
                                    long insuranceDeduction, long housingDeduction,
                                    long otherDeduction) {}
    public record Benefit          (BenefitType type, long annualValue, boolean taxable) {}
    public record PerformanceRecord(int year, double rating, String performanceBand,
                                    boolean promotionEligible) {}

    // ============================================================
    // EMPLOYEE AGGREGATE  (public record — the main value type)
    // ============================================================

    public record Employee(
            long            id,
            String          employeeCode,
            String          firstName,
            String          lastName,
            String          email,
            LocalDate       dateOfBirth,
            LocalDate       joiningDate,
            LocalDate       terminationDate,
            EmploymentType  employmentType,
            EmploymentStatus status,
            WorkMode        workMode,
            int             businessUnitId,
            int             departmentId,
            int             teamId,
            int             locationId,
            int             jobGradeId,
            String          designation,
            Long            managerId,
            long            annualSalary,
            CompensationProfile compensationProfile,
            AttendanceLedger    attendance,
            List<LeaveRecord>   leaves,
            List<Bonus>         bonuses,
            List<Reimbursement> reimbursements,
            List<Loan>          loans,
            List<SalaryRevision>    salaryRevisions,
            List<Benefit>           benefits,
            TaxProfile              taxProfile,
            List<PerformanceRecord> performance) {

        @Override
        public String toString() {
            return "Employee[" + employeeCode + ", " + firstName + " " + lastName
                    + ", " + designation + ", " + status + "/" + employmentType + "/" + workMode
                    + ", BU=" + businessUnitId + ", Dept=" + departmentId + ", Team=" + teamId
                    + ", Mgr=" + managerId + ", salary=" + Numbers.formatMoney(annualSalary)
                    + ", attendanceDays=" + attendance.size() + ", leaves=" + leaves.size()
                    + ", bonuses=" + bonuses.size() + ", reimbursements=" + reimbursements.size()
                    + ", loans=" + loans.size() + ", revisions=" + salaryRevisions.size()
                    + ", benefits=" + benefits.size() + ", reviews=" + performance.size() + "]";
        }
    }

    // ============================================================
    // ORGANIZATION  (public — needed for BU / department lookups)
    // ============================================================

    public static final class Organization {
        public final List<BusinessUnit> businessUnits;
        public final List<Department>  departments;
        public final List<Team>        teams;
        public final List<Location>    locations;
        public final List<JobGrade>    jobGrades;
        private final int[][]          teamIdsByDepartment;

        private Organization(List<BusinessUnit> bu, List<Department> dept, List<Team> teams,
                             List<Location> locations, List<JobGrade> grades, int[][] teamIdsByDepartment) {
            this.businessUnits        = bu;
            this.departments          = dept;
            this.teams                = teams;
            this.locations            = locations;
            this.jobGrades            = grades;
            this.teamIdsByDepartment  = teamIdsByDepartment;
        }

        public Department department(int id) { return departments.get(id - 1); }
        public Team       team      (int id) { return teams      .get(id - 1); }
        public JobGrade   grade     (int id) { return jobGrades  .get(id - 1); }
        public Location   location  (int id) { return locations  .get(id - 1); }
        public Location   remoteLocation()   { return locations.get(locations.size() - 1); }
        int[]             teamIdsOf (int departmentId) { return teamIdsByDepartment[departmentId - 1]; }

        static Organization build(long seed) {
            Rng rng = new Rng(Seed.mix(seed, -1));

            String[] buNames = {
                    "Banking","Insurance","Healthcare","Manufacturing","Retail","Telecom","Energy",
                    "Public Sector","Technology","Financial Services","Automotive","Aerospace",
                    "Pharmaceuticals","Media","Travel","Logistics","Consumer Products",
                    "Professional Services","Cloud Services","Digital Engineering"};
            List<BusinessUnit> bus = new ArrayList<>();
            for (int i = 0; i < Limits.BUSINESS_UNIT_COUNT; i++) {
                bus.add(new BusinessUnit(i + 1, buNames[i % buNames.length]));
            }

            String[] deptNames = {
                    "Engineering","Information Technology","Finance","Human Resources","Operations",
                    "Sales","Marketing","Legal","Procurement","Quality Assurance","Cyber Security",
                    "Data Analytics","Research","Customer Support","Architecture","Project Management",
                    "Consulting","Infrastructure","DevOps","Testing"};
            List<Department> depts = new ArrayList<>();
            for (int i = 0; i < Limits.DEPARTMENT_COUNT; i++) {
                depts.add(new Department(i + 1,
                        deptNames[i % deptNames.length] + " " + (i / deptNames.length + 1),
                        rng.between(1, Limits.BUSINESS_UNIT_COUNT)));
            }

            List<Team> teams = new ArrayList<>();
            int[] teamCounts = new int[Limits.DEPARTMENT_COUNT];
            for (int i = 0; i < Limits.TEAM_COUNT; i++) {
                int deptId = i < Limits.DEPARTMENT_COUNT ? i + 1 : rng.between(1, Limits.DEPARTMENT_COUNT);
                teams.add(new Team(i + 1, "Team-" + Numbers.zeroPad(i + 1, 5), deptId));
                teamCounts[deptId - 1]++;
            }
            int[][] byDept = new int[Limits.DEPARTMENT_COUNT][];
            for (int d = 0; d < byDept.length; d++) byDept[d] = new int[teamCounts[d]];
            int[] fill = new int[Limits.DEPARTMENT_COUNT];
            for (Team t : teams) byDept[t.departmentId() - 1][fill[t.departmentId() - 1]++] = t.id();

            String[][] loc = {
                    {"Bangalore","Karnataka"},{"Hyderabad","Telangana"},{"Pune","Maharashtra"},
                    {"Mumbai","Maharashtra"},{"Delhi","Delhi"},{"Gurgaon","Haryana"},
                    {"Noida","Uttar Pradesh"},{"Chennai","Tamil Nadu"},{"Kolkata","West Bengal"},
                    {"Ahmedabad","Gujarat"},{"Jaipur","Rajasthan"},{"Kochi","Kerala"},
                    {"Indore","Madhya Pradesh"},{"Chandigarh","Punjab"},{"Bhubaneswar","Odisha"},
                    {"Coimbatore","Tamil Nadu"},{"Mysore","Karnataka"},{"Nagpur","Maharashtra"},
                    {"Surat","Gujarat"},{"Vadodara","Gujarat"},{"Lucknow","Uttar Pradesh"},
                    {"Patna","Bihar"},{"Visakhapatnam","Andhra Pradesh"},
                    {"Thiruvananthapuram","Kerala"},{"Remote","Remote"}}; // Remote MUST stay last
            List<Location> locations = new ArrayList<>();
            for (int i = 0; i < loc.length; i++) {
                locations.add(new Location(i + 1, loc[i][0], loc[i][1]));
            }

            List<JobGrade> grades = new ArrayList<>();
            for (int i = 1; i <= Limits.JOB_GRADE_COUNT; i++) {
                long min = Numbers.rupees(300_000L + i * 200_000L);
                long max = min + Numbers.rupees(700_000L);
                grades.add(new JobGrade(i, "G" + Numbers.zeroPad(i, 2), "Grade " + i, min, max));
            }

            return new Organization(
                    List.copyOf(bus), List.copyOf(depts), List.copyOf(teams),
                    List.copyOf(locations), List.copyOf(grades), byDept);
        }
    }

    // ============================================================
    // UTILITIES  (public for downstream consumers)
    // ============================================================

    public static final class Numbers {
        private Numbers() {}

        public static double round2(double v)        { return Math.round(v * 100) / 100.0; }
        public static long   rupees(long rupees)     { return rupees * 100; }

        /** value * bps / 10_000, rounded half-up. */
        public static long mulBps(long value, int bps) {
            return (value * bps + 5_000) / 10_000;
        }

        public static String formatMoney(long paise) {
            long abs = Math.abs(paise);
            return (paise < 0 ? "-" : "") + "INR "
                    + String.format(Locale.US, "%,d.%02d", abs / 100, abs % 100);
        }

        public static String zeroPad(long value, int width) {
            String s = Long.toString(value);
            return s.length() >= width ? s : "0".repeat(width - s.length()) + s;
        }
    }

    // ============================================================
    // INTERNAL HELPERS
    // ============================================================

    /** Flyweight cache of LocalDate (millions of records share ~40 k dates). */
    static final class Dates {
        private static final long       MIN   = LocalDate.of(1950, 1, 1).toEpochDay();
        private static final LocalDate[] CACHE =
                new LocalDate[(int) (LocalDate.of(2060, 12, 31).toEpochDay() - MIN + 1)];
        static {
            for (int i = 0; i < CACHE.length; i++) CACHE[i] = LocalDate.ofEpochDay(MIN + i);
        }
        private Dates() {}

        static LocalDate of(long epochDay) {
            long idx = epochDay - MIN;
            return (idx >= 0 && idx < CACHE.length)
                    ? CACHE[(int) idx] : LocalDate.ofEpochDay(epochDay);
        }
    }

    static final class Seed {
        private Seed() {}
        /** SplitMix64-style per-(seed, id) independent stream. */
        static long mix(long seed, long id) {
            long z = seed + id * 0x9E3779B97F4A7C15L;
            z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
            z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
            return z ^ (z >>> 31);
        }
    }

    static final class Rng {
        private final SplittableRandom random;
        Rng(long seed) { this.random = new SplittableRandom(seed); }

        int     nextInt(int bound)            { return random.nextInt(bound); }
        int     between(int min, int max)     { return random.nextInt(min, max + 1); }
        long    betweenLong(long min, long max){ return random.nextLong(min, max + 1); }
        double  nextDouble()                  { return random.nextDouble(); }
        boolean nextBoolean()                 { return random.nextBoolean(); }
        boolean chance(int percent)           { return random.nextInt(100) < percent; }
        <T> T   pick(T[] array)               { return array[random.nextInt(array.length)]; }
        <T> T   pick(List<T> list)            { return list.get(random.nextInt(list.size())); }
    }

    // ============================================================
    // EMPLOYEE FACTORY  (package-private)
    // ============================================================

    static final class EmployeeFactory {

        private static final String[] FIRST_NAMES = {
                "Aarav","Vivaan","Aditya","Arjun","Rohan","Rahul","Karan","Vikram","Amit","Raj",
                "Neha","Priya","Ananya","Sneha","Kavya","Pooja","Meera","Isha","Riya","Aditi",
                "Nikhil","Varun","Sakshi","Simran","Akash"};

        private static final String[] LAST_NAMES = {
                "Sharma","Verma","Patel","Mehta","Shah","Gupta","Singh","Kumar","Joshi","Malhotra",
                "Reddy","Iyer","Nair","Kapoor","Bansal","Mishra","Chopra","Agarwal","Saxena","Choudhary"};

        private static final String[] JUNIOR = {"Associate","Junior Analyst","Software Engineer"};
        private static final String[] MIDDLE = {"Software Engineer","Senior Software Engineer",
                "Business Analyst","Consultant"};
        private static final String[] SENIOR = {"Technical Lead","Architect","Project Manager",
                "Senior Manager","Principal Consultant"};

        private static final long DOB_MIN  = LocalDate.of(1960,  1, 1).toEpochDay();
        private static final long DOB_MAX  = LocalDate.of(2003, 12,31).toEpochDay();
        private static final long JOIN_MIN = LocalDate.of(2010,  1, 1).toEpochDay();

        private final GeneratorConfig cfg;
        private final Organization    org;
        private final LocalDate       referenceDate;
        private final long            today;
        private final int             executives;
        private final int             managers;

        EmployeeFactory(GeneratorConfig cfg, Organization org) {
            this.cfg           = cfg;
            this.org           = org;
            this.referenceDate = cfg.referenceDate();
            this.today         = referenceDate.toEpochDay();
            this.executives    = Math.max(1, cfg.employeeCount() / 100);
            this.managers      = Math.max(executives, cfg.employeeCount() / 10);
        }

        private record Window(long start, long end) {
            boolean isEmpty()  { return end < start; }
            long    length()   { return end - start + 1; }
        }

        Employee create(int id) {
            Rng rng = new Rng(Seed.mix(cfg.seed(), id));

            String    firstName  = rng.pick(FIRST_NAMES);
            String    lastName   = rng.pick(LAST_NAMES);
            String    code       = "EMP" + Numbers.zeroPad(id, 7);
            String    email      = firstName.toLowerCase(Locale.ROOT) + "."
                    + lastName.toLowerCase(Locale.ROOT) + id + "@enterprise.com";

            LocalDate dob         = Dates.of(rng.betweenLong(DOB_MIN, DOB_MAX));
            long      earliestJoin = Math.min(Math.max(JOIN_MIN, dob.plusYears(21).toEpochDay()), today);
            long      joinDay      = rng.betweenLong(earliestJoin, today);
            LocalDate joiningDate  = Dates.of(joinDay);

            EmploymentType   employmentType = rng.pick(EmploymentType.VALUES);
            EmploymentStatus status         = randomStatus(rng);
            WorkMode         workMode       = rng.pick(WorkMode.VALUES);

            LocalDate terminationDate = null;
            if (status == EmploymentStatus.RESIGNED || status == EmploymentStatus.TERMINATED) {
                terminationDate = Dates.of(rng.betweenLong(Math.max(joinDay, today - 1_000), today));
            }
            LocalDate endDate = terminationDate != null ? terminationDate : referenceDate;
            Window    window  = new Window(Math.max(joinDay, today - 364), endDate.toEpochDay());

            Department department      = org.department(rng.between(1, Limits.DEPARTMENT_COUNT));
            int[]      candidateTeams  = org.teamIdsOf(department.id());
            Team       team            = org.team(candidateTeams[rng.nextInt(candidateTeams.length)]);
            Location   location        = workMode == WorkMode.REMOTE
                    ? org.remoteLocation()
                    : org.location(rng.between(1, org.locations.size() - 1));
            JobGrade   grade           = org.grade(rng.between(1, Limits.JOB_GRADE_COUNT));

            long annualSalary = Numbers.rupees(
                    rng.betweenLong(grade.minimumSalary() / 100, grade.maximumSalary() / 100));

            return new Employee(
                    id, code, firstName, lastName, email,
                    dob, joiningDate, terminationDate,
                    employmentType, status, workMode,
                    department.businessUnitId(), department.id(), team.id(), location.id(), grade.id(),
                    designation(rng, grade.id()),
                    managerOf(id, rng),
                    annualSalary,
                    compensation(annualSalary),
                    attendance(rng, window),
                    leaves(rng, window),
                    bonuses(rng, annualSalary, window),
                    reimbursements(rng, window),
                    loans(rng, joiningDate, endDate),
                    salaryRevisions(rng, annualSalary, joiningDate, endDate),
                    benefits(rng, annualSalary),
                    taxProfile(rng, annualSalary),
                    performance(rng, joiningDate.getYear()));
        }

        // ---- helpers ----

        private static EmploymentStatus randomStatus(Rng rng) {
            int v = rng.nextInt(100);
            if (v < 90) return EmploymentStatus.ACTIVE;
            if (v < 94) return EmploymentStatus.ON_LEAVE;
            if (v < 97) return EmploymentStatus.RESIGNED;
            if (v < 99) return EmploymentStatus.TERMINATED;
            return EmploymentStatus.SUSPENDED;
        }

        private static String designation(Rng rng, int grade) {
            if (grade <= 4) return rng.pick(JUNIOR);
            if (grade <= 9) return rng.pick(MIDDLE);
            return rng.pick(SENIOR);
        }

        private Long managerOf(int id, Rng rng) {
            if (id <= executives) return null;
            if (id <= managers)   return (long) rng.between(1, executives);
            int low = managers > executives ? executives + 1 : 1;
            return (long) rng.between(low, managers);
        }

        private static CompensationProfile compensation(long annualSalary) {
            long monthly = Numbers.mulBps(annualSalary, 10_000) / 12;
            long basic   = Numbers.mulBps(monthly, 4_000);
            return new CompensationProfile(
                    basic,
                    Numbers.mulBps(basic, 5_000),
                    Numbers.mulBps(monthly, 2_000),
                    Numbers.rupees(3_000),
                    Numbers.rupees(2_000),
                    Numbers.mulBps(basic, 1_200),
                    Numbers.mulBps(basic, 481),
                    Numbers.mulBps(annualSalary, 1_000));
        }

        private AttendanceLedger attendance(Rng rng, Window w) {
            if (w.isEmpty()) return AttendanceLedger.EMPTY;
            int     days   = (int) Math.min(w.length(),
                    rng.between(Limits.MIN_ATTENDANCE_DAYS, Limits.MAX_ATTENDANCE_DAYS));
            short[] packed = new short[days];
            for (int i = 0; i < days; i++) {
                long             epochDay = w.start() + i;
                int              dow      = (int) Math.floorMod(epochDay + 3, 7L);
                AttendanceStatus status;
                if (dow >= 5) {
                    status = AttendanceStatus.WEEK_OFF;
                } else {
                    int roll = rng.nextInt(100);
                    if      (roll < 86) status = AttendanceStatus.PRESENT;
                    else if (roll < 91) status = AttendanceStatus.HALF_DAY;
                    else if (roll < 96) status = AttendanceStatus.LEAVE;
                    else if (roll < 98) status = AttendanceStatus.HOLIDAY;
                    else                status = AttendanceStatus.ABSENT;
                }
                int work = 0, overtime = 0;
                if      (status == AttendanceStatus.PRESENT)  { work = 84 + rng.nextInt(25); overtime = rng.nextInt(37); }
                else if (status == AttendanceStatus.HALF_DAY) { work = 48; }
                packed[i] = AttendanceLedger.pack(status, work, overtime);
            }
            return new AttendanceLedger(w.start(), packed);
        }

        private List<LeaveRecord> leaves(Rng rng, Window w) {
            int count = rng.between(0, Limits.MAX_LEAVE_RECORDS);
            if (count == 0 || w.isEmpty()) return List.of();
            LeaveRecord[] result = new LeaveRecord[count];
            for (int i = 0; i < count; i++) {
                long start = w.start() + rng.betweenLong(0, w.length() - 1);
                long end   = Math.min(start + rng.between(1, 7) - 1, w.end());
                result[i]  = new LeaveRecord(rng.pick(LeaveType.VALUES),
                        Dates.of(start), Dates.of(end), (int)(end - start + 1), rng.chance(90));
            }
            return List.of(result);
        }

        private List<Bonus> bonuses(Rng rng, long annualSalary, Window w) {
            int count = rng.between(0, Limits.MAX_BONUSES);
            if (count == 0 || w.isEmpty()) return List.of();
            Bonus[] result = new Bonus[count];
            for (int i = 0; i < count; i++) {
                BonusType type   = rng.pick(BonusType.VALUES);
                long      amount = Numbers.mulBps(annualSalary, rng.between(200, 1_700));
                result[i] = new Bonus(type, amount,
                        Dates.of(w.start() + rng.betweenLong(0, w.length() - 1)), type.reason);
            }
            return List.of(result);
        }

        private List<Reimbursement> reimbursements(Rng rng, Window w) {
            int count = rng.between(0, Limits.MAX_REIMBURSEMENTS);
            if (count == 0 || w.isEmpty()) return List.of();
            Reimbursement[] result = new Reimbursement[count];
            for (int i = 0; i < count; i++) {
                result[i] = new Reimbursement(rng.pick(ReimbursementType.VALUES),
                        rng.betweenLong(Numbers.rupees(500), Numbers.rupees(50_500)),
                        Dates.of(w.start() + rng.betweenLong(0, w.length() - 1)), rng.chance(85));
            }
            return List.of(result);
        }

        private List<Loan> loans(Rng rng, LocalDate joining, LocalDate end) {
            int months = (int) ChronoUnit.MONTHS.between(joining, end);
            int count  = rng.between(0, Limits.MAX_LOANS);
            if (count == 0 || months < 1) return List.of();
            Loan[] result = new Loan[count];
            for (int i = 0; i < count; i++) {
                long principal = Numbers.rupees(rng.betweenLong(50_000, 500_000));
                int  rateBps   = rng.between(800, 1_600);
                int  tenure    = rng.between(12, 60);
                long emi       = Loan.computeEmi(principal, rateBps, tenure);
                int  maxPaid   = Math.min(months, tenure);
                boolean active = rng.nextBoolean() || maxPaid < tenure;
                int  paid      = active ? rng.between(0, Math.min(maxPaid, tenure - 1)) : tenure;
                long outstanding = active
                        ? Loan.outstandingAfter(principal, rateBps, tenure, emi, paid) : 0;
                result[i] = new Loan(rng.pick(LoanType.VALUES), principal, rateBps, tenure, emi,
                        outstanding, active, end.minusMonths(paid), paid);
            }
            return List.of(result);
        }

        private List<SalaryRevision> salaryRevisions(Rng rng, long currentSalary,
                                                     LocalDate joining, LocalDate end) {
            int years = (int) ChronoUnit.YEARS.between(joining, end);
            int count = Math.min(rng.between(0, Limits.MAX_SALARY_REVISIONS), years);
            if (count <= 0) return List.of();
            SalaryRevision[] result = new SalaryRevision[count];
            long salary = currentSalary;
            for (int k = count; k >= 1; k--) {
                int  bps      = rng.between(500, 2_000);
                long divisor  = 10_000L + bps;
                long previous = (salary * 10_000L + divisor / 2) / divisor;
                result[k - 1] = new SalaryRevision(
                        Dates.of(joining.plusYears(k).toEpochDay()), previous, salary, bps);
                salary = previous;
            }
            return List.of(result);
        }

        private static List<Benefit> benefits(Rng rng, long annualSalary) {
            List<Benefit> list = new ArrayList<>(5);
            add(list, BenefitType.HEALTH_INSURANCE);
            add(list, BenefitType.LIFE_INSURANCE);
            if (annualSalary > Numbers.rupees(1_000_000)) add(list, BenefitType.COMPANY_CAR);
            if (rng.nextBoolean())  add(list, BenefitType.LEARNING_ALLOWANCE);
            if (rng.nextBoolean())  add(list, BenefitType.INTERNET_ALLOWANCE);
            return List.copyOf(list);
        }
        private static void add(List<Benefit> list, BenefitType t) {
            list.add(new Benefit(t, Numbers.rupees(t.annualValueRupees), t.taxable));
        }

        private static TaxProfile taxProfile(Rng rng, long annualSalary) {
            return new TaxProfile(
                    rng.pick(TaxRegime.VALUES),
                    Numbers.mulBps(annualSalary, rng.between(0, 2_000)),
                    Numbers.rupees(rng.betweenLong(10_000, 50_000)),
                    Numbers.rupees(rng.betweenLong(20_000, 100_000)),
                    Numbers.rupees(rng.betweenLong(0, 30_000)));
        }

        private List<PerformanceRecord> performance(Rng rng, int joiningYear) {
            int currentYear = referenceDate.getYear();
            int first       = Math.max(currentYear - 3, joiningYear);
            if (first > currentYear) return List.of();
            PerformanceRecord[] result = new PerformanceRecord[currentYear - first + 1];
            for (int year = first; year <= currentYear; year++) {
                double rating  = Numbers.round2(1 + rng.nextDouble() * 4);
                result[year - first] = new PerformanceRecord(year, rating, band(rating), rating >= 4);
            }
            return List.of(result);
        }

        private static String band(double rating) {
            if (rating >= 4.5) return "OUTSTANDING";
            if (rating >= 4.0) return "EXCEEDS_EXPECTATIONS";
            if (rating >= 3.0) return "MEETS_EXPECTATIONS";
            if (rating >= 2.0) return "NEEDS_IMPROVEMENT";
            return "UNSATISFACTORY";
        }
    }

    // ============================================================
    // PARALLEL GENERATION ENGINE
    // ============================================================

    static List<Employee> generateEmployees(GeneratorConfig cfg,
                                            EmployeeFactory factory,
                                            boolean printProgress) throws Exception {
        int        total   = cfg.employeeCount();
        Employee[] result  = new Employee[total];
        AtomicInteger done = new AtomicInteger();

        ExecutorService pool = Executors.newFixedThreadPool(cfg.threads(), r -> {
            Thread t = new Thread(r, "payroll-gen");
            t.setDaemon(true);
            return t;
        });

        try {
            List<Future<?>> futures = new ArrayList<>();
            for (int from = 0; from < total; from += Limits.CHUNK_SIZE) {
                final int s = from;
                final int e = Math.min(total, from + Limits.CHUNK_SIZE);
                futures.add(pool.submit(() -> {
                    for (int i = s; i < e; i++) result[i] = factory.create(i + 1);
                    int len = e - s;
                    int d   = done.addAndGet(len);
                    if (printProgress && (d - len) / 100_000 != d / 100_000) {
                        System.out.printf(Locale.US, "  generated %,d / %,d%n", d, total);
                    }
                }));
            }
            for (Future<?> f : futures) f.get();   // propagates exceptions + establishes happens-before
        } finally {
            pool.shutdown();
        }
        return Collections.unmodifiableList(Arrays.asList(result));
    }

    // ============================================================
    // VALIDATION ENGINE  (public — usable independently)
    // ============================================================

    /**
     * Run all integrity checks.
     *
     * @return empty map if clean; otherwise rule-name → violation-count
     */
    public static Map<String, Long> validate(List<Employee> employees,
                                             Organization org,
                                             GeneratorConfig cfg) {
        ConcurrentHashMap<String, LongAdder> violations = new ConcurrentHashMap<>();
        LocalDate ref = cfg.referenceDate();

        IntStream.range(0, employees.size()).parallel().forEach(i -> {
            Employee e = employees.get(i);
            check(e.id() == i + 1L, "id not sequential", violations);
            check(!e.joiningDate().isBefore(e.dateOfBirth().plusYears(21)), "joined before age 21", violations);
            check(!e.joiningDate().isAfter(ref), "joining date in future", violations);

            boolean left = e.status() == EmploymentStatus.RESIGNED
                    || e.status() == EmploymentStatus.TERMINATED;
            check(left == (e.terminationDate() != null), "termination date/status mismatch", violations);
            if (e.terminationDate() != null) {
                check(!e.terminationDate().isBefore(e.joiningDate()), "terminated before joining", violations);
            }
            LocalDate end = e.terminationDate() != null ? e.terminationDate() : ref;

            JobGrade grade = org.grade(e.jobGradeId());
            check(e.annualSalary() >= grade.minimumSalary()
                    && e.annualSalary() <= grade.maximumSalary(), "salary outside grade band", violations);

            check(org.department(e.departmentId()).businessUnitId() == e.businessUnitId(),
                    "department/business unit mismatch", violations);
            check(org.team(e.teamId()).departmentId() == e.departmentId(),
                    "team/department mismatch", violations);

            if (e.managerId() != null) {
                check(e.managerId() >= 1 && e.managerId() < e.id(),
                        "invalid manager reference", violations);
            }

            if (e.attendance().size() > 0) {
                check(!e.attendance().startDate().isBefore(e.joiningDate())
                                && !e.attendance().endDate().isAfter(end),
                        "attendance outside employment", violations);
            }

            for (Loan loan : e.loans()) {
                check(loan.outstandingAmount() >= 0 && loan.outstandingAmount() <= loan.principal(),
                        "loan outstanding out of range", violations);
                check(loan.paidInstallments() <= loan.tenureMonths(), "loan paid > tenure", violations);
                check(loan.active() == (loan.outstandingAmount() > 0),
                        "loan active/outstanding mismatch", violations);
                check(!loan.disbursementDate().isBefore(e.joiningDate()),
                        "loan before joining", violations);
            }

            List<SalaryRevision> revs = e.salaryRevisions();
            if (!revs.isEmpty()) {
                check(revs.get(revs.size() - 1).newSalary() == e.annualSalary(),
                        "last revision != current salary", violations);
                for (int r = 0; r < revs.size(); r++) {
                    SalaryRevision rev = revs.get(r);
                    check(!rev.effectiveDate().isAfter(ref)
                                    && rev.effectiveDate().isAfter(e.joiningDate()),
                            "revision date invalid", violations);
                    if (r + 1 < revs.size()) {
                        check(rev.newSalary() == revs.get(r + 1).previousSalary(),
                                "revision chain broken", violations);
                    }
                }
            }
            for (PerformanceRecord p : e.performance()) {
                check(p.year() >= e.joiningDate().getYear(), "review before joining", violations);
            }
        });

        Map<String, Long> out = new TreeMap<>();
        violations.forEach((k, v) -> out.put(k, v.sum()));
        return out;
    }

    private static void check(boolean condition, String rule,
                              ConcurrentHashMap<String, LongAdder> sink) {
        if (!condition) sink.computeIfAbsent(rule, k -> new LongAdder()).increment();
    }

    // ============================================================
    // STANDALONE MAIN  (optional; shows library usage patterns)
    // ============================================================

    /**
     * Run as a standalone program:
     *   java -Xmx8g EnterprisePayrollLibrary
     *   java -Xmx8g EnterprisePayrollLibrary --employees=500000 --seed=7 --threads=8 --date=2026-10-02
     */
    public static void main(String[] args) throws Exception {
        // --- parse CLI (falls back to library defaults) ---
        GeneratorConfig cfg = GeneratorConfig.parse(args);

        System.out.println("============================================================");
        System.out.println("  EnterprisePayrollLibrary — demo");
        System.out.println("============================================================");
        System.out.printf(Locale.US, "employees=%,d  seed=%d  threads=%d  date=%s%n",
                cfg.employeeCount(), cfg.seed(), cfg.threads(), cfg.referenceDate());

        long t0 = System.nanoTime();

        // --- generate via the public library API ---
        PayrollDataset dataset = PayrollLibrary.builder()
                .employees(cfg.employeeCount())
                .seed(cfg.seed())
                .threads(cfg.threads())
                .referenceDate(cfg.referenceDate())
                .validate(true)
                .printProgress(true)
                .build();

        long genMs = (System.nanoTime() - t0) / 1_000_000;
        System.out.printf(Locale.US, "%nGenerated %,d employees in %,d ms (%,d emp/sec)%n",
                dataset.employees().size(), genMs,
                genMs == 0 ? 0 : dataset.employees().size() * 1000L / genMs);

        // --- stats ---
        dataset.stats().print(dataset.organization());

        // --- sample employees ---
        System.out.println("\n--- Sample employees ---");
        dataset.stream().limit(3).forEach(System.out::println);

        // --- single employee (unit-test style) ---
        System.out.println("\n--- generateOne(seed=42, id=999, today) ---");
        Employee one = PayrollLibrary.generateOne(42L, 999, LocalDate.now());
        System.out.println(one);

        // --- demo: active employees earning above 15 LPA ---
        long highEarners = dataset.parallelStream()
                .filter(e -> e.status() == EmploymentStatus.ACTIVE
                        && e.annualSalary() > Numbers.rupees(1_500_000))
                .count();
        System.out.printf(Locale.US, "%nActive employees earning > INR 15,00,000 pa: %,d%n", highEarners);

        // --- loan schedule demo ---
        dataset.stream()
                .filter(e -> !e.loans().isEmpty())
                .findFirst()
                .ifPresent(e -> {
                    Loan loan = e.loans().get(0);
                    System.out.println("\n--- Loan schedule sample for " + e.employeeCode() + " ---");
                    System.out.println("  " + loan.type() + "  principal=" + Numbers.formatMoney(loan.principal())
                            + "  EMI=" + Numbers.formatMoney(loan.monthlyEmi())
                            + "  paid=" + loan.paidInstallments() + "/" + loan.tenureMonths());
                    loan.schedule().stream().limit(3)
                            .forEach(inst -> System.out.println("    " + inst));
                });

        // --- memory ---
        System.gc();
        Runtime rt  = Runtime.getRuntime();
        long usedMb = (rt.totalMemory() - rt.freeMemory()) >> 20;
        System.out.printf(Locale.US, "%nHeap after GC: ~%,d MB (~%,d bytes/employee)%n",
                usedMb, (usedMb << 20) / dataset.employees().size());

        System.out.println("\ndataset is ready — use dataset.parallelStream() for analytics.");
    }
}

// ============================================================
// PUBLIC FAÇADE  (top-level class — the canonical entry point)
// ============================================================

/**
 * Static façade.  Import this in application code:
 *
 *   import static com.basics.basics.PayrollLibrary.*;
 *
 *   PayrollDataset ds = generate(500_000);
 *   ds.parallelStream()
 *     .filter(e -> e.status() == ACTIVE)
 *     .mapToLong(Employee::annualSalary)
 *     .summaryStatistics();
 */
final class PayrollLibrary {
    private PayrollLibrary() {}

    public static EnterprisePayrollLibrary.PayrollDataset generate() {
        return EnterprisePayrollLibrary.generate();
    }

    public static EnterprisePayrollLibrary.PayrollDataset generate(int employeeCount) {
        return EnterprisePayrollLibrary.generate(employeeCount);
    }

    public static EnterprisePayrollLibrary.Employee generateOne(
            long seed, int employeeId, LocalDate referenceDate) {
        return EnterprisePayrollLibrary.generateOne(seed, employeeId, referenceDate);
    }

    public static EnterprisePayrollLibrary.Builder builder() {
        return EnterprisePayrollLibrary.builder();
    }
}