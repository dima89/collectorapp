package com.tsi.repository;

import com.tsi.entity.Comment;
import com.tsi.entity.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service("repositoryService")
@Transactional
public class RepositoryServiceImpl implements RepositoryService
{
    private static List<String> APIs;

    private static final AtomicLong counter = new AtomicLong();

    static{
        APIs = populateAPIs();
    }

    public ResponseEntity<List> findAllDocuments() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> response = restTemplate.getForEntity(APIs.get(0) + "/getAllDocuments", List.class);
        return response;
    }

    public Document findById(String id) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Document> response = restTemplate.getForEntity(APIs.get(0) + "/get/" + id, Document.class);
        return response.getBody();
    }

    public void saveDocument(Document document) {
       document.setId(UUID.randomUUID());

       Comment comment = new Comment();
       comment.setId(UUID.randomUUID());
       comment.setUserId(document.getId());

       RestTemplate restTemplate = new RestTemplate();
       restTemplate.postForEntity(APIs.get(0) + "/add", document, Document.class);
    }

    public void updateDocument(Document document) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(APIs.get(0) + "/update", document, Document.class);
    }

    public void deleteDocumentById(String id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(APIs.get(0) + "/delete/" + id);
    }

    private static List<String> populateAPIs(){
        List<String> apis = new ArrayList<String>();
        apis.add("http://localhost:8091/repo1/rest");
        apis.add("http://localhost:8082/repo2/rest");
        return apis;
    }
}