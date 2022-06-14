package com.nttdata.bootcamp.transactiondomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.transactiondomain.model.AccountTransaction;
import com.nttdata.bootcamp.transactiondomain.repository.AccountTransactionRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AccountTransactionService {

	@Autowired
	private AccountTransactionRepository transactionRepository;

	public Flux<AccountTransaction> findAll() {
		return transactionRepository.findAll();
	}

	public Mono<AccountTransaction> findById(String id) {
		return transactionRepository.findById(id);
	}

	public Mono<AccountTransaction> save(AccountTransaction transaction) {
		return transactionRepository.save(transaction);
	}

	public Mono<Void> delete(AccountTransaction transaction) {
		return transactionRepository.delete(transaction);
	}
}
