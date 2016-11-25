package com.tsi.repository;

import com.tsi.entity.Document;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RepositoryService {
    Document findById(long id);

    void saveDocument(Document user);

    void updateDocument(Document user);

    void deleteDocumentById(long id);

    ResponseEntity<List> findAllDocuments();

    boolean isDocumentExist(Document user);
}
