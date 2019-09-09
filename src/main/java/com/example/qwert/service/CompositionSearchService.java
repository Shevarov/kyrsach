package com.example.qwert.service;

import com.example.qwert.domain.Composition;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class CompositionSearchService {
    private static final Class<Composition> TARGET_CLASS = Composition.class;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Composition> findComposition(String searchText) throws InterruptedException{
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
       // fullTextEntityManager.createIndexer().startAndWait();
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
            .buildQueryBuilder()
            .forEntity(Composition.class)
            .get();
        Query luceneQuery = queryBuilder
            .keyword()
            .fuzzy()
            .onFields("title","genre","shortDescription","author.username","tages.tagName",
                "chapter.chapterName","chapter.text","comments.commentary")
            .matching(searchText)
            .createQuery();
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, TARGET_CLASS);
        return fullTextQuery.getResultList();
    }
}
