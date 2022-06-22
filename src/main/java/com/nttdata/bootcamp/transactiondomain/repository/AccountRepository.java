package com.nttdata.bootcamp.transactiondomain.repository;

import com.nttdata.bootcamp.transactiondomain.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * AccountTransaction Repository.
 */
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

  /**
   * find by customerType and customerId.
   */
  Flux<Account> findByCustomerTypeAndCustomerId(String customerType, String customerId);

}
