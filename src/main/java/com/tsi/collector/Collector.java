package com.tsi.collector;

import com.tsi.entity.Document;
import com.tsi.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
public class Collector
{
    @Autowired
    RepositoryService repositoryService;

    @RequestMapping(value = "/rest/getAllDocuments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Document>> getAllDocuments()
    {
        ResponseEntity<List<Document>> documents = repositoryService.findAllDocuments();
        return documents;
    }

    @RequestMapping(value = "/rest/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") String id)
    {
        System.out.println("Fetching document with id " + id);
        ResponseEntity<Document> document = repositoryService.findById(id);

        if (document == null) {
            System.out.println("Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Document>(document.getBody(), HttpStatus.OK);
    }

    @RequestMapping(value = "/rest/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addDocument(@RequestBody Document document, UriComponentsBuilder ucBuilder)
    {
        System.out.println("Creating document " + document.getName());

        repositoryService.saveDocument(document);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/rest/add").buildAndExpand(document.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/rest/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> deleteDocument(@PathVariable("id") String id)
    {
        System.out.println("Fetching & Deleting document with id " + id);

        ResponseEntity<Document> document = repositoryService.findById(id);

        if (document == null) {
            System.out.println("Unable to delete. Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }

        repositoryService.deleteDocumentById(id, String.valueOf(document.getHeaders().get("Location")));
        return new ResponseEntity<Document>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/rest/update/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> updateDocument(@PathVariable("id") String id, @RequestBody Document documentReq)
    {
        System.out.println("Updating document " + id);

        ResponseEntity<Document> currentDocument = repositoryService.findById(id);

        if (currentDocument == null) {
            System.out.println("Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }

        Document documentRes = currentDocument.getBody();
        System.out.println("Document with id " + documentRes.getId() + " found");

        documentRes.setName(documentReq.getName());
        documentRes.setTitle(documentReq.getTitle());
        documentRes.setContent(documentReq.getContent());
        documentRes.setIndexMap(documentReq.getIndexMap());
        documentRes.setComments(documentReq.getComments());

        repositoryService.updateDocument(documentRes, String.valueOf(currentDocument.getHeaders().get("Location")));
        return new ResponseEntity<Document>(documentRes, HttpStatus.OK);
    }
}