package com.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
	private Long bankAccountId;
	private String bankName;
	private String accountNumber;
	private String accountHolderName;
}
