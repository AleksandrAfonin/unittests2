package ru.otus.java.professional.unittests2.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import ru.otus.java.professional.unittests2.dao.AgreementDao;
import ru.otus.java.professional.unittests2.entity.Account;
import ru.otus.java.professional.unittests2.entity.Agreement;
import ru.otus.java.professional.unittests2.service.impl.AgreementServiceImpl;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;

public class AgreementServiceImplTest {

  private AgreementDao dao = Mockito.mock(AgreementDao.class);

  AgreementServiceImpl agreementServiceImpl;

  @BeforeEach
  public void init() {
    agreementServiceImpl = new AgreementServiceImpl(dao);
  }

  @Test
  public void testFindByName() {
    String name = "test";
    Agreement agreement = new Agreement();
    agreement.setId(10L);
    agreement.setName(name);

    Mockito.when(dao.findByName(name)).thenReturn(
            Optional.of(agreement));

    Optional<Agreement> result = agreementServiceImpl.findByName(name);

    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(10, agreement.getId());
  }

  @Test
  public void testFindByNameWithCaptor() {
    String name = "test";
    Agreement agreement = new Agreement();
    agreement.setId(10L);
    agreement.setName(name);

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

    Mockito.when(dao.findByName(captor.capture())).thenReturn(
            Optional.of(agreement));

    Optional<Agreement> result = agreementServiceImpl.findByName(name);

    Assertions.assertEquals("test", captor.getValue());
    Assertions.assertTrue(result.isPresent());
    Assertions.assertEquals(10, agreement.getId());
  }

  @Test
  public void testAddAgreement() {
    ArgumentMatcher<Agreement> accountArgumentMatcher =
            argument ->
                    argument.getName().equals("test");

    agreementServiceImpl.addAgreement("test");

    verify(dao).save(argThat(accountArgumentMatcher));
  }
}
