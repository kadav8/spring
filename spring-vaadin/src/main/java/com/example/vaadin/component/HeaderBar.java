package com.example.vaadin.component;

import java.util.Collection;

import com.example.vaadin.VaadinHelper;
import com.example.vaadin.dto.PageInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;

public class HeaderBar extends Div {

	HeaderBar(Collection<PageInfo> pages) {
		setClassName("nav-bar");
		addClassName("unselectable");
		for (PageInfo page : pages) {
			add(new HeaderButton(page.getTitle(), page.getLink()));
		}
	}

	class HeaderButton extends Div {
		HeaderButton(String title, String href) {
			setClassName("header-button");
			addClassName("bold-text");
			Span link = VaadinHelper.span(title, "header-link");
			link.addClickListener(e -> UI.getCurrent().navigate(href));
			add(link);
		}
	}
}