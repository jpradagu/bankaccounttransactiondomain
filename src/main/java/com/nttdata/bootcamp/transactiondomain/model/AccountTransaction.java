package com.nttdata.bootcamp.transactiondomain.model;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection="accountTransactions")
public class AccountTransaction {

	@Id
	private String id;
	private CustomerType customerType;
	private String customerId;
	private MovementType movementType;
	private BigDecimal amount;
	private Channel channel;
	private String bankAccountId;
}
