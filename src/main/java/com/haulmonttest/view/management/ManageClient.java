package com.haulmonttest.view.management;

import com.haulmonttest.domain.Client;
import com.haulmonttest.domain.UuidMap;
import com.haulmonttest.repo.ClientRepository;
import com.haulmonttest.repo.UuidMapRepository;
import com.haulmonttest.view.entityTables.Clients;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Route("manageClient")
@PageTitle("Manage client")
public class ManageClient extends VerticalLayout implements HasUrlParameter<Integer>, KeyNotifier {
    private UUID id;
    private VerticalLayout clientForm;
    private TextField name;
    private TextField email;
    private EmailValidator emailValidator;
    private TextField passport;
    private Button save;
    private Button cancel;

    private final HorizontalLayout toolbar;

    private ClientRepository clientRepository;
    private UuidMapRepository uuidMapRepository;

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer intByUuid) {
        if(uuidMapRepository.findById(intByUuid).isPresent())
            id = uuidMapRepository.findById(intByUuid).get().getUuid();
        if (id != null) {
            toolbar.add(new H3("Change client"));
        } else {
            toolbar.add(new H3("Add new client"));
        }
        fillForm();
    }

    @Autowired
    public ManageClient(ClientRepository clientRepository, UuidMapRepository uuidMapRepository) {
        this.clientRepository = clientRepository;
        this.uuidMapRepository = uuidMapRepository;

        toolbar = new HorizontalLayout();
        clientForm = new VerticalLayout();
        name = new TextField("Full name");
        name.setRequired(true);
        email = new TextField("Email");
        email.setRequired(true);
        passport = new TextField("Passport");
        passport.setRequired(true);
        save = new Button("Save");
        cancel = new Button("Cancel");

        clientForm.add(name, email, passport, save, cancel);

       add(toolbar, clientForm);
    }

    public void fillForm() {

        if (id != null) {
            Client client = clientRepository.findById(id);
            name.setValue(client.getName());
            email.setValue(client.getEmail());
            passport.setValue(client.getPassport());
        }

        save.addClickListener(e -> {
            save();
        });

        addKeyPressListener(Key.ENTER, e -> {
            save();
        });

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate(Clients.class);
        });
    }

    private void save() {
        Notification errorNotification = new Notification("Invalid input", 1500);
        errorNotification.setPosition(Notification.Position.MIDDLE);

        if(name.isEmpty() || email.isEmpty() || passport.isEmpty()) {
            errorNotification.setText("All fields must be filled!");
            errorNotification.open();
        }
        else if (id == null && (clientRepository.findByEmailAndPassport(email.getValue(), passport.getValue()) != null)) {
            errorNotification.setText("Client already exists!");
            errorNotification.open();
        }
        else {
            Client newClient = new Client();

            newClient.setName(name.getValue());
            newClient.setEmail(email.getValue());
            newClient.setPassport(passport.getValue());
            if (id != null) {
                newClient.setId(id);
                newClient.setBank(clientRepository.findById(id).getBank());
                clientRepository.save(newClient);
            } else {
                clientRepository.save(newClient);
                UuidMap uuidMap = new UuidMap(newClient.getId());
                uuidMapRepository.save(uuidMap);
            }

            Notification notification = new Notification(id == null ? "Client successfully added" : "Client successfully changed", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(Clients.class);
            });
            clientForm.setEnabled(false);
            notification.open();
        }
    }

}
