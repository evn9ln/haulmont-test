package com.haulmonttest.view.subViews;

import com.haulmonttest.extraClasses.NavigationBar;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Banks Service")
public class EmptyMapping extends VerticalLayout {

    public EmptyMapping() {
        add(NavigationBar.getBar());
    }
}
