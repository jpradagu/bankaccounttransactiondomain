package com.nttdata.bootcamp.transactiondomain.service.external.impl;

import com.nttdata.bootcamp.transactiondomain.service.external.ExternalAccountService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.CommercialAccountDto;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.PersonalAccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

/**
 * Service external account product.
 */
@Service
public class ExternalAccountImplService implements ExternalAccountService {

  @Autowired
  private WebClient.Builder webClient;

  @Override
  public Mono<PersonalAccountDto> findPersonalById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/personal/".concat(id)).retrieve()
        .bodyToMono(PersonalAccountDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("Personal account not found"));
          }
          return Mono.error(error);
        });
  }


  @Override
  public Mono<CommercialAccountDto> findCommercialById(String id) {
    return webClient.baseUrl(URL_GATEWAY).build().get().uri("/corporate/".concat(id)).retrieve()
        .bodyToMono(CommercialAccountDto.class).onErrorResume(error -> {
          WebClientResponseException resp = (WebClientResponseException) error;
          if (resp.getStatusCode() == HttpStatus.NOT_FOUND) {
            return Mono.error(new RuntimeException("corporate account not found"));
          }
          return Mono.error(error);
        });
  }

  @Override
  public Mono<PersonalAccountDto> updatePersonal(PersonalAccountDto personalAccountDto, String id) {
    return webClient.baseUrl(URL_GATEWAY).build().put().uri("/personal/".concat(id))
        .bodyValue(personalAccountDto).retrieve().bodyToMono(
            PersonalAccountDto.class)
        .onErrorResume(Mono::error);
  }

  @Override
  public Mono<CommercialAccountDto> updateCommercial(CommercialAccountDto commercialAccountDto,
                                                     String id) {
    return webClient.baseUrl(URL_GATEWAY).build().put().uri("/corporate/".concat(id))
        .bodyValue(commercialAccountDto).retrieve().bodyToMono(
            CommercialAccountDto.class)
        .onErrorResume(Mono::error);
  }
}
