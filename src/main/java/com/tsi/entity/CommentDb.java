package com.tsi.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "COMMENT")
public class CommentDb {

    @Id
    @Column(name = "ID", nullable = false)
    UUID id;

    @Column(name = "USER_ID", nullable = false)
    UUID userId;

    @ManyToOne
    @JoinColumn(name = "FK_ID", nullable = false)
    DocumentDb document;

    @Column(name = "CONTENT", nullable = false)
    String content;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DocumentDb getDocument() {
       return document;
    }

    public void setDocument(DocumentDb document) {
       this.document = document;
    }
}
