package com.nttdata.bootcamp.transactiondomain.service.external.impl;

import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCustomerService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.CommercialCustomerDto;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.PersonalCustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * External Account service.
 */
@Service
public class ExternalCustomerImplService implements ExternalCustomerService {

  @Autowired
  private WebClient.Builder webClient;


  @Override
  public Mono<PersonalCustomerDto> findPersonalById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/personal/".concat(id)).retrieve()
        .bodyToMono(PersonalCustomerDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("Personal customer not found"));
          }
          return Mono.error(error);
        });
  }

  @Override
  public Mono<CommercialCustomerDto> findCommercialById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/enterprise/".concat(id)).retrieve()
        .bodyToMono(CommercialCustomerDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("Enterprise customer not found"));
          }
          return Mono.error(error);
        });
  }
}
