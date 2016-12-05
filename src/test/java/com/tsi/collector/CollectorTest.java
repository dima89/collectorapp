package com.tsi.collector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.DocumentTestBase;
import com.tsi.entity.Document;
import com.tsi.repository.RepositoryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class CollectorTest extends DocumentTestBase {

    private MockMvc mockMvc;

    @Mock
    private RepositoryServiceImpl repositoryService;

    @InjectMocks
    private Collector collector;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(collector)
                .build();
    }

    @Test
    public void test_getAllDocuments() throws Exception {
        List<Document> documents = populateDocuments();
        when(repositoryService.findAllDocuments()).thenReturn(new ResponseEntity<List<Document>>(documents, HttpStatus.OK));

        mockMvc.perform(get("/rest/getAllDocuments"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(getFirstDocument());
    }

    @Test
    public void test_getDocumentById() throws Exception {
        String id = "664a8fa0-312c-48ee-9863-dcb88ca0c50a";
        Document documents = populateDocuments().get(0);
        when(repositoryService.findById(id)).thenReturn(new ResponseEntity<Document>(documents, HttpStatus.OK));

        mockMvc.perform(get("/rest/get/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(getFirstDocument());
    }

    @Test
    public void test_addDocument() throws Exception {
        String id = "664a8fa0-312c-48ee-9863-dcb88ca0c50a";
        Document document = populateDocuments().get(0);
        ObjectMapper mapper = new ObjectMapper();
        String documentJson = mapper.writeValueAsString(document);

        mockMvc.perform(post("/rest/add").contentType("application/json").content(documentJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void test_deleteDocumentById() throws Exception {
        String id = "664a8fa0-312c-48ee-9863-dcb88ca0c50a";
        Document document = populateDocuments().get(0);
        when(repositoryService.findById(id)).thenReturn(new ResponseEntity<Document>(document, HttpStatus.OK));

        mockMvc.perform(delete("/rest/delete/" + id))
                .andExpect(status().isNoContent());
    }


    @Test
    public void test_updateDocument() throws Exception {
        String id = "664a8fa0-312c-48ee-9863-dcb88ca0c50a";
        List<Document> document = populateDocuments();

        when(repositoryService.findById(id)).thenReturn(new ResponseEntity<Document>(document.get(0), HttpStatus.OK));

        ObjectMapper mapper = new ObjectMapper();
        String documentJson = mapper.writeValueAsString(document.get(1));

        mockMvc.perform(put("/rest/update/" + id).contentType("application/json").content(documentJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(getSecondDocumentSingleResult());
    }

}
