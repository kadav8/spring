package com.example.jpa;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication
public class SpringDataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaApplication.class, args);
	}
}

@Order(1)
@Component
class DocumentService implements ApplicationRunner {

	@Autowired
	DocumentRepository repo;

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
		// save documents
		Document lastSaved = null;
		for(int i = 0; i < 3; i++) {
			Document doc = new Document();
			doc.setDocumentName("Document-" + UUID.randomUUID().toString().substring(0, 5));
			doc.setProperties(Map.of("CreationDate", new Date().toString()));
			lastSaved = repo.saveAndFlush(doc);
			System.out.println(String.format("Saved: %s", lastSaved));
		}

		// find document by Id
		Optional<Document> od = repo.findById(lastSaved.getDocumentId());
		if(od.isPresent()) {
			System.out.println(od.get());
		}

		// find all
		System.out.println(repo.findAll());
	}
}

interface DocumentRepository extends JpaRepository<Document, Long> {
}

@Entity
class Document {
	
	@Id @GeneratedValue
	private Long documentId;
	private String documentName;
	@ElementCollection
	private Map<String,String> properties;
	
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
	public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public Map<String, String> getProperties() {
		return properties;
	}
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	@Override
	public String toString() {
		return "Document [documentId=" + documentId + ", documentName=" + documentName + ", properties=" + properties
				+ "]";
	}
}
