package com.haulmonttest.view.entityTables;

import com.haulmonttest.domain.CreditOffer;
import com.haulmonttest.extraClasses.NavigationBar;
import com.haulmonttest.repo.CreditOfferRepository;
import com.haulmonttest.repo.UuidMapRepository;
import com.haulmonttest.view.management.CreateCredit;
import com.haulmonttest.view.subViews.CreditInfo;
import com.haulmonttest.view.subViews.Payment;
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

@Route("creditOffers")
@PageTitle("Credit Offers")
public class CreditOffers extends VerticalLayout {

    private CreditOfferRepository creditOfferRepository;
    private UuidMapRepository uuidMapRepository;

    private Grid<CreditOffer> grid;
    private RouterLink createLink;
    private RouterLink infoLink;

    private final HorizontalLayout createCredit;

    @Autowired
    public CreditOffers(CreditOfferRepository creditOfferRepository,
                        UuidMapRepository uuidMapRepository) {
        this.creditOfferRepository = creditOfferRepository;
        this.uuidMapRepository = uuidMapRepository;

        grid = new Grid<>();
        createLink = new RouterLink("Add new credit offer", CreateCredit.class);

        createCredit = new HorizontalLayout(createLink);

        add(NavigationBar.getBar(), createCredit, grid);
    }

    @PostConstruct
    public void fillGrid() {

        List<CreditOffer> creditOfferList = creditOfferRepository.findAll();
        if(!creditOfferList.isEmpty()) {
            grid.addColumn(CreditOffer::getId).setHeader("<UUID>").setSortable(true);
            grid.addColumn(CreditOffer::getClient).setHeader("<Client UUID>").setSortable(true);
            grid.addColumn(CreditOffer::getClientName).setHeader("Name").setSortable(true);
            grid.addColumn(CreditOffer::getBankName).setHeader("Bank").setSortable(true);
            grid.addColumn(CreditOffer::getCreditType).setHeader("Credit").setSortable(true);

            grid.addColumn(new NativeButtonRenderer<>("info", creditOffer -> {
                UI.getCurrent().navigate(CreditInfo.class, uuidMapRepository.findByUuid(creditOffer.getId()).getId());
            }));

            grid.addColumn(new NativeButtonRenderer<>("Delete", creditOffer -> {
                Dialog dialog = new Dialog();
                Button confirm = new Button("Delete");
                Button cancel = new Button("Cancel");
                Label label = new Label("Are you sure?");

                dialog.add(label, confirm, cancel);

                confirm.addClickListener(e -> {
                    uuidMapRepository.deleteByUuid(creditOffer.getId());

                    creditOffer.removeCredit();
                    creditOfferRepository.delete(creditOffer);

                    dialog.close();

                    Notification notification = new Notification("Credit offer successfully deleted.", 1000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    grid.setItems(creditOfferRepository.findAll());
                });
                cancel.addClickListener(e -> {
                    dialog.close();
                });

                dialog.open();
            }));

            grid.addColumn(new NativeButtonRenderer<>("Pay", creditOffer -> {
                UI.getCurrent().navigate(Payment.class, uuidMapRepository.findByUuid(creditOffer.getId()).getId());
            }));

            grid.setItems(creditOfferList);
        }
    }
}
