package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.BankAccount;

@Repository
public interface BankAccountRepo extends JpaRepository<BankAccount, Long> {

	BankAccount findByBankName(String bankName);

}
