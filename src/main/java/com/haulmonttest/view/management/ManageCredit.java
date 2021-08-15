package com.haulmonttest.view.management;

import com.haulmonttest.domain.Credit;
import com.haulmonttest.domain.UuidMap;
import com.haulmonttest.repo.BankRepository;
import com.haulmonttest.repo.CreditRepository;
import com.haulmonttest.repo.UuidMapRepository;
import com.haulmonttest.services.BankService;
import com.haulmonttest.view.entityTables.Credits;
import com.haulmonttest.view.subViews.EmptyMapping;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route("manageCredit")
@PageTitle("Manage Credit")
public class ManageCredit extends VerticalLayout implements HasUrlParameter<Integer> {
    private UUID id;
    private VerticalLayout creditForm;
    private ComboBox<String> bank;
    private TextField type;
    private TextField limit;
    private TextField percent;
    private Button save;
    private Button cancel;

    private final HorizontalLayout toolbar;

    private CreditRepository creditRepository;
    private BankRepository bankRepository;
    private UuidMapRepository uuidMapRepository;

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer intByUuid) {
        if(uuidMapRepository.findById(intByUuid).isPresent())
            id = uuidMapRepository.findById(intByUuid).get().getUuid();
        if (id != null) {
            toolbar.add(new H3("Change credit"));
        } else {
            toolbar.add(new H3("Add new credit"));
        }
        fillForm(intByUuid);
    }

    @Autowired
    public ManageCredit(CreditRepository creditRepository,
                        BankRepository bankRepository,UuidMapRepository uuidMapRepository) {
        this.creditRepository = creditRepository;
        this.bankRepository = bankRepository;
        this.uuidMapRepository = uuidMapRepository;

        toolbar = new HorizontalLayout();
        creditForm = new VerticalLayout();

        bank = new ComboBox<>();
        bank.setPlaceholder("Bank");
        bank.setAllowCustomValue(false);
        bank.setItems(BankService.getBankList(bankRepository));

        type = new TextField("Type");
        limit = new TextField("Limit");
        percent = new TextField("Percent");
        save = new Button("Save");
        cancel = new Button("Cancel");

        creditForm.add(bank,type, limit, percent, save, cancel);

        add(toolbar, creditForm);
    }

    public void fillForm(int intByUUID) {
        if(id == null && intByUUID != 0) {
            bank.setVisible(false);
            type.setVisible(false);
            limit.setVisible(false);
            percent.setVisible(false);
            save.setVisible(false);

            toolbar.setVisible(false);
        }
        else {
            if (id != null) {
                Credit credit = creditRepository.findById(id);
                bank.setValue(credit.getBankName());
                type.setValue(credit.getType());
                limit.setValue(Integer.toString(credit.getLimit()));
                percent.setValue(Integer.toString(credit.getPercent()));

            }

            save.addClickListener(e -> {
                save();
            });

        }
        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(Credits.class);
        });
    }

    private void save() {
        Notification errorNotification = new Notification("Invalid input!", 1500);
        errorNotification.setPosition(Notification.Position.MIDDLE);
        try {
            if (bank.isEmpty() || type.isEmpty() || limit.isEmpty() || percent.isEmpty()) {
                errorNotification.setText("All fields must be filled!");
                errorNotification.open();
            }
            else if (Integer.parseInt(limit.getValue()) <= 0 || Integer.parseInt(percent.getValue()) <= 0) {
                errorNotification.setText("Limit and percent must be greater than 0!");
                errorNotification.open();
            }
            else if(creditRepository.findByTypeAndAndBankId(type.getValue(),
                    bankRepository.findByName(bank.getValue())) != null) {
                errorNotification.setText("Credit with the same name exists!");
                errorNotification.open();
            }
            else {
                Credit newCredit = new Credit();

                newCredit.setBankId(bankRepository.findByName(bank.getValue()));
                newCredit.setType(type.getValue());
                newCredit.setLimit(Integer.parseInt(limit.getValue()));
                newCredit.setPercent(Integer.parseInt(percent.getValue()));
                if (id != null) {
                    newCredit.setId(id);
                    creditRepository.save(newCredit);
                } else {
                    creditRepository.save(newCredit);
                    UuidMap uuidMap = new UuidMap(newCredit.getId());
                    uuidMapRepository.save(uuidMap);
                }

                Notification notification = new Notification(id == null ? "Credit successfully added" : "Credit successfully changed", 1000);
                notification.setPosition(Notification.Position.MIDDLE);
                notification.addDetachListener(detachEvent -> {
                    UI.getCurrent().navigate(Credits.class);
                });
                creditForm.setEnabled(false);
                notification.open();
            }
        } catch (NumberFormatException exception) {
            errorNotification.setText("Limit and percent must be integer!");
            errorNotification.open();
        }
    }
}
