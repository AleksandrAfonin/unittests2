package ru.otus.java.professional.unittests2;

import ru.otus.java.professional.unittests2.dao.AccountDao;
import ru.otus.java.professional.unittests2.dao.AgreementDao;
import ru.otus.java.professional.unittests2.entity.Account;
import ru.otus.java.professional.unittests2.entity.Agreement;
import ru.otus.java.professional.unittests2.service.AccountService;
import ru.otus.java.professional.unittests2.service.AgreementService;
import ru.otus.java.professional.unittests2.service.PaymentProcessor;
import ru.otus.java.professional.unittests2.service.impl.AccountServiceImpl;
import ru.otus.java.professional.unittests2.service.impl.AgreementServiceImpl;
import ru.otus.java.professional.unittests2.service.impl.PaymentProcessorImpl;

import java.math.BigDecimal;

public class Main {

    public static AgreementService agreementService;
    public static AccountService accountService;

    public static PaymentProcessor paymentProcessor;

    public static void main(String[] args) {

        init();
        printAccounts();

        makeTransfer2();

    }

    private static void makeTransfer2() {
        Agreement bankAgreement = agreementService.findByName("Bank").get();
        Agreement clientAgreement = agreementService.findByName("Client1").get();

         paymentProcessor.makeTransferWithComission(bankAgreement, clientAgreement, 0, 0,
                 new BigDecimal(22), new BigDecimal("0.1"));

        System.out.println(accountService.getAccounts());

    }

    private static void makeTransfer() {
        Agreement bankAgreement = agreementService.findByName("Bank").get();
        Agreement clientAgreement = agreementService.findByName("Client2").get();

        Account bankAccount = accountService.getAccounts(bankAgreement).get(0);
        Account clientAccount = accountService.getAccounts(clientAgreement).get(0);

        accountService.makeTransfer(bankAccount.getId(), clientAccount.getId(), BigDecimal.TEN);

        System.out.println(accountService.getAccounts());

    }

    public static void init() {
        AccountDao accountDao = new AccountDao();
        AgreementDao agreementDao = new AgreementDao();

        agreementService = new AgreementServiceImpl(agreementDao);
        accountService = new AccountServiceImpl(accountDao);
        paymentProcessor = new PaymentProcessorImpl(accountService);

        Agreement clientAgreement1 = agreementService.addAgreement("Client1");
        Agreement clientAgreement2 = agreementService.addAgreement("Client2");
        Agreement bankAgreement = agreementService.addAgreement("Bank");

        Account client1Account1 = accountService.addAccount(clientAgreement1,
                clientAgreement1.getName() + "_acc1", 0, new BigDecimal(1000));

        Account client2Account1 = accountService.addAccount(clientAgreement2,
                clientAgreement2.getName() + "_acc1", 0, new BigDecimal(1000));
        Account bankAccount1 = accountService.addAccount(bankAgreement,
                bankAgreement.getName() + "_acc1", 0, new BigDecimal(1000000));
    }

    public static void printAccounts() {
        System.out.println(accountService.getAccounts());
    }
}