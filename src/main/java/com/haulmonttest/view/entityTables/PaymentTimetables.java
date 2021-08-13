package com.haulmonttest.view.entityTables;

import com.haulmonttest.domain.PaymentTimetable;
import com.haulmonttest.extraClasses.NavigationBar;
import com.haulmonttest.repo.PaymentTimetableRepository;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Route("paymentTable")
@PageTitle("Payments table")
public class PaymentTimetables extends VerticalLayout {

    private PaymentTimetableRepository paymentTimetableRepository;

    private Grid<PaymentTimetable> grid;

    @Autowired
    public PaymentTimetables(PaymentTimetableRepository paymentTimetableRepository) {
        this.paymentTimetableRepository = paymentTimetableRepository;

        grid = new Grid<>();

        add(NavigationBar.getBar(), grid);
    }

    @PostConstruct
    public void fillGrid() {
        List<PaymentTimetable> paymentTimetableList = paymentTimetableRepository.findAll().stream()
                .sorted(Comparator.comparing(PaymentTimetable::getCreditOfferId))
                .collect(Collectors.toList());

        if (!paymentTimetableList.isEmpty()) {
            grid.addColumn(PaymentTimetable::getId).setHeader("<UUID>").setSortable(true);
            grid.addColumn(PaymentTimetable::getCreditOfferId).setHeader("<Credit offer UUID>").setSortable(true);
            grid.addColumn(PaymentTimetable::getAmount).setHeader("Amount").setSortable(true);
            grid.addColumn(PaymentTimetable::getDate).setHeader("Date").setSortable(true);

            grid.setItems(paymentTimetableList);
        }

    }
}
