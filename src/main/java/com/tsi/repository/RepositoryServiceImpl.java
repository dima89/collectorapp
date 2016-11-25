package com.tsi.repository;

import com.tsi.entity.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        ResponseEntity<List> response = restTemplate.getForEntity(APIs.get(0)+"/getAllDocuments", List.class);
        return response;
    }

    public Document findById(long id) {
        /*for(Document document : documents){
            if(document.getId() == id){
                return document;
            }
        }*/
        return null;
    }

    public void saveDocument(Document document) {
       /* document.setId(1);
        documents.add(document);*/
    }

    public void updateDocument(Document document) {
        /*int index = documents.indexOf(document);
        documents.set(index, document);*/
    }

    public void deleteDocumentById(long id) {
         /*for (Iterator<Document> iterator = documents.iterator(); iterator.hasNext(); ) {
             Document document = iterator.next();
            if (document.getId() == id) {
                iterator.remove();
            }
        }*/
    }

    public boolean isDocumentExist(Document document) {
        return findById(document.getId())!=null;
    }

    private static List<String> populateAPIs(){
        List<String> apis = new ArrayList<String>();
        apis.add("http://localhost:8091/repo1/rest");
        apis.add("http://localhost:8082/repo2/rest");
        //users.add(new User(counter.incrementAndGet(),"Sam",30, 70000));
        //users.add(new User(counter.incrementAndGet(),"Tom",40, 50000));
        //users.add(new User(counter.incrementAndGet(),"Jerome",45, 30000));
        //users.add(new User(counter.incrementAndGet(),"Silvia",50, 40000));
        return apis;
    }
}