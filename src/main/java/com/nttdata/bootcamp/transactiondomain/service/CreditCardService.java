package com.nttdata.bootcamp.transactiondomain.service;

import com.nttdata.bootcamp.transactiondomain.model.CreditCard;
import com.nttdata.bootcamp.transactiondomain.model.CustomerType;
import com.nttdata.bootcamp.transactiondomain.repository.CreditCardRepository;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCreditCardService;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCustomerService;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * CreditCardTransaction Service.
 */
@Service
public class CreditCardService {
  @Autowired
  private CreditCardRepository creditCardRepository;

  @Autowired
  private ExternalCustomerService externalCustomerService;

  @Autowired
  private ExternalCreditCardService externalCreditCardService;

  /**
   * findAll CreditCardTransaction.
   */
  public Flux<CreditCard> findAll() {
    return creditCardRepository.findAll();
  }

  /**
   * find CreditCardTransaction.
   */
  public Mono<CreditCard> findById(String id) {
    return creditCardRepository.findById(id);
  }

  /**
   * save CreditCardTransaction.
   */
  public Mono<CreditCard> save(CreditCard creditTransaction) {
    if (Objects.equals(creditTransaction.getCustomerType(), CustomerType.PERSONAL)) {
      return personalTransaction(creditTransaction);

    } else if (Objects.equals(creditTransaction.getCustomerType(), CustomerType.COMMERCIAL)) {
      return commercialTransaction(creditTransaction);
    }
    return Mono.error(new RuntimeException("Type of customer not defined"));
  }

  private Mono<CreditCard> personalTransaction(CreditCard creditTransaction) {
    return externalCustomerService.findPersonalById(creditTransaction.getCustomerId())
        .flatMap(personal -> externalCreditCardService.findById(creditTransaction.getCreditCardId())
            .flatMap(credit -> {
              if (creditTransaction.getAmount().compareTo(credit.getLimitAmount()) > 0) {
                return Mono.error(new RuntimeException("Amount exceeds allowable limit"));
              }
              creditTransaction.setTradeDate(new Date());
              creditTransaction.setInterestRate(new BigDecimal("0.00"));
              credit.setUsedAmount(credit.getLimitAmount().subtract(creditTransaction.getAmount()));
              return externalCreditCardService.update(credit, creditTransaction.getCreditCardId())
                  .flatMap(p -> creditCardRepository.save(creditTransaction));
            }));
  }

  private Mono<CreditCard> commercialTransaction(
      CreditCard creditTransaction) {
    return externalCustomerService.findCommercialById(creditTransaction.getCustomerId())
        .flatMap(
            commercial -> externalCreditCardService.findById(creditTransaction.getCreditCardId())
                .flatMap(credit -> {
                  if (creditTransaction.getAmount().compareTo(credit.getLimitAmount()) > 0) {
                    return Mono.error(new RuntimeException("Amount exceeds allowable limit"));
                  }
                  creditTransaction.setTradeDate(new Date());
                  creditTransaction.setInterestRate(new BigDecimal("0.00"));
                  credit.setUsedAmount(
                      credit.getLimitAmount().subtract(creditTransaction.getAmount()));
                  return externalCreditCardService.update(credit,
                          creditTransaction.getCreditCardId())
                      .flatMap(p -> creditCardRepository.save(creditTransaction));
                }));
  }

  /**
   * delete CreditCardTransaction.
   */
  public Mono<Void> delete(CreditCard creditTransaction) {
    return creditCardRepository.delete(creditTransaction);
  }

  /**
   * find CreditCardTransaction by customerType and customerId.
   */
  public Flux<CreditCard> findByCustomerTypeAndCustomerId(String customerType,
                                                          String customerId) {
    return creditCardRepository.findByCustomerTypeAndCustomerId(customerType, customerId);
  }
}
