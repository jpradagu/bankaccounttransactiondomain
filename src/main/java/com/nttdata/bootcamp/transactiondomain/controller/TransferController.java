package com.nttdata.bootcamp.transactiondomain.controller;

import com.nttdata.bootcamp.transactiondomain.exception.ResumenError;
import com.nttdata.bootcamp.transactiondomain.model.Transfer;
import com.nttdata.bootcamp.transactiondomain.service.TransferService;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
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
 * TransferController.
 */
@RestController
@RequestMapping("/api/transfer")
public class TransferController {

  @Autowired
  private TransferService transferService;

  /**
   * findAll Transfer.
   */
  @GetMapping
  public Mono<ResponseEntity<Flux<Transfer>>> findAll() {
    return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
        .body(transferService.findAll()));
  }

  /**
   * find Transfer.
   */
  @GetMapping("/{id}")
  public Mono<ResponseEntity<Transfer>> findById(@PathVariable String id) {
    return transferService.findById(id)
        .map(ce -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(ce))
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  /**
   * create Transfer.
   */
  @PostMapping
  public Mono<ResponseEntity<Map<String, Object>>> create(
      @Valid @RequestBody Mono<Transfer> monoAccount) {
    Map<String, Object> result = new HashMap<>();
    return monoAccount.flatMap(a -> {
      a.setId(null);
      return transferService.save(a).map(account -> ResponseEntity.created(
              URI.create("/api/account-transaction/".concat(account.getId())))
          .contentType(MediaType.APPLICATION_JSON).body(result));
    }).onErrorResume(ResumenError::errorResumenException);
  }

  /**
   * delete Transfer.
   */
  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
    return transferService.findById(id).flatMap(e -> transferService.delete(e)
            .then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
        .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

}
