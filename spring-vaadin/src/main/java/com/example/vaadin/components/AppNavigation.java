package com.example.vaadin.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.vaadin.dto.PageInfo;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;

public class AppNavigation extends Div implements AfterNavigationObserver {

	private Tabs tabs = new Tabs();
	private List<String> hrefs = new ArrayList<>();
	private String defaultHref;
	private String currentHref;

	public void init(String defaultHref, Collection<PageInfo> pages) {
		setClassName("nav-bar");
		this.defaultHref = defaultHref;
		for (PageInfo page : pages) {
			Tab tab = new Tab(page.getTitle());
			tab.getElement().setAttribute("theme", "icon-on-top");
			hrefs.add(page.getLink());
			tabs.add(tab);
		}
		tabs.addSelectedChangeListener(e -> navigate());
		add(tabs);
	}

	private void navigate() {
		int idx = tabs.getSelectedIndex();
		if (idx >= 0 && idx < hrefs.size()) {
			String href = hrefs.get(idx);
			if (!href.equals(currentHref)) {
				UI.getCurrent().navigate(href);
			}
		}
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		String href = event.getLocation().getFirstSegment().isEmpty() ? defaultHref
				: event.getLocation().getFirstSegment();
		currentHref = href;
		tabs.setSelectedIndex(hrefs.indexOf(href));
	}
}
