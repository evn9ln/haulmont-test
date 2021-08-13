package com.haulmonttest.view.entityTables;

import com.haulmonttest.domain.Client;
import com.haulmonttest.extraClasses.NavigationBar;
import com.haulmonttest.repo.ClientRepository;
import com.haulmonttest.repo.CreditOfferRepository;
import com.haulmonttest.repo.UuidMapRepository;
import com.haulmonttest.services.CreditOfferService;
import com.haulmonttest.view.management.ManageClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Route("clients")
@PageTitle("Clients")
public class Clients extends VerticalLayout {

    private ClientRepository clientRepository;
    private CreditOfferRepository creditOfferRepository;
    private UuidMapRepository uuidMapRepository;

    private Grid<Client> grid;
    private RouterLink manageLink;

    private final HorizontalLayout addClients;

    @Autowired
    public Clients(ClientRepository clientRepository, CreditOfferRepository creditOfferRepository,
                   UuidMapRepository uuidMapRepository) {
        this.clientRepository = clientRepository;
        this.creditOfferRepository = creditOfferRepository;
        this.uuidMapRepository = uuidMapRepository;

        grid = new Grid<>();

        manageLink = new RouterLink("Add new client", ManageClient.class, 0);

        addClients = new HorizontalLayout(manageLink);

        add(NavigationBar.getBar(), manageLink, grid);
    }

    @PostConstruct
    public void fillGrid() {
        List<Client> clientList = clientRepository.findAll();

        if(!clientList.isEmpty()) {
            grid.addColumn(Client::getId).setHeader("<UUID>").setSortable(true);
            grid.addColumn(Client::getName).setHeader("Name").setSortable(true);
            grid.addColumn(Client::getEmail).setHeader("Email").setSortable(true);
            grid.addColumn(Client::getPassport).setHeader("Passport").setSortable(true);
            grid.addColumn(Client::getBankName).setHeader("Bank").setSortable(true);

            grid.addColumn(new NativeButtonRenderer<>("Change", client -> {
                UI.getCurrent().navigate(ManageClient.class, uuidMapRepository.findByUuid(client.getId()).getId());
            }));

            grid.addColumn(new NativeButtonRenderer<>("Delete", client -> {
                Dialog dialog = new Dialog();
                Button confirm = new Button("Delete");
                Button cancel = new Button("Cancel");
                Label label = new Label("Are you sure? It also deletes all credit offers of this client.");

                dialog.add(label, confirm, cancel);

                confirm.addClickListener(e -> {
                    CreditOfferService.deleteAllByClient(uuidMapRepository, creditOfferRepository, client);
                    uuidMapRepository.deleteByUuid(client.getId());

                    clientRepository.deleteById(client.getId());

                    dialog.close();

                    Notification notification = new Notification("Client successfully deleted.", 1000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    grid.setItems(clientRepository.findAll());
                });
                cancel.addClickListener(e -> {
                    dialog.close();
                });

                dialog.open();
            }));
            grid.setItems(clientList);
        }
    }
}