package com.nttdata.bootcamp.transactiondomain.service.external;

import com.nttdata.bootcamp.transactiondomain.service.external.dto.CreditCardDto;
import reactor.core.publisher.Mono;

/**
 * Interface ExternalCreditCard Service.
 */
public interface ExternalCreditCardService {

  String URL_GATEWAY = "http://localhost:8080/api/register/credit-card";

  /**
   * find creditCard by id.
   */
  Mono<CreditCardDto> findById(String id);

  /**
   * update creditCard.
   */
  Mono<CreditCardDto> update(CreditCardDto creditCardDto, String id);
}
