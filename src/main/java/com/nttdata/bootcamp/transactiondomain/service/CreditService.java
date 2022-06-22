package com.nttdata.bootcamp.transactiondomain.service;

import com.nttdata.bootcamp.transactiondomain.model.Credit;
import com.nttdata.bootcamp.transactiondomain.model.CustomerType;
import com.nttdata.bootcamp.transactiondomain.repository.CreditRepository;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCreditService;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCustomerService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
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
  private ExternalCustomerService externalCustomerService;

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
    if (Objects.equals(creditTransaction.getCustomerType(), CustomerType.PERSONAL)) {
      return personalTransaction(creditTransaction);
    } else if (Objects.equals(creditTransaction.getCustomerType(), CustomerType.COMMERCIAL)) {
      return commercialTransaction(creditTransaction);
    }
    return Mono.error(new RuntimeException("Type of customer not defined"));
  }

  private Mono<Credit> personalTransaction(Credit creditTransaction) {
    return externalCustomerService.findPersonalById(creditTransaction.getCustomerId())
        .flatMap(personal -> externalCreditService.findById(creditTransaction.getCreditId())
            .flatMap(credit -> {
              creditTransaction.setPaymentDate(new Date());
              creditTransaction.setInterestRate(new BigDecimal("0.00"));
              credit.setFeesPaid(credit.getFeesPaid() + 1);
              credit.setAmountPaid(credit.getAmountPaid().add(creditTransaction.getAmount()));
              return externalCreditService.update(credit, creditTransaction.getCreditId())
                  .flatMap(p -> creditRepository.save(creditTransaction));
            }));
  }

  private Mono<Credit> commercialTransaction(Credit creditTransaction) {
    return externalCustomerService.findCommercialById(creditTransaction.getCustomerId())
        .flatMap(commercial -> externalCreditService.findById(creditTransaction.getCreditId())
            .flatMap(credit -> {
              creditTransaction.setPaymentDate(new Date());
              creditTransaction.setInterestRate(new BigDecimal("0.00"));
              credit.setFeesPaid(credit.getFeesPaid() + 1);
              credit.setAmountPaid(credit.getAmountPaid().add(creditTransaction.getAmount()));
              return externalCreditService.update(credit, creditTransaction.getCreditId())
                  .flatMap(p -> creditRepository.save(creditTransaction));
            }));
  }

  /**
   * delete creditTransaction.
   */
  public Mono<Void> delete(Credit creditTransaction) {
    return creditRepository.delete(creditTransaction);
  }

  /**
   * find creditTransaction by customerType and customerId.
   */
  public Flux<Credit> findByCustomerTypeAndCustomerId(String customerType,
                                                      String customerId) {
    return creditRepository.findByCustomerTypeAndCustomerId(customerType, customerId);
  }

}
