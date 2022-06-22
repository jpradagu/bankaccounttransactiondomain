package com.nttdata.bootcamp.transactiondomain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TransferType.
 */
@AllArgsConstructor
@Getter
public enum TransferType {
  THIRD_ACCOUNT, SELF_ACCOUNT
}
