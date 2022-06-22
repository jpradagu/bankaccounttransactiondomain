package com.nttdata.bootcamp.transactiondomain.controller;

import com.nttdata.bootcamp.transactiondomain.exception.ResumenError;
import com.nttdata.bootcamp.transactiondomain.model.Account;
import com.nttdata.bootcamp.transactiondomain.service.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * AccountController.
 */
@RestController
@RequestMapping("/api/transaction/account")
public class AccountController {

  private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private AccountService accountService;

  /**
   * findAll accountTransaction.
   */
  @GetMapping
  public Mono<ResponseEntity<Flux<Account>>> findAll() {
    return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(accountService.findAll()));
  }

  /**
   * find accountTransaction.
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Account>> findById(@PathVariable String id) {
    return accountService.findById(id)
        .map(ce -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ce))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * create accountTransaction.
   */
  @PostMapping
  @CircuitBreaker(name = "allCB", fallbackMethod = "fallbackCreateAccount")
  public Mono<ResponseEntity<Map<String, Object>>> create(
      @Valid @RequestBody Mono<Account> monoAccount) {
    Map<String, Object> result = new HashMap<>();
    return monoAccount.flatMap(a -> {
      a.setId(null);
      return accountService.save(a).map(account -> ResponseEntity.created(
              URI.create("/api/account-transaction/".concat(account.getId())))
          .contentType(MediaType.APPLICATION_JSON).body(result));
    }).onErrorResume(ResumenError::errorResumenException);
  }

  /**
   * delete accountTransaction.
   */
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return accountService.findById(id).flatMap(e -> accountService.delete(e)
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  /**
   * findAll accountTransaction by customerType and customerId.
   */
  @GetMapping("/{customerType}/{customerId}")
  public Mono<ResponseEntity<Flux<Account>>> findAllByCustomerTypeAndCustomerId(
      @PathVariable String customerType, @PathVariable String customerId) {
    return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(accountService.findByCustomerTypeAndCustomerId(customerType, customerId)));
  }

  public Mono<ResponseEntity<Map<String, Object>>> fallbackCreateAccount(RuntimeException e) {
    logger.error("transaction failed: ".concat(e.getMessage()));
    return Mono.just(ResponseEntity.internalServerError().build());
  }
}
