package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import java.math.BigDecimal;
import lombok.Data;

/**
 * TypeAccountDto.
 */
@Data
public class TypeAccountDto {

  private String id;
  private String type;
  private BigDecimal maintenanceCommission;
  private BigDecimal transactionCommission;
  private BigDecimal minimumOpeningAmount;
  private Integer numLimitMovements;
  private Boolean allowCompany;
  private Boolean allowPerson;
  private Integer dayMovement;
  private Boolean needCreditCard;
}
