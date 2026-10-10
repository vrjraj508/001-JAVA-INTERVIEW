package com.streams.stream01basics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// =============================================================
//  SINGLE-FILE ENTERPRISE STREAM API LIBRARY
//  Pure Java 21 · Zero dependencies · ~15,000+ in-memory objects
//  Run: javac Main.java && java com.customer.service.streams.Main
// =============================================================
public class Main {

    // =========================================================
    // ENUMS
    // =========================================================

    public enum CustomerType {
        INDIVIDUAL, SMALL_BUSINESS, ENTERPRISE
    }

    public enum EmployeeStatus {
        ACTIVE, ON_LEAVE, INACTIVE, TERMINATED
    }

    public enum TicketPriority {

        P1("Critical", 4),
        P2("High",     8),
        P3("Medium",  24),
        P4("Low",     72);

        private final String label;
        private final int    slaHours;

        TicketPriority(String label, int slaHours) {
            this.label    = label;
            this.slaHours = slaHours;
        }

        public String label()    { return label; }
        public int    slaHours() { return slaHours; }
    }

    public enum TicketStatus {
        OPEN, IN_PROGRESS, WAITING_CUSTOMER, ESCALATED, RESOLVED, CLOSED
    }

    public enum InteractionType {
        EMAIL, PHONE, CHAT, VIDEO_CALL, PORTAL
    }

    // =========================================================
    // MODEL RECORDS
    // =========================================================

    public record Department(long id, String code, String name) {}

    public record Team(long id, String code, String name, Department department) {}

    public record Employee(
            long           id,
            String         employeeCode,
            String         firstName,
            String         lastName,
            String         email,
            BigDecimal     salary,
            EmployeeStatus status,
            LocalDate      hireDate,
            Department     department,
            Team           team,
            Employee       manager
    ) {
        public String fullName() { return firstName + " " + lastName; }
    }

    public record Customer(
            long         id,
            String       customerCode,
            String       firstName,
            String       lastName,
            String       companyName,
            String       email,
            CustomerType type,
            String       status,
            String       city,
            String       country
    ) {
        public String displayName() {
            return (companyName != null && !companyName.isBlank())
                    ? companyName
                    : firstName + " " + lastName;
        }
    }

    public record CustomerAccount(
            long       id,
            String     accountNumber,
            Customer   customer,
            String     accountType,
            String     status,
            BigDecimal monthlyValue,
            LocalDate  openedDate
    ) {}

    public record Ticket(
            long          id,
            String        ticketNumber,
            Customer      customer,
            CustomerAccount account,
            String        category,
            TicketPriority priority,
            String        subject,
            TicketStatus  status,
            LocalDateTime createdAt,
            LocalDateTime firstResponseAt,
            LocalDateTime resolvedAt,
            int           reopenedCount
    ) {}

    public record TicketAssignment(
            long          id,
            Ticket        ticket,
            Employee      employee,
            Team          team,
            LocalDateTime assignedAt,
            LocalDateTime unassignedAt
    ) {}

    public record TicketComment(
            long          id,
            Ticket        ticket,
            Employee      employee,
            String        comment,
            boolean       internal,
            LocalDateTime createdAt
    ) {}

    public record TicketStatusHistory(
            long          id,
            Ticket        ticket,
            TicketStatus  oldStatus,
            TicketStatus  newStatus,
            Employee      employee,
            LocalDateTime changedAt
    ) {}

    public record CustomerInteraction(
            long          id,
            Customer      customer,
            Ticket        ticket,
            Employee      employee,
            InteractionType type,
            LocalDateTime interactionAt,
            int           durationSeconds,
            String        outcome
    ) {}

    // =========================================================
    // IN-MEMORY DATABASE
    // =========================================================

    public static class InMemoryDatabase {

        private final List<Department>          departments         = new ArrayList<>();
        private final List<Team>                teams               = new ArrayList<>();
        private final List<Employee>            employees           = new ArrayList<>();
        private final List<Customer>            customers           = new ArrayList<>();
        private final List<CustomerAccount>     customerAccounts    = new ArrayList<>();
        private final List<Ticket>              tickets             = new ArrayList<>();
        private final List<TicketAssignment>    ticketAssignments   = new ArrayList<>();
        private final List<TicketComment>       ticketComments      = new ArrayList<>();
        private final List<TicketStatusHistory> ticketStatusHistory = new ArrayList<>();
        private final List<CustomerInteraction> customerInteractions = new ArrayList<>();

        public List<Department>          departments()          { return departments; }
        public List<Team>                teams()                { return teams; }
        public List<Employee>            employees()            { return employees; }
        public List<Customer>            customers()            { return customers; }
        public List<CustomerAccount>     customerAccounts()     { return customerAccounts; }
        public List<Ticket>              tickets()              { return tickets; }
        public List<TicketAssignment>    ticketAssignments()    { return ticketAssignments; }
        public List<TicketComment>       ticketComments()       { return ticketComments; }
        public List<TicketStatusHistory> ticketStatusHistory()  { return ticketStatusHistory; }
        public List<CustomerInteraction> customerInteractions() { return customerInteractions; }
    }

    // =========================================================
    // DATA INITIALIZER  –  seeded Random = deterministic runs
    // =========================================================

    public static class DataInitializer {

        // ── tuneable knobs ────────────────────────────────────
        private static final int TEAMS_PER_DEPT     = 4;   // was 3  → 40  teams
        private static final int EMPLOYEES_PER_TEAM = 40;  // was 35 → 1,600 employees
        private static final int CUSTOMER_COUNT     = 1_500;
        private static final int TICKET_COUNT       = 2_000;
        // ──────────────────────────────────────────────────────

        private final Random random = new Random(42);

        // ── department master data (extended to 15) ───────────
        private static final String[][] DEPT_DATA = {
                {"CS",      "Customer Service"},
                {"TECH",    "Technical Support"},
                {"ESC",     "Escalations"},
                {"QA",      "Quality Assurance"},
                {"SALES",   "Sales"},
                {"OPS",     "Operations"},
                {"BILLING", "Billing"},
                {"RET",     "Retention"},
                {"TRAIN",   "Training"},
                {"VIP",     "VIP Support"},
                {"INFRA",   "Infrastructure"},
                {"SEC",     "Security"},
                {"DATA",    "Data & Analytics"},
                {"CLOUD",   "Cloud Services"},
                {"HR",      "Human Resources"}
        };

        // ── realistic name pools ──────────────────────────────
        private static final String[] FIRST_NAMES = {
                "Aarav","Vivaan","Aditya","Vihaan","Arjun","Sai","Reyansh","Ayaan",
                "Krishna","Ishaan","Rohan","Kabir","Shaurya","Atharv","Advik",
                "Priya","Ananya","Pooja","Neha","Kavya","Shruti","Riya","Divya",
                "Meera","Lakshmi","Anjali","Simran","Sneha","Preeti","Deepika",
                "Rahul","Amit","Suresh","Vikram","Rajesh","Nikhil","Karan","Manish",
                "Sanjay","Deepak","Harish","Ravi","Praveen","Sachin","Vishal"
        };

        private static final String[] LAST_NAMES = {
                "Sharma","Verma","Singh","Kumar","Gupta","Patel","Shah","Mehta",
                "Joshi","Nair","Iyer","Reddy","Rao","Pillai","Menon","Bhat",
                "Malhotra","Kapoor","Agarwal","Saxena","Srivastava","Pandey",
                "Tiwari","Mishra","Shukla","Dubey","Chauhan","Yadav","Bose","Das"
        };

        private static final String[] COMPANIES = {
                "Infosys","TCS","Wipro","HCL","Tech Mahindra","Cognizant",
                "Mphasis","Hexaware","Mindtree","L&T Infotech","NIIT Technologies",
                "Persistent Systems","Mastek","Zensar Technologies","Cyient",
                "Firstsource","WNS Global","EXL Service","Syntel","Mphasis",
                "Birlasoft","Sonata Software","Saksoft","Intellect Design",
                "Nucleus Software","Kpit Technologies","Tata Elxsi","Sasken",
                "Subex","Geometric"
        };

        private static final String[] CITIES = {
                "Delhi","Gurugram","Noida","Mumbai","Pune",
                "Bengaluru","Hyderabad","Chennai","Kolkata","Jaipur",
                "Ahmedabad","Surat","Lucknow","Kanpur","Nagpur",
                "Indore","Bhopal","Patna","Vadodara","Coimbatore"
        };

        private static final String[] COUNTRIES = {
                "India","India","India","India","India",
                "India","India","India","India","India",
                "India","USA","UK","Singapore","UAE"
        };

        private static final String[] CATEGORIES = {
                "LOGIN","PAYMENT","ACCOUNT","API","INTEGRATION",
                "SECURITY","PERFORMANCE","REPORTING","BILLING","ACCESS",
                "NETWORK","DATABASE","UI_BUG","DATA_LOSS","COMPLIANCE",
                "BACKUP","MONITORING","LATENCY","AUTH","ONBOARDING"
        };

        private static final String[] OUTCOMES = {
                "Interaction completed",
                "Issue resolved",
                "Escalated to L2",
                "Customer satisfied",
                "Follow-up scheduled",
                "No response from customer",
                "Workaround provided",
                "Ticket closed",
                "Under investigation",
                "Pending approval"
        };

        // ─────────────────────────────────────────────────────

        public void initialize(InMemoryDatabase db) {
            createDepartments(db);
            createTeams(db);
            createEmployees(db);
            createCustomers(db);
            createAccounts(db);
            createTickets(db);
            createAssignments(db);
            createComments(db);
            createStatusHistory(db);
            createInteractions(db);
        }

        // ── departments ───────────────────────────────────────

        private void createDepartments(InMemoryDatabase db) {
            long id = 1;
            for (String[] d : DEPT_DATA) {
                db.departments().add(new Department(id++, d[0], d[1]));
            }
        }

        // ── teams (TEAMS_PER_DEPT per department) ─────────────

        private void createTeams(InMemoryDatabase db) {
            long id = 1;
            for (Department dept : db.departments()) {
                for (int i = 1; i <= TEAMS_PER_DEPT; i++) {
                    db.teams().add(new Team(
                            id++,
                            dept.code() + "-T" + i,
                            dept.name() + " – Team " + i,
                            dept
                    ));
                }
            }
        }

        // ── employees (EMPLOYEES_PER_TEAM per team) ───────────

        private void createEmployees(InMemoryDatabase db) {
            long id = 1;
            for (Team team : db.teams()) {
                for (int i = 1; i <= EMPLOYEES_PER_TEAM; i++) {

                    String firstName = FIRST_NAMES[
                            (int)(id % FIRST_NAMES.length)];
                    String lastName  = LAST_NAMES[
                            (int)(id % LAST_NAMES.length)];

                    Employee manager = findManagerForTeam(
                            db.employees(), team);

                    db.employees().add(new Employee(
                            id,
                            String.format("EMP%06d", id),
                            firstName,
                            lastName,
                            firstName.toLowerCase() + "." +
                                    lastName.toLowerCase() + id + "@company.com",
                            BigDecimal.valueOf(30_000 + random.nextInt(170_000)),
                            randomEmployeeStatus(),
                            LocalDate.of(2015, 1, 1)
                                    .plusDays(random.nextInt(3_650)),
                            team.department(),
                            team,
                            manager
                    ));
                    id++;
                }
            }
        }

        private Employee findManagerForTeam(
                List<Employee> employees, Team team) {
            return employees.stream()
                    .filter(e -> e.team().equals(team))
                    .filter(e -> e.manager() == null)
                    .findFirst()
                    .orElse(null);
        }

        private EmployeeStatus randomEmployeeStatus() {
            int v = random.nextInt(100);
            if (v < 82) return EmployeeStatus.ACTIVE;
            if (v < 91) return EmployeeStatus.ON_LEAVE;
            if (v < 97) return EmployeeStatus.INACTIVE;
            return EmployeeStatus.TERMINATED;
        }

        // ── customers ─────────────────────────────────────────

        private void createCustomers(InMemoryDatabase db) {
            long id = 1;
            for (int i = 0; i < CUSTOMER_COUNT; i++) {

                CustomerType type = switch (i % 3) {
                    case 0  -> CustomerType.ENTERPRISE;
                    case 1  -> CustomerType.SMALL_BUSINESS;
                    default -> CustomerType.INDIVIDUAL;
                };

                boolean   isCompany   = type != CustomerType.INDIVIDUAL;
                String    companyName = isCompany
                        ? COMPANIES[(int)(id % COMPANIES.length)] + " Ltd " + id
                        : null;
                String    firstName   = FIRST_NAMES[(int)(id % FIRST_NAMES.length)];
                String    lastName    = LAST_NAMES[(int)(id % LAST_NAMES.length)];
                String    city        = CITIES[i % CITIES.length];
                String    country     = COUNTRIES[i % COUNTRIES.length];
                String    status      = (i % 40 == 0) ? "INACTIVE" : "ACTIVE";

                db.customers().add(new Customer(
                        id,
                        String.format("CUST%06d", id),
                        firstName,
                        lastName,
                        companyName,
                        (isCompany
                                ? companyName.replace(" ", "").toLowerCase()
                                : firstName.toLowerCase() + "." + lastName.toLowerCase())
                                + id + "@example.com",
                        type,
                        status,
                        city,
                        country
                ));
                id++;
            }
        }

        // ── accounts (1–3 per customer) ───────────────────────

        private void createAccounts(InMemoryDatabase db) {
            long id = 1;
            String[] accountTypes = {"PRIMARY", "SECONDARY", "TERTIARY"};

            for (Customer customer : db.customers()) {
                int count = 1 + random.nextInt(3);
                for (int i = 0; i < count; i++) {
                    db.customerAccounts().add(new CustomerAccount(
                            id,
                            String.format("ACC%08d", id),
                            customer,
                            accountTypes[Math.min(i, accountTypes.length - 1)],
                            (i == 2) ? "INACTIVE" : "ACTIVE",
                            BigDecimal.valueOf(5_000 + random.nextInt(500_000)),
                            LocalDate.of(2018, 1, 1)
                                    .plusDays(random.nextInt(2_500))
                    ));
                    id++;
                }
            }
        }

        // ── tickets ───────────────────────────────────────────

        private void createTickets(InMemoryDatabase db) {
            long id = 1;
            int custSize    = db.customers().size();
            int accountSize = db.customerAccounts().size();

            for (int i = 0; i < TICKET_COUNT; i++) {

                Customer        customer  = db.customers()
                        .get(random.nextInt(custSize));
                CustomerAccount account   = db.customerAccounts()
                        .get(random.nextInt(accountSize));
                TicketPriority  priority  = TicketPriority.values()[
                        random.nextInt(4)];
                TicketStatus    status    = TicketStatus.values()[
                        random.nextInt(6)];

                LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 9, 0)
                        .plusHours(random.nextInt(8_760));   // spread over 2024-2025
                LocalDateTime firstResponse = createdAt
                        .plusMinutes(10 + random.nextInt(480));
                LocalDateTime resolvedAt =
                        (status == TicketStatus.RESOLVED || status == TicketStatus.CLOSED)
                                ? firstResponse.plusHours(1 + random.nextInt(120))
                                : null;

                db.tickets().add(new Ticket(
                        id,
                        String.format("TKT%08d", id),
                        customer,
                        account,
                        CATEGORIES[random.nextInt(CATEGORIES.length)],
                        priority,
                        "Issue – " + CATEGORIES[random.nextInt(CATEGORIES.length)]
                                + " – ticket #" + id,
                        status,
                        createdAt,
                        firstResponse,
                        resolvedAt,
                        random.nextInt(5)
                ));
                id++;
            }
        }

        // ── assignments (1–4 per ticket) ──────────────────────

        private void createAssignments(InMemoryDatabase db) {
            long id = 1;
            int empSize = db.employees().size();

            for (Ticket ticket : db.tickets()) {
                int count = 1 + random.nextInt(4);
                for (int i = 0; i < count; i++) {
                    Employee employee = db.employees()
                            .get(random.nextInt(empSize));
                    db.ticketAssignments().add(new TicketAssignment(
                            id++,
                            ticket,
                            employee,
                            employee.team(),
                            ticket.createdAt().plusHours(i),
                            (i == count - 1)
                                    ? null
                                    : ticket.createdAt().plusHours(i + 1)
                    ));
                }
            }
        }

        // ── comments (1–6 per ticket) ─────────────────────────

        private void createComments(InMemoryDatabase db) {
            long id = 1;
            int empSize = db.employees().size();

            for (Ticket ticket : db.tickets()) {
                int count = 1 + random.nextInt(6);
                for (int i = 0; i < count; i++) {
                    Employee employee = db.employees()
                            .get(random.nextInt(empSize));
                    db.ticketComments().add(new TicketComment(
                            id++,
                            ticket,
                            employee,
                            "Update " + (i + 1) + " for " + ticket.ticketNumber()
                                    + ": investigating " + ticket.category(),
                            i % 3 == 0,
                            ticket.createdAt().plusMinutes(30L * (i + 1))
                    ));
                }
            }
        }

        // ── status history ────────────────────────────────────

        private void createStatusHistory(InMemoryDatabase db) {
            long id = 1;
            int empSize = db.employees().size();

            for (Ticket ticket : db.tickets()) {

                List<TicketStatus> path = new ArrayList<>();
                path.add(TicketStatus.OPEN);

                if (ticket.status() != TicketStatus.OPEN) {
                    path.add(TicketStatus.IN_PROGRESS);

                    switch (ticket.status()) {
                        case WAITING_CUSTOMER ->
                                path.add(TicketStatus.WAITING_CUSTOMER);
                        case ESCALATED ->
                                path.add(TicketStatus.ESCALATED);
                        case RESOLVED -> {
                            path.add(TicketStatus.RESOLVED);
                        }
                        case CLOSED -> {
                            path.add(TicketStatus.RESOLVED);
                            path.add(TicketStatus.CLOSED);
                        }
                        default -> { /* IN_PROGRESS stays */ }
                    }
                }

                TicketStatus previous = null;
                for (int step = 0; step < path.size(); step++) {
                    TicketStatus current = path.get(step);
                    Employee employee = db.employees()
                            .get(random.nextInt(empSize));
                    db.ticketStatusHistory().add(new TicketStatusHistory(
                            id++,
                            ticket,
                            previous,
                            current,
                            employee,
                            ticket.createdAt().plusMinutes(25L * step)
                    ));
                    previous = current;
                }
            }
        }

        // ── interactions (1–5 per ticket) ────────────────────

        private void createInteractions(InMemoryDatabase db) {
            long id = 1;
            int empSize = db.employees().size();
            InteractionType[] types = InteractionType.values();

            for (Ticket ticket : db.tickets()) {
                int count = 1 + random.nextInt(5);
                for (int i = 0; i < count; i++) {
                    Employee employee = db.employees()
                            .get(random.nextInt(empSize));
                    db.customerInteractions().add(new CustomerInteraction(
                            id++,
                            ticket.customer(),
                            ticket,
                            employee,
                            types[random.nextInt(types.length)],
                            ticket.createdAt().plusMinutes(15L * i),
                            120 + random.nextInt(5_400),
                            OUTCOMES[random.nextInt(OUTCOMES.length)]
                    ));
                }
            }
        }
    }

    // =========================================================
    // DATABASE BROWSER  –  pretty-print helpers
    // =========================================================

    public static class DatabaseBrowser {

        private final InMemoryDatabase db;
        private static final String LINE =
                "─".repeat(90);

        public DatabaseBrowser(InMemoryDatabase db) {
            this.db = db;
        }

        // ── summary ───────────────────────────────────────────

        public void printSummary() {
            banner("IN-MEMORY DATABASE – RECORD COUNTS");
            row("Departments",          db.departments().size());
            row("Teams",                db.teams().size());
            row("Employees",            db.employees().size());
            row("Customers",            db.customers().size());
            row("Customer Accounts",    db.customerAccounts().size());
            row("Tickets",              db.tickets().size());
            row("Ticket Assignments",   db.ticketAssignments().size());
            row("Ticket Comments",      db.ticketComments().size());
            row("Status History",       db.ticketStatusHistory().size());
            row("Interactions",         db.customerInteractions().size());
            System.out.println(LINE);

            long total = db.departments().size()
                    + db.teams().size()
                    + db.employees().size()
                    + db.customers().size()
                    + db.customerAccounts().size()
                    + db.tickets().size()
                    + db.ticketAssignments().size()
                    + db.ticketComments().size()
                    + db.ticketStatusHistory().size()
                    + db.customerInteractions().size();

            System.out.printf("  %-30s %,d%n", "TOTAL OBJECTS", total);
            System.out.println(LINE);
        }

        // ── employees ─────────────────────────────────────────

        public void printEmployees(int limit) {
            banner("EMPLOYEES  (first " + limit + ")");
            System.out.printf("%-10s | %-24s | %-22s | %-8s | %-15s | %10s%n",
                    "Code","Full Name","Department","Status","Team","Salary");
            System.out.println(LINE);
            db.employees().stream().limit(limit).forEach(e ->
                    System.out.printf("%-10s | %-24s | %-22s | %-8s | %-15s | %,10.0f%n",
                            e.employeeCode(),
                            e.fullName(),
                            e.department().name(),
                            e.status(),
                            e.team().code(),
                            e.salary()
                    )
            );
        }

        // ── customers ─────────────────────────────────────────

        public void printCustomers(int limit) {
            banner("CUSTOMERS  (first " + limit + ")");
            System.out.printf("%-10s | %-28s | %-14s | %-18s | %-10s | %s%n",
                    "Code","Display Name","Type","City","Status","Country");
            System.out.println(LINE);
            db.customers().stream().limit(limit).forEach(c ->
                    System.out.printf("%-10s | %-28s | %-14s | %-18s | %-10s | %s%n",
                            c.customerCode(),
                            truncate(c.displayName(), 28),
                            c.type(),
                            c.city(),
                            c.status(),
                            c.country()
                    )
            );
        }

        // ── tickets ───────────────────────────────────────────

        public void printTickets(int limit) {
            banner("TICKETS  (first " + limit + ")");
            System.out.printf("%-12s | %-28s | %-4s | %-17s | %-18s | %s%n",
                    "Number","Customer","Pri","Status","Category","Created");
            System.out.println(LINE);
            db.tickets().stream().limit(limit).forEach(t ->
                    System.out.printf("%-12s | %-28s | %-4s | %-17s | %-18s | %s%n",
                            t.ticketNumber(),
                            truncate(t.customer().displayName(), 28),
                            t.priority(),
                            t.status(),
                            t.category(),
                            t.createdAt().toLocalDate()
                    )
            );
        }

        // ── departments with team count ───────────────────────

        public void printDepartments() {
            banner("DEPARTMENTS");
            System.out.printf("%-6s | %-24s | %s%n",
                    "Code", "Name", "Teams");
            System.out.println(LINE);
            db.departments().forEach(d -> {
                long teamCount = db.teams().stream()
                        .filter(t -> t.department().equals(d))
                        .count();
                System.out.printf("%-6s | %-24s | %d%n",
                        d.code(), d.name(), teamCount);
            });
        }

        // ── interactions ──────────────────────────────────────

        public void printInteractions(int limit) {
            banner("CUSTOMER INTERACTIONS  (first " + limit + ")");
            System.out.printf("%-12s | %-28s | %-10s | %-10s | %s%n",
                    "Ticket","Customer","Type","Duration","Employee");
            System.out.println(LINE);
            db.customerInteractions().stream().limit(limit).forEach(i ->
                    System.out.printf("%-12s | %-28s | %-10s | %5ds     | %s%n",
                            i.ticket().ticketNumber(),
                            truncate(i.customer().displayName(), 28),
                            i.type(),
                            i.durationSeconds(),
                            i.employee().fullName()
                    )
            );
        }

        // ── quick tips for learners ───────────────────────────

        public void printStreamApiHints() {
            banner("STREAM API EXERCISE ENTRY POINTS");
            String[][] hints = {
                    {"db.employees().stream()",            "1,600 employees · filter/map/sort/collect"},
                    {"db.customers().stream()",             "1,500 customers · groupingBy type/city/country"},
                    {"db.tickets().stream()",               "2,000 tickets   · partitioningBy priority/status"},
                    {"db.ticketAssignments().stream()",     "~6,000 rows     · flatMap employee→tickets"},
                    {"db.ticketComments().stream()",        "~8,000 rows     · count internal vs external"},
                    {"db.ticketStatusHistory().stream()",   "~7,000 rows     · reduce / joining"},
                    {"db.customerInteractions().stream()",  "~7,000 rows     · averagingInt durationSeconds"},
                    {"db.customerAccounts().stream()",      "~3,500 rows     · summarizingDouble monthlyValue"},
            };
            for (String[] h : hints) {
                System.out.printf("  %-46s → %s%n", h[0], h[1]);
            }
            System.out.println(LINE);
        }

        // ── private helpers ───────────────────────────────────

        private void banner(String title) {
            System.out.println();
            System.out.println(LINE);
            System.out.println("  " + title);
            System.out.println(LINE);
        }

        private void row(String label, int count) {
            System.out.printf("  %-30s %,d%n", label, count);
        }

        private String truncate(String s, int max) {
            return (s != null && s.length() > max)
                    ? s.substring(0, max - 1) + "…"
                    : s;
        }
    }

    // =========================================================
    // MAIN ENTRY POINT
    // =========================================================

    public static void main(String[] args) {

        long t0 = System.currentTimeMillis();

        System.out.println("\nInitialising in-memory database …");

        InMemoryDatabase db          = new InMemoryDatabase();
        DataInitializer  initializer = new DataInitializer();
        initializer.initialize(db);

        long elapsed = System.currentTimeMillis() - t0;
        System.out.printf("Done in %d ms.%n", elapsed);

        DatabaseBrowser browser = new DatabaseBrowser(db);

        browser.printSummary();
        browser.printDepartments();
        browser.printEmployees(15);
        browser.printCustomers(15);
        browser.printTickets(15);
        browser.printInteractions(10);
        browser.printStreamApiHints();

        // ─────────────────────────────────────────────────────
        // STARTER EXERCISES  (uncomment to try)
        // ─────────────────────────────────────────────────────

        // -- 1. Count active employees
        // long active = db.employees().stream()
        //     .filter(e -> e.status() == EmployeeStatus.ACTIVE)
        //     .count();
        // System.out.println("Active employees: " + active);

        // -- 2. Tickets by priority
        // db.tickets().stream()
        //     .collect(java.util.stream.Collectors.groupingBy(
        //         Ticket::priority,
        //         java.util.stream.Collectors.counting()))
        //     .forEach((p, c) -> System.out.println(p + " → " + c));

        // -- 3. Average salary by department
        // db.employees().stream()
        //     .collect(java.util.stream.Collectors.groupingBy(
        //         e -> e.department().name(),
        //         java.util.stream.Collectors.averagingDouble(
        //             e -> e.salary().doubleValue())))
        //     .forEach((d, avg) ->
        //         System.out.printf("%-25s %.2f%n", d, avg));

        // -- 4. Top 5 customers by ticket count
        // db.tickets().stream()
        //     .collect(java.util.stream.Collectors.groupingBy(
        //         Ticket::customer,
        //         java.util.stream.Collectors.counting()))
        //     .entrySet().stream()
        //     .sorted(java.util.Map.Entry.<Customer,Long>
        //         comparingByValue().reversed())
        //     .limit(5)
        //     .forEach(e -> System.out.println(
        //         e.getKey().displayName() + " → " + e.getValue()));
    }
}
