package com.haulmonttest.view.subViews;

import com.haulmonttest.domain.CreditOffer;
import com.haulmonttest.domain.PaymentTimetable;
import com.haulmonttest.repo.CreditOfferRepository;
import com.haulmonttest.repo.PaymentTimetableRepository;
import com.haulmonttest.repo.UuidMapRepository;
import com.haulmonttest.view.entityTables.CreditOffers;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

@Route("payment")
@PageTitle("Payment")
public class Payment extends VerticalLayout implements HasUrlParameter<Integer>, KeyNotifier {

    private UUID id;
    private TextField name;
    private TextField creditType;
    private TextField totalSumToPay;
    private TextField balance;
    private TextField paymentAmount;
    private TextField minPay;

    private Button pay;
    private Button cancel;

    private CreditOfferRepository creditOfferRepository;
    private PaymentTimetableRepository paymentTimetableRepository;
    private UuidMapRepository uuidMapRepository;

    private HorizontalLayout title;

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer intByUuid) {
        if(uuidMapRepository.findById(intByUuid).isPresent())
            id = uuidMapRepository.findById(intByUuid).get().getUuid();
        fillPage();
    }

    @Autowired
    public Payment(CreditOfferRepository creditOfferRepository,
                   PaymentTimetableRepository paymentTimetableRepository,
                   UuidMapRepository uuidMapRepository) {
        this.creditOfferRepository = creditOfferRepository;
        this.uuidMapRepository = uuidMapRepository;
        this.paymentTimetableRepository = paymentTimetableRepository;

        title = new HorizontalLayout();
        title.add(new H3("Credit payment"));

        name = new TextField("Client name");
        creditType = new TextField("Credit type");
        totalSumToPay = new TextField("Total sum to pay");
        balance = new TextField("Current balance");
        minPay = new TextField("Minimal pay");
        paymentAmount = new TextField("Payment amount");

        pay = new Button("Pay");
        cancel = new Button("Cancel");

        add(title, name, creditType, totalSumToPay, balance, minPay, paymentAmount, pay, cancel);
    }

    public void fillPage() {
        CreditOffer creditOffer = creditOfferRepository.findById(id);

        name.setValue(creditOffer.getClientName());
        name.setReadOnly(true);
        creditType.setValue(creditOffer.getCreditType());
        creditType.setReadOnly(true);
        int total = (int)(creditOffer.getAmount()
                * (1 +  (double)(creditOffer.getCreditId().getPercent()) / 100));
        totalSumToPay.setValue(Integer.toString(total));
        totalSumToPay.setReadOnly(true);
        balance.setValue(Integer.toString(creditOffer.getBalance()));
        balance.setReadOnly(true);
        int minimalPay = total/creditOffer.getMonths();
        if (minimalPay > total - creditOffer.getBalance()) {
            minPay.setValue(Integer.toString(total - creditOffer.getBalance()));
        }
        else {
            minPay.setValue(Integer.toString(minimalPay));
        }
        minPay.setReadOnly(true);

        Dialog dialog = new Dialog();
        Button dialogConfirm = new Button("Pay");
        Button dialogCancel = new Button("Cancel");
        Label label = new Label("Confirm payment");

        dialog.add(label, dialogConfirm, dialogCancel);

        pay.addClickListener(e -> {
            pay(creditOffer, dialog, total);
        });

        addKeyPressListener(Key.ENTER, e -> {
            pay(creditOffer, dialog, total);
        });


        dialogConfirm.addClickListener(e -> {

            creditOffer.setBalance(creditOffer.getBalance() + Integer.parseInt(paymentAmount.getValue()));
            creditOfferRepository.save(creditOffer);

            PaymentTimetable paymentTimetable = new PaymentTimetable();
            paymentTimetable.setCreditOfferId(creditOffer.getId());
            paymentTimetable.setDate(new Date());
            paymentTimetable.setAmount(Integer.parseInt(paymentAmount.getValue()));
            int bodyPayment = (int)(Double.parseDouble(paymentAmount.getValue()) * (1.0 - (double)creditOffer.getCreditId().getPercent()/100));
            paymentTimetable.setRepaymentAmount(bodyPayment);
            paymentTimetable.setPercentRepaymentAmount(Integer.parseInt(paymentAmount.getValue()) - bodyPayment);
            paymentTimetableRepository.save(paymentTimetable);

            dialog.close();

            Notification notification = new Notification("Payment successfully completed", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(CreditOffers.class);
            });
            notification.open();
        });

        dialogCancel.addClickListener(e -> {
            dialog.close();
        });

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(CreditOffers.class);
        });

    }

    private void pay(CreditOffer creditOffer, Dialog dialog, int total) {
        Notification errorNotification = new Notification("Payment amount is greater than need!", 1500);
        errorNotification.setPosition(Notification.Position.MIDDLE);
        try {
            if (paymentAmount.isEmpty()) {
                errorNotification.setText("Field must be filled!");
                errorNotification.open();
            }
            else if (creditOffer.getBalance() >= total) {
                errorNotification.setText("Credit is already closed.");
                errorNotification.open();
            }
            else if (creditOffer.getBalance() + Integer.parseInt(paymentAmount.getValue()) > total) {
                errorNotification.open();
            }
            else if (Integer.parseInt(paymentAmount.getValue()) < Integer.parseInt(minPay.getValue()) || Integer.parseInt(paymentAmount.getValue()) <= 0) {
                errorNotification.setText("Payment amount is too low!");
                errorNotification.open();
            }
            else {
                dialog.open();
            }
        } catch (NumberFormatException exception) {
            errorNotification.open();
        }
    }
}
