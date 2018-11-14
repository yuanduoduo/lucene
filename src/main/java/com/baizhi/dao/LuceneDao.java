package com.baizhi.dao;

import com.baizhi.entity.Artical;
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
public class LuceneDao {
    public void addIndex(Artical artical){
        IndexWriter indexWriter= LuceneUtil.getIndexWriter();
        Document docFromArt = getDocFromArt(artical);
        try {
            indexWriter.addDocument(docFromArt);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }
    public List<Artical> searcheIndex(String param){
        List<Artical> articals = new ArrayList<>();
        IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
        TermQuery content = new TermQuery(new Term("content", param));
        try {
            TopDocs search = indexSearcher.search(content, 100);
            ScoreDoc[] scoreDocs = search.scoreDocs;
            for (int i = 0; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                int doc = scoreDoc.doc;
                Document document = indexSearcher.doc(doc);
                Artical artFromDoc = getArtFromDoc(document);
                articals.add(artFromDoc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return articals;
    }
    public void deleteIndex(String id){
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            indexWriter.deleteDocuments(new Term("id",id));
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }
    public void updateindex(Artical artical){
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        try {
            indexWriter.updateDocument(new Term("id",artical.getId()),getDocFromArt(artical));
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }
    }
    public Document getDocFromArt(Artical artical){
        Document document = new Document();
        document.add(new StringField("id", artical.getId(), Field.Store.YES));
        document.add(new StringField("title", artical.getTitle(), Field.Store.YES));
        document.add(new StringField("author", artical.getAuthor(), Field.Store.YES));
        document.add(new TextField("content", artical.getContent(), Field.Store.YES));
        return document;
    }
    public Artical getArtFromDoc(Document document){
        Artical artical = new Artical(document.get("id"), document.get("title"), document.get("author"), document.get("content"));
        return artical;
    }
}
