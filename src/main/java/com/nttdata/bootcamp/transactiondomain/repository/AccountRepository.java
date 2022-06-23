package com.nttdata.bootcamp.transactiondomain.repository;

import com.nttdata.bootcamp.transactiondomain.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

/**
 * AccountTransaction Repository.
 */
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

  /**
   * findAll by customerType and customerId.
   */
  Flux<Account> findAllByAccountTypeAndCustomerId(String accountType, String customerId);

}
