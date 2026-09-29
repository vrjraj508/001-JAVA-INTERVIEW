package com.streams.streamday001;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class Utils {

    // ─────────────────────────────────────────────────────────────
    // DOMAIN MODELS
    // ─────────────────────────────────────────────────────────────

    public record Employee(
            int id,
            String name,
            String department,
            String role,
            double salary,
            int age,
            int experienceYears,
            String city,
            boolean isActive,
            List<String> skills
    ) {}

    public record Product(
            int id,
            String name,
            String category,
            double price,
            int stock,
            double rating,
            String brand,
            boolean isAvailable
    ) {}

    public record Order(
            int orderId,
            int customerId,
            String customerName,
            List<String> items,
            double totalAmount,
            String status,           // PLACED, SHIPPED, DELIVERED, CANCELLED
            LocalDate orderDate,
            String city
    ) {}

    public record Student(
            int id,
            String name,
            int age,
            String branch,
            double cgpa,
            int semester,
            boolean hasBacklog,
            List<String> subjects
    ) {}

    public record Transaction(
            String txnId,
            String accountHolder,
            String type,             // CREDIT, DEBIT
            double amount,
            String category,         // SALARY, FOOD, TRAVEL, SHOPPING, EMI, UTILITY
            LocalDateTime timestamp,
            String status            // SUCCESS, FAILED, PENDING
    ) {}

    // ─────────────────────────────────────────────────────────────
    // 1. EMPLOYEES  (50 records across 5 departments)
    // ─────────────────────────────────────────────────────────────

    public static List<Employee> giveEmployees() {
        return List.of(
                new Employee(1,  "Viraj Mehta",      "Engineering",  "Senior Dev",       120000, 30, 7,  "Mumbai",    true,  List.of("Java","Spring","Kafka")),
                new Employee(2,  "Haris Khan",       "Engineering",  "Junior Dev",        55000, 23, 1,  "Pune",      true,  List.of("Java","React")),
                new Employee(3,  "Shubham Joshi",    "Engineering",  "Tech Lead",        180000, 35, 12, "Bangalore", true,  List.of("Java","AWS","Microservices")),
                new Employee(4,  "Shariq Ali",       "Engineering",  "DevOps Engineer",  110000, 28, 5,  "Hyderabad", true,  List.of("Docker","Kubernetes","CI/CD")),
                new Employee(5,  "Ashutosh Verma",   "Engineering",  "Senior Dev",       125000, 31, 8,  "Chennai",   true,  List.of("Python","Django","Redis")),
                new Employee(6,  "Prajiy Nair",      "Engineering",  "Junior Dev",        48000, 22, 1,  "Kochi",     false, List.of("JavaScript","Node.js")),
                new Employee(7,  "Rohan Gupta",      "Engineering",  "Architect",        250000, 40, 18, "Mumbai",    true,  List.of("Java","System Design","AWS")),
                new Employee(8,  "Aman Singh",       "Engineering",  "Mid Dev",           82000, 26, 3,  "Delhi",     true,  List.of("Java","Angular","SQL")),
                new Employee(9,  "Rahul Sharma",     "Engineering",  "QA Engineer",       65000, 27, 4,  "Pune",      true,  List.of("Selenium","JUnit","JIRA")),
                new Employee(10, "Karan Bajaj",      "Engineering",  "Senior Dev",       118000, 29, 6,  "Bangalore", true,  List.of("Go","gRPC","Kafka")),

                new Employee(11, "Vikram Tiwari",    "Sales",        "Sales Manager",    150000, 38, 14, "Mumbai",    true,  List.of("CRM","Negotiation","Excel")),
                new Employee(12, "Arjun Patel",      "Sales",        "Sales Executive",   60000, 25, 2,  "Ahmedabad", true,  List.of("CRM","Cold Calling")),
                new Employee(13, "Siddharth Roy",    "Sales",        "Sales Executive",   58000, 24, 2,  "Kolkata",   false, List.of("CRM","Excel")),
                new Employee(14, "Ankit Sharma",     "Sales",        "Key Account Mgr",  135000, 33, 10, "Delhi",     true,  List.of("Salesforce","Strategy")),
                new Employee(15, "Nikhil Dubey",     "Sales",        "Sales Analyst",     72000, 27, 4,  "Jaipur",    true,  List.of("Power BI","Excel","SQL")),
                new Employee(16, "Manish Yadav",     "Sales",        "Sales Executive",   55000, 23, 1,  "Lucknow",   true,  List.of("CRM","Cold Calling")),
                new Employee(17, "Deepak Mishra",    "Sales",        "Regional Head",    200000, 42, 20, "Mumbai",    true,  List.of("Leadership","Strategy","CRM")),
                new Employee(18, "Saurabh Sinha",    "Sales",        "Sales Executive",   61000, 26, 3,  "Patna",     false, List.of("CRM","Excel")),
                new Employee(19, "Abhishek Das",     "Sales",        "BD Executive",      68000, 27, 3,  "Bhubaneswar",true, List.of("Networking","Proposal Writing")),
                new Employee(20, "Pratik Mukherjee", "Sales",        "Sales Analyst",     75000, 29, 5,  "Kolkata",   true,  List.of("Tableau","SQL","Excel")),

                new Employee(21, "Yash Kapoor",      "HR",           "HR Manager",       140000, 36, 13, "Mumbai",    true,  List.of("Recruitment","HRIS","Labour Law")),
                new Employee(22, "Aditya Kumar",     "HR",           "HR Executive",      55000, 24, 2,  "Delhi",     true,  List.of("Recruitment","Onboarding")),
                new Employee(23, "Akash Reddy",      "HR",           "HR Business Partner",95000, 30, 7, "Hyderabad", true,  List.of("Talent Mgmt","OD","Analytics")),
                new Employee(24, "Ritesh Bansal",    "HR",           "HR Executive",      52000, 23, 1,  "Noida",     false, List.of("Recruitment","Excel")),
                new Employee(25, "Sumit Aggarwal",   "HR",           "L&D Specialist",    80000, 28, 5,  "Gurugram",  true,  List.of("Training","LMS","Facilitation")),
                new Employee(26, "Pankaj Shukla",    "HR",           "Payroll Specialist",70000, 29, 6,  "Kanpur",    true,  List.of("SAP HR","Excel","Payroll")),
                new Employee(27, "Rajat Chopra",     "HR",           "HR Manager",       145000, 37, 14, "Bangalore", true,  List.of("Recruitment","Labour Law","HRIS")),
                new Employee(28, "Mohit Sethi",      "HR",           "HR Executive",      53000, 24, 2,  "Chandigarh",true,  List.of("Recruitment","Onboarding")),
                new Employee(29, "Sachin Pillai",    "HR",           "Compensation Lead", 115000, 34, 11, "Chennai",  true,  List.of("Comp & Ben","Analytics","SAP")),
                new Employee(30, "Rakesh Iyer",      "HR",           "HR Executive",      50000, 22, 1,  "Coimbatore",false, List.of("Recruitment")),

                new Employee(31, "Varun Malhotra",   "Finance",      "CFO",              500000, 48, 25, "Mumbai",    true,  List.of("Financial Planning","IFRS","Leadership")),
                new Employee(32, "Tarun Grover",     "Finance",      "Finance Manager",  175000, 38, 15, "Delhi",     true,  List.of("SAP FICO","Excel","Taxation")),
                new Employee(33, "Gaurav Agarwal",   "Finance",      "Sr Accountant",     95000, 31, 8,  "Pune",      true,  List.of("Tally","GST","Auditing")),
                new Employee(34, "Naveen Jain",      "Finance",      "Jr Accountant",     52000, 24, 2,  "Jaipur",    true,  List.of("Tally","Excel")),
                new Employee(35, "Kunal Saxena",     "Finance",      "Financial Analyst", 110000, 30, 7,  "Bangalore", true,  List.of("Excel","Python","Financial Modeling")),
                new Employee(36, "Mayank Trivedi",   "Finance",      "Tax Consultant",   130000, 33, 9,  "Ahmedabad", true,  List.of("GST","Income Tax","SAP")),
                new Employee(37, "Akhil Rastogi",    "Finance",      "Jr Accountant",     48000, 23, 1,  "Allahabad", false, List.of("Tally","Excel")),
                new Employee(38, "Rohit Bhatt",      "Finance",      "Audit Manager",    160000, 36, 13, "Mumbai",    true,  List.of("Auditing","IFRS","Risk Mgmt")),
                new Employee(39, "Hemant Goel",      "Finance",      "Financial Analyst",105000, 29, 6,  "Noida",     true,  List.of("Excel","Power BI","SAP")),
                new Employee(40, "Vivek Nanda",      "Finance",      "Sr Accountant",     92000, 32, 9,  "Chennai",   false, List.of("Tally","GST","Excel")),

                new Employee(41, "Amit Chaturvedi",  "Marketing",    "CMO",              450000, 45, 22, "Mumbai",    true,  List.of("Brand Strategy","Digital Mktg","Leadership")),
                new Employee(42, "Praveen Suri",     "Marketing",    "Marketing Manager",155000, 35, 12, "Bangalore", true,  List.of("SEO","SEM","Analytics")),
                new Employee(43, "Nitin Arora",      "Marketing",    "Content Strategist",80000, 27, 4,  "Delhi",     true,  List.of("Content Writing","SEO","HubSpot")),
                new Employee(44, "Lokesh Chadha",    "Marketing",    "Social Media Lead", 72000, 26, 3,  "Mumbai",    true,  List.of("Instagram","Meta Ads","Canva")),
                new Employee(45, "Suraj Solanki",    "Marketing",    "Jr Marketer",       45000, 22, 1,  "Surat",     true,  List.of("Canva","Meta Ads")),
                new Employee(46, "Bhavesh Rana",     "Marketing",    "SEO Specialist",    78000, 28, 5,  "Ahmedabad", true,  List.of("SEO","Google Ads","Analytics")),
                new Employee(47, "Umesh Pandey",     "Marketing",    "Brand Manager",    135000, 33, 10, "Kolkata",   true,  List.of("Brand Mgmt","ATL/BTL","Research")),
                new Employee(48, "Tejas Rathod",     "Marketing",    "Jr Marketer",       43000, 22, 0,  "Vadodara",  false, List.of("Canva","Social Media")),
                new Employee(49, "Chirag Shah",      "Marketing",    "Growth Hacker",     95000, 29, 6,  "Pune",      true,  List.of("Growth Hacking","A/B Testing","Python")),
                new Employee(50, "Dhruv Chawla",     "Marketing",    "Marketing Analyst", 85000, 28, 5,  "Gurugram",  true,  List.of("Google Analytics","SQL","Tableau"))
        );
    }

    // ─────────────────────────────────────────────────────────────
    // 2. PRODUCTS  (30 records across 6 categories)
    // ─────────────────────────────────────────────────────────────

    public static List<Product> giveProducts() {
        return List.of(
                // Electronics
                new Product(1,  "iPhone 15 Pro",         "Electronics", 134999, 45,  4.8, "Apple",    true),
                new Product(2,  "Samsung Galaxy S24",    "Electronics", 109999, 60,  4.6, "Samsung",  true),
                new Product(3,  "OnePlus 12",            "Electronics",  64999, 80,  4.5, "OnePlus",  true),
                new Product(4,  "MacBook Air M3",        "Electronics", 114999, 20,  4.9, "Apple",    true),
                new Product(5,  "Dell XPS 15",           "Electronics", 149999, 15,  4.7, "Dell",     true),
                new Product(6,  "Sony WH-1000XM5",       "Electronics",  29999, 100, 4.8, "Sony",     true),
                new Product(7,  "iPad Pro 12.9",         "Electronics",  99999, 30,  4.7, "Apple",    true),
                new Product(8,  "Redmi Note 13 Pro",     "Electronics",  24999, 200, 4.4, "Xiaomi",   true),

                // Clothing
                new Product(9,  "Levi's 501 Jeans",      "Clothing",     5999,  150, 4.3, "Levi's",   true),
                new Product(10, "Nike Air Force 1",      "Clothing",     8999,  80,  4.6, "Nike",     true),
                new Product(11, "Adidas Ultraboost 22",  "Clothing",    12999,  60,  4.5, "Adidas",   true),
                new Product(12, "Puma T-Shirt Combo",    "Clothing",     1499,  300, 4.1, "Puma",     true),
                new Product(13, "Woodland Boots",        "Clothing",     6499,  90,  4.4, "Woodland", true),
                new Product(14, "Raymond Suit",          "Clothing",    15999,  40,  4.7, "Raymond",  false),

                // Home Appliances
                new Product(15, "LG 1.5T AC 5 Star",    "Appliances",  45999,  25,  4.5, "LG",       true),
                new Product(16, "Samsung 8kg Washer",   "Appliances",  39999,  20,  4.4, "Samsung",  true),
                new Product(17, "Voltas 1.2T AC",       "Appliances",  34999,  30,  4.2, "Voltas",   true),
                new Product(18, "IFB Microwave 30L",    "Appliances",  14999,  55,  4.3, "IFB",      true),
                new Product(19, "Philips Air Fryer",    "Appliances",   8999,  120, 4.6, "Philips",  true),

                // Books
                new Product(20, "Effective Java",       "Books",          799,  500, 4.9, "O'Reilly", true),
                new Product(21, "Clean Code",           "Books",          699,  400, 4.8, "Prentice", true),
                new Product(22, "System Design Interview","Books",        1299, 350, 4.7, "ByteByByte",true),
                new Product(23, "The Pragmatic Programmer","Books",        899, 450, 4.8, "O'Reilly", true),
                new Product(24, "Spring in Action 6",   "Books",         1499,  200, 4.6, "Manning",  false),

                // Furniture
                new Product(25, "Wooden Study Desk",    "Furniture",    12999,  30,  4.3, "Nilkamal", true),
                new Product(26, "Ergonomic Chair",      "Furniture",    18999,  25,  4.5, "Green Soul",true),
                new Product(27, "King Size Bed Frame",  "Furniture",    34999,  10,  4.4, "Durian",   true),

                // Food & Grocery
                new Product(28, "Organic Honey 1kg",    "Grocery",        799, 600, 4.6, "Patanjali", true),
                new Product(29, "Basmati Rice 5kg",     "Grocery",        599, 1000,4.5, "India Gate",true),
                new Product(30, "Cold Pressed Olive Oil","Grocery",      1299,  400, 4.7, "DiSano",   true)
        );
    }

    // ─────────────────────────────────────────────────────────────
    // 3. ORDERS  (30 records)
    // ─────────────────────────────────────────────────────────────

    public static List<Order> giveOrders() {
        return List.of(
                new Order(1001, 1, "Viraj Mehta",      List.of("iPhone 15 Pro","AirPods"),       142999, "DELIVERED", LocalDate.of(2024,1,5),  "Mumbai"),
                new Order(1002, 2, "Haris Khan",       List.of("Effective Java","Clean Code"),     1498, "DELIVERED", LocalDate.of(2024,1,8),  "Pune"),
                new Order(1003, 3, "Shubham Joshi",    List.of("MacBook Air M3"),                114999, "SHIPPED",   LocalDate.of(2024,1,12), "Bangalore"),
                new Order(1004, 4, "Shariq Ali",       List.of("Ergonomic Chair","Study Desk"),   31998, "DELIVERED", LocalDate.of(2024,1,15), "Hyderabad"),
                new Order(1005, 5, "Ashutosh Verma",   List.of("Philips Air Fryer"),               8999, "DELIVERED", LocalDate.of(2024,1,18), "Chennai"),
                new Order(1006, 6, "Prajiy Nair",      List.of("Redmi Note 13 Pro"),              24999, "CANCELLED", LocalDate.of(2024,1,20), "Kochi"),
                new Order(1007, 7, "Rohan Gupta",      List.of("Dell XPS 15","Sony WH-1000XM5"), 179998, "DELIVERED", LocalDate.of(2024,2,1),  "Mumbai"),
                new Order(1008, 8, "Aman Singh",       List.of("Samsung Galaxy S24"),            109999, "SHIPPED",   LocalDate.of(2024,2,3),  "Delhi"),
                new Order(1009, 9, "Rahul Sharma",     List.of("Spring in Action 6","Clean Code"),  2198, "PLACED",   LocalDate.of(2024,2,5),  "Pune"),
                new Order(1010,10, "Karan Bajaj",      List.of("Levi's 501 Jeans","Nike AF1"),    14998, "DELIVERED", LocalDate.of(2024,2,8),  "Bangalore"),

                new Order(1011,11, "Vikram Tiwari",    List.of("LG 1.5T AC 5 Star"),             45999, "DELIVERED", LocalDate.of(2024,2,10), "Mumbai"),
                new Order(1012,12, "Arjun Patel",      List.of("Organic Honey 1kg","Basmati Rice 5kg"),1398,"DELIVERED",LocalDate.of(2024,2,12),"Ahmedabad"),
                new Order(1013,13, "Siddharth Roy",    List.of("OnePlus 12"),                    64999, "CANCELLED", LocalDate.of(2024,2,15), "Kolkata"),
                new Order(1014,14, "Ankit Sharma",     List.of("iPad Pro 12.9"),                 99999, "SHIPPED",   LocalDate.of(2024,2,18), "Delhi"),
                new Order(1015,15, "Nikhil Dubey",     List.of("Puma T-Shirt Combo","Adidas Ultraboost"),14498,"DELIVERED",LocalDate.of(2024,3,1),"Jaipur"),

                new Order(1016,16, "Manish Yadav",     List.of("Samsung 8kg Washer"),            39999, "DELIVERED", LocalDate.of(2024,3,5),  "Lucknow"),
                new Order(1017,17, "Deepak Mishra",    List.of("iPhone 15 Pro","MacBook Air M3"),249998,"DELIVERED",  LocalDate.of(2024,3,8),  "Mumbai"),
                new Order(1018,18, "Saurabh Sinha",    List.of("System Design Interview"),        1299, "PLACED",    LocalDate.of(2024,3,10), "Patna"),
                new Order(1019,19, "Abhishek Das",     List.of("Woodland Boots"),                 6499, "DELIVERED", LocalDate.of(2024,3,12), "Bhubaneswar"),
                new Order(1020,20, "Pratik Mukherjee", List.of("King Size Bed Frame"),           34999, "SHIPPED",   LocalDate.of(2024,3,15), "Kolkata"),

                new Order(1021,21, "Yash Kapoor",      List.of("IFB Microwave 30L"),             14999, "DELIVERED", LocalDate.of(2024,3,18), "Mumbai"),
                new Order(1022,22, "Aditya Kumar",     List.of("Redmi Note 13 Pro","Puma T-Shirt Combo"),26498,"DELIVERED",LocalDate.of(2024,3,20),"Delhi"),
                new Order(1023,23, "Akash Reddy",      List.of("Sony WH-1000XM5"),               29999, "DELIVERED", LocalDate.of(2024,4,1),  "Hyderabad"),
                new Order(1024,24, "Ritesh Bansal",    List.of("Effective Java","Pragmatic Programmer"),1698,"CANCELLED",LocalDate.of(2024,4,3),"Noida"),
                new Order(1025,25, "Sumit Aggarwal",   List.of("Nike Air Force 1"),               8999, "PLACED",    LocalDate.of(2024,4,5),  "Gurugram"),

                new Order(1026,26, "Pankaj Shukla",    List.of("Cold Pressed Olive Oil","Organic Honey 1kg"),2098,"DELIVERED",LocalDate.of(2024,4,8),"Kanpur"),
                new Order(1027,27, "Rajat Chopra",     List.of("Ergonomic Chair"),               18999, "DELIVERED", LocalDate.of(2024,4,10), "Bangalore"),
                new Order(1028,28, "Mohit Sethi",      List.of("Voltas 1.2T AC"),                34999, "SHIPPED",   LocalDate.of(2024,4,12), "Chandigarh"),
                new Order(1029,29, "Sachin Pillai",    List.of("Dell XPS 15"),                  149999, "DELIVERED", LocalDate.of(2024,4,15), "Chennai"),
                new Order(1030,30, "Rakesh Iyer",      List.of("Raymond Suit"),                  15999, "PLACED",    LocalDate.of(2024,4,18), "Coimbatore")
        );
    }

    // ─────────────────────────────────────────────────────────────
    // 4. STUDENTS  (30 records across 5 branches)
    // ─────────────────────────────────────────────────────────────

    public static List<Student> giveStudents() {
        return List.of(
                new Student(1,  "Varun Malhotra",  21, "Computer Science",      9.1, 7, false, List.of("DSA","OS","DBMS","CN","Java")),
                new Student(2,  "Tarun Grover",    20, "Computer Science",      7.8, 5, false, List.of("DSA","OS","DBMS","Java")),
                new Student(3,  "Gaurav Agarwal",  22, "Computer Science",      6.4, 7, true,  List.of("DSA","OS","CN")),
                new Student(4,  "Naveen Jain",     20, "Computer Science",      8.5, 5, false, List.of("DSA","DBMS","Java","CN")),
                new Student(5,  "Kunal Saxena",    21, "Computer Science",      9.4, 7, false, List.of("DSA","ML","CN","Java","OS")),
                new Student(6,  "Mayank Trivedi",  22, "Computer Science",      5.9, 7, true,  List.of("OS","DBMS")),

                new Student(7,  "Akhil Rastogi",   20, "Information Technology",7.2, 5, false, List.of("Web Dev","Cloud","DBMS")),
                new Student(8,  "Rohit Bhatt",     21, "Information Technology",8.0, 7, false, List.of("Web Dev","Linux","Networking")),
                new Student(9,  "Hemant Goel",     22, "Information Technology",6.7, 7, true,  List.of("Cloud","DBMS")),
                new Student(10, "Vivek Nanda",     20, "Information Technology",9.0, 5, false, List.of("Web Dev","Cloud","Linux","Cybersecurity")),
                new Student(11, "Amit Chaturvedi", 21, "Information Technology",7.5, 7, false, List.of("Web Dev","Networking")),

                new Student(12, "Praveen Suri",    21, "Electronics",           8.2, 7, false, List.of("VLSI","Signals","Embedded C")),
                new Student(13, "Nitin Arora",     20, "Electronics",           7.0, 5, false, List.of("VLSI","Digital Electronics")),
                new Student(14, "Lokesh Chadha",   22, "Electronics",           5.5, 7, true,  List.of("Signals","Circuits")),
                new Student(15, "Suraj Solanki",   20, "Electronics",           9.2, 5, false, List.of("VLSI","Signals","Embedded C","IoT")),
                new Student(16, "Bhavesh Rana",    21, "Electronics",           6.8, 7, true,  List.of("Digital Electronics","Circuits")),

                new Student(17, "Umesh Pandey",    21, "Mechanical",            7.3, 7, false, List.of("Thermodynamics","Fluid Mech","CAD")),
                new Student(18, "Tejas Rathod",    20, "Mechanical",            6.1, 5, true,  List.of("Thermodynamics","CAD")),
                new Student(19, "Chirag Shah",     22, "Mechanical",            8.6, 7, false, List.of("Thermodynamics","Fluid Mech","CAD","SolidWorks")),
                new Student(20, "Dhruv Chawla",    21, "Mechanical",            7.9, 7, false, List.of("Fluid Mech","CAD","Manufacturing")),
                new Student(21, "Viraj Mehta",     20, "Mechanical",            5.7, 5, true,  List.of("Thermodynamics")),

                new Student(22, "Haris Khan",      21, "Civil",                 8.1, 7, false, List.of("Structural Engg","AutoCAD","Surveying")),
                new Student(23, "Shubham Joshi",   22, "Civil",                 7.4, 7, false, List.of("Structural Engg","AutoCAD")),
                new Student(24, "Shariq Ali",      20, "Civil",                 9.3, 5, false, List.of("Structural Engg","AutoCAD","Surveying","STAAD Pro")),
                new Student(25, "Ashutosh Verma",  21, "Civil",                 6.0, 7, true,  List.of("AutoCAD","Surveying")),
                new Student(26, "Prajiy Nair",     20, "Civil",                 7.7, 5, false, List.of("Structural Engg","STAAD Pro")),
                new Student(27, "Rohan Gupta",     22, "Civil",                 5.3, 7, true,  List.of("AutoCAD")),
                new Student(28, "Aman Singh",      21, "Civil",                 8.8, 7, false, List.of("Structural Engg","AutoCAD","Surveying","GIS")),
                new Student(29, "Rahul Sharma",    20, "Civil",                 7.1, 5, false, List.of("Structural Engg","AutoCAD")),
                new Student(30, "Karan Bajaj",     22, "Civil",                 6.5, 7, true,  List.of("AutoCAD","Surveying"))
        );
    }

    // ─────────────────────────────────────────────────────────────
    // 5. TRANSACTIONS  (30 records for banking-style stream problems)
    // ─────────────────────────────────────────────────────────────

    public static List<Transaction> giveTransactions() {
        return List.of(
                new Transaction("TXN001","Viraj Mehta",    "CREDIT",120000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN002","Viraj Mehta",    "DEBIT",   4500,"FOOD",     LocalDateTime.of(2024,1,3,13,0), "SUCCESS"),
                new Transaction("TXN003","Viraj Mehta",    "DEBIT",  12000,"SHOPPING", LocalDateTime.of(2024,1,5,16,0), "SUCCESS"),
                new Transaction("TXN004","Viraj Mehta",    "DEBIT",  18000,"EMI",      LocalDateTime.of(2024,1,7,9,0),  "SUCCESS"),
                new Transaction("TXN005","Haris Khan",     "CREDIT",  55000,"SALARY",  LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN006","Haris Khan",     "DEBIT",   2200,"FOOD",     LocalDateTime.of(2024,1,4,12,0), "SUCCESS"),
                new Transaction("TXN007","Haris Khan",     "DEBIT",   8000,"TRAVEL",   LocalDateTime.of(2024,1,6,8,0),  "FAILED"),
                new Transaction("TXN008","Shubham Joshi",  "CREDIT", 180000,"SALARY",  LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN009","Shubham Joshi",  "DEBIT",  5500,"FOOD",      LocalDateTime.of(2024,1,5,19,0), "SUCCESS"),
                new Transaction("TXN010","Shubham Joshi",  "DEBIT", 25000,"SHOPPING",  LocalDateTime.of(2024,1,8,15,0), "SUCCESS"),

                new Transaction("TXN011","Ashutosh Verma", "CREDIT",125000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN012","Ashutosh Verma", "DEBIT",  3200,"FOOD",      LocalDateTime.of(2024,1,3,14,0), "SUCCESS"),
                new Transaction("TXN013","Ashutosh Verma", "DEBIT", 15000,"TRAVEL",    LocalDateTime.of(2024,1,10,7,0), "SUCCESS"),
                new Transaction("TXN014","Ashutosh Verma", "DEBIT", 10000,"UTILITY",   LocalDateTime.of(2024,1,12,11,0),"SUCCESS"),
                new Transaction("TXN015","Rohan Gupta",    "CREDIT",250000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN016","Rohan Gupta",    "DEBIT",  8000,"FOOD",      LocalDateTime.of(2024,1,4,20,0), "SUCCESS"),
                new Transaction("TXN017","Rohan Gupta",    "DEBIT", 50000,"SHOPPING",  LocalDateTime.of(2024,1,9,17,0), "SUCCESS"),
                new Transaction("TXN018","Rohan Gupta",    "DEBIT", 45000,"EMI",       LocalDateTime.of(2024,1,7,9,0),  "SUCCESS"),
                new Transaction("TXN019","Karan Bajaj",    "CREDIT",118000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN020","Karan Bajaj",    "DEBIT",  4000,"FOOD",      LocalDateTime.of(2024,1,5,13,0), "SUCCESS"),

                new Transaction("TXN021","Karan Bajaj",    "DEBIT", 20000,"TRAVEL",    LocalDateTime.of(2024,1,11,6,0), "PENDING"),
                new Transaction("TXN022","Ankit Sharma",   "CREDIT",135000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN023","Ankit Sharma",   "DEBIT",  6000,"FOOD",      LocalDateTime.of(2024,1,6,12,0), "SUCCESS"),
                new Transaction("TXN024","Ankit Sharma",   "DEBIT", 35000,"SHOPPING",  LocalDateTime.of(2024,1,8,16,0), "FAILED"),
                new Transaction("TXN025","Vikram Tiwari",  "CREDIT",150000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN026","Vikram Tiwari",  "DEBIT",  5000,"FOOD",      LocalDateTime.of(2024,1,3,13,0), "SUCCESS"),
                new Transaction("TXN027","Vikram Tiwari",  "DEBIT", 30000,"EMI",       LocalDateTime.of(2024,1,7,9,0),  "SUCCESS"),
                new Transaction("TXN028","Vikram Tiwari",  "DEBIT", 12000,"UTILITY",   LocalDateTime.of(2024,1,14,11,0),"SUCCESS"),
                new Transaction("TXN029","Deepak Mishra",  "CREDIT",200000,"SALARY",   LocalDateTime.of(2024,1,1,10,0), "SUCCESS"),
                new Transaction("TXN030","Deepak Mishra",  "DEBIT",  9000,"FOOD",      LocalDateTime.of(2024,1,5,20,0), "SUCCESS")
        );
    }

    // ─────────────────────────────────────────────────────────────
    // 6. LEGACY COLLECTIONS (retained & enhanced)
    // ─────────────────────────────────────────────────────────────

    public static List<String> giveNames() {
        return List.of(
                "Viraj","Haris","Shubham","Shariq","Ashutosh","Prajiy","Rohan","Aman",
                "Rahul","Karan","Vikram","Arjun","Siddharth","Ankit","Nikhil","Manish",
                "Deepak","Saurabh","Abhishek","Pratik","Yash","Aditya","Akash","Ritesh",
                "Sumit","Pankaj","Rajat","Mohit","Sachin","Rakesh","Varun","Tarun",
                "Gaurav","Naveen","Kunal","Mayank","Akhil","Rohit","Hemant","Vivek",
                "Amit","Praveen","Nitin","Lokesh","Suraj","Bhavesh","Umesh","Tejas",
                "Chirag","Dhruv"
        );
    }

    public static List<Integer> giveNums() {
        return List.of(
                45, 12, 78, 34, 90, 23, 56, 89, 11, 67,
                43, 21, 98, 76, 54, 32, 19, 87, 65, 44,
                29, 73, 15, 92, 38, 61, 27, 84, 49, 17,
                95, 52, 31, 68, 24, 81, 13, 57, 99, 36,
                71, 28, 46, 88, 14, 63, 22, 79, 41, 58
        );
    }

    public static List<Double> giveSalaries() {
        return List.of(
                120000.0, 55000.0, 180000.0, 110000.0, 125000.0,
                48000.0, 250000.0, 82000.0, 65000.0, 118000.0,
                150000.0, 60000.0, 58000.0, 135000.0, 72000.0,
                55000.0, 200000.0, 61000.0, 68000.0, 75000.0,
                140000.0, 55000.0, 95000.0, 52000.0, 80000.0,
                70000.0, 145000.0, 53000.0, 115000.0, 50000.0,
                500000.0, 175000.0, 95000.0, 52000.0, 110000.0,
                130000.0, 48000.0, 160000.0, 105000.0, 92000.0,
                450000.0, 155000.0, 80000.0, 72000.0, 45000.0,
                78000.0, 135000.0, 43000.0, 95000.0, 85000.0
        );
    }

    /** Dept → avg salary (for joining / map stream exercises) */
    public static Map<String, Double> giveDeptBudget() {
        return Map.of(
                "Engineering", 5_000_000.0,
                "Sales",       3_000_000.0,
                "HR",          2_000_000.0,
                "Finance",     4_000_000.0,
                "Marketing",   2_500_000.0
        );
    }
}