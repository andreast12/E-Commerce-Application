package com.app.controllers;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Category;
import com.app.entites.Discount;
import com.app.entites.Product;
import com.app.payloads.AddressDTO;
import com.app.payloads.UserDTO;
import com.app.repositories.CategoryRepo;
import com.app.repositories.DiscountRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.UserRepo;
import com.app.services.UserService;

@RestController
@RequestMapping("/api")
public class SeedController {

	@Autowired
	private UserService userService;

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

	@PostMapping("/seed")
	public ResponseEntity<Map<String, Object>> seedDatabase() {
		Map<String, Object> result = new LinkedHashMap<>();

		result.put("users", seedUsers());
		result.put("categories_and_products", seedCategoriesAndProducts());
		result.put("store_discounts", seedStoreDiscounts());

		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	private String seedUsers() {
		int count = 0;

		if (userRepo.findByEmail("admin@webshop.com").isEmpty()) {
			UserDTO adminDTO = new UserDTO();
			adminDTO.setFirstName("Admin");
			adminDTO.setLastName("Webshop");
			adminDTO.setMobileNumber("0812345678");
			adminDTO.setEmail("admin@webshop.com");
			adminDTO.setPassword(passwordEncoder.encode("admin123"));

			AddressDTO adminAddress = new AddressDTO();
			adminAddress.setStreet("Jl. Dipatiukur");
			adminAddress.setBuildingName("Gedung Informatika");
			adminAddress.setCity("Bandung");
			adminAddress.setState("Jawa Barat");
			adminAddress.setCountry("Indonesia");
			adminAddress.setPincode("401234");
			adminDTO.setAddress(adminAddress);

			userService.registerUser(adminDTO);
			count++;
		}

		if (userRepo.findByEmail("user@webshop.com").isEmpty()) {
			UserDTO userDTO = new UserDTO();
			userDTO.setFirstName("Pelanggan");
			userDTO.setLastName("Webshop");
			userDTO.setMobileNumber("0898765432");
			userDTO.setEmail("user@webshop.com");
			userDTO.setPassword(passwordEncoder.encode("user1234"));

			AddressDTO userAddress = new AddressDTO();
			userAddress.setStreet("Jl. Ganesha");
			userAddress.setBuildingName("Gedung Labtek V");
			userAddress.setCity("Bandung");
			userAddress.setState("Jawa Barat");
			userAddress.setCountry("Indonesia");
			userAddress.setPincode("401235");
			userDTO.setAddress(userAddress);

			userService.registerUser(userDTO);
			count++;
		}

		return count > 0 ? count + " users created" : "users already exist";
	}

	private String seedCategoriesAndProducts() {
		if (categoryRepo.count() > 0) {
			return "categories and products already exist";
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

		createProduct("Laptop Gaming ASUS", "Laptop gaming dengan prosesor Intel i7 dan GPU RTX 4060", 10, 15000000, 5, electronics);
		createProduct("Samsung Galaxy S24", "Smartphone Samsung terbaru dengan kamera 200MP", 25, 12000000, 10, electronics);
		createProduct("Sony WF-1000XM5", "Earbuds wireless noise cancelling terbaik di kelasnya", 30, 4500000, 15, electronics);
		createProduct("Kaos Polos Premium", "Kaos polos bahan cotton combed 30s premium quality", 100, 150000, 0, clothing);
		createProduct("Jaket Hoodie Unisex", "Jaket hoodie fleece tebal cocok untuk cuaca dingin", 50, 350000, 10, clothing);
		createProduct("Belajar Java Spring Boot", "Buku panduan lengkap belajar Java Spring Boot untuk pemula", 40, 120000, 5, books);
		createProduct("Novel Laskar Pelangi", "Novel bestseller karya Andrea Hirata tentang kehidupan anak-anak Belitung", 60, 85000, 0, books);

		return "3 categories and 7 products created";
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

	private String seedStoreDiscounts() {
		if (discountRepo.count() > 0) {
			return "store discounts already exist";
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

		return "2 store discounts created";
	}
}
