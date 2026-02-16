package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.BankAccount;
import com.app.payloads.BankAccountDTO;
import com.app.services.BankAccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BankAccountController {

	@Autowired
	private BankAccountService bankAccountService;

	@PostMapping("/admin/bank-accounts")
	public ResponseEntity<BankAccountDTO> createBankAccount(@Valid @RequestBody BankAccount bankAccount) {
		BankAccountDTO savedBankAccountDTO = bankAccountService.createBankAccount(bankAccount);

		return new ResponseEntity<BankAccountDTO>(savedBankAccountDTO, HttpStatus.CREATED);
	}

	@GetMapping("/public/bank-accounts")
	public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts() {
		List<BankAccountDTO> bankAccountDTOs = bankAccountService.getAllBankAccounts();

		return new ResponseEntity<List<BankAccountDTO>>(bankAccountDTOs, HttpStatus.OK);
	}

	@GetMapping("/admin/bank-accounts/{bankAccountId}")
	public ResponseEntity<BankAccountDTO> getBankAccountById(@PathVariable Long bankAccountId) {
		BankAccountDTO bankAccountDTO = bankAccountService.getBankAccountById(bankAccountId);

		return new ResponseEntity<BankAccountDTO>(bankAccountDTO, HttpStatus.OK);
	}

	@PutMapping("/admin/bank-accounts/{bankAccountId}")
	public ResponseEntity<BankAccountDTO> updateBankAccount(@RequestBody BankAccount bankAccount, @PathVariable Long bankAccountId) {
		BankAccountDTO bankAccountDTO = bankAccountService.updateBankAccount(bankAccount, bankAccountId);

		return new ResponseEntity<BankAccountDTO>(bankAccountDTO, HttpStatus.OK);
	}

	@DeleteMapping("/admin/bank-accounts/{bankAccountId}")
	public ResponseEntity<String> deleteBankAccount(@PathVariable Long bankAccountId) {
		String status = bankAccountService.deleteBankAccount(bankAccountId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
