package com.nttdata.bootcamp.transactiondomain.service;

import com.nttdata.bootcamp.transactiondomain.model.Credit;
import com.nttdata.bootcamp.transactiondomain.repository.CreditRepository;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCreditService;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Credit service.
 */
@Service
public class CreditService {

  @Autowired
  private CreditRepository creditRepository;

  @Autowired
  private ExternalCreditService externalCreditService;

  public Flux<Credit> findAll() {
    return creditRepository.findAll();
  }

  public Mono<Credit> findById(String id) {
    return creditRepository.findById(id);
  }


  /**
   * save credit transaction.
   */
  public Mono<Credit> save(Credit creditTransaction) {
    return externalCreditService.findById(creditTransaction.getCreditId())
        .flatMap(credit -> {
          creditTransaction.setCustomerId(credit.getCustomerId());
          creditTransaction.setPaymentDate(new Date());
          creditTransaction.setInterestRate(new BigDecimal("0.00"));
          credit.setFeesPaid(credit.getFeesPaid() + 1);
          credit.setAmountPaid(credit.getAmountPaid().add(creditTransaction.getAmount()));
          return externalCreditService.update(credit, creditTransaction.getCreditId())
              .flatMap(p -> creditRepository.save(creditTransaction));
        });
  }

  /**
   * delete creditTransaction.
   */
  public Mono<Void> delete(Credit creditTransaction) {
    return creditRepository.delete(creditTransaction);
  }

  /**
   * findAll creditTransaction by CustomerId.
   */
  public Flux<Credit> findAllByCustomerId(String customerId) {
    return creditRepository.findAllByCustomerId(customerId);
  }

}
