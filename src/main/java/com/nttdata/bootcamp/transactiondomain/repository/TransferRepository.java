package com.nttdata.bootcamp.transactiondomain.repository;

import com.nttdata.bootcamp.transactiondomain.model.Transfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * TransferRepository.
 */
public interface TransferRepository extends ReactiveMongoRepository<Transfer, String> {
}
