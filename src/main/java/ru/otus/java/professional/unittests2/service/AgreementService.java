package ru.otus.java.professional.unittests2.service;

import ru.otus.java.professional.unittests2.entity.Agreement;

import java.util.Optional;

public interface AgreementService {
    Agreement addAgreement(String name);

    Optional<Agreement> findByName(String name);
}
