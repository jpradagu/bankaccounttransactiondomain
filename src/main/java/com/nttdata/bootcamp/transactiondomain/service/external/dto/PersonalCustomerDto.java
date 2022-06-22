package com.nttdata.bootcamp.transactiondomain.service.external.dto;

import lombok.Data;

/** PersonalCustomerDto.*/
@Data
public class PersonalCustomerDto {
  private String id;
  private String dni;
  private String name;
  private String lastname;
  private String email;
  private String phone;
}
