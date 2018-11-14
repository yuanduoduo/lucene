package com.baizhi.test;

import com.baizhi.util.LuceneUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.junit.Test;

import java.io.IOException;

public class TestLucene {
    @Test
    public void testCreteIndex() {
            IndexWriter indexWriter = LuceneUtil.getIndexWriter();
            Document document = new Document();
            document.add(new StringField("id", "1", Field.Store.YES));
            document.add(new StringField("title", "背影", Field.Store.YES));
            document.add(new StringField("author", "朱自清", Field.Store.YES));
            document.add(new TextField("content", "你在这里不动，我去买个橘子", Field.Store.YES));
        try {
            indexWriter.addDocument(document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            e.printStackTrace();
            LuceneUtil.rollback(indexWriter);
        }


    }

    @Test
    public void testSearcherIndex() {
        try {
            IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
            Query query = new TermQuery(new Term("content", "橘子"));
            TopDocs topDocs = indexSearcher.search(query, 100);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                int doc = scoreDoc.doc;
                //根据索引编号拿到document
                Document document = indexSearcher.doc(doc);
                String title = document.get("title");
                String author = document.get("author");
                String content = document.get("content");
                System.out.println(title);
                System.out.println(author);
                System.out.println(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testDeleteIndex() {

            IndexWriter indexWriter = LuceneUtil.getIndexWriter();
            Term term=new Term("id","1");
        try {
            indexWriter.deleteDocuments(term);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }

    }
    @Test
    public void testUpdateIndex() {
            IndexWriter indexWriter = LuceneUtil.getIndexWriter();
            Document document = new Document();
            document.add(new StringField("id","1",Field.Store.YES));
            document.add(new StringField("title","背影",Field.Store.YES));
            document.add(new StringField("author","朱自清",Field.Store.YES));
            document.add(new TextField("content","你在这里不动，我去买个橘子橘子橘子橘子橘子123123123123123",Field.Store.YES));
            Term term = new Term("id", "1");
        try {
            indexWriter.updateDocument(term,document);
            LuceneUtil.commit(indexWriter);
        } catch (IOException e) {
            LuceneUtil.rollback(indexWriter);
            e.printStackTrace();
        }

    }
}
