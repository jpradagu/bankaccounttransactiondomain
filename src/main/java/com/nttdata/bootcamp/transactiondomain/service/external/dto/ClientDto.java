package com.nttdata.bootcamp.transactiondomain.service.external.dto;


import lombok.Data;
import lombok.ToString;

/**
 * ClientDto.
 */
@Data
@ToString
public class ClientDto {
  private String dni;
  private String name;
  private String email;
  private String phone;
}
