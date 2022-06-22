package com.nttdata.bootcamp.transactiondomain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * CreditCard.
 */
@Document
@Data
public class CreditCard {
  @Id
  private String id;
  @NotNull
  private String creditCardId;
  private BigDecimal amount;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date tradeDate;
  @NotNull
  private CustomerType customerType;
  @NotNull
  private String customerId;
  private BigDecimal interestRate;
  private Integer fees;
}
