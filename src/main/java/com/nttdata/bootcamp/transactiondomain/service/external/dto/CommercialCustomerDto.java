package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import lombok.Data;

/** CommercialCustomerDto.*/
@Data
public class CommercialCustomerDto {
  private String id;
  private String ruc;
  private String reasonSocial;
  private String email;
  private String phone;
}
