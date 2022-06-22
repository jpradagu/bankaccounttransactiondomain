package com.nttdata.bootcamp.transactiondomain.service;

import com.nttdata.bootcamp.transactiondomain.model.CustomerType;
import com.nttdata.bootcamp.transactiondomain.model.Transfer;
import com.nttdata.bootcamp.transactiondomain.repository.TransferRepository;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalAccountService;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCustomerService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * TransferService.
 */
@Service
public class TransferService {

  @Autowired
  private TransferRepository transferRepository;

  @Autowired
  private ExternalAccountService accountService;

  @Autowired
  private ExternalCustomerService customerService;

  /**
   * findAll transfer.
   */
  public Flux<Transfer> findAll() {
    return transferRepository.findAll();
  }

  /**
   * find transfer.
   */
  public Mono<Transfer> findById(String id) {
    return transferRepository.findById(id);
  }

  /**
   * save transfer.
   */
  public Mono<Transfer> save(Transfer transfer) {
    if (Objects.equals(transfer.getRecipientCustomerType(), CustomerType.PERSONAL)) {
      return personalTransfer(transfer);
    } else if (Objects.equals(transfer.getRecipientCustomerType(), CustomerType.COMMERCIAL)) {
      return commercialTransfer(transfer);
    }
    return Mono.error(new RuntimeException("Type of customer not defined"));
  }

  private Mono<Transfer> personalTransfer(Transfer transfer) {
    return accountService.findPersonalById(transfer.getRecipientAccountId())
        .flatMap(personalAccountDto -> customerService.findPersonalById(
                personalAccountDto.getCustomerId())
            .flatMap(personalCustomerDto -> {
              if (personalCustomerDto.getDni().equals(transfer.getRecipientDocumentNumber())) {
                return transferRepository.save(transfer);
              } else {
                return Mono.error(new RuntimeException("Incorrect document number"));
              }
            }));
  }

  private Mono<Transfer> commercialTransfer(Transfer transfer) {
    return accountService.findCommercialById(transfer.getRecipientAccountId())
        .flatMap(commercialAccountDto -> customerService.findCommercialById(
                commercialAccountDto.getCustomerId())
            .flatMap(commercialCustomerDto -> {
              if (commercialCustomerDto.getReasonSocial()
                  .equals(transfer.getRecipientDocumentNumber())) {
                return transferRepository.save(transfer);
              } else {
                return Mono.error(new RuntimeException("Incorrect document number"));
              }
            }));
  }

  /**
   * delete transfer.
   */
  public Mono<Void> delete(Transfer tranfer) {
    return transferRepository.delete(tranfer);
  }
}
