package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

/** PersonalAccountDto.*/
@Data
@ToString
public class PersonalAccountDto {
  @NotNull
  private String id;
  private String code;
  private String accountNumber;
  private Date openingDate;
  private String customerId;
  private String typeAccountId;
  private Boolean state;
  private BigDecimal amount;
}
