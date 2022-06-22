package com.nttdata.bootcamp.transactiondomain.exception;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * ResumenError class.
 */
@Slf4j
public class ResumenError {

  private ResumenError() {
  }

  /**
   * errorResumenExcetion.
   */
  public static Mono<ResponseEntity<Map<String, Object>>> errorResumenException(Throwable t) {
    Map<String, Object> result = new HashMap<>();
    if ((t instanceof WebExchangeBindException)) {
      return Mono.just(t)
          .cast(WebExchangeBindException.class)
          .flatMap(e -> Mono.just(e.getFieldErrors()))
          .flatMapMany(Flux::fromIterable)
          .map(err -> "The field " + err.getField() + " " + err.getDefaultMessage())
          .collectList().flatMap(list -> {
            log.info(list.toString());
            result.put("errors", list);
            return Mono.just(ResponseEntity.badRequest().body(result));
          });
    } else {
      return Mono.just(t).cast(RuntimeException.class).flatMap(e -> {
        log.info(e.getMessage());
        result.put("errors", t.getMessage());
        return Mono.just(ResponseEntity.internalServerError().body(result));

      });
    }
  }
}