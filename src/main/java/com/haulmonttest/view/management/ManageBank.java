package com.haulmonttest.view.management;

import com.haulmonttest.domain.Bank;
import com.haulmonttest.domain.UuidMap;
import com.haulmonttest.repo.BankRepository;
import com.haulmonttest.repo.UuidMapRepository;
import com.haulmonttest.view.entityTables.Banks;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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

@Route("manageBank")
@PageTitle("Manage Bank")
public class ManageBank extends VerticalLayout implements HasUrlParameter<Integer>, KeyNotifier {
    private UUID id;
    private VerticalLayout bankForm;
    private TextField name;
    private Button save;
    private Button cancel;

    private final HorizontalLayout toolbar;

    private BankRepository bankRepository;
    private UuidMapRepository uuidMapRepository;

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer intByUuid) {
        if(uuidMapRepository.findById(intByUuid).isPresent())
            id = uuidMapRepository.findById(intByUuid).get().getUuid();
        if (id != null) {
            toolbar.add(new H3("Change bank"));
        } else {
            toolbar.add(new H3("Add new bank"));
        }
        fillForm();
    }

    @Autowired
    public ManageBank(BankRepository bankRepository, UuidMapRepository uuidMapRepository) {
        this.bankRepository = bankRepository;
        this.uuidMapRepository = uuidMapRepository;

        toolbar = new HorizontalLayout();
        bankForm = new VerticalLayout();
        name = new TextField("Name");
        name.setRequired(true);
        save = new Button("Save");
        cancel = new Button("Cancel");

        bankForm.add(name, save, cancel);

        add(toolbar, bankForm);
    }

    public void fillForm() {

        if (id != null) {
            Bank bank = bankRepository.findById(id);
            name.setValue(bank.getName());
        }

        save.addClickListener(e -> {
            save();
        });

        addKeyPressListener(Key.ENTER, e -> {
            save();
        });

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(Banks.class);
        });
    }

    private void save() {
        Notification errorNotification = new Notification("Bank with the same name already exists!", 1500);
        errorNotification.setPosition(Notification.Position.MIDDLE);

        if (name.isEmpty()) {
            errorNotification.setText("Field must be filled!");
            errorNotification.open();
        }
        else if (id == null && bankRepository.findByName(name.getValue()) != null) {
            errorNotification.open();
        }
        else {
            Bank newBank = new Bank();

            newBank.setName(name.getValue());
            if (id != null) {
                newBank.setId(id);
                bankRepository.save(newBank);
            } else {
                bankRepository.save(newBank);
                UuidMap uuidMap = new UuidMap(newBank.getId());
                uuidMapRepository.save(uuidMap);
            }

            Notification notification = new Notification(id == null ? "Bank successfully added" : "Bank successfully changed", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(Banks.class);
            });
            bankForm.setEnabled(false);
            notification.open();
        }
    }
}
