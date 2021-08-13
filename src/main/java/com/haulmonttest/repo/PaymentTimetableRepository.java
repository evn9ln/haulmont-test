package com.haulmonttest.repo;

import com.haulmonttest.domain.PaymentTimetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentTimetableRepository extends JpaRepository<PaymentTimetable, Integer> {

    List<PaymentTimetable> findAllByCreditOfferId(UUID creditOffer);
}
