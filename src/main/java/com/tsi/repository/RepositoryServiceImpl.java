package com.tsi.repository;

import com.tsi.adapter.DocumentAdapter;
import com.tsi.configuration.ApiConfig;
import com.tsi.dao.DocumentDbDao;
import com.tsi.entity.Comment;
import com.tsi.entity.Document;
import com.tsi.entity.DocumentDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.*;

@Service("repositoryService")
@Transactional
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private DocumentDbDao documentDao;

    @Autowired
    private RestTemplate restTemplate;

    public class GetAllDocuments implements Callable {
        private final String url;

        public GetAllDocuments(String url) {
            this.url = url;
        }

        public ResponseEntity<List<Document>> call() throws Exception {
            try {
                ResponseEntity<Document[]> documents = restTemplate.getForEntity(url + "/getAllDocuments", Document[].class);
                List<Document> list = Arrays.asList(documents.getBody());
                return new ResponseEntity<List<Document>>(list, HttpStatus.OK);
            } catch (ResourceAccessException ex) {
                System.out.println("Repository " + url + " is currently offline");
                return new ResponseEntity<List<Document>>(new ArrayList<>(), HttpStatus.OK);
            }
        }
    }

    public class GetDocument implements Callable {
        private final String url;

        public GetDocument(String url) {
            this.url = url;
        }

        public ResponseEntity<Document> call() throws Exception {
            try {
                return restTemplate.getForEntity(url, Document.class);
            } catch (ResourceAccessException ex) {
                System.out.println("Repository " + url + " is currently offline");
                return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
            }
        }
    }

    public ResponseEntity<List<Document>> findAllDocuments() {

        ExecutorService service = Executors.newSingleThreadExecutor();
        Set <Callable<ResponseEntity<List<Document>>>> callables = new HashSet<Callable<ResponseEntity<List<Document>>>>();

        for (String apiUrl: apiConfig.getApiList()) {
            Callable worker = new GetAllDocuments(apiUrl);
            callables.add(worker);
        }

        try
        {
            List<Future<ResponseEntity<List<Document>>>> futures = service.invokeAll(callables);
            ResponseEntity<List<Document>> response = new ResponseEntity<List<Document>>(new ArrayList(), HttpStatus.OK);
            for (Future<ResponseEntity<List<Document>>> future : futures)
            {
                System.out.println (future.get());
                for (Document documentObj: future.get().getBody()) {
                    documentDao.save(DocumentAdapter.fromDocument(documentObj));
                    response.getBody().add(documentObj);
                }
            }
            response.getBody().sort((n1, n2) -> n1.getName().compareTo(n2.getName()));
            return response;
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        service.shutdown();

        return new ResponseEntity<List<Document>>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Document> findById(String id) {

        DocumentDb documentFromDb = documentDao.getCachedDocument(id);
        if (documentFromDb != null) {
            System.out.println("Found document in DB with id: " + documentFromDb.getId());
            Document document = DocumentAdapter.fromDocumentDb(documentFromDb);
            return new ResponseEntity<Document>(document, HttpStatus.OK);
        }

        ExecutorService service = Executors.newSingleThreadExecutor();
        Set <Callable<ResponseEntity<Document>>> callables = new HashSet<Callable<ResponseEntity<Document>>>();

        for (String apiUrl: apiConfig.getApiList()) {
            Callable worker = new GetDocument(apiUrl + "/get/" + id);
            callables.add(worker);
        }

        try
        {
            List<Future<ResponseEntity<Document>>> futures = service.invokeAll(callables);
            for (Future<ResponseEntity<Document>> future : futures)
            {
                if (future.get().getBody() != null) {
                    System.out.println(future.get().getBody());
                    documentDao.save(DocumentAdapter.fromDocument(future.get().getBody()));
                    return future.get();
                }
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        service.shutdown();

        return null;
    }

    public void saveDocument(Document document) {
        String url = apiConfig.getApiList().get(new Random().nextInt(2));
        try {
            restTemplate.postForEntity(url + "/add", document, Document.class);
        } catch (ResourceAccessException ex) {
            System.out.println("Repository " + url + " is currently offline");
        }
    }

    public void updateDocument(Document document, String location) {
        restTemplate.put(apiConfig.getCorrectApi(location) + "/update", document, Document.class);
    }

    public void deleteDocumentById(String id, String location) {
        restTemplate.delete(apiConfig.getCorrectApi(location) + "/delete/" + id);
    }

}