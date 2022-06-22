package com.nttdata.bootcamp.transactiondomain.service.external;

import com.nttdata.bootcamp.transactiondomain.service.external.dto.CommercialCustomerDto;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.PersonalCustomerDto;
import reactor.core.publisher.Mono;

/**
 * Interface external account service.
 */
public interface ExternalCustomerService {

  String URL_GATEWAY = "http://localhost:8080/api/customer";

  /**
   * find customer personal by id.
   */
  Mono<PersonalCustomerDto> findPersonalById(String id);

  /**
   * find commercial customer by id.
   */
  Mono<CommercialCustomerDto> findCommercialById(String id);
}
