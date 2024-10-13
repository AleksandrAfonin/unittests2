package ru.otus.java.professional.unittests2.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.java.professional.unittests2.entity.Account;
import ru.otus.java.professional.unittests2.entity.Agreement;
import ru.otus.java.professional.unittests2.service.AccountService;
import ru.otus.java.professional.unittests2.service.impl.PaymentProcessorImpl;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorImplTest {

  @Mock
  AccountService accountService;

  @InjectMocks
  PaymentProcessorImpl paymentProcessor;

  @Test
  public void testTransfer() {
    Agreement sourceAgreement = new Agreement();
    sourceAgreement.setId(1L);

    Agreement destinationAgreement = new Agreement();
    destinationAgreement.setId(2L);

    Account sourceAccount = new Account();
    sourceAccount.setAmount(BigDecimal.TEN);
    sourceAccount.setType(0);

    Account destinationAccount = new Account();
    destinationAccount.setAmount(BigDecimal.ZERO);
    destinationAccount.setType(0);

    when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
      @Override
      public boolean matches(Agreement argument) {
        return argument != null && argument.getId() == 1L;
      }
    }))).thenReturn(List.of(sourceAccount));

    when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
      @Override
      public boolean matches(Agreement argument) {
        return argument != null && argument.getId() == 2L;
      }
    }))).thenReturn(List.of(destinationAccount));

    paymentProcessor.makeTransfer(sourceAgreement, destinationAgreement,
            0, 0, BigDecimal.ONE);

  }

  @Test
  public void testTransferWithCommission() {
    Agreement sourceAgreement = new Agreement();
    sourceAgreement.setId(1L);

    Agreement destinationAgreement = new Agreement();
    destinationAgreement.setId(2L);

    Account sourceAccount = new Account();
    sourceAccount.setId(1L);
    sourceAccount.setAmount(BigDecimal.valueOf(100));
    sourceAccount.setType(0);

    Account destinationAccount = new Account();
    destinationAccount.setId(2L);
    destinationAccount.setAmount(BigDecimal.valueOf(0));
    destinationAccount.setType(0);

    when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
      @Override
      public boolean matches(Agreement argument) {
        return argument != null && argument.getId() == 1L;
      }
    }))).thenReturn(List.of(sourceAccount));

    when(accountService.getAccounts(argThat(new ArgumentMatcher<Agreement>() {
      @Override
      public boolean matches(Agreement argument) {
        return argument != null && argument.getId() == 2L;
      }
    }))).thenReturn(List.of(destinationAccount));

    paymentProcessor.makeTransferWithComission(sourceAgreement, destinationAgreement,
            0, 0, BigDecimal.valueOf(10), BigDecimal.valueOf(0.1));

  }
}
