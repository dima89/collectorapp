package com.tsi.dao;

import com.tsi.entity.DocumentDb;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.UUID;

@Transactional
@Repository
public class DocumentDbDao
{
    @Autowired
    SessionFactory sessionFactory;

    public void save(DocumentDb document)
    {
        Session session = sessionFactory.getCurrentSession();
        DocumentDb cachedDocument = getCachedDocument(document.getId().toString());
        if (cachedDocument != null) {
            session.save(document);
            session.flush();
        }
    }

    public DocumentDb getCachedDocument(String id) {
        System.out.println("Searching document in DB with id: " + id);
        CriteriaBuilder builder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<DocumentDb> criteriaQuery = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(DocumentDb.class);
        Root<DocumentDb> documentRoot = criteriaQuery.from(DocumentDb.class);
        criteriaQuery.where(builder.equal(documentRoot.get("id"), UUID.fromString(id)));
        criteriaQuery.select(documentRoot);
        return sessionFactory.getCurrentSession().createQuery(criteriaQuery).uniqueResult();
    }

}