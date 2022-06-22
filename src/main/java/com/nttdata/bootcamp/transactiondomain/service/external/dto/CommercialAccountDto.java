package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.ToString;

/** CommercialAccountDto.*/
@Data
@ToString
public class CommercialAccountDto {
  private String id;
  private String code;
  private String accountNumber;
  private Date openingDate;
  private String customerId;
  private String typeAccountId;
  private Boolean state;
  private BigDecimal amount;
  private List<ClientDto> holders;
  private List<ClientDto> signers;
}
