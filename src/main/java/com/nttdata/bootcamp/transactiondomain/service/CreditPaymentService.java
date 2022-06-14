package com.nttdata.bootcamp.transactiondomain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.bootcamp.transactiondomain.model.CreditPayment;
import com.nttdata.bootcamp.transactiondomain.repository.CreditPaymentRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CreditPaymentService {

	@Autowired
	private CreditPaymentRepository paymentRepository;

	public Flux<CreditPayment> findAll() {
		return paymentRepository.findAll();
	}

	public Mono<CreditPayment> findById(String id) {
		return paymentRepository.findById(id);
	}

	public Mono<CreditPayment> save(CreditPayment payment) {
		return paymentRepository.save(payment);
	}

	public Mono<Void> delete(CreditPayment payment) {
		return paymentRepository.delete(payment);
	}
}
