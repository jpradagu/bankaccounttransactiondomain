package com.nttdata.bootcamp.transactiondomain.service.external;

import com.nttdata.bootcamp.transactiondomain.service.external.dto.CreditDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Interface External Credit Service.
 */
@Service
public interface ExternalCreditService {
  String URL_GATEWAY = "http://localhost:8080/api/register/credit";

  /**
   * find credit by id.
   */
  Mono<CreditDto> findById(String id);

  /**
   * update credit.
   */
  Mono<CreditDto> update(CreditDto creditDto, String id);
}
