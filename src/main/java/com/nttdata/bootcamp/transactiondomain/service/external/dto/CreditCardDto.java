package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * CreditCardDto.
 */
@Data
public class CreditCardDto {
  private String id;
  private String creditCardNumber;
  private BigDecimal limitAmount;
  private BigDecimal usedAmount;
  private Date openingDate;
  private String customerType;
  private String customerId;
  private String typeCreditCardId;
  private Boolean state;
}
