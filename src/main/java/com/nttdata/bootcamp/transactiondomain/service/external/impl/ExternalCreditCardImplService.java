package com.nttdata.bootcamp.transactiondomain.service.external.impl;

import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCreditCardService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.CreditCardDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * External Credit Card Service Implementation.
 */
@Service
public class ExternalCreditCardImplService implements ExternalCreditCardService {

  @Autowired
  private WebClient.Builder webClient;

  @Override
  public Mono<CreditCardDto> findById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/".concat(id)).retrieve()
        .bodyToMono(CreditCardDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("CreditCard not found"));
          }
          return Mono.error(error);
        });
  }

  @Override
  public Mono<CreditCardDto> update(CreditCardDto creditCardDto, String id) {
    return webClient.baseUrl(URL_GATEWAY).build().put().uri("/".concat(id)).bodyValue(creditCardDto)
        .retrieve().bodyToMono(CreditCardDto.class).onErrorResume(Mono::error);
  }
}
