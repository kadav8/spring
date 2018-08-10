package com.example.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;

@SpringBootApplication
public class SpringWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebApplication.class, args);
	}
}

@RestController
class Endpoints {

	Map<String, DocumentDto> documentsMap = new HashMap<>();

	@PostMapping("document/save")
	public void saveDocument(DocumentDto document) {
		documentsMap.put(document.getDocumentId(), document);
	}

	@GetMapping("document/{documentId}")
	public DocumentDto getDocument(@PathVariable String documentId) {
		return documentsMap.get(documentId);
	}

	@GetMapping("documents")
	public Collection<DocumentDto> getDocuments() {
		return documentsMap.values();
	}

	@GetMapping("properties/{documentId}")
	public Collection<PropertyDto> getProperties(@PathVariable String documentId) {
		return documentsMap.get(documentId).getProperties();
	}
}

@Data
class DocumentDto {
	private String documentId;
	private String documentName;
	private List<PropertyDto> properties;
}

@Data
class PropertyDto {
	private String name;
	private String value;
}