package com.nttdata.bootcamp.transactiondomain.repository;

import com.nttdata.bootcamp.transactiondomain.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * CreditCardTransaction Repository.
 */
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

  /**
   * findAll by customerId.
   */
  Flux<CreditCard> findAllByCustomerId(String customerId);
}
