package ru.otus.java.professional.unittests2.service.impl;

import ru.otus.java.professional.unittests2.dao.AgreementDao;
import ru.otus.java.professional.unittests2.entity.Agreement;
import ru.otus.java.professional.unittests2.service.AgreementService;

import java.util.Optional;

public class AgreementServiceImpl implements AgreementService {

    private AgreementDao agreementDao;

    public AgreementServiceImpl(AgreementDao agreementDao) {
        this.agreementDao = agreementDao;
    }

    @Override
    public Agreement addAgreement(String name) {
        Agreement agreement = new Agreement();
        agreement.setName(name);

        return agreementDao.save(agreement);
    }

    public Optional<Agreement> findByName(String name) {
        return agreementDao.findByName(name);
    }


}
