package com.nttdata.bootcamp.transactiondomain.service.external.impl;

import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCreditService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.CreditDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Service external credit product.
 */
@Service
public class ExternalCreditImplService implements ExternalCreditService {

  @Autowired
  private WebClient.Builder webClient;

  @Override
  public Mono<CreditDto> findById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/".concat(id)).retrieve()
        .bodyToMono(CreditDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("Credit not found"));
          }
          return Mono.error(error);
        });
  }

  @Override
  public Mono<CreditDto> update(CreditDto creditDto, String id) {
    return webClient.baseUrl(URL_GATEWAY).build().put().uri("/".concat(id)).bodyValue(creditDto)
        .retrieve().bodyToMono(CreditDto.class).onErrorResume(Mono::error);
  }
}
