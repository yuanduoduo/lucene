package com.baizhi.dao;

import com.baizhi.entity.Product;
import com.baizhi.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductDao {
    public void addIndex(Product product) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document docFromArt = getDocFromArt(product);
        try {
            indexWriter.addDocument(docFromArt);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }

    public void deleteIndex(String id) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            indexWriter.deleteDocuments(new Term("id", id));
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }

    public void updateIndex(Product product) {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        Document docFromArt = getDocFromArt(product);
        try {
            indexWriter.updateDocument(new Term("id", product.getId()), docFromArt);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }

    public List<Product> searcheIndex(String param) {
        List<Product> products = new ArrayList<>();
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        TermQuery name = new TermQuery(new Term("name", param));
        try {
            TopDocs search = indexSearcher.search(name, 100);
            ScoreDoc[] scoreDocs = search.scoreDocs;
            for (int i = 0; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                int doc = scoreDoc.doc;
                Document document = indexSearcher.doc(doc);
                Product artFromDoc = getArtFromDoc(document);
                products.add(artFromDoc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Document getDocFromArt(Product product) {
        Document document = new Document();
        document.add(new StringField("id", product.getId(), Field.Store.YES));
        document.add(new TextField("name", product.getName(), Field.Store.YES));
        document.add(new StringField("price", product.getPrice(), Field.Store.YES));
        document.add(new StringField("desc", product.getDesc(), Field.Store.YES));
        document.add(new StringField("img", product.getImg(), Field.Store.YES));
        document.add(new StringField("status", product.getStatus(), Field.Store.YES));
        document.add(new StringField("proDate", product.getDesc(), Field.Store.YES));
        document.add(new StringField("poace", product.getPoace(), Field.Store.YES));
        return document;
    }

    public Product getArtFromDoc(Document document) {
        Product product = new Product(document.get("id"), document.get("name"),document.get("price"), document.get("desc"),document.get("img"), document.get("status"),document.get("proDate"), document.get("poace"));
        return product;
    }
}

