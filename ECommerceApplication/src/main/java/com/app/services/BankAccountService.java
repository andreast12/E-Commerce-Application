package com.app.services;

import java.util.List;

import com.app.entites.BankAccount;
import com.app.payloads.BankAccountDTO;

public interface BankAccountService {

	BankAccountDTO createBankAccount(BankAccount bankAccount);

	List<BankAccountDTO> getAllBankAccounts();

	BankAccountDTO getBankAccountById(Long bankAccountId);

	BankAccountDTO getBankAccountByBankName(String bankName);

	BankAccountDTO updateBankAccount(BankAccount bankAccount, Long bankAccountId);

	String deleteBankAccount(Long bankAccountId);

}
