package com.app.services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.BankAccount;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.BankAccountDTO;
import com.app.repositories.BankAccountRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BankAccountServiceImpl implements BankAccountService {

	@Autowired
	private BankAccountRepo bankAccountRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public BankAccountDTO createBankAccount(BankAccount bankAccount) {
		BankAccount existing = bankAccountRepo.findByBankName(bankAccount.getBankName());

		if (existing != null) {
			throw new APIException("Bank account with the name '" + bankAccount.getBankName() + "' already exists !!!");
		}

		BankAccount saved = bankAccountRepo.save(bankAccount);

		return modelMapper.map(saved, BankAccountDTO.class);
	}

	@Override
	public List<BankAccountDTO> getAllBankAccounts() {
		List<BankAccount> bankAccounts = bankAccountRepo.findAll();

		if (bankAccounts.size() == 0) {
			throw new APIException("No bank accounts found");
		}

		return bankAccounts.stream()
				.map(bankAccount -> modelMapper.map(bankAccount, BankAccountDTO.class))
				.collect(Collectors.toList());
	}

	@Override
	public BankAccountDTO getBankAccountById(Long bankAccountId) {
		BankAccount bankAccount = bankAccountRepo.findById(bankAccountId)
				.orElseThrow(() -> new ResourceNotFoundException("BankAccount", "bankAccountId", bankAccountId));

		return modelMapper.map(bankAccount, BankAccountDTO.class);
	}

	@Override
	public BankAccountDTO getBankAccountByBankName(String bankName) {
		BankAccount bankAccount = bankAccountRepo.findByBankName(bankName);

		if (bankAccount == null) {
			throw new ResourceNotFoundException("BankAccount", "bankName", bankName);
		}

		return modelMapper.map(bankAccount, BankAccountDTO.class);
	}

	@Override
	public BankAccountDTO updateBankAccount(BankAccount bankAccount, Long bankAccountId) {
		bankAccountRepo.findById(bankAccountId)
				.orElseThrow(() -> new ResourceNotFoundException("BankAccount", "bankAccountId", bankAccountId));

		bankAccount.setBankAccountId(bankAccountId);

		BankAccount saved = bankAccountRepo.save(bankAccount);

		return modelMapper.map(saved, BankAccountDTO.class);
	}

	@Override
	public String deleteBankAccount(Long bankAccountId) {
		BankAccount bankAccount = bankAccountRepo.findById(bankAccountId)
				.orElseThrow(() -> new ResourceNotFoundException("BankAccount", "bankAccountId", bankAccountId));

		bankAccountRepo.delete(bankAccount);

		return "Bank account with bankAccountId: " + bankAccountId + " deleted successfully !!!";
	}

}
