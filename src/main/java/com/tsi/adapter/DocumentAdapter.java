package com.tsi.adapter;

import com.tsi.entity.Comment;
import com.tsi.entity.CommentDb;
import com.tsi.entity.Document;
import com.tsi.entity.DocumentDb;

public class DocumentAdapter {

    public static Document fromDocumentDb(DocumentDb documentDb) {
        if (documentDb != null) {
            Document document = new Document();

            document.setId(documentDb.getId());
            document.setName(documentDb.getName());
            document.setContent(documentDb.getContent());
            document.setIndexMap(documentDb.getIndexMap());
            document.setTitle(documentDb.getTitle());

            for (CommentDb commentDb : documentDb.getComments()) {
                Comment comment = new Comment();
                comment.setId(commentDb.getId());
                comment.setUserId(commentDb.getUserId());
                comment.setContent(commentDb.getContent());
                document.addComment(comment);
            }

            return document;
        }
        return null;
    }

    public static DocumentDb fromDocument(Document document) {
        if (document != null) {
            DocumentDb documentDb = new DocumentDb();

            documentDb.setId(document.getId());
            documentDb.setName(document.getName());
            documentDb.setContent(document.getContent());
            documentDb.setIndexMap(document.getIndexMap());
            documentDb.setTitle(document.getTitle());

            for (Comment comment : document.getComments()) {
                CommentDb commentDb = new CommentDb();
                commentDb.setId(comment.getId());
                commentDb.setUserId(comment.getUserId());
                commentDb.setContent(comment.getContent());
                documentDb.addComment(commentDb);
            }

            return documentDb;
        }
        return null;
    }
}
