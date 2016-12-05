package com.tsi;


import com.tsi.configuration.AppConfiguration;
import com.tsi.entity.Comment;
import com.tsi.entity.Document;
import com.tsi.entity.DocumentBuilder;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfiguration.class)
@WebAppConfiguration
public class DocumentTestBase {

    public List<Document> populateDocuments() {

        List<Document> documentList = new ArrayList<Document>();

        Document documentFirst = new DocumentBuilder()
                .setId(UUID.fromString("664a8fa0-312c-48ee-9863-dcb88ca0c50a"))
                .setName("First name doc")
                .setContent("First content doc")
                .setTitle("First title doc")
                .setIndexMap("index one", "index two")
                .setIndexMap("index two", "index three")
                .addComment(new Comment(UUID.fromString("f9bc64b5-03c9-415b-976b-9dd597d585e6"), UUID.fromString("32895c35-f496-4cb1-a505-7ccb4fbd1be1"), "First comment doc"))
                .addComment(new Comment(UUID.fromString("f9bc64b5-03c9-415b-976b-9dd597d585e7"), UUID.fromString("32895c35-f496-4cb1-a505-7ccb4fbd1be2"), "Second comment doc"))
                .build();

        System.out.println("Created document with id " + documentFirst.getId());

        Document documentSecond = new DocumentBuilder()
                .setId(UUID.fromString("d88c925f-a8ec-45f0-836b-0a3e6bb621d0"))
                .setName("Second name doc")
                .setContent("Second content doc")
                .setTitle("Second title doc")
                .setIndexMap("index one", "index two")
                .addComment(new Comment(UUID.fromString("ed60422f-9be9-4230-af61-95080c1871f5"), UUID.fromString("59d02fff-0c02-4ad1-8958-c0b22e6b2478"), "Second comment doc"))
                .build();

        System.out.println("Created document with id " + documentSecond.getId());

        Document documentThird = new DocumentBuilder()
                .setId(UUID.fromString("1cb38092-37dd-4475-80f0-8772a988a6ce"))
                .setName("Third name doc")
                .setContent("Third content doc")
                .setTitle("Third title doc")
                .setIndexMap("index one", "index two")
                .addComment(new Comment(UUID.fromString("7fcadc44-ad7b-4207-8d3a-973e09190b22"), UUID.fromString("0cd81f37-dbf2-49d5-8438-964304914b5e"), "Third comment doc"))
                .build();

        System.out.println("Created document with id " + documentThird.getId());

        documentList.add(documentFirst);
        documentList.add(documentSecond);
        documentList.add(documentThird);

        return documentList;
    }

    public List<Document> populateDocumentsSecondRepo(){
        List<Document> documentList = new ArrayList<Document>();

        Document documentFour = new DocumentBuilder()
                .setId(UUID.fromString("664a8fa0-312c-48ee-9863-dcb88ca0c50b"))
                .setName("Four name doc")
                .setContent("Four content doc")
                .setTitle("Four title doc")
                .setIndexMap("index one", "index two")
                .addComment(new Comment(UUID.fromString("a0dbc5fc-dafc-4cf7-9abb-f0db37e69aab"), UUID.fromString("34c09743-3b4a-45e4-a6b4-8ce0491adbe5"), "Four comment doc"))
                .build();

        System.out.println("Created document with id " + documentFour.getId());

        Document documentFive = new DocumentBuilder()
                .setId(UUID.fromString("d88c925f-a8ec-45f0-836b-0a3e6bb621d1"))
                .setName("Five name doc")
                .setContent("Five content doc")
                .setTitle("Five title doc")
                .setIndexMap("index one", "index two")
                .addComment(new Comment(UUID.fromString("026d27c6-70fe-4e78-90a4-2c50257c6cd3"), UUID.fromString("95bdf0ef-8495-4924-86e8-4b5e8a326632"), "Five comment doc"))
                .build();

        System.out.println("Created document with id " + documentFive.getId());

        documentList.add(documentFour);
        documentList.add(documentFive);

        return documentList;
    }

    public ResultMatcher getFirstDocument() {
        ResultMatcher rm = mvcResult -> {
            jsonPath("$[0].id", is("664a8fa0-312c-48ee-9863-dcb88ca0c50a"));
            jsonPath("$[0].name", is("First name doc"));
            jsonPath("$[0].title", is("First title doc"));
            jsonPath("$[0].indexMap", hasEntry("index one", "index two"));
            jsonPath("$[0].indexMap", hasEntry("index one", "index two"));
            jsonPath("$[0].content", is("First content doc"));
            jsonPath("$[0].comments[0].id", is("f9bc64b5-03c9-415b-976b-9dd597d585e6"));
            jsonPath("$[0].comments[0].userId", is("32895c35-f496-4cb1-a505-7ccb4fbd1be1"));
            jsonPath("$[0].comments[0].content", is("First comment doc"));
            jsonPath("$[0].comments[1].id", is("f9bc64b5-03c9-415b-976b-9dd597d585e7"));
            jsonPath("$[0].comments[1].userId", is("32895c35-f496-4cb1-a505-7ccb4fbd1be2"));
            jsonPath("$[0].comments[1].content", is("Second comment doc"));
        };
        return rm;
    }

    public ResultMatcher getSecondDocument() {
        ResultMatcher rm = mvcResult -> {
            jsonPath("$[1].id", is("d88c925f-a8ec-45f0-836b-0a3e6bb621d0"));
            jsonPath("$[1].name", is("Second name doc"));
            jsonPath("$[1].title", is("Second title doc"));
            jsonPath("$[1].indexMap", hasEntry("index one", "index two"));
            jsonPath("$[1].content", is("Second content doc"));
            jsonPath("$[1].comments[0].id", is("ed60422f-9be9-4230-af61-95080c1871f5"));
            jsonPath("$[1].comments[0].userId", is("59d02fff-0c02-4ad1-8958-c0b22e6b2478"));
            jsonPath("$[1].comments[0].content", is("Second comment doc"));
        };
        return rm;
    }

    public ResultMatcher getThirdDocument() {
        ResultMatcher rm = mvcResult -> {
            jsonPath("$[2].id", is("1cb38092-37dd-4475-80f0-8772a988a6ce"));
            jsonPath("$[2].name", is("Third name doc"));
            jsonPath("$[2].title", is("Third title doc"));
            jsonPath("$[2].indexMap", hasEntry("index one", "index two"));
            jsonPath("$[2].content", is("Third content doc"));
            jsonPath("$[2].comments[0].id", is("7fcadc44-ad7b-4207-8d3a-973e09190b22"));
            jsonPath("$[2].comments[0].userId", is("0cd81f37-dbf2-49d5-8438-964304914b5e"));
            jsonPath("$[2].comments[0].content", is("Third comment doc"));
        };
        return rm;
    }

    public ResultMatcher getFirstDocumentSecondRepo() {
        ResultMatcher rm = mvcResult -> {
            jsonPath("$[0].id", is("664a8fa0-312c-48ee-9863-dcb88ca0c50b"));
            jsonPath("$[0].name", is("Four name doc"));
            jsonPath("$[0].title", is("Four title doc"));
            jsonPath("$[0].indexMap", hasEntry("index one", "index two"));
            jsonPath("$[0].content", is("Four content doc"));
            jsonPath("$[0].comments[0].id", is("a0dbc5fc-dafc-4cf7-9abb-f0db37e69aab"));
            jsonPath("$[0].comments[0].userId", is("34c09743-3b4a-45e4-a6b4-8ce0491adbe5"));
            jsonPath("$[0].comments[0].content", is("Four comment doc"));
        };
        return rm;
    }

    public ResultMatcher getSecondDocumentSecondRepo() {
        ResultMatcher rm = mvcResult -> {
            jsonPath("$[1].id", is("d88c925f-a8ec-45f0-836b-0a3e6bb621d1"));
            jsonPath("$[1].name", is("Five name doc"));
            jsonPath("$[1].title", is("Five title doc"));
            jsonPath("$[1].indexMap", hasEntry("index one", "index two"));
            jsonPath("$[1].content", is("Five content doc"));
            jsonPath("$[1].comments[0].id", is("026d27c6-70fe-4e78-90a4-2c50257c6cd3"));
            jsonPath("$[1].comments[0].userId", is("95bdf0ef-8495-4924-86e8-4b5e8a326632"));
            jsonPath("$[1].comments[0].content", is("Five comment doc"));
        };
        return rm;
    }

    public ResultMatcher getSecondDocumentSingleResult() {
        ResultMatcher rm = mvcResult -> {
            jsonPath("$.id", is("664a8fa0-312c-48ee-9863-dcb88ca0c50a"));
            jsonPath("$.name", is("Second name doc"));
            jsonPath("$.title", is("Second title doc"));
            jsonPath("$.indexMap", hasEntry("index one", "index two"));
            jsonPath("$.content", is("Second content doc"));
            jsonPath("$.comments[0].id", is("ed60422f-9be9-4230-af61-95080c1871f5"));
            jsonPath("$.comments[0].userId", is("59d02fff-0c02-4ad1-8958-c0b22e6b2478"));
            jsonPath("$.comments[0].content", is("Second comment doc"));
        };
        return rm;
    }
}
