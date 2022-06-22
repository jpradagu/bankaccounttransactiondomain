package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/** CreditDto.*/
@Data
public class CreditDto {
  private String id;
  private String numberCredit;
  private BigDecimal amountGiven;
  private BigDecimal amountPaid;
  private Integer fees;
  private Integer feesPaid;
  private String typeCreditId;
  private Boolean state;
  private Date deliveryDate;
  private String customerId;
  private String customerType;
}
