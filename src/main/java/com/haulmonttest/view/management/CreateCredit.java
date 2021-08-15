package com.haulmonttest.view.management;

import com.haulmonttest.domain.Client;
import com.haulmonttest.domain.CreditOffer;
import com.haulmonttest.domain.UuidMap;
import com.haulmonttest.repo.*;
import com.haulmonttest.services.BankService;
import com.haulmonttest.services.ClientService;
import com.haulmonttest.services.CreditService;
import com.haulmonttest.view.entityTables.CreditOffers;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route("createCreditOffer")
@PageTitle("Create credit offer")
public class CreateCredit extends VerticalLayout {
    private VerticalLayout creditForm;
    private ComboBox<String> nameSelect;
    private ComboBox<String> bankSelect;
    private ComboBox<String> creditSelect;
    private TextField amount;
    private TextField months;
    private Button save;
    private Button cancel;

    private final HorizontalLayout toolbar;

    private ClientRepository clientRepository;
    private BankRepository bankRepository;
    private CreditRepository creditRepository;
    private CreditOfferRepository creditOfferRepository;
    private UuidMapRepository uuidMapRepository;


    @Autowired
    public CreateCredit(ClientRepository clientRepository, UuidMapRepository uuidMapRepository,
                        BankRepository bankRepository, CreditRepository creditRepository,
                        CreditOfferRepository creditOfferRepository) {
        this.clientRepository = clientRepository;
        this.bankRepository = bankRepository;
        this.creditRepository = creditRepository;
        this.creditOfferRepository = creditOfferRepository;
        this.uuidMapRepository = uuidMapRepository;

        toolbar = new HorizontalLayout();
        creditForm = new VerticalLayout();

        nameSelect = new ComboBox<>();
        bankSelect = new ComboBox<>();
        creditSelect = new ComboBox<>();
        amount = new TextField("Amount");
        amount.setRequired(true);
        months = new TextField("Months");
        months.setRequired(true);

        save = new Button("Save");
        cancel = new Button("Cancel");
        toolbar.add(new H3("Create new credit offer"));

        creditForm.add(nameSelect, bankSelect, creditSelect,
                amount, months, save, cancel);

        add(toolbar, creditForm);

        fillForm();
    }

    public void fillForm() {

        nameSelect.setPlaceholder("Client");
        nameSelect.setItems(ClientService.getClientNames(clientRepository));
        nameSelect.setAllowCustomValue(false);

        creditSelect.setPlaceholder("Credit");
        creditSelect.setAllowCustomValue(false);
        creditSelect.setVisible(false);
        creditSelect.setItems();

        bankSelect.setPlaceholder("Bank");
        nameSelect.setAllowCustomValue(false);
        bankSelect.setItems(BankService.getBankList(bankRepository));

        bankSelect.addValueChangeListener(event -> {
            if (event.getValue() == null) {
                creditSelect.setItems();
                creditSelect.setVisible(false);
            }
            else {
                creditSelect.setItems(CreditService.getCreditNames(creditRepository,
                        bankRepository.findByName(event.getValue())));
                creditSelect.setVisible(true);
            }
        });


        save.addClickListener(e -> {
            save();
        });


        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(CreditOffers.class);
        });
    }

    private void save() {
        Notification errorNotification = new Notification("Invalid input", 1500);
        errorNotification.setPosition(Notification.Position.MIDDLE);

        try {
            if(nameSelect.isEmpty() || bankSelect.isEmpty() || creditSelect.isEmpty()
            || amount.isEmpty() || months.isEmpty()) {
                errorNotification.setText("All fields must be filled!");
                errorNotification.open();
            }
            else if (Integer.parseInt(amount.getValue()) <= 0 || Integer.parseInt(months.getValue()) <=0) {
                errorNotification.setText("Amount and months must be greater than 0!");
                errorNotification.open();
            }
            else if (creditOfferRepository.findByClientIdAndCreditId(clientRepository.findByName(nameSelect.getValue()),
                    creditRepository.findByType(creditSelect.getValue())) != null) {
                errorNotification.setText("Chosen credit already exists for this client!");
                errorNotification.open();
            }
            else if (Integer.parseInt(amount.getValue()) > creditRepository.findByType(creditSelect
                    .getValue()).getLimit()) {
                errorNotification.setText("Amount is greater than credit limit! \n\nLimit for chosen credit is: "
                + creditRepository.findByType(creditSelect.getValue()).getLimit());
                errorNotification.open();
            }
            else {

                CreditOffer creditOffer = new CreditOffer();
                creditOffer.setClientId(clientRepository.findByName(nameSelect.getValue()));
                creditOffer.setCreditId(creditRepository.findByType(creditSelect.getValue()));
                creditOffer.setBankId(bankRepository.findByName(bankSelect.getValue()));
                creditOffer.setAmount(Integer.parseInt(amount.getValue()));
                creditOffer.setMonths(Integer.parseInt(months.getValue()));
                creditOffer.setBalance(0);
                creditOfferRepository.save(creditOffer);

                UuidMap uuidMap = new UuidMap(creditOffer.getId());
                uuidMapRepository.save(uuidMap);

                Client client = clientRepository.findByName(nameSelect.getValue());
                client.setBank(bankRepository.findByName(bankSelect.getValue()));
                clientRepository.save(client);

                Notification notification = new Notification("Credit offer successfully added",1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.addDetachListener(detachEvent -> {
                    UI.getCurrent().navigate(CreditOffers.class);
                });
                notification.open();
            }

        }
        catch (NumberFormatException exception) {
            errorNotification.setText("Amount and months must be integer!");
            errorNotification.open();
        }

    }

}
