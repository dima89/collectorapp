package com.tsi.collector;

import com.tsi.entity.Comment;
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
    public ResponseEntity<List> getAllDocuments()
    {
        ResponseEntity<List> documents = repositoryService.findAllDocuments();
        return documents;
    }

    @RequestMapping(value = "/rest/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> getDocumentById(@PathVariable("id") String id)
    {
        System.out.println("Fetching document with id " + id);
        Document document = repositoryService.findById(id);
        if (document == null) {
            System.out.println("Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Document>(document, HttpStatus.OK);
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

        Document document = repositoryService.findById(id);

        if (document == null) {
            System.out.println("Unable to delete. Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }

        repositoryService.deleteDocumentById(id);
        return new ResponseEntity<Document>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/rest/update/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Document> updateDocument(@PathVariable("id") String id, @RequestBody Document document)
    {
        System.out.println("Updating document " + id);

        Document currentDocument = repositoryService.findById(id);

        if (currentDocument==null) {
            System.out.println("Document with id " + id + " not found");
            return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
        }
        System.out.println("Document with id " + currentDocument.getId() + " found");

        currentDocument.setName(document.getName());
        currentDocument.setTitle(document.getTitle());
        currentDocument.setContent(document.getContent());
        currentDocument.setIndexMap(document.getIndexMap());
        currentDocument.setComments(document.getComments());

        repositoryService.updateDocument(currentDocument);
        return new ResponseEntity<Document>(currentDocument, HttpStatus.OK);
    }
}