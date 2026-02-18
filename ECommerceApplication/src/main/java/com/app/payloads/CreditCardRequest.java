package com.app.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardRequest {

	@NotBlank(message = "Card number is required")
	@Pattern(regexp = "^\\d{13,19}$", message = "Card number must be between 13 and 19 digits")
	private String cardNumber;

	@NotBlank(message = "CVC is required")
	@Pattern(regexp = "^\\d{3,4}$", message = "CVC must be 3 or 4 digits")
	private String cardCvc;
}
