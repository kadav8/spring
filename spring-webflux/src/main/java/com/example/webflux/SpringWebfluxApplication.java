package com.example.webflux;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringWebfluxApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApplication.class, args);
	}
}

@RestController
class ReactiveDocumentEndpoints {

	@Autowired
	DocumentRepository repo;

	@PostMapping("document/save")
	public Mono<DocumentDto> saveDocument(DocumentDto document) {
		return repo.saveDocument(document);
	}

	@GetMapping("document/{documentId}")
	public Mono<DocumentDto> getDocument(@PathVariable String documentId) {
		return repo.findDocument(documentId);
	}

	@GetMapping("documents")
	public Flux<DocumentDto> getDocuments() {
		return repo.findAllDocument();
	}

	@GetMapping("document/generaterandom/{count}")
	public void generateRandom(@PathVariable Integer count) {
		for(int i = 0; i < count; i++) {
			DocumentDto doc = new DocumentDto();
			doc.setDocumentId(UUID.randomUUID().toString());
			doc.setDocumentName("Document-" + UUID.randomUUID().toString().substring(0, 5));
			doc.setProperties(List.of(new PropertyDto("CreationDate", new Date().toString())));
			repo.saveDocument(doc);
		}
	}
}

@Service
class DocumentRepository {
	Map<String, DocumentDto> documentsMap = new HashMap<>();

	public Mono<DocumentDto> saveDocument(DocumentDto document) {
		documentsMap.put(document.getDocumentId(), document);
		return Mono.just(documentsMap.get(document.getDocumentId()));
	}

	public Mono<DocumentDto> findDocument(String documentId) {
		return Mono.justOrEmpty(documentsMap.get(documentId));
	}

	public Flux<DocumentDto> findAllDocument() {
		return Flux.fromStream(documentsMap.values().stream());
	}
}

class DocumentDto {
	private String documentId;
	private String documentName;
	private List<PropertyDto> properties;
	
	public String getDocumentId() {
		return documentId;
	}
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public List<PropertyDto> getProperties() {
		return properties;
	}
	public void setProperties(List<PropertyDto> properties) {
		this.properties = properties;
	}
}

class PropertyDto {
	private String name;
	private String value;
	
	public PropertyDto(String name, String value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
