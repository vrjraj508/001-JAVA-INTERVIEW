package com.stream.stream01basics.data;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamLab {

    // ============================================================
    // MAIN
    // ============================================================

    public static void main(String[] args) {

        System.out.println("==============================================");
        System.out.println("       VRJ ENTERPRISE STREAM LAB");
        System.out.println("==============================================");

        InMemoryDatabase db = DataGenerator.generate();

        db.printDatabaseStatistics();

        System.out.println();
        System.out.println("Database loaded successfully.");
        System.out.println("Now your 300 Stream exercises will operate");
        System.out.println("on this same dataset.");
    }


    // ============================================================
    // ENUMS
    // ============================================================

    enum EmployeeStatus {
        ACTIVE,
        INACTIVE,
        ON_LEAVE
    }

    enum ProjectStatus {
        ACTIVE,
        COMPLETED,
        ON_HOLD,
        CANCELLED
    }

    enum TransactionType {
        PAYMENT,
        REFUND,
        PURCHASE,
        TRANSFER
    }


    // ============================================================
    // EMPLOYEE
    // ============================================================

    static class Employee {

        private final Long id;
        private final String firstName;
        private final String lastName;
        private final String email;
        private final int age;
        private final double salary;
        private final EmployeeStatus status;

        private Department department;
        private Employee manager;
        private Address address;

        private final List<Skill> skills;
        private final List<Project> projects;
        private final List<SalaryHistory> salaryHistory;
        private final List<PerformanceReview> reviews;
        private final List<Transaction> transactions;

        public Employee(
                Long id,
                String firstName,
                String lastName,
                String email,
                int age,
                double salary,
                EmployeeStatus status
        ) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.age = age;
            this.salary = salary;
            this.status = status;

            this.skills = new ArrayList<>();
            this.projects = new ArrayList<>();
            this.salaryHistory = new ArrayList<>();
            this.reviews = new ArrayList<>();
            this.transactions = new ArrayList<>();
        }

        public Long getId() {
            return id;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getEmail() {
            return email;
        }

        public int getAge() {
            return age;
        }

        public double getSalary() {
            return salary;
        }

        public EmployeeStatus getStatus() {
            return status;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        public Employee getManager() {
            return manager;
        }

        public void setManager(Employee manager) {
            this.manager = manager;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public List<Skill> getSkills() {
            return skills;
        }

        public List<Project> getProjects() {
            return projects;
        }

        public List<SalaryHistory> getSalaryHistory() {
            return salaryHistory;
        }

        public List<PerformanceReview> getReviews() {
            return reviews;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "id=" + id +
                    ", name='" + firstName + " " + lastName + '\'' +
                    ", salary=" + salary +
                    ", status=" + status +
                    ", department=" +
                    (department != null ? department.getName() : null) +
                    '}';
        }
    }


    // ============================================================
    // DEPARTMENT
    // ============================================================

    static class Department {

        private final Long id;
        private final String name;
        private final String code;

        public Department(Long id, String name, String code) {
            this.id = id;
            this.name = name;
            this.code = code;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    // ============================================================
    // CLIENT
    // ============================================================

    static class Client {

        private final Long id;
        private final String companyName;
        private final String industry;
        private final double contractValue;

        public Client(
                Long id,
                String companyName,
                String industry,
                double contractValue
        ) {
            this.id = id;
            this.companyName = companyName;
            this.industry = industry;
            this.contractValue = contractValue;
        }

        public Long getId() {
            return id;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getIndustry() {
            return industry;
        }

        public double getContractValue() {
            return contractValue;
        }

        @Override
        public String toString() {
            return companyName;
        }
    }


    // ============================================================
    // PROJECT
    // ============================================================

    static class Project {

        private final Long id;
        private final String name;
        private final double budget;
        private final ProjectStatus status;
        private final Client client;

        public Project(
                Long id,
                String name,
                double budget,
                ProjectStatus status,
                Client client
        ) {
            this.id = id;
            this.name = name;
            this.budget = budget;
            this.status = status;
            this.client = client;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getBudget() {
            return budget;
        }

        public ProjectStatus getStatus() {
            return status;
        }

        public Client getClient() {
            return client;
        }

        @Override
        public String toString() {
            return "Project{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", budget=" + budget +
                    ", status=" + status +
                    '}';
        }
    }


    // ============================================================
    // SKILL
    // ============================================================

    static class Skill {

        private final Long id;
        private final String name;
        private final String category;
        private final String level;

        public Skill(
                Long id,
                String name,
                String category,
                String level
        ) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.level = level;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }

        public String getLevel() {
            return level;
        }

        @Override
        public String toString() {
            return name;
        }
    }


    // ============================================================
    // ADDRESS
    // ============================================================

    static class Address {

        private final String city;
        private final String state;
        private final String country;
        private final String postalCode;

        public Address(
                String city,
                String state,
                String country,
                String postalCode
        ) {
            this.city = city;
            this.state = state;
            this.country = country;
            this.postalCode = postalCode;
        }

        public String getCity() {
            return city;
        }

        public String getState() {
            return state;
        }

        public String getCountry() {
            return country;
        }

        public String getPostalCode() {
            return postalCode;
        }
    }


    // ============================================================
    // SALARY HISTORY
    // ============================================================

    static class SalaryHistory {

        private final Long employeeId;
        private final LocalDate effectiveDate;
        private final double salary;

        public SalaryHistory(
                Long employeeId,
                LocalDate effectiveDate,
                double salary
        ) {
            this.employeeId = employeeId;
            this.effectiveDate = effectiveDate;
            this.salary = salary;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public LocalDate getEffectiveDate() {
            return effectiveDate;
        }

        public double getSalary() {
            return salary;
        }
    }


    // ============================================================
    // PERFORMANCE REVIEW
    // ============================================================

    static class PerformanceReview {

        private final Long employeeId;
        private final int year;
        private final double rating;
        private final String comments;

        public PerformanceReview(
                Long employeeId,
                int year,
                double rating,
                String comments
        ) {
            this.employeeId = employeeId;
            this.year = year;
            this.rating = rating;
            this.comments = comments;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public int getYear() {
            return year;
        }

        public double getRating() {
            return rating;
        }

        public String getComments() {
            return comments;
        }
    }


    // ============================================================
    // TRANSACTION
    // ============================================================

    static class Transaction {

        private final Long id;
        private final Long employeeId;
        private final TransactionType type;
        private final double amount;
        private final LocalDate date;

        public Transaction(
                Long id,
                Long employeeId,
                TransactionType type,
                double amount,
                LocalDate date
        ) {
            this.id = id;
            this.employeeId = employeeId;
            this.type = type;
            this.amount = amount;
            this.date = date;
        }

        public Long getId() {
            return id;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public TransactionType getType() {
            return type;
        }

        public double getAmount() {
            return amount;
        }

        public LocalDate getDate() {
            return date;
        }
    }


    // ============================================================
    // EMPLOYEE-PROJECT ASSIGNMENT
    // ============================================================

    static class ProjectAssignment {

        private final Long employeeId;
        private final Long projectId;
        private final int hoursAllocated;

        public ProjectAssignment(
                Long employeeId,
                Long projectId,
                int hoursAllocated
        ) {
            this.employeeId = employeeId;
            this.projectId = projectId;
            this.hoursAllocated = hoursAllocated;
        }

        public Long getEmployeeId() {
            return employeeId;
        }

        public Long getProjectId() {
            return projectId;
        }

        public int getHoursAllocated() {
            return hoursAllocated;
        }
    }


    // ============================================================
    // IN-MEMORY DATABASE
    // ============================================================

    static class InMemoryDatabase {

        private final List<Employee> employees;
        private final List<Department> departments;
        private final List<Client> clients;
        private final List<Project> projects;
        private final List<Skill> skills;
        private final List<Address> addresses;
        private final List<SalaryHistory> salaryHistory;
        private final List<PerformanceReview> performanceReviews;
        private final List<Transaction> transactions;
        private final List<ProjectAssignment> projectAssignments;

        private final Map<Long, Employee> employeeById;
        private final Map<Long, Department> departmentById;
        private final Map<Long, Client> clientById;
        private final Map<Long, Project> projectById;
        private final Map<Long, Skill> skillById;

        public InMemoryDatabase(
                List<Employee> employees,
                List<Department> departments,
                List<Client> clients,
                List<Project> projects,
                List<Skill> skills,
                List<Address> addresses,
                List<SalaryHistory> salaryHistory,
                List<PerformanceReview> performanceReviews,
                List<Transaction> transactions,
                List<ProjectAssignment> projectAssignments
        ) {

            this.employees = employees;
            this.departments = departments;
            this.clients = clients;
            this.projects = projects;
            this.skills = skills;
            this.addresses = addresses;
            this.salaryHistory = salaryHistory;
            this.performanceReviews = performanceReviews;
            this.transactions = transactions;
            this.projectAssignments = projectAssignments;

            this.employeeById = employees.stream()
                    .collect(Collectors.toMap(
                            Employee::getId,
                            e -> e
                    ));

            this.departmentById = departments.stream()
                    .collect(Collectors.toMap(
                            Department::getId,
                            d -> d
                    ));

            this.clientById = clients.stream()
                    .collect(Collectors.toMap(
                            Client::getId,
                            c -> c
                    ));

            this.projectById = projects.stream()
                    .collect(Collectors.toMap(
                            Project::getId,
                            p -> p
                    ));

            this.skillById = skills.stream()
                    .collect(Collectors.toMap(
                            Skill::getId,
                            s -> s
                    ));
        }

        public List<Employee> getEmployees() {
            return employees;
        }

        public List<Department> getDepartments() {
            return departments;
        }

        public List<Client> getClients() {
            return clients;
        }

        public List<Project> getProjects() {
            return projects;
        }

        public List<Skill> getSkills() {
            return skills;
        }

        public List<Address> getAddresses() {
            return addresses;
        }

        public List<SalaryHistory> getSalaryHistory() {
            return salaryHistory;
        }

        public List<PerformanceReview> getPerformanceReviews() {
            return performanceReviews;
        }

        public List<Transaction> getTransactions() {
            return transactions;
        }

        public List<ProjectAssignment> getProjectAssignments() {
            return projectAssignments;
        }

        public Map<Long, Employee> getEmployeeById() {
            return employeeById;
        }

        public Map<Long, Department> getDepartmentById() {
            return departmentById;
        }

        public Map<Long, Client> getClientById() {
            return clientById;
        }

        public Map<Long, Project> getProjectById() {
            return projectById;
        }

        public Map<Long, Skill> getSkillById() {
            return skillById;
        }

        public void printDatabaseStatistics() {

            System.out.println();
            System.out.println("============== DATABASE STATISTICS ==============");

            System.out.printf(
                    "Employees           : %,d%n",
                    employees.size()
            );

            System.out.printf(
                    "Departments         : %,d%n",
                    departments.size()
            );

            System.out.printf(
                    "Clients             : %,d%n",
                    clients.size()
            );

            System.out.printf(
                    "Projects            : %,d%n",
                    projects.size()
            );

            System.out.printf(
                    "Skills              : %,d%n",
                    skills.size()
            );

            System.out.printf(
                    "Addresses           : %,d%n",
                    addresses.size()
            );

            System.out.printf(
                    "Salary History      : %,d%n",
                    salaryHistory.size()
            );

            System.out.printf(
                    "Performance Reviews : %,d%n",
                    performanceReviews.size()
            );

            System.out.printf(
                    "Transactions        : %,d%n",
                    transactions.size()
            );

            System.out.printf(
                    "Project Assignments : %,d%n",
                    projectAssignments.size()
            );

            System.out.println("==================================================");
        }
    }


    // ============================================================
    // DATA GENERATOR
    // ============================================================

    static class DataGenerator {

        private static final Random RANDOM =
                new Random(42);

        private static final String[] FIRST_NAMES = {
                "Aarav", "Aditi", "Arjun", "Ananya",
                "Rahul", "Riya", "Rohan", "Priya",
                "Vikram", "Neha", "Karan", "Sneha",
                "Amit", "Pooja", "Raj", "Kavya",
                "Aditya", "Meera", "Varun", "Isha",
                "Vivek", "Nisha", "Akash", "Simran"
        };

        private static final String[] LAST_NAMES = {
                "Sharma", "Patel", "Verma", "Gupta",
                "Mehta", "Reddy", "Singh", "Kumar",
                "Joshi", "Shah", "Malhotra", "Iyer",
                "Nair", "Desai", "Kapoor", "Mishra"
        };

        private static final String[] DEPARTMENT_NAMES = {
                "Engineering",
                "Information Technology",
                "Human Resources",
                "Finance",
                "Marketing",
                "Sales",
                "Operations",
                "Legal",
                "Research",
                "Customer Success",
                "Product",
                "Security",
                "Data Science",
                "Quality Assurance",
                "DevOps",
                "Architecture"
        };

        private static final String[] INDUSTRIES = {
                "Banking",
                "Healthcare",
                "Retail",
                "Insurance",
                "Technology",
                "Telecom",
                "Automotive",
                "Education",
                "Manufacturing",
                "Travel"
        };

        private static final String[] CITIES = {
                "Mumbai",
                "Pune",
                "Bangalore",
                "Hyderabad",
                "Delhi",
                "Chennai",
                "Kolkata",
                "Ahmedabad",
                "Noida",
                "Gurgaon"
        };

        private static final String[] STATES = {
                "Maharashtra",
                "Karnataka",
                "Telangana",
                "Delhi",
                "Tamil Nadu",
                "West Bengal",
                "Gujarat",
                "Uttar Pradesh"
        };

        private static final String[] SKILL_NAMES = {
                "Java",
                "Spring Boot",
                "Spring Security",
                "Hibernate",
                "JPA",
                "PostgreSQL",
                "MySQL",
                "MongoDB",
                "Redis",
                "Kafka",
                "RabbitMQ",
                "Docker",
                "Kubernetes",
                "AWS",
                "Azure",
                "React",
                "Angular",
                "JavaScript",
                "TypeScript",
                "Python",
                "Machine Learning",
                "Git",
                "Linux",
                "Terraform",
                "Jenkins",
                "Microservices",
                "REST API",
                "GraphQL",
                "System Design",
                "SQL"
        };

        public static InMemoryDatabase generate() {

            System.out.println();
            System.out.println("Generating enterprise dataset...");

            List<Department> departments =
                    generateDepartments();

            List<Client> clients =
                    generateClients(300);

            List<Skill> skills =
                    generateSkills();

            List<Project> projects =
                    generateProjects(
                            500,
                            clients
                    );

            List<Employee> employees =
                    generateEmployees(
                            10_000,
                            departments
                    );

            List<Address> addresses =
                    new ArrayList<>();

            List<SalaryHistory> salaryHistory =
                    new ArrayList<>();

            List<PerformanceReview> reviews =
                    new ArrayList<>();

            List<Transaction> transactions =
                    new ArrayList<>();

            List<ProjectAssignment> assignments =
                    new ArrayList<>();

            establishEmployeeRelationships(
                    employees,
                    departments
            );

            generateAddresses(
                    employees,
                    addresses
            );

            generateSkills(
                    employees,
                    skills
            );

            generateProjectsForEmployees(
                    employees,
                    projects,
                    assignments
            );

            generateSalaryHistory(
                    employees,
                    salaryHistory
            );

            generateReviews(
                    employees,
                    reviews
            );

            generateTransactions(
                    employees,
                    transactions
            );

            System.out.println(
                    "Dataset generation complete."
            );

            return new InMemoryDatabase(
                    employees,
                    departments,
                    clients,
                    projects,
                    skills,
                    addresses,
                    salaryHistory,
                    reviews,
                    transactions,
                    assignments
            );
        }


        // --------------------------------------------------------
        // DEPARTMENTS
        // --------------------------------------------------------

        private static List<Department> generateDepartments() {

            List<Department> departments =
                    new ArrayList<>();

            long id = 1;

            for (String name : DEPARTMENT_NAMES) {

                String code =
                        name
                                .replaceAll("[^A-Za-z]", "")
                                .substring(
                                        0,
                                        Math.min(
                                                4,
                                                name.replaceAll(
                                                        "[^A-Za-z]",
                                                        ""
                                                ).length()
                                        )
                                )
                                .toUpperCase();

                departments.add(
                        new Department(
                                id++,
                                name,
                                code
                        )
                );
            }

            return departments;
        }


        // --------------------------------------------------------
        // CLIENTS
        // --------------------------------------------------------

        private static List<Client> generateClients(
                int count
        ) {

            List<Client> clients =
                    new ArrayList<>();

            for (long i = 1; i <= count; i++) {

                String companyName =
                        "Client-" + String.format(
                                "%04d",
                                i
                        );

                String industry =
                        INDUSTRIES[
                                RANDOM.nextInt(
                                        INDUSTRIES.length
                                )
                                ];

                double contractValue =
                        randomDouble(
                                500_000,
                                50_000_000
                        );

                clients.add(
                        new Client(
                                i,
                                companyName,
                                industry,
                                contractValue
                        )
                );
            }

            return clients;
        }


        // --------------------------------------------------------
        // SKILLS
        // --------------------------------------------------------

        private static List<Skill> generateSkills() {

            List<Skill> skills =
                    new ArrayList<>();

            long id = 1;

            for (String skillName : SKILL_NAMES) {

                String category;

                if (
                        skillName.equals("Java")
                                ||
                                skillName.equals("Spring Boot")
                                ||
                                skillName.equals("Hibernate")
                                ||
                                skillName.equals("JPA")
                                ||
                                skillName.equals("Microservices")
                ) {
                    category = "Backend";
                }
                else if (
                        skillName.equals("React")
                                ||
                                skillName.equals("Angular")
                                ||
                                skillName.equals("JavaScript") ||
                                skillName.equals("TypeScript")
                ) {
                    category = "Frontend";
                }
                else if (
                        skillName.equals("Docker")
                                ||
                                skillName.equals("Kubernetes")
                                ||
                                skillName.equals("AWS") ||
                                skillName.equals("Azure") ||
                                skillName.equals("Terraform")
                ) {
                    category = "DevOps";
                }
                else {
                    category = "Technology";
                }

                String level =
                        switch (
                                RANDOM.nextInt(4)
                                ) {
                            case 0 -> "BEGINNER";
                            case 1 -> "INTERMEDIATE";
                            case 2 -> "ADVANCED";
                            default -> "EXPERT";
                        };

                skills.add(
                        new Skill(
                                id++,
                                skillName,
                                category,
                                level
                        )
                );
            }

            return skills;
        }


        // --------------------------------------------------------
        // PROJECTS
        // --------------------------------------------------------

        private static List<Project> generateProjects(
                int count,
                List<Client> clients
        ) {

            List<Project> projects =
                    new ArrayList<>();

            for (long i = 1; i <= count; i++) {

                Client client =
                        clients.get(
                                RANDOM.nextInt(
                                        clients.size()
                                )
                        );

                ProjectStatus status =
                        ProjectStatus.values()[
                                RANDOM.nextInt(
                                        ProjectStatus.values().length
                                )
                                ];

                double budget =
                        randomDouble(
                                100_000,
                                20_000_000
                        );

                projects.add(
                        new Project(
                                i,
                                "Project-" +
                                        String.format(
                                                "%04d",
                                                i
                                        ),
                                budget,
                                status,
                                client
                        )
                );
            }

            return projects;
        }


        // --------------------------------------------------------
        // EMPLOYEES
        // --------------------------------------------------------

        private static List<Employee> generateEmployees(
                int count,
                List<Department> departments
        ) {

            List<Employee> employees =
                    new ArrayList<>();

            for (long i = 1; i <= count; i++) {

                String firstName =
                        FIRST_NAMES[
                                RANDOM.nextInt(
                                        FIRST_NAMES.length
                                )
                                ];

                String lastName =
                        LAST_NAMES[
                                RANDOM.nextInt(
                                        LAST_NAMES.length
                                )
                                ];

                String email =
                        firstName.toLowerCase()
                                + "."
                                + lastName.toLowerCase()
                                + i
                                + "@enterprise.com";

                int age =
                        randomInt(
                                21,
                                60
                        );

                double salary =
                        randomDouble(
                                35_000,
                                250_000
                        );

                EmployeeStatus status =
                        randomEmployeeStatus();

                Employee employee =
                        new Employee(
                                i,
                                firstName,
                                lastName,
                                email,
                                age,
                                salary,
                                status
                        );

                employee.setDepartment(
                        departments.get(
                                RANDOM.nextInt(
                                        departments.size()
                                )
                        )
                );

                employees.add(employee);
            }

            return employees;
        }


        // --------------------------------------------------------
        // EMPLOYEE RELATIONSHIPS
        // --------------------------------------------------------

        private static void establishEmployeeRelationships(
                List<Employee> employees,
                List<Department> departments
        ) {

            Map<Department, List<Employee>> byDepartment =
                    employees.stream()
                            .collect(
                                    Collectors.groupingBy(
                                            Employee::getDepartment
                                    )
                            );

            for (List<Employee> departmentEmployees :
                    byDepartment.values()) {

                if (departmentEmployees.size() < 2) {
                    continue;
                }

                Employee manager =
                        departmentEmployees.get(0);

                for (int i = 1;
                     i < departmentEmployees.size();
                     i++) {

                    departmentEmployees
                            .get(i)
                            .setManager(manager);
                }
            }
        }


        // --------------------------------------------------------
        // ADDRESSES
        // --------------------------------------------------------

        private static void generateAddresses(
                List<Employee> employees,
                List<Address> addresses
        ) {

            for (Employee employee : employees) {

                String city =
                        CITIES[
                                RANDOM.nextInt(
                                        CITIES.length
                                )
                                ];

                String state =
                        STATES[
                                RANDOM.nextInt(
                                        STATES.length
                                )
                                ];

                Address address =
                        new Address(
                                city,
                                state,
                                "India",
                                String.valueOf(
                                        randomInt(
                                                100000,
                                                999999
                                        )
                                )
                        );

                employee.setAddress(address);

                addresses.add(address);
            }
        }


        // --------------------------------------------------------
        // EMPLOYEE SKILLS
        // --------------------------------------------------------

        private static void generateSkills(
                List<Employee> employees,
                List<Skill> skills
        ) {

            for (Employee employee : employees) {

                int skillCount =
                        randomInt(3, 8);

                List<Skill> shuffled =
                        new ArrayList<>(skills);

                Collections.shuffle(
                        shuffled,
                        RANDOM
                );

                for (int i = 0;
                     i < skillCount;
                     i++) {

                    employee
                            .getSkills()
                            .add(
                                    shuffled.get(i)
                            );
                }
            }
        }


        // --------------------------------------------------------
        // PROJECT ASSIGNMENTS
        // --------------------------------------------------------

        private static void generateProjectsForEmployees(
                List<Employee> employees,
                List<Project> projects,
                List<ProjectAssignment> assignments
        ) {

            long assignmentId = 1;

            for (Employee employee : employees) {

                int projectCount =
                        randomInt(1, 5);

                List<Project> shuffled =
                        new ArrayList<>(projects);

                Collections.shuffle(
                        shuffled,
                        RANDOM
                );

                for (int i = 0;
                     i < projectCount;
                     i++) {

                    Project project =
                            shuffled.get(i);

                    employee
                            .getProjects()
                            .add(project);

                    int hours =
                            randomInt(
                                    20,
                                    500
                            );

                    assignments.add(
                            new ProjectAssignment(
                                    employee.getId(),
                                    project.getId(),
                                    hours
                            )
                    );
                }
            }
        }


        // --------------------------------------------------------
        // SALARY HISTORY
        // --------------------------------------------------------

        private static void generateSalaryHistory(
                List<Employee> employees,
                List<SalaryHistory> salaryHistory
        ) {

            for (Employee employee : employees) {

                int historyCount =
                        randomInt(2, 5);

                double currentSalary =
                        employee.getSalary();

                for (int i = 0;
                     i < historyCount;
                     i++) {

                    LocalDate date =
                            LocalDate.now()
                                    .minusYears(
                                            historyCount - i
                                    );

                    double historicalSalary =
                            currentSalary *
                                    (0.70 +
                                            RANDOM.nextDouble()
                                                    * 0.25);

                    salaryHistory.add(
                            new SalaryHistory(
                                    employee.getId(),
                                    date,
                                    historicalSalary
                            )
                    );
                }
            }
        }


        // --------------------------------------------------------
        // PERFORMANCE REVIEWS
        // --------------------------------------------------------

        private static void generateReviews(
                List<Employee> employees,
                List<PerformanceReview> reviews
        ) {

            int[] years = {
                    2023,
                    2024,
                    2025
            };

            for (Employee employee : employees) {

                for (int year : years) {

                    double rating =
                            Math.round(
                                    (
                                            2.5 +
                                                    RANDOM.nextDouble()
                                                            * 2.5
                                    ) * 10
                            ) / 10.0;

                    reviews.add(
                            new PerformanceReview(
                                    employee.getId(),
                                    year,
                                    rating,
                                    randomReviewComment()
                            )
                    );

                    employee
                            .getReviews()
                            .add(
                                    reviews.get(
                                            reviews.size() - 1
                                    )
                            );
                }
            }
        }


        // --------------------------------------------------------
        // TRANSACTIONS
        // --------------------------------------------------------

        private static void generateTransactions(
                List<Employee> employees,
                List<Transaction> transactions
        ) {

            long transactionId = 1;

            for (Employee employee : employees) {

                int count =
                        randomInt(5, 15);

                for (int i = 0;
                     i < count;
                     i++) {

                    TransactionType type =
                            TransactionType.values()[
                                    RANDOM.nextInt(
                                            TransactionType
                                                    .values()
                                                    .length
                                    )
                                    ];

                    double amount =
                            randomDouble(
                                    100,
                                    100_000
                            );

                    LocalDate date =
                            LocalDate.now()
                                    .minusDays(
                                            RANDOM.nextInt(
                                                    1000
                                            )
                                    );

                    Transaction transaction =
                            new Transaction(
                                    transactionId++,
                                    employee.getId(),
                                    type,
                                    amount,
                                    date
                            );

                    transactions.add(
                            transaction
                    );

                    employee
                            .getTransactions()
                            .add(
                                    transaction
                            );
                }
            }
        }


        // --------------------------------------------------------
        // RANDOM HELPERS
        // --------------------------------------------------------

        private static int randomInt(
                int min,
                int max
        ) {

            return RANDOM.nextInt(
                    max - min + 1
            ) + min;
        }

        private static double randomDouble(
                double min,
                double max
        ) {

            return min +
                    RANDOM.nextDouble()
                            * (max - min);
        }

        private static EmployeeStatus randomEmployeeStatus() {

            int value =
                    RANDOM.nextInt(100);

            if (value < 80) {
                return EmployeeStatus.ACTIVE;
            }

            if (value < 95) {
                return EmployeeStatus.INACTIVE;
            }

            return EmployeeStatus.ON_LEAVE;
        }

        private static String randomReviewComment() {

            String[] comments = {
                    "Excellent performance",
                    "Strong technical contribution",
                    "Meets expectations",
                    "Needs improvement",
                    "Outstanding leadership",
                    "Strong teamwork",
                    "Excellent delivery"
            };

            return comments[
                    RANDOM.nextInt(
                            comments.length
                    )
                    ];
        }
    }
}