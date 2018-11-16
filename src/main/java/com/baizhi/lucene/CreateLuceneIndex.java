package com.baizhi.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class CreateLuceneIndex {
    public static void main(String[] args) {
        try {
            //索引库位置
            FSDirectory dir = FSDirectory.open(new File("/Users/yuanduo/Documents/index"));
            //分词器  参数：lucene当前版本
            Analyzer analyzer = new IKAnalyzer();
            //索引写入对象相关配置  参数1：lucene当前版本 参数二：分词器
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44,analyzer);
            //构建索引写入对象  参数1 索引库路径   参数2 索引写入对象相关配置   分词器
            IndexWriter indexWriter = new IndexWriter(dir, config);
            //通过indexWriter来创建索引
            //索引库里面的数据  要遵守一定的结构 (索引结构，document)
            Document document = new Document();
            /**
             * 1.字段的名称 2.该字段的值 3.字段在数据库中是否存储
             * StringField是一体的
             * TextField是可分的
             */
            document.add(new StringField("title","黄涛",Field.Store.YES));
            document.add(new StringField("author","仁波切",Field.Store.YES));
            document.add(new TextField("content","这是一个不可描述的故事",Field.Store.YES));
            //Field.Store.YES 保存内容
            //Field.Store.ON  不保存内容 不关注内容
            // document里面也有很多字段
            indexWriter.addDocument(document);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
