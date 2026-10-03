package com.basics.basics;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

public class OrderManagementSystem {

    // =========================================================
    // ENUMS
    // =========================================================

    enum OrderStatus {
        CREATED,
        CONFIRMED,
        PROCESSING,
        SHIPPED,
        DELIVERED,
        CANCELLED
    }

    enum PaymentStatus {
        PENDING,
        PAID,
        FAILED,
        REFUNDED
    }

    enum CustomerTier {
        STANDARD,
        SILVER,
        GOLD,
        PLATINUM
    }

    enum Category {
        ELECTRONICS,
        MOBILE,
        LAPTOP,
        ACCESSORIES,
        HOME,
        OFFICE
    }

    // =========================================================
    // PRODUCT
    // =========================================================

    record Product(
            int productId,
            String name,
            Category category,
            BigDecimal price
    ) {}

    // =========================================================
    // CUSTOMER
    // =========================================================

    record Customer(
            int customerId,
            String name,
            String city,
            CustomerTier tier
    ) {}

    // =========================================================
    // ORDER ITEM
    // =========================================================

    record OrderItem(
            int productId,
            String productName,
            Category category,
            int quantity,
            BigDecimal unitPrice
    ) {

        BigDecimal totalPrice() {
            return unitPrice.multiply(
                    BigDecimal.valueOf(quantity)
            );
        }
    }

    // =========================================================
    // ORDER
    // =========================================================

    record Order(
            int orderId,
            Customer customer,
            List<OrderItem> items,
            OrderStatus status,
            PaymentStatus paymentStatus,
            LocalDate orderDate,
            String shippingCity,
            boolean priority
    ) {

        BigDecimal totalAmount() {
            return items.stream()
                    .map(OrderItem::totalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        int totalItems() {
            return items.stream()
                    .mapToInt(OrderItem::quantity)
                    .sum();
        }
    }

    // =========================================================
    // IN-MEMORY DATABASE
    // =========================================================

    static class InMemoryDatabase {

        private final List<Customer> customers;
        private final List<Product> products;
        private final List<Order> orders;

        InMemoryDatabase() {
            this.products = createProducts();
            this.customers = createCustomers();
            this.orders = createOrders();
        }

        List<Customer> customers() {
            return customers;
        }

        List<Product> products() {
            return products;
        }

        List<Order> orders() {
            return orders;
        }

        // -----------------------------------------------------
        // PRODUCTS
        // -----------------------------------------------------

        private List<Product> createProducts() {

            List<Product> products = new ArrayList<>();

            products.add(new Product(
                    1, "MacBook Pro", Category.LAPTOP,
                    new BigDecimal("180000")
            ));

            products.add(new Product(
                    2, "Dell XPS", Category.LAPTOP,
                    new BigDecimal("145000")
            ));

            products.add(new Product(
                    3, "ThinkPad X1", Category.LAPTOP,
                    new BigDecimal("135000")
            ));

            products.add(new Product(
                    4, "iPhone 17", Category.MOBILE,
                    new BigDecimal("95000")
            ));

            products.add(new Product(
                    5, "Samsung Galaxy", Category.MOBILE,
                    new BigDecimal("85000")
            ));

            products.add(new Product(
                    6, "Google Pixel", Category.MOBILE,
                    new BigDecimal("75000")
            ));

            products.add(new Product(
                    7, "iPad Pro", Category.ELECTRONICS,
                    new BigDecimal("110000")
            ));

            products.add(new Product(
                    8, "Monitor 4K", Category.ELECTRONICS,
                    new BigDecimal("45000")
            ));

            products.add(new Product(
                    9, "Mechanical Keyboard", Category.ACCESSORIES,
                    new BigDecimal("12000")
            ));

            products.add(new Product(
                    10, "Wireless Mouse", Category.ACCESSORIES,
                    new BigDecimal("5000")
            ));

            products.add(new Product(
                    11, "Noise Cancelling Headphones", Category.ACCESSORIES,
                    new BigDecimal("25000")
            ));

            products.add(new Product(
                    12, "Office Chair", Category.OFFICE,
                    new BigDecimal("30000")
            ));

            products.add(new Product(
                    13, "Standing Desk", Category.OFFICE,
                    new BigDecimal("40000")
            ));

            products.add(new Product(
                    14, "Air Purifier", Category.HOME,
                    new BigDecimal("22000")
            ));

            products.add(new Product(
                    15, "Smart TV", Category.HOME,
                    new BigDecimal("80000")
            ));

            return products;
        }

        // -----------------------------------------------------
        // CUSTOMERS
        // -----------------------------------------------------

        private List<Customer> createCustomers() {

            String[] names = {
                    "Aarav",
                    "Vivaan",
                    "Aditya",
                    "Arjun",
                    "Rahul",
                    "Rohan",
                    "Vikram",
                    "Karan",
                    "Amit",
                    "Varun",
                    "Neha",
                    "Priya",
                    "Ananya",
                    "Isha",
                    "Kavya",
                    "Sneha",
                    "Pooja",
                    "Riya",
                    "Meera",
                    "Diya"
            };

            String[] cities = {
                    "Delhi",
                    "Mumbai",
                    "Bangalore",
                    "Hyderabad",
                    "Pune",
                    "Chennai",
                    "Kolkata",
                    "Ahmedabad"
            };

            CustomerTier[] tiers = CustomerTier.values();

            List<Customer> customers = new ArrayList<>();

            for (int i = 0; i < 50; i++) {

                String name =
                        names[i % names.length]
                                + " "
                                + (i + 1);

                String city =
                        cities[i % cities.length];

                CustomerTier tier =
                        tiers[i % tiers.length];

                customers.add(
                        new Customer(
                                1001 + i,
                                name,
                                city,
                                tier
                        )
                );
            }

            return customers;
        }

        // -----------------------------------------------------
        // ORDERS
        // -----------------------------------------------------

        private List<Order> createOrders() {

            List<Order> orders = new ArrayList<>();

            Random random = new Random(42);

            OrderStatus[] statuses =
                    OrderStatus.values();

            PaymentStatus[] paymentStatuses =
                    PaymentStatus.values();

            for (int i = 0; i < 500; i++) {

                Customer customer =
                        customers.get(
                                i % customers.size()
                        );

                int itemCount =
                        1 + random.nextInt(4);

                List<OrderItem> items =
                        new ArrayList<>();

                for (int j = 0; j < itemCount; j++) {

                    Product product =
                            products.get(
                                    random.nextInt(
                                            products.size()
                                    )
                            );

                    int quantity =
                            1 + random.nextInt(4);

                    items.add(
                            new OrderItem(
                                    product.productId(),
                                    product.name(),
                                    product.category(),
                                    quantity,
                                    product.price()
                            )
                    );
                }

                OrderStatus status =
                        statuses[
                                random.nextInt(
                                        statuses.length
                                )
                                ];

                PaymentStatus paymentStatus =
                        paymentStatuses[
                                random.nextInt(
                                        paymentStatuses.length
                                )
                                ];

                LocalDate orderDate =
                        LocalDate.of(
                                2026,
                                1,
                                1
                        ).plusDays(
                                random.nextInt(273)
                        );

                boolean priority =
                        random.nextInt(5) == 0;

                orders.add(
                        new Order(
                                50001 + i,
                                customer,
                                items,
                                status,
                                paymentStatus,
                                orderDate,
                                customer.city(),
                                priority
                        )
                );
            }

            return orders;
        }
    }

    // =========================================================
    // ORDER REPOSITORY
    // =========================================================

    static class OrderRepository {

        private final InMemoryDatabase database;

        OrderRepository(InMemoryDatabase database) {
            this.database = database;
        }

        List<Order> findAll() {
            return database.orders();
        }

        Optional<Order> findById(int orderId) {

            return database.orders()
                    .stream()
                    .filter(order ->
                            order.orderId() == orderId)
                    .findFirst();
        }
    }

    // =========================================================
    // MAIN
    // =========================================================

}
