package com.nttdata.bootcamp.transactiondomain.model;

import java.math.BigDecimal;
import lombok.Data;

/** Transfer.*/
@Data
public class Transfer {
  private String id;
  private String sourceAccountId;
  private String recipientAccountId;
  private TransferType transferType;
  private CustomerType recipientCustomerType;
  private String recipientDocumentNumber;
  private BigDecimal amount;
}
