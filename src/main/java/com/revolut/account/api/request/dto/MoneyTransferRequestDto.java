package com.revolut.account.api.request.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class MoneyTransferRequestDto {

	private String fromAccountNumber;
	private String toAccountNumber;
	private Double amount;
	private Date createdAt;
}
