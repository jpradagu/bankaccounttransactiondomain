package com.nttdata.bootcamp.transactiondomain.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Document(collection = "creditPayments")
@Data
public class CreditPayment {
	
	@Id
	private String id;
	private String creditCard;
	private Channel chanel;
	private BigDecimal amount;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date paymentDate;
	private String loanProgramId;
}
