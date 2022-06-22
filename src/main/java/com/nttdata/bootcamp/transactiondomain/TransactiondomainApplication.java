package com.nttdata.bootcamp.transactiondomain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Transaction domain application.
 */
@EnableDiscoveryClient
@SpringBootApplication
public class TransactiondomainApplication {

  public static void main(String[] args) {
    SpringApplication.run(TransactiondomainApplication.class, args);
  }

}
