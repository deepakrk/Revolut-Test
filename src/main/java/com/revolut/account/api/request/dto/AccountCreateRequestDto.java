package com.revolut.account.api.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateRequestDto {

	private String fullName;
	private String accountNumber;
	private Double depositAmount;

}
