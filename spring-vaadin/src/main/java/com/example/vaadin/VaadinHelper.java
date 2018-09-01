package com.example.vaadin;

import java.util.List;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class VaadinHelper {

	public static Div withDiv(Component comp) {
    	Div itemWrapper = new Div();
    	itemWrapper.add(comp);
    	return itemWrapper;
    }

	public static Div label(String label) {
		Span span = new Span(label);
		return withDiv(span);
	}

	public static Span span(String label, String className) {
		Span span = new Span(label);
		span.setClassName(className);
		return span;
	}

	public static <T> Grid<T> grid(List<T> elements) {
		Grid<T> grid = new Grid<>();
		grid.setHeight("100%");
		grid.setWidth("100%");
		grid.setItems(elements);
		return grid;
	}
}
