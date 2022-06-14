package com.nttdata.bootcamp.transactiondomain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.bootcamp.transactiondomain.model.AccountTransaction;

public interface AccountTransactionRepository extends ReactiveMongoRepository<AccountTransaction, String> {

}
