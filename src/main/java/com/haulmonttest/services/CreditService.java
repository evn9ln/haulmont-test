package com.haulmonttest.services;

import com.haulmonttest.domain.Bank;
import com.haulmonttest.domain.Credit;
import com.haulmonttest.repo.CreditRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditService {
    public static List<String> getCreditNames(CreditRepository creditRepository, Bank bank) {
        List<String> creditList = new ArrayList<>();
        for (Credit credit : creditRepository.findAllByBankId(bank)) creditList.add(credit.getType());
        return creditList;
    }
}
