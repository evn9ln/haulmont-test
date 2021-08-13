package com.haulmonttest.services;

import com.haulmonttest.domain.CreditOffer;
import com.haulmonttest.domain.PaymentTimetable;
import com.haulmonttest.repo.PaymentTimetableRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentTimetableService {

    public static String getTimetable(PaymentTimetableRepository paymentTimetableRepository, CreditOffer creditOffer) {
        String timetable = "";
        List<PaymentTimetable> paymentTimetableList = paymentTimetableRepository.findAllByCreditOfferId(creditOffer.getId());

        if (!paymentTimetableList.isEmpty()) {

            for (PaymentTimetable paymentTimetable : paymentTimetableList.stream().sorted(Comparator.comparing(PaymentTimetable::getDate)).collect(Collectors.toList())) {
                timetable += "Payment date: " + paymentTimetable.getDate() + "\n"
                        + "Payment amount: " + paymentTimetable.getAmount() + "\n"
                        + "Credit body payment: " + paymentTimetable.getRepaymentAmount() + "\n"
                        + "Credit percents payment: " + paymentTimetable.getPercentRepaymentAmount() + "\n\n";
            }
        }
        return timetable;
    }


}
