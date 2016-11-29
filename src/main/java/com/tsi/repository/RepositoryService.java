package com.tsi.repository;

import com.tsi.entity.Document;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface RepositoryService {
    Document findById(String id);

    void saveDocument(Document user);

    void updateDocument(Document user);

    void deleteDocumentById(String id);

    ResponseEntity<List> findAllDocuments();
}
