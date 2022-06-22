package com.nttdata.bootcamp.transactiondomain.service.external;

import com.nttdata.bootcamp.transactiondomain.service.external.dto.CommercialAccountDto;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.PersonalAccountDto;
import reactor.core.publisher.Mono;

/**
 * interface external product service.
 */
public interface ExternalAccountService {

  String URL_GATEWAY = "http://localhost:8080/api/register/account";

  /**
   * find personal account by id.
   */
  Mono<PersonalAccountDto> findPersonalById(String id);

  /**
   * find commercial account by id.
   */
  Mono<CommercialAccountDto> findCommercialById(String id);

  /**
   * update personal account.
   */
  Mono<PersonalAccountDto> updatePersonal(PersonalAccountDto personalAccountDto, String id);

  /**
   * update commercial account.
   */
  Mono<CommercialAccountDto> updateCommercial(CommercialAccountDto commercialAccountDto, String id);
}
