package com.app.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.entites.Address;
import com.app.entites.Cart;
import com.app.entites.Category;
import com.app.entites.Discount;
import com.app.entites.Product;
import com.app.entites.Role;
import com.app.entites.User;
import com.app.repositories.CategoryRepo;
import com.app.repositories.DiscountRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.RoleRepo;
import com.app.repositories.UserRepo;

//@Configuration
public class DataSeeder implements CommandLineRunner {

	@Autowired
	private RoleRepo roleRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private CategoryRepo categoryRepo;

	@Autowired
	private ProductRepo productRepo;

	@Autowired
	private DiscountRepo discountRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		seedRoles();
		seedUsers();
		seedCategoriesAndProducts();
		seedStoreDiscounts();
	}

	private void seedRoles() {
		if (roleRepo.findByRoleName("ADMIN").isEmpty()) {
			Role adminRole = new Role();
			adminRole.setRoleId(AppConstants.ADMIN_ID);
			adminRole.setRoleName("ADMIN");
			roleRepo.save(adminRole);
			System.out.println("ADMIN role seeded.");
		}

		if (roleRepo.findByRoleName("USER").isEmpty()) {
			Role userRole = new Role();
			userRole.setRoleId(AppConstants.USER_ID);
			userRole.setRoleName("USER");
			roleRepo.save(userRole);
			System.out.println("USER role seeded.");
		}
	}

	private void seedUsers() {
		if (userRepo.findByEmail("admin@webshop.com").isEmpty()) {
			Role adminRole = roleRepo.findByRoleName("ADMIN").get();

			Address adminAddress = new Address("Indonesia", "Jawa Barat", "Bandung", "401234", "Jl. Dipatiukur", "Gedung Informatika");

			User admin = new User();
			admin.setFirstName("Admin");
			admin.setLastName("Webshop");
			admin.setMobileNumber("0812345678");
			admin.setEmail("admin@webshop.com");
			admin.setPassword(passwordEncoder.encode("admin123"));
			admin.getRoles().add(adminRole);
			admin.setAddresses(new ArrayList<>(List.of(adminAddress)));

			Cart adminCart = new Cart();
			admin.setCart(adminCart);
			adminCart.setUser(admin);

			userRepo.save(admin);
			System.out.println("Admin user seeded.");
		}

		if (userRepo.findByEmail("user@webshop.com").isEmpty()) {
			Role userRole = roleRepo.findByRoleName("USER").get();

			Address userAddress = new Address("Indonesia", "Jawa Barat", "Bandung", "401235", "Jl. Ganesha", "Gedung Labtek V");

			User user = new User();
			user.setFirstName("Pelanggan");
			user.setLastName("Webshop");
			user.setMobileNumber("0898765432");
			user.setEmail("user@webshop.com");
			user.setPassword(passwordEncoder.encode("user1234"));
			user.getRoles().add(userRole);
			user.setAddresses(new ArrayList<>(List.of(userAddress)));

			Cart userCart = new Cart();
			user.setCart(userCart);
			userCart.setUser(user);

			userRepo.save(user);
			System.out.println("Regular user seeded.");
		}
	}

	private void seedCategoriesAndProducts() {
		if (categoryRepo.count() > 0) {
			return;
		}

		Category electronics = new Category();
		electronics.setCategoryName("Electronics");
		electronics = categoryRepo.save(electronics);

		Category clothing = new Category();
		clothing.setCategoryName("Clothing");
		clothing = categoryRepo.save(clothing);

		Category books = new Category();
		books.setCategoryName("Books");
		books = categoryRepo.save(books);

		createProduct("Laptop Gaming ASUS", "Laptop gaming dengan prosesor Intel i7 dan GPU RTX 4060",
				10, 15000000, 5, electronics);
		createProduct("Samsung Galaxy S24", "Smartphone Samsung terbaru dengan kamera 200MP",
				25, 12000000, 10, electronics);
		createProduct("Sony WF-1000XM5", "Earbuds wireless noise cancelling terbaik di kelasnya",
				30, 4500000, 15, electronics);

		createProduct("Kaos Polos Premium", "Kaos polos bahan cotton combed 30s premium quality",
				100, 150000, 0, clothing);
		createProduct("Jaket Hoodie Unisex", "Jaket hoodie fleece tebal cocok untuk cuaca dingin",
				50, 350000, 10, clothing);

		createProduct("Belajar Java Spring Boot", "Buku panduan lengkap belajar Java Spring Boot untuk pemula",
				40, 120000, 5, books);
		createProduct("Novel Laskar Pelangi", "Novel bestseller karya Andrea Hirata tentang kehidupan anak-anak Belitung",
				60, 85000, 0, books);

		System.out.println("Categories and Products seeded.");
	}

	private void createProduct(String name, String description, int quantity, double price, double discount, Category category) {
		Product product = new Product();
		product.setProductName(name);
		product.setDescription(description);
		product.setQuantity(quantity);
		product.setPrice(price);
		product.setDiscount(discount);
		product.setSpecialPrice(price - (discount * 0.01 * price));
		product.setImage("default.png");
		product.setCategory(category);
		productRepo.save(product);
	}

	private void seedStoreDiscounts() {
		if (discountRepo.count() > 0) {
			return;
		}

		Discount birthdayDiscount = new Discount();
		birthdayDiscount.setDiscountName("Diskon Ulang Tahun Toko");
		birthdayDiscount.setDiscountPercentage(20);
		birthdayDiscount.setStartDate(LocalDate.now());
		birthdayDiscount.setEndDate(LocalDate.now().plusDays(7));
		discountRepo.save(birthdayDiscount);

		Discount yearEndDiscount = new Discount();
		yearEndDiscount.setDiscountName("Diskon Akhir Tahun");
		yearEndDiscount.setDiscountPercentage(15);
		yearEndDiscount.setStartDate(LocalDate.of(2026, 12, 20));
		yearEndDiscount.setEndDate(LocalDate.of(2026, 12, 31));
		discountRepo.save(yearEndDiscount);

		System.out.println("Store Discounts seeded.");
	}
}
