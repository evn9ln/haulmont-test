package com.haulmonttest.services;

import com.haulmonttest.domain.Bank;
import com.haulmonttest.domain.Client;
import com.haulmonttest.domain.Credit;
import com.haulmonttest.domain.CreditOffer;
import com.haulmonttest.repo.CreditOfferRepository;
import com.haulmonttest.repo.UuidMapRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditOfferService {

    public static void deleteAllByBank(UuidMapRepository uuidMapRepository, CreditOfferRepository creditOfferRepository, Bank bank) {
        List<CreditOffer> creditOfferList = creditOfferRepository.findAllByBankId(bank);
        if(!creditOfferList.isEmpty()) {
            for (CreditOffer creditOffer : creditOfferList) {
                uuidMapRepository.deleteByUuid(creditOffer.getId());
                creditOffer.removeCredit();
                creditOfferRepository.delete(creditOffer);
            }
        }
    }

    public static void deleteAllByClient(UuidMapRepository uuidMapRepository, CreditOfferRepository creditOfferRepository, Client client) {

        List<CreditOffer> creditOfferList = creditOfferRepository.findAllByClientId(client);
        if(!creditOfferList.isEmpty()) {
            for (CreditOffer creditOffer : creditOfferList) {
                uuidMapRepository.deleteByUuid(creditOffer.getId());
                creditOffer.removeCredit();
                creditOfferRepository.delete(creditOffer);
            }
        }
    }

    public static void deleteAllByCredit(UuidMapRepository uuidMapRepository, CreditOfferRepository creditOfferRepository, Credit credit) {

        List<CreditOffer> creditOfferList = creditOfferRepository.findAllByCreditId(credit);
        if(!creditOfferList.isEmpty()) {
            for (CreditOffer creditOffer : creditOfferList) {
                uuidMapRepository.deleteByUuid(creditOffer.getId());
                creditOffer.removeCredit();
                creditOfferRepository.delete(creditOffer);
            }
        }
    }
}
