package com.example.vaadin.view;

import com.example.vaadin.component.DocumentTable;
import com.example.vaadin.component.MainLayout;
import com.example.vaadin.service.DocumentRepository;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "documents", layout = MainLayout.class)
public class DocumentsView extends VerticalLayout {

	public DocumentsView(DocumentRepository repo) {
		setHeight("100%");
		setWidth("100%");
		add(new DocumentTable(repo.getDocuments()));
	}
}
