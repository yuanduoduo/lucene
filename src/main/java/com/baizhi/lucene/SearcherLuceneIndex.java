package com.baizhi.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;

public class SearcherLuceneIndex {
    public static void main(String[] args) {
        try {
            //索引库位置
            FSDirectory dir = FSDirectory.open(new File("/Users/yuanduo/Documents/index"));
            //配置索引库路径
            DirectoryReader reader = DirectoryReader.open(dir);
            //通过indexSearcher去检索索引目录
            IndexSearcher indexSearcher = new IndexSearcher(reader);
            //参数1 query 索引库检索的条件 关键字查询
            //八种基本类型加String类型的数据不分词
            //标准分词器的分词规则 为单字分词器
            Query query=new TermQuery(new Term("content","不"));
            //参数1 query 索引库检索的条件 关键字查询
            //参数2 查询条数
            TopDocs topDocs = indexSearcher.search(query, 100);
            //相关度排序后的结果
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < scoreDocs.length; i++) {
                ScoreDoc scoreDoc = scoreDocs[i];
                //得分采用VSM算法
                System.out.println("相关得分："+scoreDoc.score);
                //拿到索引编号
                int doc = scoreDoc.doc;
                //根据索引编号拿到document 获取到真正的数据
                Document document = indexSearcher.doc(doc);
                String title = document.get("title");
                String author = document.get("author");
                String content = document.get("content");
                System.out.println(title);
                System.out.println(author);
                System.out.println(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
