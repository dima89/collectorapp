package com.tsi.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "DOCUMENT")
public class DocumentDb {

    @Id
    @Column(name = "ID", nullable = false)
    UUID id;

    @Column(name = "NAME", nullable = false)
    String name;

    @Column(name = "TITLE", nullable = false)
    String title;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="INDEX_NAME")
    @Column(name="INDEX_VALUE", nullable = false)
    Map<String, String> indexMap = new HashMap<>();

    @Column(name = "CONTENT", nullable = false)
    String content;

    @OneToMany(mappedBy="document", targetEntity = CommentDb.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    List<CommentDb> comments = new ArrayList<>();

    public UUID getId() {
            return id;
        }

    public void setId(UUID id) {
            this.id = id;
        }

    public String getName() {
            return name;
        }

    public void setName(String name) {
            this.name = name;
        }

    public String getTitle() {
            return title;
        }

    public void setTitle(String title) {
            this.title = title;
        }

    public Map<String, String> getIndexMap() {
            return indexMap;
        }

    public void setIndexMap(Map<String, String> indexMap) {
            this.indexMap = indexMap;
        }

    public String getContent() {
            return content;
        }

    public void setContent(String content) {
            this.content = content;
        }

    public List<CommentDb> getComments() {
        return comments;
    }

    public void setComments(List<CommentDb> comments) {
        this.comments = comments;
    }

    public void addComment(CommentDb comment) {
        comment.setDocument(this);
        comments.add(comment);
    }

}