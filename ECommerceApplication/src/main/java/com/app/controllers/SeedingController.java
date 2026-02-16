package com.app.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.BankAccount;
import com.app.entites.Category;
import com.app.entites.Product;
import com.app.entites.PromoCode;
import com.app.payloads.CreateAddressDTO;
import com.app.payloads.UserDTO;
import com.app.services.AddressService;
import com.app.services.BankAccountService;
import com.app.services.CategoryService;
import com.app.services.ProductService;
import com.app.services.PromoCodeService;
import com.app.services.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class SeedingController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UserService userService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private BankAccountService bankAccountService;

	@Autowired
	private PromoCodeService promoCodeService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/admin/seed")
	@Transactional
	public ResponseEntity<Map<String, Object>> seedDatabase() {
		Map<String, Object> response = new HashMap<>();

		try {
			// 1. Seed Categories
			Category electronics = new Category();
			electronics.setCategoryName("Electronics");
			electronics.setProducts(new ArrayList<>());
			var electronicsDTO = categoryService.createCategory(electronics);

			Category clothing = new Category();
			clothing.setCategoryName("Clothing");
			clothing.setProducts(new ArrayList<>()); 
			var clothingDTO = categoryService.createCategory(clothing);

			Category books = new Category();
			books.setCategoryName("Books and Magazines");
			books.setProducts(new ArrayList<>());
			var booksDTO = categoryService.createCategory(books);

			Category furniture = new Category();
			furniture.setCategoryName("Furniture");
			furniture.setProducts(new ArrayList<>());
			var furnitureDTO = categoryService.createCategory(furniture);

			Category sports = new Category();
			sports.setCategoryName("Sports Equipment");
			sports.setProducts(new ArrayList<>());
			var sportsDTO = categoryService.createCategory(sports);

			response.put("categories", "5 categories created");

			// 2. Seed Products for each category
			// Electronics Products
			Product laptop = new Product();
			laptop.setProductName("Gaming Laptop");
			laptop.setDescription("High-performance gaming laptop with RTX graphics");
			laptop.setPrice(1299.99);
			laptop.setDiscount(10.0);
			laptop.setQuantity(50);
			productService.addProduct(electronicsDTO.getCategoryId(), laptop);

			Product smartphone = new Product();
			smartphone.setProductName("Smartphone Pro");
			smartphone.setDescription("Latest flagship smartphone with OLED display");
			smartphone.setPrice(899.99);
			smartphone.setDiscount(5.0);
			smartphone.setQuantity(100);
			productService.addProduct(electronicsDTO.getCategoryId(), smartphone);

			Product headphones = new Product();
			headphones.setProductName("Wireless Headphones");
			headphones.setDescription("Noise-cancelling wireless headphones");
			headphones.setPrice(199.99);
			headphones.setDiscount(15.0);
			headphones.setQuantity(75);
			productService.addProduct(electronicsDTO.getCategoryId(), headphones);

			// Clothing Products
			Product tshirt = new Product();
			tshirt.setProductName("Cotton T-Shirt");
			tshirt.setDescription("Premium quality cotton t-shirt in various colors");
			tshirt.setPrice(29.99);
			tshirt.setDiscount(20.0);
			tshirt.setQuantity(200);
			productService.addProduct(clothingDTO.getCategoryId(), tshirt);

			Product jeans = new Product();
			jeans.setProductName("Denim Jeans");
			jeans.setDescription("Classic blue denim jeans with comfortable fit");
			jeans.setPrice(59.99);
			jeans.setDiscount(10.0);
			jeans.setQuantity(150);
			productService.addProduct(clothingDTO.getCategoryId(), jeans);

			Product jacket = new Product();
			jacket.setProductName("Winter Jacket");
			jacket.setDescription("Warm and stylish winter jacket for cold weather");
			jacket.setPrice(129.99);
			jacket.setDiscount(25.0);
			jacket.setQuantity(80);
			productService.addProduct(clothingDTO.getCategoryId(), jacket);

			// Books Products
			Product novel = new Product();
			novel.setProductName("Mystery Novel");
			novel.setDescription("Bestselling mystery novel by renowned author");
			novel.setPrice(19.99);
			novel.setDiscount(0.0);
			novel.setQuantity(120);
			productService.addProduct(booksDTO.getCategoryId(), novel);

			Product cookbook = new Product();
			cookbook.setProductName("Cookbook Collection");
			cookbook.setDescription("Comprehensive cookbook with international recipes");
			cookbook.setPrice(34.99);
			cookbook.setDiscount(5.0);
			cookbook.setQuantity(90);
			productService.addProduct(booksDTO.getCategoryId(), cookbook);

			// Furniture Products
			Product chair = new Product();
			chair.setProductName("Office Chair");
			chair.setDescription("Ergonomic office chair with lumbar support");
			chair.setPrice(249.99);
			chair.setDiscount(15.0);
			chair.setQuantity(60);
			productService.addProduct(furnitureDTO.getCategoryId(), chair);

			Product desk = new Product();
			desk.setProductName("Standing Desk");
			desk.setDescription("Adjustable height standing desk for modern workspace");
			desk.setPrice(399.99);
			desk.setDiscount(10.0);
			desk.setQuantity(40);
			productService.addProduct(furnitureDTO.getCategoryId(), desk);

			// Sports Products
			Product yoga = new Product();
			yoga.setProductName("Yoga Mat Premium");
			yoga.setDescription("Non-slip premium yoga mat with carrying strap");
			yoga.setPrice(39.99);
			yoga.setDiscount(8.0);
			yoga.setQuantity(150);
			productService.addProduct(sportsDTO.getCategoryId(), yoga);

			Product dumbbell = new Product();
			dumbbell.setProductName("Dumbbell Set");
			dumbbell.setDescription("Adjustable dumbbell set for home workouts");
			dumbbell.setPrice(149.99);
			dumbbell.setDiscount(12.0);
			dumbbell.setQuantity(70);
			productService.addProduct(sportsDTO.getCategoryId(), dumbbell);

			response.put("products", "12 products created across 5 categories");

			// 3. Seed Bank Accounts
			BankAccount bank1 = new BankAccount();
			bank1.setBankName("Chase Bank");
			bank1.setAccountNumber("1234567890");
			bank1.setAccountHolderName("E-Commerce LLC");
			bankAccountService.createBankAccount(bank1);

			BankAccount bank2 = new BankAccount();
			bank2.setBankName("Bank of America");
			bank2.setAccountNumber("0987654321");
			bank2.setAccountHolderName("E-Commerce LLC");
			bankAccountService.createBankAccount(bank2);

			BankAccount bank3 = new BankAccount();
			bank3.setBankName("Wells Fargo");
			bank3.setAccountNumber("1122334455");
			bank3.setAccountHolderName("E-Commerce LLC");
			bankAccountService.createBankAccount(bank3);

			response.put("bankAccounts", "3 bank accounts created");

			// 4. Seed Promo Codes
			PromoCode promo1 = new PromoCode();
			promo1.setCode("WELCOME10");
			promo1.setDiscountPercentage(10.0);
			promo1.setExpiryDate(LocalDate.now().plusMonths(3));
			promo1.setActive(true);
			promoCodeService.createPromoCode(promo1);

			PromoCode promo2 = new PromoCode();
			promo2.setCode("SUMMER25");
			promo2.setDiscountPercentage(25.0);
			promo2.setExpiryDate(LocalDate.now().plusMonths(6));
			promo2.setActive(true);
			promoCodeService.createPromoCode(promo2);

			PromoCode promo3 = new PromoCode();
			promo3.setCode("BLACKFRIDAY50");
			promo3.setDiscountPercentage(50.0);
			promo3.setExpiryDate(LocalDate.now().plusYears(1));
			promo3.setActive(true);
			promoCodeService.createPromoCode(promo3);

			PromoCode promo4 = new PromoCode();
			promo4.setCode("EXPIRED5");
			promo4.setDiscountPercentage(5.0);
			promo4.setExpiryDate(LocalDate.now().minusDays(1));
			promo4.setActive(false);
			promoCodeService.createPromoCode(promo4);

			response.put("promoCodes", "4 promo codes created (3 active, 1 expired)");

			response.put("status", "success");
			response.put("message", "Database seeded successfully with all test data!");

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace(); 
			response.put("status", "error");
			response.put("message", "Error seeding database: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/admin/seed/users")
	@Transactional
	public ResponseEntity<Map<String, Object>> seedUsers() {
		Map<String, Object> response = new HashMap<>();

		try {
			// Admin User
			UserDTO adminUser = new UserDTO();
			adminUser.setFirstName("Admin");
			adminUser.setLastName("User");
			adminUser.setEmail("admin@ecommerce.com");
			adminUser.setMobileNumber("+1234567890");
			adminUser.setPassword(passwordEncoder.encode("admin123"));
			userService.registerUser(adminUser);

			// Regular Users
			UserDTO user1 = new UserDTO();
			user1.setFirstName("John");
			user1.setLastName("Doe");
			user1.setEmail("john.doe@example.com");
			user1.setMobileNumber("+1234567891");
			user1.setPassword(passwordEncoder.encode("password123"));
			userService.registerUser(user1);

			UserDTO user2 = new UserDTO();
			user2.setFirstName("Jane");
			user2.setLastName("Smith");
			user2.setEmail("jane.smith@example.com");
			user2.setMobileNumber("+1234567892");
			user2.setPassword(passwordEncoder.encode("password123"));
			userService.registerUser(user2);

			UserDTO user3 = new UserDTO();
			user3.setFirstName("Bob");
			user3.setLastName("Johnson");
			user3.setEmail("bob.johnson@example.com");
			user3.setMobileNumber("+1234567893");
			user3.setPassword(passwordEncoder.encode("password123"));
			userService.registerUser(user3);

			UserDTO user4 = new UserDTO();
			user4.setFirstName("Alice");
			user4.setLastName("Williams");
			user4.setEmail("alice.williams@example.com");
			user4.setMobileNumber("+1234567894");
			user4.setPassword(passwordEncoder.encode("password123"));
			userService.registerUser(user4);

			response.put("users", "5 users created (1 admin, 4 regular users)");
			response.put("status", "success");
			response.put("message", "Users seeded successfully!");
			response.put("note", "Default password for all users: password123 (admin: admin123)");

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace(); 
			response.put("status", "error");
			response.put("message", "Error seeding users: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/admin/seed/addresses")
	@Transactional
	public ResponseEntity<Map<String, Object>> seedAddresses() {
		Map<String, Object> response = new HashMap<>();

		try {
			CreateAddressDTO address1 = new CreateAddressDTO();
			address1.setStreet("123 Main Street");
			address1.setBuildingName("Building A");
			address1.setCity("New York");
			address1.setState("NY");
			address1.setCountry("USA");
			address1.setPincode("10001");
			addressService.createAddress(address1);

			CreateAddressDTO address2 = new CreateAddressDTO();
			address2.setStreet("456 Oak Avenue");
			address2.setBuildingName("Tower B");
			address2.setCity("Los Angeles");
			address2.setState("CA");
			address2.setCountry("USA");
			address2.setPincode("90001");
			addressService.createAddress(address2);

			CreateAddressDTO address3 = new CreateAddressDTO();
			address3.setStreet("789 Pine Road");
			address3.setBuildingName("Suite 100");
			address3.setCity("Chicago");
			address3.setState("IL");
			address3.setCountry("USA");
			address3.setPincode("60601");
			addressService.createAddress(address3);

			response.put("addresses", "3 addresses created");
			response.put("status", "success");
			response.put("message", "Addresses seeded successfully!");

			return new ResponseEntity<>(response, HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace(); 
			response.put("status", "error");
			response.put("message", "Error seeding addresses: " + e.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
