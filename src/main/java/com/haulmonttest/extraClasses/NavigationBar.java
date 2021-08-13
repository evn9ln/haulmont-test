package com.haulmonttest.extraClasses;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class NavigationBar {
    public static HorizontalLayout getBar() {
        HorizontalLayout toolbar = new HorizontalLayout();

        Button clientsLink = new Button("Clients");
        Button banksLink = new Button("Banks");
        Button creditsLink = new Button("Credits");
        Button creditOffersLink = new Button("Credit Offers");
        Button payments = new Button("Payments");

        clientsLink.addClickListener(e -> {
            UI.getCurrent().navigate("clients");
        });

        banksLink.addClickListener(e -> {
            UI.getCurrent().navigate("banks");
        });

        creditsLink.addClickListener(e -> {
            UI.getCurrent().navigate("credits");
        });

        creditOffersLink.addClickListener(e -> {
            UI.getCurrent().navigate("creditOffers");
        });

        payments.addClickListener(e -> {
           UI.getCurrent().navigate("paymentTable");
        });

        toolbar = new HorizontalLayout(clientsLink, banksLink, creditsLink, creditOffersLink, payments);
        return toolbar;
    }
}
