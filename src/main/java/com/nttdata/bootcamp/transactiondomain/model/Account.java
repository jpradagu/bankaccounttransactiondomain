package com.nttdata.bootcamp.transactiondomain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Account.
 */
@Data
@Document(collection = "accountTransactions")
public class Account {

  @Id
  private String id;
  @NotNull
  private CustomerType customerType;
  @NotNull
  private String customerId;
  @NotNull
  private MovementType movementType;
  @NotNull
  private BigDecimal amount;
  @NotNull
  private BigDecimal amountCommission;
  @NotNull
  private String accountId;
  private Date transactionDate;
}
