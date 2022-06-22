package com.nttdata.bootcamp.transactiondomain.service;

import com.nttdata.bootcamp.transactiondomain.model.Account;
import com.nttdata.bootcamp.transactiondomain.model.CustomerType;
import com.nttdata.bootcamp.transactiondomain.model.MovementType;
import com.nttdata.bootcamp.transactiondomain.repository.AccountRepository;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalAccountService;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalCustomerService;
import com.nttdata.bootcamp.transactiondomain.service.external.ExternalTypeAccountService;
import com.nttdata.bootcamp.transactiondomain.service.external.dto.TypeAccountDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Account transaction service.
 */
@Service
public class AccountService {


  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private ExternalAccountService externalAccountService;

  @Autowired
  private ExternalCustomerService externalCustomerService;

  @Autowired
  private ExternalTypeAccountService typeAccountService;

  /**
   * findAll account transaction.
   */
  public Flux<Account> findAll() {
    return accountRepository.findAll();
  }

  /**
   * findById account transaction.
   */
  public Mono<Account> findById(String id) {
    return accountRepository.findById(id);
  }

  /**
   * save account transaction.
   */
  public Mono<Account> save(Account transaction) {
    return findByCustomerTypeAndCustomerId(transaction.getCustomerType().name(),
        transaction.getCustomerId())
        .collectList()
        .flatMap(lst -> {
          if (Objects.equals(transaction.getCustomerType(), CustomerType.PERSONAL)) {
            return personalTransaction(transaction, lst);
          } else if (Objects.equals(transaction.getCustomerType(), CustomerType.COMMERCIAL)) {
            return commercialTransaction(transaction, lst);
          }
          return Mono.error(new RuntimeException("Type of customer not defined"));
        });
  }

  private Mono<Account> commercialTransaction(
      Account transaction, List<Account> transactionList) {
    return externalCustomerService.findCommercialById(transaction.getCustomerId())
        .flatMap(
            customerDto -> externalAccountService.findCommercialById(transaction.getAccountId())
                .flatMap(accountDto -> typeAccountService.findById(accountDto.getTypeAccountId())
                    .flatMap(typeAccDto -> {
                      BigDecimal commision = calculateCommision(typeAccDto, transactionList);
                      if (allowMovementTransaction(typeAccDto, transactionList)) {
                        return Mono.error(new RuntimeException("Not allowed operation of the day"));
                      }
                      transaction.setTransactionDate(new Date());
                      transaction.setAmountCommission(commision);
                      if (Objects.equals(transaction.getMovementType(), MovementType.DEPOSIT)) {
                        accountDto.setAmount(accountDto.getAmount().add(transaction.getAmount())
                            .subtract(commision));
                        return externalAccountService.updateCommercial(accountDto,
                                accountDto.getId())
                            .flatMap(accountTx -> accountRepository.save(transaction));

                      } else if (Objects.equals(transaction.getMovementType(),
                          MovementType.WITHDRAWALS)) {
                        if (accountDto.getAmount().compareTo(transaction.getAmount()) < 0) {
                          return Mono.error(new RuntimeException("Insufficient amount"));
                        }
                        accountDto.setAmount(
                            accountDto.getAmount().subtract(transaction.getAmount())
                                .subtract(commision));
                        return externalAccountService.updateCommercial(accountDto,
                                accountDto.getId())
                            .flatMap(accountTx -> accountRepository.save(transaction));
                      }
                      return Mono.error(new RuntimeException("type of movement not defined"));

                    })));
  }

  private boolean allowMovementTransaction(TypeAccountDto typeAccDto,
                                           List<Account> transactionList) {
    int day = LocalDate.now().getDayOfMonth();
    int countMovements = getNumberTransactionsCurrentMonth(transactionList);
    int dayMovement = typeAccDto.getDayMovement();
    int numLimitMovements = typeAccDto.getNumLimitMovements();
    return dayMovement > 0 && (dayMovement != day || numLimitMovements == countMovements);
  }

  private BigDecimal calculateCommision(TypeAccountDto typeAccDto,
                                        List<Account> accountTransactions) {
    int countMovements = getNumberTransactionsCurrentMonth(accountTransactions);
    BigDecimal amountCommision = BigDecimal.ZERO;
    if (countMovements > typeAccDto.getNumLimitMovements()) {
      amountCommision = amountCommision.add(typeAccDto.getTransactionCommission());
    }
    return amountCommision;
  }

  private Mono<Account> personalTransaction(
      Account transaction,
      List<Account> accountTransactions) {
    return externalCustomerService.findPersonalById(transaction.getCustomerId())
        .flatMap(personal -> externalAccountService.findPersonalById(transaction.getAccountId())
            .flatMap(
                accountDto -> typeAccountService.findById(accountDto.getTypeAccountId())
                    .flatMap(typeAccDto -> {
                      BigDecimal commision = calculateCommision(typeAccDto, accountTransactions);
                      if (allowMovementTransaction(typeAccDto, accountTransactions)) {
                        return Mono.error(new RuntimeException("Not allowed operation of the day"));
                      }
                      transaction.setTransactionDate(new Date());
                      transaction.setAmountCommission(commision);
                      if (transaction.getMovementType().equals(MovementType.DEPOSIT)) {
                        accountDto.setAmount(accountDto.getAmount().add(transaction.getAmount())
                            .subtract(commision));
                        return externalAccountService.updatePersonal(accountDto, accountDto.getId())
                            .flatMap(accountTx -> accountRepository.save(transaction));

                      } else if (transaction.getMovementType().equals(MovementType.WITHDRAWALS)) {
                        if (accountDto.getAmount().compareTo(transaction.getAmount()) < 0) {
                          return Mono.error(new RuntimeException("Insufficient amount"));
                        }
                        accountDto.setAmount(accountDto.getAmount().add(transaction.getAmount())
                            .subtract(commision));
                        return externalAccountService.updatePersonal(accountDto, accountDto.getId())
                            .flatMap(accountTx -> accountRepository.save(transaction));
                      }
                      return Mono.empty();
                    })));
  }

  private int getNumberTransactionsCurrentMonth(List<Account> accountTransactions) {
    ZoneId defaultZoneId = ZoneId.systemDefault();
    LocalDate firstDateLocal = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    LocalDate endDateLocal = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());
    Date firstDate = Date.from(firstDateLocal.atStartOfDay(defaultZoneId).toInstant());
    Date endDate = Date.from(endDateLocal.atStartOfDay(defaultZoneId).toInstant());
    return (int) accountTransactions
        .stream()
        .filter(account -> {
          int dateGreaterThanEqualFirstDayMonth = account.getTransactionDate().compareTo(firstDate);
          int dateLessthanEqualLastDayMonth = account.getTransactionDate().compareTo(endDate);
          return dateGreaterThanEqualFirstDayMonth >= 0 && dateLessthanEqualLastDayMonth <= 0;
        }).count();
  }

  /**
   * delete accountTransaction.
   */
  public Mono<Void> delete(Account transaction) {
    return accountRepository.delete(transaction);
  }

  /**
   * find accountTransaction by customerType and customerId.
   */
  public Flux<Account> findByCustomerTypeAndCustomerId(String customerType,
                                                       String customerId) {
    return accountRepository.findByCustomerTypeAndCustomerId(customerType, customerId);
  }
}
