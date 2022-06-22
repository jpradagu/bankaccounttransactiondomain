package com.nttdata.bootcamp.transactiondomain.repository;

import com.nttdata.bootcamp.transactiondomain.model.CreditCard;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * CreditCardTransaction Repository.
 */
public interface CreditCardRepository extends ReactiveMongoRepository<CreditCard, String> {

  /**
   * find by customerType and customerId.
   */
  Flux<CreditCard> findByCustomerTypeAndCustomerId(String customerType, String customerId);
}
