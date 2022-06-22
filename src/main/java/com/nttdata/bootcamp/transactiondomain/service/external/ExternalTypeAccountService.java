package com.nttdata.bootcamp.transactiondomain.service.external;

import com.nttdata.bootcamp.transactiondomain.service.external.dto.TypeAccountDto;
import reactor.core.publisher.Mono;

/**
 * ExternalTypeAccountService.
 */
public interface ExternalTypeAccountService {

  String URL_GATEWAY = "http://localhost:8080/api/product/account-bank";

  /**
   * find TypeAccount by id.
   */
  Mono<TypeAccountDto> findById(String id);
}
