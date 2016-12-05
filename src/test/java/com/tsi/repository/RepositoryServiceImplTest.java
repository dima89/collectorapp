package com.tsi.repository;

import com.tsi.DocumentTestBase;
import com.tsi.configuration.ApiConfig;
import com.tsi.dao.DocumentDbDao;
import com.tsi.entity.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


public class RepositoryServiceImplTest extends DocumentTestBase {

    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DocumentDbDao documentDbDao;

    @InjectMocks
    private RepositoryServiceImpl repositoryService;

    @Autowired
    private ApiConfig apiConfig;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(repositoryService)
                .build();
        ReflectionTestUtils.setField(repositoryService, "apiConfig", apiConfig);
    }

    @Test
    public void test_getAllDocuments() throws Exception {
        List<Document> documentList = populateDocuments();
        Document[] documents = documentList.toArray(new Document[documentList.size()]);
        List<Document> documentListSecondRepo = populateDocumentsSecondRepo();
        Document[] documentsSecondRepo = documentListSecondRepo.toArray(new Document[documentListSecondRepo.size()]);
        when(restTemplate.getForEntity(apiConfig.getApiList().get(0) + "/getAllDocuments", Document[].class)).thenReturn(new ResponseEntity<Document[]>(documents, HttpStatus.OK));
        when(restTemplate.getForEntity(apiConfig.getApiList().get(1) + "/getAllDocuments", Document[].class)).thenReturn(new ResponseEntity<Document[]>(documentsSecondRepo, HttpStatus.OK));

        ResponseEntity<List<Document>> documentsResponse = repositoryService.findAllDocuments();
        assertEquals(documentsResponse.getBody().get(0).getId().toString(), documentList.get(0).getId().toString());
        assertEquals(documentsResponse.getBody().get(3).getId().toString(), documentList.get(1).getId().toString());
        assertEquals(documentsResponse.getBody().get(4).getId().toString(), documentList.get(2).getId().toString());
        assertEquals(documentsResponse.getBody().get(2).getId().toString(), documentListSecondRepo.get(0).getId().toString());
        assertEquals(documentsResponse.getBody().get(1).getId().toString(), documentListSecondRepo.get(1).getId().toString());
    }

    @Test
    public void test_getOneDocument() throws Exception {
        List<Document> documentList = populateDocuments();
        when(restTemplate.getForEntity(apiConfig.getApiList().get(0) + "/get/" + documentList.get(0).getId(), Document.class)).thenReturn(new ResponseEntity<Document>(documentList.get(0), HttpStatus.OK));
        when(restTemplate.getForEntity(apiConfig.getApiList().get(1) + "/get/" + documentList.get(0).getId(), Document.class)).thenReturn(new ResponseEntity<Document>(documentList.get(0), HttpStatus.OK));

        ResponseEntity<Document> documentResponse = repositoryService.findById(documentList.get(0).getId().toString());
        assertEquals(documentList.get(0).getId(), documentResponse.getBody().getId());
    }

    @Test
    public void test_updateDocument() throws Exception {
        List<Document> documentList = populateDocuments();
        when(restTemplate.getForEntity(apiConfig.getApiList().get(0) + "/get/" + documentList.get(0).getId(), Document.class)).thenReturn(new ResponseEntity<Document>(documentList.get(0), HttpStatus.OK));

        repositoryService.updateDocument(documentList.get(1), apiConfig.getApiList().get(0));
    }

    @Test
    public void test_deleteDocument() throws Exception {
        List<Document> documentList = populateDocuments();
        when(restTemplate.getForEntity(apiConfig.getApiList().get(0) + "/delete/" + documentList.get(0).getId(), Document.class)).thenReturn(new ResponseEntity<Document>(documentList.get(0), HttpStatus.OK));

        repositoryService.deleteDocumentById(documentList.get(0).getId().toString(), apiConfig.getApiList().get(0));
    }
}
