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
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.*;

@Service("repositoryService")
@Transactional
public class RepositoryServiceImpl implements RepositoryService {

    @Autowired
    private ApiConfig apiConfig;

    @Autowired
    private DocumentDbDao documentDao;

    public static class GetAllDocuments implements Callable {
        private final String url;

        public GetAllDocuments(String url) {
            this.url = url;
        }

        public ResponseEntity<List<Document>> call() throws Exception {
            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<Document[]> documents = restTemplate.getForEntity(url + "/getAllDocuments", Document[].class);
            List<Document> list = Arrays.asList(documents.getBody());
            return new ResponseEntity<List<Document>>(list, HttpStatus.OK);
        }
    }

    public static class GetDocument implements Callable {
        private final String url;

        public GetDocument(String url) {
            this.url = url;
        }

        public ResponseEntity<Document> call() throws Exception {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Document> document = restTemplate.getForEntity(url, Document.class);
            return document;
        }
    }

    public ResponseEntity<List<Document>> findAllDocuments() {

        ExecutorService service = Executors.newCachedThreadPool();
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

        ExecutorService service = Executors.newCachedThreadPool();
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
       RestTemplate restTemplate = new RestTemplate();
       restTemplate.postForEntity(apiConfig.getApiList().get(new Random().nextInt(2)) + "/add", document, Document.class);
    }

    public void updateDocument(Document document, String location) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(apiConfig.getCorrectApi(location) + "/update", document, Document.class);
    }

    public void deleteDocumentById(String id, String location) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(apiConfig.getCorrectApi(location) + "/delete/" + id);
    }

}