package com.haulmonttest.services;

import com.haulmonttest.domain.Bank;
import com.haulmonttest.repo.BankRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {
    public static List<String> getBankList(BankRepository bankRepository) {
        List<String> bankList = new ArrayList<String>();
        for (Bank bank : bankRepository.findAll())
            bankList.add(bank.getName());
        return bankList;
    }
}
