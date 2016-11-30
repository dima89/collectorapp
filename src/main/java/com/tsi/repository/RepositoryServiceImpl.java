package com.tsi.repository;

import com.tsi.entity.Comment;
import com.tsi.entity.Document;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.*;
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

    public static class GetAllDocuments implements Callable {
        private final String url;

        public GetAllDocuments(String url) {
            this.url = url;
        }

        public ResponseEntity<List> call() throws Exception {
            RestTemplate restTemplate = new RestTemplate();
            return restTemplate.getForEntity(url + "/getAllDocuments", List.class);
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

    public ResponseEntity<List> findAllDocuments() {
        ExecutorService service = Executors.newCachedThreadPool();
        Set <Callable<ResponseEntity<List>>> callables = new HashSet<Callable<ResponseEntity<List>>>();

        for (String apiUrl: APIs) {
            Callable worker = new GetAllDocuments(apiUrl);
            callables.add(worker);
        }

        try
        {
            List<Future<ResponseEntity<List>>> futures = service.invokeAll(callables);
            ResponseEntity<List> response = new ResponseEntity<List>(new ArrayList(), HttpStatus.OK);
            for (Future<ResponseEntity<List>> future : futures)
            {
                System.out.println (future.get());
                response.getBody().add(future.get().getBody());
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

        return new ResponseEntity<List>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<Document> findById(String id) {
        ExecutorService service = Executors.newCachedThreadPool();
        Set <Callable<ResponseEntity<Document>>> callables = new HashSet<Callable<ResponseEntity<Document>>>();

        for (String apiUrl: APIs) {
            Callable worker = new GetDocument(apiUrl + "/get/" + id);
            callables.add(worker);
        }

        try
        {
            List<Future<ResponseEntity<Document>>> futures = service.invokeAll(callables);
            for (Future<ResponseEntity<Document>> future : futures)
            {
                System.out.println (future.get());
                return future.get();
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

       // RestTemplate restTemplate = new RestTemplate();
        //ResponseEntity<Document> response = restTemplate.getForEntity(APIs.get(0) + "/get/" + id, Document.class);
        return null;
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

    public void deleteDocumentById(String id, String location) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(getCorrectApi(location) + "/delete/" + id);
    }

    private static List<String> populateAPIs(){
        List<String> apis = new ArrayList<String>();
        apis.add("http://localhost:8091/repo1/rest");
        apis.add("http://localhost:8092/repo2/rest");
        return apis;
    }

    private String getCorrectApi(String location) {
        for (String api: APIs) {
            if (location.contains(api)) {
                System.out.println("Repository API location: " + api);
                return api;
            }
        }
        throw new IllegalArgumentException("Repository API is not registered " + location);
    }
}