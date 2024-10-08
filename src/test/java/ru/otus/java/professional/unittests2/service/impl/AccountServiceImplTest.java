package ru.otus.java.professional.unittests2.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.java.professional.unittests2.dao.AccountDao;
import ru.otus.java.professional.unittests2.entity.Account;
import ru.otus.java.professional.unittests2.entity.Agreement;
import ru.otus.java.professional.unittests2.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
  @Mock
  AccountDao accountDao;

  @InjectMocks
  AccountServiceImpl accountServiceImpl;

  @ParameterizedTest
  @MethodSource("args")
  public void transferTest(BigDecimal srcAmount, BigDecimal destAmount, BigDecimal sum, boolean expectedResult) {
    Account sourceAccount = new Account();
    sourceAccount.setAmount(srcAmount);

    Account destinationAccount = new Account();
    destinationAccount.setAmount(destAmount);

    when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
    when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

    boolean result = accountServiceImpl.makeTransfer(1L, 2L, sum);

    assertEquals(expectedResult, result);
  }

  public static Stream<? extends Arguments> args() {
    return Stream.of(
            Arguments.of(new BigDecimal(100), new BigDecimal(0), new BigDecimal(10), true),
            Arguments.of(new BigDecimal(0), new BigDecimal(0), new BigDecimal(10), false),
            Arguments.of(new BigDecimal(10), new BigDecimal(0), new BigDecimal(10), true)
    );
  }

  @Test
  public void testTransfer() {
    Account sourceAccount = new Account();
    sourceAccount.setAmount(new BigDecimal(100));

    Account destinationAccount = new Account();
    destinationAccount.setAmount(new BigDecimal(10));

    when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
    when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

    accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

    assertEquals(new BigDecimal(90), sourceAccount.getAmount());
    assertEquals(new BigDecimal(20), destinationAccount.getAmount());
  }

  @Test
  public void testTransferNoAccount() {
    Account sourceAccount = new Account();
    sourceAccount.setAmount(new BigDecimal(100));

    Account destinationAccount = new Account();
    destinationAccount.setAmount(new BigDecimal(0));

    lenient().when(accountDao.findById(eq(1L))).thenReturn(Optional.empty());
    lenient().when(accountDao.findById(eq(2L))).thenReturn(Optional.empty());

    assertThrows(AccountException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));
      }
    });
  }

  @Test
  public void makeTransferTrueValues() {
    Account sourceAccount = new Account();
    sourceAccount.setAmount(new BigDecimal(100));

    Account destinationAccount = new Account();
    destinationAccount.setAmount(new BigDecimal(0));

    when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
    when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

    ArgumentCaptor<Account> savedAccounts = ArgumentCaptor.captor();
    when(accountDao.save(savedAccounts.capture())).thenReturn(sourceAccount);

    boolean result = accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

    assertTrue(result);
    assertEquals(new BigDecimal(90), savedAccounts.getAllValues().get(0).getAmount());
    assertEquals(new BigDecimal(10), savedAccounts.getAllValues().get(1).getAmount());

  }

  @Test
  public void makeTransferTrueValuesCalls() {
    Account sourceAccount = new Account();
    sourceAccount.setAmount(new BigDecimal(100));

    Account destinationAccount = new Account();
    destinationAccount.setAmount(new BigDecimal(0));

    when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
    when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

    ArgumentCaptor<Account> savedAccounts = ArgumentCaptor.captor();
    when(accountDao.save(savedAccounts.capture())).thenReturn(sourceAccount);

    boolean result = accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

    ArgumentMatcher<Account> argumentMatcher0 =
            account -> (account != null && account.getAmount().equals(new BigDecimal(90)));

    ArgumentMatcher<Account> argumentMatcher1 =
            account -> (account != null && account.getAmount().equals(new BigDecimal(10)));

    verify(accountDao).save(argThat(argumentMatcher0));
    verify(accountDao).save(argThat(argumentMatcher1));

    assertTrue(result);
  }

  @Test
  public void testSourceNotFound() {
    when(accountDao.findById(any())).thenReturn(Optional.empty());

    AccountException result = assertThrows(AccountException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));
      }
    });
    assertEquals("No source account", result.getLocalizedMessage());
  }


  @Test
  public void testTransferWithVerify() {
    Account sourceAccount = new Account();
    sourceAccount.setAmount(new BigDecimal(100));
    sourceAccount.setId(1L);

    Account destinationAccount = new Account();
    destinationAccount.setAmount(new BigDecimal(10));
    destinationAccount.setId(2L);

    when(accountDao.findById(eq(1L))).thenReturn(Optional.of(sourceAccount));
    when(accountDao.findById(eq(2L))).thenReturn(Optional.of(destinationAccount));

    ArgumentMatcher<Account> sourceMatcher =
            argument -> argument.getId().equals(1L) && argument.getAmount().equals(new BigDecimal(90));

    ArgumentMatcher<Account> destinationMatcher =
            argument -> argument.getId().equals(2L) && argument.getAmount().equals(new BigDecimal(20));

    accountServiceImpl.makeTransfer(1L, 2L, new BigDecimal(10));

    verify(accountDao).save(argThat(sourceMatcher));
    verify(accountDao).save(argThat(destinationMatcher));
  }

  @Test
  public void testChargeTrue(){
    Account account = new Account();
    account.setAmount(new BigDecimal(100));

    when(accountDao.findById(eq(1L))).thenReturn(Optional.of(account));

    boolean result = accountServiceImpl.charge(1L, new BigDecimal(10));

    assertTrue(result);
  }

  @Test
  public void testChargeNoAccount() {
    when(accountDao.findById(any())).thenReturn(Optional.empty());

    AccountException result = assertThrows(AccountException.class, new Executable() {
      @Override
      public void execute() throws Throwable {
        accountServiceImpl.charge(1L, new BigDecimal(10));
      }
    });
    assertEquals("No source account", result.getLocalizedMessage());
  }

  @Test
  public void testGetAccounts(){
    Account account1 = new Account();
    account1.setId(1L);
    account1.setAmount(new BigDecimal(100));
    account1.setType(1);
    account1.setNumber("1234");
    account1.setAgreementId(1L);

    Account account2 = new Account();
    account2.setId(2L);
    account1.setAmount(new BigDecimal(120));
    account1.setType(1);
    account1.setNumber("4321");
    account1.setAgreementId(2L);

    HashMap<Long, Account> accountHashMap = new HashMap<>();
    accountHashMap.put(1L, account1);
    accountHashMap.put(2L, account2);

    List<Account> accountList = new ArrayList<>();
    accountList.add(account1);
    accountList.add(account2);

    when(accountDao.findAll()).thenReturn(accountHashMap.values());

    List<Account> result = accountServiceImpl.getAccounts();

    for (int i = 0; i < result.size(); i++) {
      assertEquals(accountList.get(i).getId(), result.get(i).getId());
      assertEquals(accountList.get(i).getAmount(), result.get(i).getAmount());
      assertEquals(accountList.get(i).getType(), result.get(i).getType());
      assertEquals(accountList.get(i).getNumber(), result.get(i).getNumber());
      assertEquals(accountList.get(i).getAgreementId(), result.get(i).getAgreementId());
    }
  }

  @Test
  public void testAddAccount(){
    Agreement agreement = new Agreement();
    agreement.setId(1L);

    ArgumentMatcher<Account> accountArgumentMatcher =
            argument ->
                    argument.getAmount().equals(new BigDecimal(90)) &&
                    argument.getType().equals(1) &&
                    argument.getNumber().equals("1234") &&
                    argument.getAgreementId().equals(1L);

    accountServiceImpl.addAccount(agreement, "1234", 1, new BigDecimal(90));

    verify(accountDao).save(argThat(accountArgumentMatcher));
  }
}
