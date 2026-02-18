package com.app.services;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Address;
import com.app.entites.Cart;
import com.app.entites.Category;
import com.app.entites.Order;
import com.app.entites.OrderItem;
import com.app.entites.Payment;
import com.app.entites.Product;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.repositories.AddressRepo;
import com.app.repositories.CartRepo;
import com.app.repositories.CategoryRepo;
import com.app.repositories.OrderItemRepo;
import com.app.repositories.OrderRepo;
import com.app.repositories.PaymentRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class SeederService {

    private final RoleRepo roleRepo;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final PaymentRepo paymentRepo;
    private final AddressRepo addressRepo;
    private final com.app.repositories.MembershipRepo membershipRepo;
    private final PasswordEncoder passwordEncoder;

    public SeederService(RoleRepo roleRepo, CategoryRepo categoryRepo, ProductRepo productRepo, UserRepo userRepo,
            CartRepo cartRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo, PaymentRepo paymentRepo,
            AddressRepo addressRepo, com.app.repositories.MembershipRepo membershipRepo, PasswordEncoder passwordEncoder) {
        this.roleRepo = roleRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.cartRepo = cartRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.paymentRepo = paymentRepo;
        this.addressRepo = addressRepo;
        this.membershipRepo = membershipRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public String seedDatabase() {
        // roles
        if (roleRepo.count() == 0) {
            Role adminRole = new Role(com.app.config.AppConstants.ADMIN_ID, "ADMIN");
            Role userRole = new Role(com.app.config.AppConstants.USER_ID, "USER");
            roleRepo.saveAll(List.of(adminRole, userRole));
        }

        // categories + products
        if (categoryRepo.count() == 0) {
            Category electronics = new Category();
            electronics.setCategoryName("Electronics");
            Category books = new Category();
            books.setCategoryName("Books");
            Category appliances = new Category();
            appliances.setCategoryName("Home Appliances");

            categoryRepo.saveAll(List.of(electronics, books, appliances));

            Product p1 = new Product();
            p1.setProductName("Wireless Headphones");
            p1.setDescription("Comfortable bluetooth headphones");
            p1.setImage("headphones.jpg");
            p1.setQuantity(100);
            p1.setPrice(59.99);
            p1.setDiscount(5.0);
            p1.setSpecialPrice(54.99);
            p1.setCategory(electronics);

            Product p2 = new Product();
            p2.setProductName("Smartphone");
            p2.setDescription("Android smartphone with 4GB RAM");
            p2.setImage("phone.jpg");
            p2.setQuantity(50);
            p2.setPrice(299.99);
            p2.setDiscount(10.0);
            p2.setSpecialPrice(269.99);
            p2.setCategory(electronics);

            Product p3 = new Product();
            p3.setProductName("Spring in Action");
            p3.setDescription("Comprehensive guide to Spring Framework");
            p3.setImage("book_spring.jpg");
            p3.setQuantity(40);
            p3.setPrice(39.99);
            p3.setDiscount(0);
            p3.setSpecialPrice(39.99);
            p3.setCategory(books);

            Product p4 = new Product();
            p4.setProductName("Blender");
            p4.setDescription("Kitchen blender with multiple speeds");
            p4.setImage("blender.jpg");
            p4.setQuantity(30);
            p4.setPrice(49.99);
            p4.setDiscount(5.0);
            p4.setSpecialPrice(44.99);
            p4.setCategory(appliances);

            productRepo.saveAll(List.of(p1, p2, p3, p4));
        }

        // sample users
        Optional<User> maybeUser = userRepo.findByEmail("user@example.com");
        if (maybeUser.isEmpty()) {
            User user = new User();
            user.setFirstName("Jonathan");
            user.setLastName("SeedAdminUser");
            user.setEmail("user@example.com");
            user.setMobileNumber("1234567890");
            user.setPassword(passwordEncoder.encode("password"));

            Role r = roleRepo.findById(com.app.config.AppConstants.USER_ID)
                .orElse(new Role(com.app.config.AppConstants.USER_ID, "USER"));
            Set<Role> roles = new HashSet<>();
            roles.add(r);
            user.setRoles(roles);

            userRepo.save(user);

            // address
            Address addr = new Address("USA", "CA", "San Francisco", "941070", "3rd Street", "Suite 100");
            addressRepo.save(addr);
            user.getAddresses().add(addr);
            userRepo.save(user);

            // cart
            Cart cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0.0);
            cartRepo.save(cart);
            user.setCart(cart);
            userRepo.save(user);

            // create a sample order with one order item if products exist
            List<Product> products = productRepo.findAll();
            if (!products.isEmpty()) {
                Product prod = products.get(0);
                Payment payment = new Payment();
                payment.setPaymentMethod("CREDIT_CARD");
                paymentRepo.save(payment);

                Order order = new Order();
                order.setEmail(user.getEmail());
                order.setOrderDate(LocalDate.now());
                order.setOrderStatus("PLACED");
                order.setPayment(payment);

                OrderItem oi = new OrderItem();
                oi.setProduct(prod);
                oi.setQuantity(1);
                // product.getDiscount() is percentage; compute amount and final unit price
                double perUnitProdDiscount = (prod.getDiscount() * 0.01) * prod.getPrice();
                oi.setDiscount(perUnitProdDiscount);
                double finalUnitPrice = prod.getPrice() - perUnitProdDiscount;
                oi.setOrderedProductPrice(finalUnitPrice);
                oi.setOrder(order);

                order.getOrderItems().add(oi);
                double total = finalUnitPrice * oi.getQuantity();
                order.setTotalAmount(total);

                orderRepo.save(order);
                orderItemRepo.save(oi);
            }
        }

        // seed a sample membership code
        if (membershipRepo.count() == 0) {
            var m = new com.app.entites.Membership();
            m.setCode("MEMBER2026");
            m.setDiscountPercent(10.0);
            m.setActive(true);
            membershipRepo.save(m);
        }

        return "Database seeding completed";
    }

}
