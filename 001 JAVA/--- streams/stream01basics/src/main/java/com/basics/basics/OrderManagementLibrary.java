package com.basics.basics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.stream.*;

/**
 * ================================================================
 *  ENTERPRISE ORDER MANAGEMENT LIBRARY  —  v2.0
 * ================================================================
 *
 *  QUICK START:
 *
 *    // Small  (1 000 orders)
 *    var db = OrderManagementLibrary.createDatabase(DataConfig.small());
 *
 *    // Medium (50 000 orders)
 *    var db = OrderManagementLibrary.createDatabase(DataConfig.medium());
 *
 *    // Large  (100 000 orders)  ← default
 *    var db = OrderManagementLibrary.createDatabase();
 *
 *    // Enterprise (1 000 000 orders — needs -Xmx2g)
 *    var db = OrderManagementLibrary.createDatabase(DataConfig.enterprise());
 *
 *    // Get repositories
 *    var orderRepo    = db.orderRepository();
 *    var customerRepo = db.customerRepository();
 *    var productRepo  = db.productRepository();
 *    var analytics    = db.analyticsService();
 *
 *    // Practice streams
 *    db.orders().stream()
 *      .filter(o -> o.finalAmount().compareTo(new BigDecimal("100000")) > 0)
 *      .sorted(Comparator.comparing(Order::finalAmount).reversed())
 *      .limit(10)
 *      .forEach(System.out::println);
 *
 * ================================================================
 */
public final class OrderManagementLibrary {

    private OrderManagementLibrary() {}

    // ============================================================
    // SECTION 1 — CONFIGURATION
    // ============================================================

    public static final class DataConfig {

        public int customerCount = 10_000;
        public int productCount  = 200;
        public int orderCount    = 100_000;
        public int supplierCount = 50;
        public int reviewCount   = 200_000;
        public long seed         = 42L;
        public LocalDate startDate = LocalDate.of(2023, 1, 1);
        public LocalDate endDate   = LocalDate.of(2026, 12, 31);

        /** 1 000 orders — quick tests */
        public static DataConfig small() {
            DataConfig c = new DataConfig();
            c.customerCount = 100;
            c.productCount  = 30;
            c.orderCount    = 1_000;
            c.supplierCount = 10;
            c.reviewCount   = 5_000;
            return c;
        }

        /** 50 000 orders */
        public static DataConfig medium() {
            DataConfig c = new DataConfig();
            c.customerCount = 2_000;
            c.productCount  = 100;
            c.orderCount    = 50_000;
            c.supplierCount = 25;
            c.reviewCount   = 100_000;
            return c;
        }

        /** 100 000 orders (default) */
        public static DataConfig large() { return new DataConfig(); }

        /** 1 000 000 orders — needs -Xmx2g */
        public static DataConfig enterprise() {
            DataConfig c = new DataConfig();
            c.customerCount = 50_000;
            c.productCount  = 500;
            c.orderCount    = 1_000_000;
            c.supplierCount = 150;
            c.reviewCount   = 2_000_000;
            return c;
        }
    }

    // ============================================================
    // SECTION 2 — ENUMS
    // ============================================================

    /**
     * Full order lifecycle.
     * Spring JPA: @Enumerated(EnumType.STRING)
     */
    public enum OrderStatus {
        DRAFT, CREATED, CONFIRMED, PROCESSING,
        PACKED, SHIPPED, OUT_FOR_DELIVERY,
        DELIVERED, CANCELLED, RETURNED, REFUNDED, ON_HOLD;

        public boolean isActive() {
            return this == CONFIRMED || this == PROCESSING ||
                    this == PACKED   || this == SHIPPED    ||
                    this == OUT_FOR_DELIVERY;
        }
        public boolean isCompleted()  { return this == DELIVERED; }
        public boolean isCancelled()  {
            return this == CANCELLED || this == RETURNED || this == REFUNDED;
        }
    }

    public enum PaymentStatus {
        PENDING, AUTHORIZED, CAPTURED, PAID,
        FAILED, REFUNDED, PARTIALLY_REFUNDED, CHARGEBACK
    }

    public enum PaymentMethod {
        CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING,
        WALLET, CASH_ON_DELIVERY, EMI, BUY_NOW_PAY_LATER
    }

    /**
     * Customer loyalty tier.
     * Each tier carries minimum spend and discount %.
     */
    public enum CustomerTier {
        STANDARD(0,       0),
        BRONZE  (10_000,  5),
        SILVER  (50_000,  10),
        GOLD    (1_00_000,15),
        PLATINUM(5_00_000,20),
        DIAMOND (10_00_000,25),
        ENTERPRISE(50_00_000,30);

        public final int minSpend;
        public final int discountPercent;
        CustomerTier(int minSpend, int discountPercent) {
            this.minSpend        = minSpend;
            this.discountPercent = discountPercent;
        }
    }

    public enum Category {
        ELECTRONICS, MOBILE, LAPTOP, TABLET,
        ACCESSORIES, HOME_APPLIANCES, KITCHEN,
        OFFICE, GAMING, WEARABLES,
        SPORTS, BEAUTY
    }

    public enum Priority    { LOW, NORMAL, HIGH, URGENT, CRITICAL }
    public enum Region      { NORTH, SOUTH, EAST, WEST, CENTRAL, NORTHEAST }
    public enum DiscountType{ PERCENTAGE, FLAT, BOGO, BULK, LOYALTY }

    public enum ShipmentCarrier {
        BLUEDART, DELHIVERY, FEDEX, DHL,
        DTDC, ECOM_EXPRESS, INDIA_POST, SHADOWFAX
    }

    public enum ShipmentStatus {
        LABEL_CREATED, PICKED_UP, IN_TRANSIT,
        OUT_FOR_DELIVERY, DELIVERED, RETURNED
    }

    // ============================================================
    // SECTION 3 — DOMAIN MODELS
    // ============================================================

    /**
     * Address — embedded value object.
     * Spring JPA: @Embeddable on a separate class.
     */
    public record Address(
            String street,
            String city,
            String state,
            String pincode,
            Region region
    ) {
        @Override public String toString() {
            return street + ", " + city + " - " + pincode + ", " + state;
        }
    }

    /**
     * Supplier — master data.
     * Spring JPA: @Entity @Table(name = "suppliers")
     */
    public record Supplier(
            int      supplierId,
            String   name,
            String   contactEmail,
            Address  address,
            List<Category> categories,
            LocalDate partnerSince,
            boolean  verified
    ) {}

    /**
     * Customer — core entity.
     * Spring JPA: @Entity @Table(name = "customers")
     */
    public record Customer(
            int          customerId,
            String       name,
            String       email,
            String       phone,
            Address      address,
            CustomerTier tier,
            LocalDate    joinDate,
            int          totalOrders,
            BigDecimal   totalSpent,
            int          loyaltyPoints,
            boolean      active
    ) {
        public boolean isPremium() {
            return tier == CustomerTier.GOLD     ||
                    tier == CustomerTier.PLATINUM ||
                    tier == CustomerTier.DIAMOND  ||
                    tier == CustomerTier.ENTERPRISE;
        }
        public String ltv() {
            if (totalSpent.compareTo(new BigDecimal("1000000")) >= 0) return "HIGH";
            if (totalSpent.compareTo(new BigDecimal("100000"))  >= 0) return "MEDIUM";
            return "LOW";
        }
    }

    /**
     * Product — catalog entity.
     * Spring JPA: @Entity @Table(name = "products")
     */
    public record Product(
            int        productId,
            String     sku,
            String     name,
            String     brand,
            Category   category,
            BigDecimal price,
            BigDecimal costPrice,
            int        supplierId,
            int        stockCount,
            double     rating,
            int        reviewCount,
            boolean    active,
            LocalDate  listedDate
    ) {
        public double marginPercent() {
            return price.subtract(costPrice)
                    .multiply(BigDecimal.valueOf(100))
                    .divide(price, 2, RoundingMode.HALF_UP)
                    .doubleValue();
        }
        public boolean inStock() { return stockCount > 0; }
    }

    /**
     * OrderItem — line item within an order.
     * Spring JPA: @Entity @ManyToOne Order
     */
    public record OrderItem(
            int        productId,
            String     productName,
            String     brand,
            Category   category,
            int        quantity,
            BigDecimal unitPrice,
            BigDecimal discount,
            DiscountType discountType
    ) {
        public BigDecimal effectivePrice() { return unitPrice.subtract(discount); }
        public BigDecimal totalPrice()     { return effectivePrice().multiply(BigDecimal.valueOf(quantity)); }
    }

    /**
     * Coupon — discount coupon applied to an order.
     */
    public record Coupon(
            String       code,
            DiscountType type,
            BigDecimal   value,
            BigDecimal   minOrderValue,
            BigDecimal   maxDiscount,
            LocalDate    expiryDate
    ) {}

    /**
     * Shipment — shipping information.
     * Spring JPA: @OneToOne mapped from Order.
     */
    public record Shipment(
            String          shipmentId,
            ShipmentCarrier carrier,
            String          trackingNumber,
            LocalDate       estimatedDelivery,
            LocalDate       actualDelivery,
            ShipmentStatus  status
    ) {}

    /**
     * Review — product review by a customer.
     * Spring JPA: @Entity @Table(name = "reviews")
     */
    public record Review(
            int       reviewId,
            int       productId,
            int       customerId,
            String    customerName,
            int       rating,           // 1–5
            String    title,
            String    comment,
            LocalDate reviewDate,
            int       helpfulVotes,
            boolean   verified
    ) {}

    /**
     * Order — the central aggregate root.
     * Spring JPA: @Entity @Table(name = "orders")
     *             @EntityListeners(AuditingEntityListener.class)
     *
     * Computed fields (NOT stored in DB, derived at runtime):
     *   subtotal(), itemDiscount(), couponDiscount(),
     *   totalDiscount(), gstAmount(), finalAmount()
     */
    public record Order(
            int           orderId,
            Customer      customer,
            List<OrderItem> items,
            OrderStatus   status,
            PaymentStatus paymentStatus,
            PaymentMethod paymentMethod,
            LocalDate     orderDate,
            LocalDate     expectedDelivery,
            LocalDate     actualDelivery,    // null if not yet delivered
            Address       shippingAddress,
            Coupon        coupon,            // null if no coupon
            Shipment      shipment,          // null if not yet shipped
            Priority      priority,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        /** Subtotal before any discount */
        public BigDecimal subtotal() {
            return items.stream()
                    .map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        /** Item-level discounts total */
        public BigDecimal itemDiscount() {
            return items.stream()
                    .map(i -> i.discount().multiply(BigDecimal.valueOf(i.quantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        /** Coupon discount amount */
        public BigDecimal couponDiscount() {
            if (coupon == null) return BigDecimal.ZERO;
            BigDecimal base = subtotal().subtract(itemDiscount());
            if (coupon.type() == DiscountType.PERCENTAGE) {
                return base.multiply(coupon.value())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        .min(coupon.maxDiscount());
            } else if (coupon.type() == DiscountType.FLAT) {
                return coupon.value().min(base);
            }
            return BigDecimal.ZERO;
        }

        public BigDecimal totalDiscount() { return itemDiscount().add(couponDiscount()); }

        /** GST @ 18% on taxable amount */
        public BigDecimal gstAmount() {
            return subtotal().subtract(totalDiscount())
                    .multiply(new BigDecimal("0.18"))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        /** ★ Final payable amount — use this in Stream exercises */
        public BigDecimal finalAmount() {
            return subtotal()
                    .subtract(totalDiscount())
                    .add(gstAmount())
                    .max(BigDecimal.ZERO);
        }

        public int    totalQuantity()    { return items.stream().mapToInt(OrderItem::quantity).sum(); }
        public int    distinctProducts() { return (int) items.stream().map(OrderItem::productId).distinct().count(); }
        public Set<Category> categories(){ return items.stream().map(OrderItem::category).collect(Collectors.toSet()); }

        // Convenience accessors
        public CustomerTier customerTier()  { return customer.tier(); }
        public String       shippingCity()  { return shippingAddress.city(); }
        public Region       shippingRegion(){ return shippingAddress.region(); }
        public boolean      isDelivered()   { return status == OrderStatus.DELIVERED; }
        public boolean      isPriority()    {
            return priority == Priority.HIGH   ||
                    priority == Priority.URGENT ||
                    priority == Priority.CRITICAL;
        }
    }

    // ============================================================
    // SECTION 4 — DTOs
    // These are what you create with stream().map()
    // ============================================================

    /** Lightweight order summary — great for list views */
    public record OrderSummaryDTO(
            int          orderId,
            String       customerName,
            CustomerTier customerTier,
            BigDecimal   finalAmount,
            OrderStatus  status,
            LocalDate    orderDate,
            String       shippingCity
    ) {}

    /** Customer analytics report */
    public record CustomerReportDTO(
            int          customerId,
            String       name,
            CustomerTier tier,
            String       city,
            long         orderCount,
            BigDecimal   totalRevenue,
            BigDecimal   avgOrderValue,
            BigDecimal   maxOrderValue
    ) {}

    /** Product sales report */
    public record ProductSalesDTO(
            int        productId,
            String     productName,
            String     brand,
            Category   category,
            long       unitsSold,
            BigDecimal totalRevenue,
            double     avgRating
    ) {}

    /** Monthly revenue snapshot */
    public record MonthlyRevenueDTO(
            int        year,
            Month      month,
            long       orderCount,
            BigDecimal totalRevenue,
            BigDecimal avgOrderValue
    ) {}

    /** City-level analytics */
    public record CityAnalyticsDTO(
            String     city,
            Region     region,
            long       orderCount,
            BigDecimal totalRevenue,
            long       customerCount
    ) {}

    // ============================================================
    // SECTION 5 — PAGINATION  (mirrors Spring's Pageable / Page<T>)
    // ============================================================

    /**
     * Spring equivalent: PageRequest.of(page, size)
     */
    public record PageRequest(int page, int size) {
        public int offset() { return page * size; }
        public static PageRequest of(int page, int size)  { return new PageRequest(page, size); }
        public static PageRequest first(int size)         { return new PageRequest(0, size); }
    }

    /**
     * Spring equivalent: Page<T>
     */
    public record Page<T>(
            List<T> content,
            int     pageNumber,
            int     pageSize,
            long    totalElements,
            int     totalPages
    ) {
        public boolean hasNext()     { return pageNumber < totalPages - 1; }
        public boolean hasPrevious() { return pageNumber > 0; }
        public boolean isFirst()     { return pageNumber == 0; }
        public boolean isLast()      { return pageNumber == totalPages - 1; }

        /** Static factory — slice a list into a page */
        public static <T> Page<T> of(List<T> all, PageRequest req) {
            int total      = all.size();
            int totalPages = (int) Math.ceil((double) total / req.size());
            List<T> content = all.stream()
                    .skip(req.offset())
                    .limit(req.size())
                    .collect(Collectors.toList());
            return new Page<>(content, req.page(), req.size(), total, totalPages);
        }
    }

    // ============================================================
    // SECTION 6 — REPOSITORY INTERFACE
    // Spring equivalent: JpaRepository<T, ID>
    // ============================================================

    public interface CrudRepository<T, ID> {
        List<T>          findAll();
        Optional<T>      findById(ID id);
        long             count();
        Page<T>          findAll(PageRequest pageRequest);
    }

    // ============================================================
    // SECTION 7 — DATA GENERATOR  (private, deterministic)
    // ============================================================

    private static final class DataGenerator {

        // ── Static reference data ─────────────────────────────────

        private static final String[] FIRST_NAMES = {
                "Aarav","Vivaan","Aditya","Arjun","Rahul","Rohan","Vikram","Karan","Amit","Varun",
                "Siddharth","Raj","Dev","Neil","Kunal","Manish","Suresh","Dinesh","Mahesh","Rajesh",
                "Pradeep","Sandeep","Deepak","Ashok","Vijay","Ajay","Sanjay","Vinay","Nikhil","Harsh",
                "Akash","Gaurav","Himanshu","Tarun","Vaibhav","Piyush","Kartik","Nitin","Lalit","Sachin",
                "Neha","Priya","Ananya","Isha","Kavya","Sneha","Pooja","Riya","Meera","Diya",
                "Nisha","Divya","Geeta","Shweta","Preeti","Anjali","Shruti","Simran","Pallavi","Jyoti",
                "Smita","Sunita","Savita","Madhuri","Rekha","Leela","Asha","Usha","Sonal","Radha"
        };

        private static final String[] LAST_NAMES = {
                "Sharma","Verma","Singh","Gupta","Patel","Shah","Mehta","Joshi","Kumar","Nair",
                "Menon","Pillai","Reddy","Rao","Naidu","Iyer","Chaudhary","Tiwari","Pandey","Mishra",
                "Shukla","Tripathi","Agarwal","Bansal","Garg","Mittal","Goyal","Jain","Desai","Parikh",
                "Bhatt","Trivedi","Chauhan","Yadav","Rajput","Choudhury","Bose","Ghosh","Das","Sen",
                "Kapoor","Khanna","Malhotra","Bhatia","Arora","Sethi","Chopra","Saxena","Srivastava","Dubey"
        };

        // {city, state, pincodePrefix, region}
        private static final String[][] CITIES = {
                {"Delhi","Delhi","110","NORTH"},
                {"Gurgaon","Haryana","122","NORTH"},
                {"Noida","Uttar Pradesh","201","NORTH"},
                {"Lucknow","Uttar Pradesh","226","NORTH"},
                {"Jaipur","Rajasthan","302","NORTH"},
                {"Chandigarh","Punjab","160","NORTH"},
                {"Agra","Uttar Pradesh","282","NORTH"},
                {"Varanasi","Uttar Pradesh","221","NORTH"},
                {"Mumbai","Maharashtra","400","WEST"},
                {"Pune","Maharashtra","411","WEST"},
                {"Ahmedabad","Gujarat","380","WEST"},
                {"Surat","Gujarat","395","WEST"},
                {"Vadodara","Gujarat","390","WEST"},
                {"Nashik","Maharashtra","422","WEST"},
                {"Bangalore","Karnataka","560","SOUTH"},
                {"Hyderabad","Telangana","500","SOUTH"},
                {"Chennai","Tamil Nadu","600","SOUTH"},
                {"Coimbatore","Tamil Nadu","641","SOUTH"},
                {"Kochi","Kerala","682","SOUTH"},
                {"Mysore","Karnataka","570","SOUTH"},
                {"Visakhapatnam","Andhra Pradesh","530","SOUTH"},
                {"Kolkata","West Bengal","700","EAST"},
                {"Bhubaneswar","Odisha","751","EAST"},
                {"Patna","Bihar","800","EAST"},
                {"Ranchi","Jharkhand","834","EAST"},
                {"Bhopal","Madhya Pradesh","462","CENTRAL"},
                {"Indore","Madhya Pradesh","452","CENTRAL"},
                {"Nagpur","Maharashtra","440","CENTRAL"},
                {"Raipur","Chhattisgarh","492","CENTRAL"},
                {"Guwahati","Assam","781","NORTHEAST"},
                {"Shillong","Meghalaya","793","NORTHEAST"},
        };

        // {name, brand, category, price, costPrice}
        private static final Object[][] PRODUCTS = {
                {"MacBook Pro 14\"",          "Apple",     "LAPTOP",          180000, 130000},
                {"MacBook Air M3",            "Apple",     "LAPTOP",          125000,  90000},
                {"Dell XPS 15",               "Dell",      "LAPTOP",          145000, 105000},
                {"Dell Inspiron 15",          "Dell",      "LAPTOP",           65000,  45000},
                {"HP Spectre x360",           "HP",        "LAPTOP",          155000, 115000},
                {"HP Pavilion 15",            "HP",        "LAPTOP",           55000,  38000},
                {"ThinkPad X1 Carbon",        "Lenovo",    "LAPTOP",          165000, 120000},
                {"IdeaPad Slim 5",            "Lenovo",    "LAPTOP",           70000,  50000},
                {"ZenBook 14",                "ASUS",      "LAPTOP",           90000,  65000},
                {"Acer Swift 3",              "Acer",      "LAPTOP",           60000,  42000},
                {"iPhone 15 Pro Max",         "Apple",     "MOBILE",          159900, 115000},
                {"iPhone 15",                 "Apple",     "MOBILE",           89900,  65000},
                {"Galaxy S24 Ultra",          "Samsung",   "MOBILE",          130000,  90000},
                {"Galaxy S24",                "Samsung",   "MOBILE",           79999,  55000},
                {"OnePlus 12",                "OnePlus",   "MOBILE",           64999,  45000},
                {"Pixel 8 Pro",               "Google",    "MOBILE",          106999,  75000},
                {"Xiaomi 14 Pro",             "Xiaomi",    "MOBILE",           69999,  48000},
                {"Vivo X100 Pro",             "Vivo",      "MOBILE",           89999,  62000},
                {"OPPO Find X7",              "OPPO",      "MOBILE",           74999,  52000},
                {"Realme GT 5",               "Realme",    "MOBILE",           39999,  28000},
                {"iPad Pro 12.9\"",           "Apple",     "TABLET",          129900,  95000},
                {"iPad Air M2",               "Apple",     "TABLET",           79900,  58000},
                {"Galaxy Tab S9",             "Samsung",   "TABLET",           89999,  65000},
                {"Tab P12 Pro",               "Lenovo",    "TABLET",           59999,  42000},
                {"Bravia 65\" 4K",            "Sony",      "HOME_APPLIANCES", 120000,  85000},
                {"OLED 55\"",                 "LG",        "HOME_APPLIANCES", 150000, 108000},
                {"QLED 75\"",                 "Samsung",   "HOME_APPLIANCES", 180000, 130000},
                {"Dyson V15 Detect",          "Dyson",     "HOME_APPLIANCES",  65000,  45000},
                {"Roomba i7",                 "iRobot",    "HOME_APPLIANCES",  55000,  38000},
                {"WH-1000XM5",               "Sony",      "ACCESSORIES",      29990,  18000},
                {"AirPods Pro 2",             "Apple",     "ACCESSORIES",      24900,  16000},
                {"QuietComfort 45",           "Bose",      "ACCESSORIES",      34990,  22000},
                {"MX Master 3S",             "Logitech",  "ACCESSORIES",       8695,   5500},
                {"MX Keys",                  "Logitech",  "ACCESSORIES",       9495,   6000},
                {"Magic Keyboard",            "Apple",     "ACCESSORIES",       9900,   6500},
                {"32\" 4K Monitor",           "Samsung",   "ELECTRONICS",      45000,  32000},
                {"UltraFine 27\" 5K",         "LG",        "ELECTRONICS",      75000,  55000},
                {"UltraSharp 32\"",           "Dell",      "ELECTRONICS",      85000,  62000},
                {"Apple Watch Series 9",      "Apple",     "WEARABLES",        45900,  32000},
                {"Galaxy Watch 6",            "Samsung",   "WEARABLES",        27999,  19000},
                {"Fenix 7",                   "Garmin",    "WEARABLES",        79990,  58000},
                {"Fitbit Charge 6",           "Fitbit",    "WEARABLES",        14999,   9500},
                {"Herman Miller Aeron",       "Herman Miller","OFFICE",         95000,  68000},
                {"MARKUS Chair",              "IKEA",      "OFFICE",           12990,   8000},
                {"SmartDesk Pro",             "Autonomous","OFFICE",            75000,  52000},
                {"PlayStation 5",             "Sony",      "GAMING",           54990,  38000},
                {"Xbox Series X",             "Microsoft", "GAMING",           54990,  38000},
                {"Switch OLED",               "Nintendo",  "GAMING",           34999,  24000},
                {"BlackShark V2",             "Razer",     "GAMING",            9999,   6500},
                {"GoPro Hero 12",             "GoPro",     "ELECTRONICS",      44990,  32000},
                {"Airwrap",                   "Dyson",     "BEAUTY",           45000,  32000},
                {"Air Fryer XXL",             "Philips",   "KITCHEN",          15000,  10000},
                {"Instant Pot Duo",           "Instant Pot","KITCHEN",          9500,   6200},
        };

        private final Random     random;
        private final DataConfig config;

        DataGenerator(DataConfig config) {
            this.config = config;
            this.random = new Random(config.seed);
        }

        private Random rnd(long salt) { return new Random(config.seed + salt); }

        // ── Suppliers ─────────────────────────────────────────────

        List<Supplier> generateSuppliers() {
            Random r = rnd(1000);
            List<Supplier> list = new ArrayList<>();
            String[] names = {
                    "TechHub Supplies","Digital Mart India","ElectroWholesale",
                    "Prime Distributors","Galaxy Tech","MobileZone India",
                    "SmartGadget Wholesale","TechTrade India","DeviceHub",
                    "InnovateTech Supplies","FutureElec","AlphaGadgets",
                    "BetaTech India","GammaMobile","DeltaElectronics",
                    "ZetaAccessories","EtaOffice Supplies","ThetaHome",
                    "IotaGaming","KappaBeauty Supplies"
            };
            @SuppressWarnings("unchecked")
            List<Category>[] catGroups = new List[]{
                    Arrays.asList(Category.LAPTOP, Category.ELECTRONICS),
                    Arrays.asList(Category.MOBILE, Category.TABLET),
                    Arrays.asList(Category.ACCESSORIES, Category.WEARABLES),
                    Arrays.asList(Category.HOME_APPLIANCES, Category.KITCHEN),
                    Arrays.asList(Category.OFFICE),
                    Arrays.asList(Category.GAMING),
                    Arrays.asList(Category.BEAUTY, Category.SPORTS)
            };
            for (int i = 0; i < config.supplierCount; i++) {
                String[] city = CITIES[r.nextInt(CITIES.length)];
                String sName  = names[i % names.length] + (i >= names.length ? " " + (i / names.length + 2) : "");
                Address addr  = new Address(
                        (100 + r.nextInt(900)) + " Industrial Estate",
                        city[0], city[1],
                        city[2] + String.format("%03d", r.nextInt(999) + 1),
                        Region.valueOf(city[3])
                );
                list.add(new Supplier(
                        2001 + i, sName,
                        "contact@" + sName.toLowerCase().replaceAll("[^a-z]","") + ".com",
                        addr,
                        catGroups[i % catGroups.length],
                        LocalDate.of(2015, 1, 1).plusDays(r.nextInt(3000)),
                        r.nextDouble() > 0.1
                ));
            }
            return list;
        }

        // ── Products ──────────────────────────────────────────────

        List<Product> generateProducts(List<Supplier> suppliers) {
            Random r = rnd(2000);
            List<Product> list = new ArrayList<>();
            int base = Math.min(PRODUCTS.length, config.productCount);

            for (int i = 0; i < base; i++) {
                Object[] p       = PRODUCTS[i];
                int      suppIdx = r.nextInt(suppliers.size());
                list.add(new Product(
                        3001 + i,
                        "SKU-" + String.format("%06d", 3001 + i),
                        (String) p[0], (String) p[1],
                        Category.valueOf((String) p[2]),
                        new BigDecimal((Integer) p[3]),
                        new BigDecimal((Integer) p[4]),
                        suppliers.get(suppIdx).supplierId(),
                        r.nextInt(500),
                        3.0 + r.nextDouble() * 2.0,
                        r.nextInt(10_000),
                        r.nextDouble() > 0.05,
                        LocalDate.of(2022, 1, 1).plusDays(r.nextInt(1000))
                ));
            }

            // Generate extra variants if productCount > catalog size
            for (int i = base; i < config.productCount; i++) {
                Object[] base0   = PRODUCTS[i % PRODUCTS.length];
                int      basePrice = (Integer) base0[3];
                int      variation = (int)(basePrice * (0.7 + r.nextDouble() * 0.6));
                int      suppIdx   = r.nextInt(suppliers.size());
                list.add(new Product(
                        3001 + i,
                        "SKU-" + String.format("%06d", 3001 + i),
                        base0[0] + " Plus", (String) base0[1],
                        Category.valueOf((String) base0[2]),
                        new BigDecimal(variation),
                        new BigDecimal((int)(variation * 0.70)),
                        suppliers.get(suppIdx).supplierId(),
                        r.nextInt(500),
                        3.0 + r.nextDouble() * 2.0,
                        r.nextInt(10_000),
                        r.nextDouble() > 0.05,
                        LocalDate.of(2022, 1, 1).plusDays(r.nextInt(1000))
                ));
            }
            return list;
        }

        // ── Customers ─────────────────────────────────────────────

        List<Customer> generateCustomers() {
            Random r = rnd(3000);
            List<Customer> list = new ArrayList<>();

            for (int i = 0; i < config.customerCount; i++) {
                String fn   = FIRST_NAMES[r.nextInt(FIRST_NAMES.length)];
                String ln   = LAST_NAMES[r.nextInt(LAST_NAMES.length)];
                String[] city = CITIES[i % CITIES.length];

                Address addr = new Address(
                        (r.nextInt(999) + 1) + "/" + (char)('A' + r.nextInt(26)) +
                                ", " + new String[]{"Sector","Block","Phase","Lane","Street"}[r.nextInt(5)] +
                                " " + (r.nextInt(50) + 1),
                        city[0], city[1],
                        city[2] + String.format("%03d", r.nextInt(999) + 1),
                        Region.valueOf(city[3])
                );

                // Weighted tier distribution
                int t = r.nextInt(100);
                CustomerTier tier;
                if      (t < 38) tier = CustomerTier.STANDARD;
                else if (t < 60) tier = CustomerTier.BRONZE;
                else if (t < 76) tier = CustomerTier.SILVER;
                else if (t < 87) tier = CustomerTier.GOLD;
                else if (t < 94) tier = CustomerTier.PLATINUM;
                else if (t < 98) tier = CustomerTier.DIAMOND;
                else             tier = CustomerTier.ENTERPRISE;

                int        orders     = r.nextInt(50) + tier.ordinal() * 8;
                BigDecimal avgOrder   = new BigDecimal(5000 + r.nextInt(95000));
                BigDecimal totalSpent = avgOrder.multiply(BigDecimal.valueOf(orders));

                // Phone: +91-7XXXXXXXXX  (10 digits after +91-)
                String phone = "+91-" + (7 + r.nextInt(3)) +
                        String.format("%09d", r.nextInt(1_000_000_000));

                list.add(new Customer(
                        4001 + i,
                        fn + " " + ln,
                        fn.toLowerCase() + "." + ln.toLowerCase() + (i + 1) + "@mail.com",
                        phone,
                        addr,
                        tier,
                        LocalDate.of(2018, 1, 1).plusDays(r.nextInt(2920)),
                        orders,
                        totalSpent,
                        orders * (10 + r.nextInt(90)),
                        r.nextDouble() > 0.02
                ));
            }
            return list;
        }

        // ── Orders ────────────────────────────────────────────────

        List<Order> generateOrders(List<Customer> customers, List<Product> products) {
            Random r    = rnd(4000);
            List<Order> list = new ArrayList<>();

            PaymentMethod[]  methods   = PaymentMethod.values();
            ShipmentCarrier[]carriers  = ShipmentCarrier.values();
            Priority[]       priorities= Priority.values();

            long startDay  = config.startDate.toEpochDay();
            long dateRange = config.endDate.toEpochDay() - startDay;

            String[] couponCodes = {"SAVE10","FLAT500","MEGA20","NEWUSER","SPECIAL15","VIP30"};

            for (int i = 0; i < config.orderCount; i++) {
                Customer customer = customers.get(r.nextInt(customers.size()));

                // 1–5 items per order
                int             itemCount    = 1 + r.nextInt(5);
                List<OrderItem> items        = new ArrayList<>();
                Set<Integer>    usedProducts = new HashSet<>();

                for (int j = 0; j < itemCount; j++) {
                    Product product;
                    int tries = 0;
                    do { product = products.get(r.nextInt(products.size())); tries++; }
                    while (usedProducts.contains(product.productId()) && tries < 10);
                    usedProducts.add(product.productId());

                    int        qty  = 1 + r.nextInt(4);
                    BigDecimal disc = BigDecimal.ZERO;
                    DiscountType dt = DiscountType.FLAT;
                    if (r.nextInt(5) == 0) {
                        double pct = 5 + r.nextInt(26);
                        disc = product.price()
                                .multiply(BigDecimal.valueOf(pct / 100))
                                .setScale(2, RoundingMode.HALF_UP);
                        dt   = DiscountType.PERCENTAGE;
                    }
                    items.add(new OrderItem(
                            product.productId(), product.name(), product.brand(),
                            product.category(), qty, product.price(), disc, dt
                    ));
                }

                LocalDate orderDate = LocalDate.ofEpochDay(startDay + (long)(r.nextDouble() * dateRange));

                // Weighted status
                int sv = r.nextInt(100);
                OrderStatus status;
                if      (sv < 4)  status = OrderStatus.DRAFT;
                else if (sv < 8)  status = OrderStatus.CREATED;
                else if (sv < 14) status = OrderStatus.CONFIRMED;
                else if (sv < 20) status = OrderStatus.PROCESSING;
                else if (sv < 25) status = OrderStatus.PACKED;
                else if (sv < 33) status = OrderStatus.SHIPPED;
                else if (sv < 38) status = OrderStatus.OUT_FOR_DELIVERY;
                else if (sv < 63) status = OrderStatus.DELIVERED;
                else if (sv < 73) status = OrderStatus.CANCELLED;
                else if (sv < 80) status = OrderStatus.RETURNED;
                else if (sv < 85) status = OrderStatus.REFUNDED;
                else              status = OrderStatus.ON_HOLD;

                PaymentStatus payStatus;
                if      (status.isCompleted())  payStatus = PaymentStatus.PAID;
                else if (status.isCancelled())  payStatus = r.nextBoolean() ? PaymentStatus.REFUNDED : PaymentStatus.FAILED;
                else                            payStatus = r.nextInt(3) == 0 ? PaymentStatus.PENDING : PaymentStatus.CAPTURED;

                PaymentMethod payMethod = methods[r.nextInt(methods.length)];

                int pv = r.nextInt(100);
                Priority priority;
                if      (pv < 38) priority = Priority.LOW;
                else if (pv < 68) priority = Priority.NORMAL;
                else if (pv < 83) priority = Priority.HIGH;
                else if (pv < 93) priority = Priority.URGENT;
                else              priority = Priority.CRITICAL;

                LocalDate expected = orderDate.plusDays(3 + r.nextInt(10));
                LocalDate actual   = status.isCompleted()
                        ? orderDate.plusDays(2 + r.nextInt(12)) : null;

                // Shipment (only if shipped or beyond)
                Shipment shipment = null;
                if (status.ordinal() >= OrderStatus.SHIPPED.ordinal() && !status.isCancelled()) {
                    ShipmentStatus ss;
                    if      (status == OrderStatus.DELIVERED)        ss = ShipmentStatus.DELIVERED;
                    else if (status == OrderStatus.OUT_FOR_DELIVERY) ss = ShipmentStatus.OUT_FOR_DELIVERY;
                    else                                             ss = ShipmentStatus.IN_TRANSIT;
                    shipment = new Shipment(
                            "SHP" + String.format("%08d", i + 1),
                            carriers[r.nextInt(carriers.length)],
                            "TRK" + (100_000_000 + r.nextInt(899_999_999)),
                            expected, actual, ss
                    );
                }

                // Coupon (12.5% of orders)
                Coupon coupon = null;
                if (r.nextInt(8) == 0) {
                    String code = couponCodes[r.nextInt(couponCodes.length)];
                    if (code.contains("10") || code.contains("20") || code.contains("15") || code.contains("30")) {
                        int pct = code.contains("10") ? 10
                                : code.contains("20") ? 20
                                : code.contains("30") ? 30 : 15;
                        coupon = new Coupon(code, DiscountType.PERCENTAGE,
                                new BigDecimal(pct), new BigDecimal("2000"),
                                new BigDecimal("5000"), LocalDate.of(2027,12,31));
                    } else {
                        coupon = new Coupon(code, DiscountType.FLAT,
                                new BigDecimal("500"), new BigDecimal("3000"),
                                new BigDecimal("500"), LocalDate.of(2027,12,31));
                    }
                }

                LocalDateTime createdAt = orderDate.atTime(r.nextInt(24), r.nextInt(60));

                list.add(new Order(
                        10001 + i,
                        customer,
                        Collections.unmodifiableList(items),
                        status, payStatus, payMethod,
                        orderDate, expected, actual,
                        customer.address(),
                        coupon, shipment, priority,
                        createdAt, createdAt.plusHours(r.nextInt(48))
                ));
            }
            return list;
        }

        // ── Reviews ───────────────────────────────────────────────

        List<Review> generateReviews(List<Customer> customers, List<Product> products) {
            Random r = rnd(5000);
            List<Review> list = new ArrayList<>();
            String[] pos = {"Excellent! Highly recommended.","Very satisfied with this purchase.",
                    "Great quality for the price.","Exceeded my expectations!",
                    "Works perfectly, love it.","Amazing build quality."};
            String[] neu = {"Decent product, does what it says.","Good value for money.",
                    "Satisfactory experience overall.","Not bad, meets basic requirements."};
            String[] neg = {"Disappointed with the quality.","Product doesn't match description.",
                    "Not worth the price.","Poor build quality."};

            int count = Math.min(config.reviewCount, customers.size() * 50);
            for (int i = 0; i < count; i++) {
                Customer customer = customers.get(r.nextInt(customers.size()));
                Product  product  = products.get(r.nextInt(products.size()));
                int      rating   = 1 + r.nextInt(5);
                String   comment  = rating >= 4 ? pos[r.nextInt(pos.length)]
                        : rating == 3 ? neu[r.nextInt(neu.length)]
                        :               neg[r.nextInt(neg.length)];
                list.add(new Review(
                        6001 + i,
                        product.productId(),
                        customer.customerId(),
                        customer.name(),
                        rating,
                        rating >= 4 ? "Great product!" : rating == 3 ? "Okay product" : "Not satisfied",
                        comment,
                        LocalDate.of(2023, 1, 1).plusDays(r.nextInt(1400)),
                        r.nextInt(100),
                        r.nextDouble() > 0.2
                ));
            }
            return list;
        }
    }

    // ============================================================
    // SECTION 8 — IN-MEMORY DATABASE
    // ============================================================

    public static final class InMemoryDatabase {

        private final List<Supplier> suppliers;
        private final List<Product>  products;
        private final List<Customer> customers;
        private final List<Order>    orders;
        private final List<Review>   reviews;

        // O(1) lookup maps
        private final Map<Integer, Customer>      customerMap;
        private final Map<Integer, Product>       productMap;
        private final Map<Integer, Order>         orderMap;
        private final Map<Integer, List<Order>>   ordersByCustomer;
        private final Map<Integer, List<Review>>  reviewsByProduct;

        InMemoryDatabase(DataConfig config) {
            DataGenerator gen = new DataGenerator(config);

            System.out.println("[OMS] Building database…");
            this.suppliers = Collections.unmodifiableList(gen.generateSuppliers());
            System.out.printf("[OMS]  ✓ %,d suppliers%n", suppliers.size());

            this.products  = Collections.unmodifiableList(gen.generateProducts(suppliers));
            System.out.printf("[OMS]  ✓ %,d products%n",  products.size());

            this.customers = Collections.unmodifiableList(gen.generateCustomers());
            System.out.printf("[OMS]  ✓ %,d customers%n", customers.size());

            this.orders    = Collections.unmodifiableList(gen.generateOrders(customers, products));
            System.out.printf("[OMS]  ✓ %,d orders%n",    orders.size());

            this.reviews   = Collections.unmodifiableList(gen.generateReviews(customers, products));
            System.out.printf("[OMS]  ✓ %,d reviews%n",   reviews.size());

            // Build lookup maps
            customerMap    = customers.stream().collect(Collectors.toUnmodifiableMap(Customer::customerId, c -> c));
            productMap     = products.stream() .collect(Collectors.toUnmodifiableMap(Product::productId,   p -> p));
            orderMap       = orders.stream()   .collect(Collectors.toUnmodifiableMap(Order::orderId,       o -> o));
            ordersByCustomer = orders.stream().collect(
                    Collectors.groupingBy(o -> o.customer().customerId(),
                            Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));
            reviewsByProduct = reviews.stream().collect(
                    Collectors.groupingBy(Review::productId,
                            Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList)));

            System.out.println("[OMS] ✅ Ready!\n");
        }

        // ── Raw lists ─────────────────────────────────────────────
        public List<Supplier> suppliers() { return suppliers; }
        public List<Product>  products()  { return products;  }
        public List<Customer> customers() { return customers; }
        public List<Order>    orders()    { return orders;    }
        public List<Review>   reviews()   { return reviews;   }

        // ── Fast ID lookups ───────────────────────────────────────
        public Optional<Customer> findCustomerById(int id) { return Optional.ofNullable(customerMap.get(id)); }
        public Optional<Product>  findProductById(int id)  { return Optional.ofNullable(productMap.get(id));  }
        public Optional<Order>    findOrderById(int id)    { return Optional.ofNullable(orderMap.get(id));    }
        public List<Order>        ordersByCustomer(int customerId) {
            return ordersByCustomer.getOrDefault(customerId, Collections.emptyList());
        }
        public List<Review>       reviewsByProduct(int productId) {
            return reviewsByProduct.getOrDefault(productId, Collections.emptyList());
        }

        // ── Repository factories ──────────────────────────────────
        public OrderRepository    orderRepository()    { return new OrderRepository(this);    }
        public CustomerRepository customerRepository() { return new CustomerRepository(this); }
        public ProductRepository  productRepository()  { return new ProductRepository(this);  }
        public ReviewRepository   reviewRepository()   { return new ReviewRepository(this);   }

        // ── Service factories ─────────────────────────────────────
        public OrderService     orderService()     { return new OrderService(this);     }
        public AnalyticsService analyticsService() { return new AnalyticsService(this); }
    }

    // ============================================================
    // SECTION 9 — REPOSITORIES
    // ============================================================

    /**
     * ORDER REPOSITORY
     *
     * Spring Data JPA equivalent:
     *
     *   @Repository
     *   public interface OrderRepository extends JpaRepository<Order, Integer> {
     *
     *     List<Order> findByStatus(OrderStatus status);
     *     List<Order> findByCustomer_CustomerId(int id);
     *     List<Order> findByOrderDateBetween(LocalDate from, LocalDate to);
     *     List<Order> findByPriority(Priority p);
     *     Page<Order> findByStatus(OrderStatus s, Pageable pageable);
     *
     *     @Query("SELECT o FROM Order o WHERE o.finalAmount() >= :amt")
     *     List<Order> findHighValueOrders(@Param("amt") BigDecimal amt);
     *   }
     */
    public static final class OrderRepository implements CrudRepository<Order, Integer> {

        private final InMemoryDatabase db;
        OrderRepository(InMemoryDatabase db) { this.db = db; }

        @Override public List<Order>     findAll()                 { return db.orders(); }
        @Override public Optional<Order> findById(Integer id)      { return db.findOrderById(id); }
        @Override public long            count()                   { return db.orders().size(); }
        @Override public Page<Order>     findAll(PageRequest req)  { return Page.of(db.orders(), req); }

        public List<Order> findByStatus(OrderStatus s) {
            return db.orders().stream().filter(o -> o.status() == s).collect(Collectors.toList());
        }
        public List<Order> findByCustomerId(int customerId) {
            return db.ordersByCustomer(customerId);
        }
        public List<Order> findByPaymentStatus(PaymentStatus s) {
            return db.orders().stream().filter(o -> o.paymentStatus() == s).collect(Collectors.toList());
        }
        public List<Order> findByPaymentMethod(PaymentMethod m) {
            return db.orders().stream().filter(o -> o.paymentMethod() == m).collect(Collectors.toList());
        }
        public List<Order> findByCity(String city) {
            return db.orders().stream()
                    .filter(o -> o.shippingCity().equalsIgnoreCase(city)).collect(Collectors.toList());
        }
        public List<Order> findByRegion(Region region) {
            return db.orders().stream()
                    .filter(o -> o.shippingRegion() == region).collect(Collectors.toList());
        }
        public List<Order> findByPriority(Priority priority) {
            return db.orders().stream().filter(o -> o.priority() == priority).collect(Collectors.toList());
        }
        public List<Order> findByCustomerTier(CustomerTier tier) {
            return db.orders().stream().filter(o -> o.customerTier() == tier).collect(Collectors.toList());
        }
        public List<Order> findByOrderDateBetween(LocalDate from, LocalDate to) {
            return db.orders().stream()
                    .filter(o -> !o.orderDate().isBefore(from) && !o.orderDate().isAfter(to))
                    .collect(Collectors.toList());
        }
        public List<Order> findByFinalAmountGreaterThan(BigDecimal amount) {
            return db.orders().stream()
                    .filter(o -> o.finalAmount().compareTo(amount) > 0).collect(Collectors.toList());
        }
        public List<Order> findByStatusAndPaymentStatus(OrderStatus s, PaymentStatus ps) {
            return db.orders().stream()
                    .filter(o -> o.status() == s && o.paymentStatus() == ps).collect(Collectors.toList());
        }
        public List<Order> findPriorityOrders() {
            return db.orders().stream().filter(Order::isPriority).collect(Collectors.toList());
        }
        public List<Order> findOrdersWithCoupon() {
            return db.orders().stream().filter(o -> o.coupon() != null).collect(Collectors.toList());
        }
        public List<Order> findOrdersWithShipment(ShipmentCarrier carrier) {
            return db.orders().stream()
                    .filter(o -> o.shipment() != null && o.shipment().carrier() == carrier)
                    .collect(Collectors.toList());
        }

        // ── Paginated version ─────────────────────────────────────
        public Page<Order> findByStatus(OrderStatus s, PageRequest req) {
            return Page.of(findByStatus(s), req);
        }
    }

    /**
     * CUSTOMER REPOSITORY
     *
     * Spring Data JPA equivalent:
     *
     *   @Repository
     *   public interface CustomerRepository extends JpaRepository<Customer, Integer> {
     *     List<Customer> findByTier(CustomerTier tier);
     *     List<Customer> findByAddress_City(String city);
     *     Optional<Customer> findByEmail(String email);
     *     List<Customer> findByActiveTrue();
     *   }
     */
    public static final class CustomerRepository implements CrudRepository<Customer, Integer> {

        private final InMemoryDatabase db;
        CustomerRepository(InMemoryDatabase db) { this.db = db; }

        @Override public List<Customer>     findAll()                 { return db.customers(); }
        @Override public Optional<Customer> findById(Integer id)      { return db.findCustomerById(id); }
        @Override public long               count()                   { return db.customers().size(); }
        @Override public Page<Customer>     findAll(PageRequest req)  { return Page.of(db.customers(), req); }

        public List<Customer>     findByTier(CustomerTier tier) {
            return db.customers().stream().filter(c -> c.tier() == tier).collect(Collectors.toList());
        }
        public List<Customer>     findByCity(String city) {
            return db.customers().stream()
                    .filter(c -> c.address().city().equalsIgnoreCase(city)).collect(Collectors.toList());
        }
        public List<Customer>     findByRegion(Region region) {
            return db.customers().stream()
                    .filter(c -> c.address().region() == region).collect(Collectors.toList());
        }
        public Optional<Customer> findByEmail(String email) {
            return db.customers().stream()
                    .filter(c -> c.email().equalsIgnoreCase(email)).findFirst();
        }
        public List<Customer>     findPremiumCustomers() {
            return db.customers().stream().filter(Customer::isPremium).collect(Collectors.toList());
        }
        public List<Customer>     findActiveCustomers() {
            return db.customers().stream().filter(Customer::active).collect(Collectors.toList());
        }
        public List<Customer>     findByTotalSpentGreaterThan(BigDecimal amount) {
            return db.customers().stream()
                    .filter(c -> c.totalSpent().compareTo(amount) > 0).collect(Collectors.toList());
        }
        public List<Customer>     findTopByTotalSpent(int n) {
            return db.customers().stream()
                    .sorted(Comparator.comparing(Customer::totalSpent).reversed())
                    .limit(n).collect(Collectors.toList());
        }
    }

    /**
     * PRODUCT REPOSITORY
     *
     * Spring Data JPA equivalent:
     *
     *   @Repository
     *   public interface ProductRepository extends JpaRepository<Product, Integer> {
     *     List<Product> findByCategory(Category c);
     *     List<Product> findByBrand(String brand);
     *     List<Product> findByStockCountGreaterThan(int qty);
     *     List<Product> findByPriceBetween(BigDecimal min, BigDecimal max);
     *   }
     */
    public static final class ProductRepository implements CrudRepository<Product, Integer> {

        private final InMemoryDatabase db;
        ProductRepository(InMemoryDatabase db) { this.db = db; }

        @Override public List<Product>     findAll()                 { return db.products(); }
        @Override public Optional<Product> findById(Integer id)      { return db.findProductById(id); }
        @Override public long              count()                   { return db.products().size(); }
        @Override public Page<Product>     findAll(PageRequest req)  { return Page.of(db.products(), req); }

        public List<Product> findByCategory(Category category) {
            return db.products().stream().filter(p -> p.category() == category).collect(Collectors.toList());
        }
        public List<Product> findByBrand(String brand) {
            return db.products().stream()
                    .filter(p -> p.brand().equalsIgnoreCase(brand)).collect(Collectors.toList());
        }
        public List<Product> findInStock() {
            return db.products().stream().filter(Product::inStock).collect(Collectors.toList());
        }
        public List<Product> findByPriceBetween(BigDecimal min, BigDecimal max) {
            return db.products().stream()
                    .filter(p -> p.price().compareTo(min) >= 0 && p.price().compareTo(max) <= 0)
                    .collect(Collectors.toList());
        }
        public List<Product> findTopRated(int n) {
            return db.products().stream()
                    .sorted(Comparator.comparingDouble(Product::rating).reversed())
                    .limit(n).collect(Collectors.toList());
        }
        public List<Product> findByPriceGreaterThan(BigDecimal price) {
            return db.products().stream()
                    .filter(p -> p.price().compareTo(price) > 0).collect(Collectors.toList());
        }
    }

    /**
     * REVIEW REPOSITORY
     */
    public static final class ReviewRepository implements CrudRepository<Review, Integer> {

        private final InMemoryDatabase db;
        ReviewRepository(InMemoryDatabase db) { this.db = db; }

        @Override public List<Review>     findAll()                { return db.reviews(); }
        @Override public Optional<Review> findById(Integer id)     {
            return db.reviews().stream().filter(r -> r.reviewId() == id).findFirst();
        }
        @Override public long             count()                  { return db.reviews().size(); }
        @Override public Page<Review>     findAll(PageRequest req) { return Page.of(db.reviews(), req); }

        public List<Review> findByProductId(int productId)   { return db.reviewsByProduct(productId); }
        public List<Review> findByCustomerId(int customerId) {
            return db.reviews().stream().filter(r -> r.customerId() == customerId).collect(Collectors.toList());
        }
        public List<Review> findByRating(int rating) {
            return db.reviews().stream().filter(r -> r.rating() == rating).collect(Collectors.toList());
        }
        public List<Review> findVerifiedReviews() {
            return db.reviews().stream().filter(Review::verified).collect(Collectors.toList());
        }
        public List<Review> findByRatingGreaterThanEqual(int min) {
            return db.reviews().stream().filter(r -> r.rating() >= min).collect(Collectors.toList());
        }
    }

    // ============================================================
    // SECTION 10 — SERVICES
    // ============================================================

    /**
     * ORDER SERVICE — business operations on orders.
     *
     * Spring equivalent:
     *   @Service
     *   @Transactional(readOnly = true)
     *   public class OrderService { ... }
     */
    public static final class OrderService {

        private final InMemoryDatabase db;
        OrderService(InMemoryDatabase db) { this.db = db; }

        /** Convert Order → lightweight OrderSummaryDTO */
        public OrderSummaryDTO toSummary(Order o) {
            return new OrderSummaryDTO(o.orderId(), o.customer().name(),
                    o.customer().tier(), o.finalAmount(), o.status(),
                    o.orderDate(), o.shippingCity());
        }

        public List<OrderSummaryDTO> getAllSummaries() {
            return db.orders().stream().map(this::toSummary).collect(Collectors.toList());
        }

        public List<Order> findHighValueOrders(BigDecimal threshold) {
            return db.orders().stream()
                    .filter(o -> o.finalAmount().compareTo(threshold) > 0)
                    .sorted(Comparator.comparing(Order::finalAmount).reversed())
                    .collect(Collectors.toList());
        }

        public List<Order> getTopOrdersByValue(int n) {
            return db.orders().stream()
                    .sorted(Comparator.comparing(Order::finalAmount).reversed())
                    .limit(n).collect(Collectors.toList());
        }

        public BigDecimal calculateTotalRevenue() {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .map(Order::finalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public BigDecimal calculateAverageOrderValue() {
            long deliveredCount = db.orders().stream().filter(Order::isDelivered).count();
            if (deliveredCount == 0) return BigDecimal.ZERO;
            return calculateTotalRevenue()
                    .divide(BigDecimal.valueOf(deliveredCount), 2, RoundingMode.HALF_UP);
        }

        public Optional<Order> findMostExpensiveOrder() {
            return db.orders().stream().max(Comparator.comparing(Order::finalAmount));
        }

        public Optional<Order> findCheapestOrder() {
            return db.orders().stream().min(Comparator.comparing(Order::finalAmount));
        }
    }

    /**
     * ANALYTICS SERVICE — aggregations and reports.
     *
     * Spring equivalent:
     *   @Service
     *   public class AnalyticsService { ... }
     */
    public static final class AnalyticsService {

        private final InMemoryDatabase db;
        AnalyticsService(InMemoryDatabase db) { this.db = db; }

        /** Orders grouped by status with count */
        public Map<OrderStatus, Long> countByStatus() {
            return db.orders().stream()
                    .collect(Collectors.groupingBy(Order::status, Collectors.counting()));
        }

        /** Orders grouped by city */
        public Map<String, Long> countByCity() {
            return db.orders().stream()
                    .collect(Collectors.groupingBy(Order::shippingCity, Collectors.counting()));
        }

        /** Revenue grouped by customer tier */
        public Map<CustomerTier, BigDecimal> revenueByTier() {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .collect(Collectors.groupingBy(Order::customerTier,
                            Collectors.reducing(BigDecimal.ZERO, Order::finalAmount, BigDecimal::add)));
        }

        /** Revenue grouped by product category */
        public Map<Category, BigDecimal> revenueByCategory() {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .flatMap(o -> o.items().stream())
                    .collect(Collectors.groupingBy(OrderItem::category,
                            Collectors.reducing(BigDecimal.ZERO, OrderItem::totalPrice, BigDecimal::add)));
        }

        /** Revenue grouped by month */
        public Map<Month, BigDecimal> revenueByMonth() {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .collect(Collectors.groupingBy(o -> o.orderDate().getMonth(),
                            Collectors.reducing(BigDecimal.ZERO, Order::finalAmount, BigDecimal::add)));
        }

        /** Revenue grouped by payment method */
        public Map<PaymentMethod, BigDecimal> revenueByPaymentMethod() {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .collect(Collectors.groupingBy(Order::paymentMethod,
                            Collectors.reducing(BigDecimal.ZERO, Order::finalAmount, BigDecimal::add)));
        }

        /** Revenue grouped by region */
        public Map<Region, BigDecimal> revenueByRegion() {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .collect(Collectors.groupingBy(Order::shippingRegion,
                            Collectors.reducing(BigDecimal.ZERO, Order::finalAmount, BigDecimal::add)));
        }

        /** Top N customers by revenue */
        public List<CustomerReportDTO> topCustomersByRevenue(int n) {
            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .collect(Collectors.groupingBy(o -> o.customer().customerId()))
                    .entrySet().stream()
                    .map(e -> {
                        List<Order> cos   = e.getValue();
                        Customer    c     = cos.get(0).customer();
                        BigDecimal  total = cos.stream().map(Order::finalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        BigDecimal  avg   = total.divide(BigDecimal.valueOf(cos.size()), 2, RoundingMode.HALF_UP);
                        BigDecimal  max   = cos.stream().map(Order::finalAmount)
                                .max(Comparator.naturalOrder()).orElse(BigDecimal.ZERO);
                        return new CustomerReportDTO(
                                c.customerId(), c.name(), c.tier(), c.address().city(),
                                cos.size(), total, avg, max);
                    })
                    .sorted(Comparator.comparing(CustomerReportDTO::totalRevenue).reversed())
                    .limit(n)
                    .collect(Collectors.toList());
        }

        /** Product sales report sorted by revenue */
        public List<ProductSalesDTO> productSalesReport() {
            Map<Integer, Double> ratingMap = db.reviews().stream()
                    .collect(Collectors.groupingBy(Review::productId, Collectors.averagingInt(Review::rating)));

            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .flatMap(o -> o.items().stream())
                    .collect(Collectors.groupingBy(OrderItem::productId))
                    .entrySet().stream()
                    .map(e -> {
                        List<OrderItem> its  = e.getValue();
                        OrderItem       first = its.get(0);
                        long   units   = its.stream().mapToLong(OrderItem::quantity).sum();
                        BigDecimal rev = its.stream().map(OrderItem::totalPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        return new ProductSalesDTO(e.getKey(), first.productName(), first.brand(),
                                first.category(), units, rev, ratingMap.getOrDefault(e.getKey(), 0.0));
                    })
                    .sorted(Comparator.comparing(ProductSalesDTO::totalRevenue).reversed())
                    .collect(Collectors.toList());
        }

        /** City analytics — count + revenue + customer count */
        public List<CityAnalyticsDTO> cityAnalytics() {
            Map<String, Long> custByCity = db.customers().stream()
                    .collect(Collectors.groupingBy(c -> c.address().city(), Collectors.counting()));

            return db.orders().stream()
                    .filter(Order::isDelivered)
                    .collect(Collectors.groupingBy(Order::shippingCity))
                    .entrySet().stream()
                    .map(e -> {
                        List<Order> cos = e.getValue();
                        Region region   = cos.get(0).shippingRegion();
                        BigDecimal rev  = cos.stream().map(Order::finalAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        return new CityAnalyticsDTO(e.getKey(), region, cos.size(), rev,
                                custByCity.getOrDefault(e.getKey(), 0L));
                    })
                    .sorted(Comparator.comparing(CityAnalyticsDTO::totalRevenue).reversed())
                    .collect(Collectors.toList());
        }
    }

    // ============================================================
    // SECTION 11 — PUBLIC FACTORY METHODS
    // ============================================================

    /** Default database: 100 000 orders */
    public static InMemoryDatabase createDatabase() {
        return new InMemoryDatabase(new DataConfig());
    }

    /** Custom-config database */
    public static InMemoryDatabase createDatabase(DataConfig config) {
        return new InMemoryDatabase(config);
    }

    // ============================================================
    // SECTION 12 — MAIN (Demo & verification)
    // ============================================================

    public static void main(String[] args) {

        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║   ENTERPRISE ORDER MANAGEMENT LIBRARY  —  v2.0          ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // ── Create a small database for this demo ─────────────────
        var db = createDatabase(DataConfig.small());

        var orderRepo    = db.orderRepository();
        var customerRepo = db.customerRepository();
        var productRepo  = db.productRepository();
        var orderSvc     = db.orderService();
        var analytics    = db.analyticsService();

        // ─────────────────────────────────────────────────────────────
        System.out.println("══ 1. Entity Counts ══════════════════════════════════════");
        System.out.printf("  Suppliers : %,d%n", db.suppliers().size());
        System.out.printf("  Products  : %,d%n", productRepo.count());
        System.out.printf("  Customers : %,d%n", customerRepo.count());
        System.out.printf("  Orders    : %,d%n", orderRepo.count());
        System.out.printf("  Reviews   : %,d%n", db.reviews().size());

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 2. Top 5 Orders by Value ══════════════════════════════");
        orderSvc.getTopOrdersByValue(5).forEach(o ->
                System.out.printf("  #%-8d %-22s %-12s ₹%,12.2f%n",
                        o.orderId(), o.customer().name(), o.status(), o.finalAmount()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 3. Order Count by Status ══════════════════════════════");
        analytics.countByStatus().entrySet().stream()
                .sorted(Map.Entry.<OrderStatus,Long>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-20s : %,d%n", e.getKey(), e.getValue()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 4. Revenue by Category ════════════════════════════════");
        analytics.revenueByCategory().entrySet().stream()
                .sorted(Map.Entry.<Category,BigDecimal>comparingByValue().reversed())
                .limit(6)
                .forEach(e -> System.out.printf("  %-20s : ₹%,12.2f%n", e.getKey(), e.getValue()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 5. Top 5 Customers ════════════════════════════════════");
        analytics.topCustomersByRevenue(5)
                .forEach(c -> System.out.printf("  %-25s %-12s ₹%,12.2f%n",
                        c.name(), c.tier(), c.totalRevenue()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 6. Revenue by Tier ════════════════════════════════════");
        analytics.revenueByTier().entrySet().stream()
                .sorted(Map.Entry.<CustomerTier,BigDecimal>comparingByValue().reversed())
                .forEach(e -> System.out.printf("  %-12s : ₹%,14.2f%n", e.getKey(), e.getValue()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 7. Pagination Demo — Page 1 of Orders ════════════════");
        Page<Order> page1 = orderRepo.findAll(PageRequest.of(0, 5));
        System.out.printf("  Showing %d of %,d orders (page %d / %d)%n",
                page1.content().size(), page1.totalElements(), page1.pageNumber() + 1, page1.totalPages());
        page1.content().forEach(o ->
                System.out.printf("  Order %-8d %s  ₹%,10.2f%n",
                        o.orderId(), o.orderDate(), o.finalAmount()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 8. Revenue Summary ════════════════════════════════════");
        System.out.printf("  Total Revenue   : ₹%,15.2f%n", orderSvc.calculateTotalRevenue());
        System.out.printf("  Avg Order Value : ₹%,15.2f%n", orderSvc.calculateAverageOrderValue());
        orderSvc.findMostExpensiveOrder().ifPresent(o ->
                System.out.printf("  Highest Order   : #%d  ₹%,12.2f%n", o.orderId(), o.finalAmount()));

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 9. Priority Orders ════════════════════════════════════");
        long priorityCount = orderRepo.findPriorityOrders().size();
        System.out.printf("  Priority orders : %,d%n", priorityCount);

        // ─────────────────────────────────────────────────────────────
        System.out.println("\n══ 10. Orders with Coupon ════════════════════════════════");
        long couponOrders = orderRepo.findOrdersWithCoupon().size();
        System.out.printf("  Orders with coupon : %,d%n", couponOrders);

        System.out.println();
        System.out.println("✅ Library verified. Start your Stream exercises:");
        System.out.println("   List<Order> orders = db.orderRepository().findAll();");
        System.out.println("   orders.stream().filter(...).sorted(...).map(...).forEach(...);");
    }
}
