package com.example.vaadin.view;

import com.example.vaadin.component.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class DashboardView extends VerticalLayout {

	public DashboardView() {

	}
}
