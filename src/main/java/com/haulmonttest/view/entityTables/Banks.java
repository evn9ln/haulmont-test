package com.haulmonttest.view.entityTables;

import com.haulmonttest.domain.Bank;
import com.haulmonttest.extraClasses.NavigationBar;
import com.haulmonttest.repo.*;
import com.haulmonttest.services.ClientService;
import com.haulmonttest.services.CreditOfferService;
import com.haulmonttest.view.management.ManageBank;
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

@Route("banks")
@PageTitle("Banks")
public class Banks extends VerticalLayout {
    private BankRepository bankRepository;
    private ClientRepository clientRepository;
    private CreditRepository creditRepository;
    private CreditOfferRepository creditOfferRepository;
    private UuidMapRepository uuidMapRepository;

    private Grid<Bank> grid;
    private RouterLink manageLink;

    private final HorizontalLayout addBank;

    @Autowired
    public Banks(BankRepository bankRepository, CreditRepository creditRepository,
                 CreditOfferRepository creditOfferRepository, ClientRepository clientRepository,
                 UuidMapRepository uuidMapRepository) {
        this.bankRepository = bankRepository;
        this.clientRepository = clientRepository;
        this.creditRepository = creditRepository;
        this.creditOfferRepository = creditOfferRepository;
        this.uuidMapRepository = uuidMapRepository;

        grid = new Grid<>();
        manageLink = new RouterLink("Add new bank", ManageBank.class, 0);

        addBank = new HorizontalLayout(manageLink);

        add(NavigationBar.getBar(), manageLink, grid);
    }

    @PostConstruct
    public void fillGrid() {
        List<Bank> bankList = bankRepository.findAll();

        if(!bankList.isEmpty()) {
            grid.addColumn(Bank::getId).setHeader("<UUID>").setSortable(true);
            grid.addColumn(Bank::getName).setHeader("Name").setSortable(true);

            grid.addColumn(new NativeButtonRenderer<>("Change", bank -> {
                UI.getCurrent().navigate(ManageBank.class, uuidMapRepository.findByUuid(bank.getId()).getId());
            }));

            grid.addColumn(new NativeButtonRenderer<>("Delete", bank -> {
                Dialog dialog = new Dialog();
                Button confirm = new Button("Delete");
                Button cancel = new Button("Cancel");
                Label label = new Label("Are you sure? It also deletes all credit and credit offers of this bank.");

                dialog.add(label, confirm, cancel);

                confirm.addClickListener(e -> {
                    ClientService.deleteBanks(clientRepository, bank);
                    creditRepository.deleteAllByBankId(bank);
                    CreditOfferService.deleteAllByBank(uuidMapRepository, creditOfferRepository, bank);
                    uuidMapRepository.deleteByUuid(bank.getId());
                    bankRepository.delete(bank);
                    dialog.close();

                    Notification notification = new Notification("Bank successfully deleted.", 1000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    grid.setItems(bankRepository.findAll());
                });
                cancel.addClickListener(e -> {
                    dialog.close();
                });

                dialog.open();
            }));
            grid.setItems(bankList);
        }
    }
}
