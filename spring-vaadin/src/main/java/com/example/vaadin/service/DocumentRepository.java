package com.example.vaadin.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.vaadin.dto.Document;

@Component
public class DocumentRepository {

	public List<Document> getDocuments() {
		List<Document> docs = new ArrayList<>();
		for(int i = 1; i <= 100; i++) {
			Document d = new Document();
			d.setId(i);
			d.setTitle("Document_" + d.getId());
			d.setCreationDate(new Date());
			docs.add(d);
		}
		return docs;
	}

}
