package com.haulmonttest.repo;

import com.haulmonttest.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankRepository extends JpaRepository<Bank, Integer> {
    Bank findByName(String name);

    Bank findById(UUID uuid);
}
