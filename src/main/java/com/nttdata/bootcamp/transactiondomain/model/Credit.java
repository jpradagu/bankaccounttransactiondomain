package com.nttdata.bootcamp.transactiondomain.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Credit.
 */
@Document(collection = "creditPayments")
@Data
public class Credit {

  @Id
  private String id;
  @NotNull
  private String creditId;
  @NotNull
  private BigDecimal amount;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date paymentDate;
  private String customerId;
  private BigDecimal interestRate;
}
