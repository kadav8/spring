package com.example.vaadin;

import com.vaadin.flow.component.Component;
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
}
