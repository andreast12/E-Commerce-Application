package com.app.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "bank_accounts")
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bankAccountId;

	@NotBlank
	@Column(unique = true)
	private String bankName;

	@NotBlank
	private String accountNumber;

	@NotBlank
	private String accountHolderName;

}
