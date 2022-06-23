package com.nttdata.bootcamp.transactiondomain.service;

import com.nttdata.bootcamp.transactiondomain.model.Account;
import com.nttdata.bootcamp.transactiondomain.model.AccountType;
import com.nttdata.bootcamp.transactiondomain.model.MovementType;
import com.nttdata.bootcamp.transactiondomain.model.Transfer;
import com.nttdata.bootcamp.transactiondomain.repository.TransferRepository;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalAccountService;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCustomerService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.CommercialAccountDto;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.PersonalAccountDto;
import java.math.BigDecimal;
import java.util.Date;
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
  private ExternalAccountService externalAccountService;

  @Autowired
  private ExternalCustomerService externalCustomerService;

  @Autowired
  private AccountService accountService;

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
    if (Objects.equals(transfer.getRecipientAccountType(), AccountType.PERSONAL)) {
      return personalTransfer(transfer);
    } else if (Objects.equals(transfer.getRecipientAccountType(), AccountType.COMMERCIAL)) {
      return commercialTransfer(transfer);
    }
    return Mono.error(new RuntimeException("Type of customer not defined"));
  }

  private Mono<Transfer> personalTransfer(Transfer transfer) {
    return externalAccountService.findPersonalById(transfer.getRecipientAccountId())
        .flatMap(accountDto -> externalCustomerService.findPersonalById(
                accountDto.getCustomerId())
            .flatMap(personalCustomerDto -> {
              if (personalCustomerDto.getDni().equals(transfer.getRecipientDocumentNumber())) {
                return buildTransferToRecipientPersonal(transfer, accountDto).flatMap(
                    isOkRecipient -> {
                      if (Boolean.TRUE.equals(isOkRecipient)) {
                        return transferRepository.save(transfer);
                      } else {
                        return Mono.error(new RuntimeException("Error transfer"));
                      }
                    });
              } else {
                return Mono.error(new RuntimeException("Incorrect document number"));
              }
            }));
  }

  private Mono<Transfer> commercialTransfer(Transfer transfer) {
    return externalAccountService.findCommercialById(transfer.getRecipientAccountId())
        .flatMap(
            accountDto -> externalCustomerService.findCommercialById(accountDto.getCustomerId())
                .flatMap(commercialCustomerDto -> {
                  if (commercialCustomerDto.getReasonSocial()
                      .equals(transfer.getRecipientDocumentNumber())) {
                    return buildTransferToRecipientCommercial(transfer, accountDto).flatMap(
                        isOkRecipient -> {
                          if (Boolean.TRUE.equals(isOkRecipient)) {
                            return transferRepository.save(transfer);
                          } else {
                            return Mono.error(new RuntimeException("Error transfer"));
                          }
                        });
                  } else {
                    return Mono.error(new RuntimeException("Incorrect document number"));
                  }
                }));
  }

  private Mono<Boolean> buildTransferToRecipientCommercial(Transfer transfer,
                                                           CommercialAccountDto accountDto) {
    return externalAccountService.findCommercialById(transfer.getSourceAccountId())
        .flatMap(comercialDto -> buildAccount(accountDto.getId(),
            MovementType.WITHDRAWALS, transfer.getAmount()).flatMap(p -> Mono.just(true)
        ).onErrorResume(e -> Mono.just(false)))
        .flatMap(isOkSource -> {
          if (Boolean.TRUE.equals(isOkSource)) {
            return buildAccount(accountDto.getId(), MovementType.DEPOSIT, transfer.getAmount())
                .flatMap(p -> Mono.just(true))
                .onErrorResume(e -> Mono.just(false));
          } else {
            return Mono.just(false);
          }
        });
  }

  private Mono<Boolean> buildTransferToRecipientPersonal(Transfer transfer,
                                                         PersonalAccountDto accountDto) {
    return externalAccountService.findPersonalById(transfer.getSourceAccountId())
        .flatMap(comercialDto -> buildAccount(accountDto.getId(),
            MovementType.WITHDRAWALS, transfer.getAmount()).flatMap(p -> Mono.just(true)
        ).onErrorResume(e -> Mono.just(false)))
        .flatMap(isOkSource -> {
          if (Boolean.TRUE.equals(isOkSource)) {
            return buildAccount(accountDto.getId(), MovementType.DEPOSIT, transfer.getAmount())
                .flatMap(p -> Mono.just(true))
                .onErrorResume(e -> Mono.just(false));
          } else {
            return Mono.just(false);
          }
        });
  }

  private Mono<Account> buildAccount(String accountId,
                                     MovementType movementType,
                                     BigDecimal amount) {
    Account account = new Account();
    account.setAccountId(accountId);
    account.setAccountType(AccountType.COMMERCIAL);
    account.setAmount(amount);
    account.setMovementType(movementType);
    account.setTransactionDate(new Date());
    return accountService.save(account);
  }

  /**
   * delete transfer.
   */
  public Mono<Void> delete(Transfer tranfer) {
    return transferRepository.delete(tranfer);
  }
}
