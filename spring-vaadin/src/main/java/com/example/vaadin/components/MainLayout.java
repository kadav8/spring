package com.example.vaadin.components;

import java.util.ArrayList;
import java.util.List;

import com.example.vaadin.dto.PageInfo;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Viewport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;

@HtmlImport("styles/shared-styles.html")
@Viewport("width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes")
@PageTitle("Vaadin Demo App")
public class MainLayout extends VerticalLayout implements RouterLayout {

	public MainLayout() {
    	setClassName("main-layout");
    	setMargin(false);
    	setPadding(false);
    	initAppNavigation();
    }

	private void initAppNavigation() {
		AppNavigation appNavigation = new AppNavigation();
    	List<PageInfo> pages = new ArrayList<>();
    	pages.add(new PageInfo("", "Dashboard"));
    	pages.add(new PageInfo("issues", "Issues"));
    	pages.add(new PageInfo("projects", "Projects"));
    	appNavigation.init("", pages);
    	add(appNavigation);
	}
}
