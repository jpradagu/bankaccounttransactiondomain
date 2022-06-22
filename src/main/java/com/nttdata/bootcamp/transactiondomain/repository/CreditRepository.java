package com.nttdata.bootcamp.transactiondomain.repository;

import com.nttdata.bootcamp.transactiondomain.model.Credit;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * Credit repository.
 */
public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {
  /**
   * find by customerType and customerId.
   */
  Flux<Credit> findByCustomerTypeAndCustomerId(String customerType, String customerId);
}
