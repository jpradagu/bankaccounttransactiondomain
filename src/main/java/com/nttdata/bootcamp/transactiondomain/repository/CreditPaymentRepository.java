package com.nttdata.bootcamp.transactiondomain.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.nttdata.bootcamp.transactiondomain.model.CreditPayment;

public interface CreditPaymentRepository extends ReactiveMongoRepository<CreditPayment, String>{

}
