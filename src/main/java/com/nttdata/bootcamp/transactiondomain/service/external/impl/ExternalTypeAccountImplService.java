package com.nttdata.bootcamp.transactiondomain.service.external.impl;

import com.nttdata.bootcamp.transactiondomain.service.external.ExternalTypeAccountService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.TypeAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * ExternalTypeAccountImplService.
 */
@Service
public class ExternalTypeAccountImplService implements ExternalTypeAccountService {

  @Autowired
  private WebClient.Builder webClient;

  @Override
  public Mono<TypeAccountDto> findById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/".concat(id)).retrieve()
        .bodyToMono(TypeAccountDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("TypeAccount not found"));
          }
          return Mono.error(error);
        });
  }
}
