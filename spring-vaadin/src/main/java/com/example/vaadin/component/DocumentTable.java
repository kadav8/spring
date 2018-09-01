package com.example.vaadin.component;

import java.util.List;

import com.example.vaadin.VaadinHelper;
import com.example.vaadin.dto.Document;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.Div;

public class DocumentTable extends Div {

	public DocumentTable(List<Document> elements) {
		setClassName("document-table");
		setHeight("100%");
		setWidth("100%");
		add(grid(elements));
	}

	private Grid<Document> grid(List<Document> elements) {
		Grid<Document> grid = VaadinHelper.grid(elements);
		grid.addColumn(Document::getId).setHeader("Id");
		grid.addColumn(Document::getTitle).setHeader("Title");
		grid.addColumn(Document::getCreationDate).setHeader("CreationDate");
		grid.setSelectionMode(SelectionMode.MULTI);
		return grid;
	}
}
