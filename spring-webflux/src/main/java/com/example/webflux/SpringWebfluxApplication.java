package com.example.webflux;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringWebfluxApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxApplication.class, args);
	}
}

@RestController
class Endpoints {

	@Autowired
	DocumentRepository repo;

	@PostMapping("document/save")
	public Mono<DocumentDto> saveDocument(DocumentDto document) {
		return saveDocument(document);
	}
}

@Service
class DocumentRepository {
	Map<String, DocumentDto> documentsMap = new HashMap<>();

	public Mono<DocumentDto> saveDocument(DocumentDto document) {
		documentsMap.put(document.getDocumentId(), document);
		return Mono.just(document);
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
