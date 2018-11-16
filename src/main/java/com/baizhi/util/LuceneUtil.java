package com.baizhi.util;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class LuceneUtil {
    private static Directory directory;
    private static Version version;
    private static Analyzer analyzer;
    private static IndexWriterConfig writerConfig;
    static {
        try {
            directory=FSDirectory.open(new File("/Users/yuanduo/Documents/index"));
            //版本号
            version=Version.LUCENE_44;
            //分词器
            analyzer=new IKAnalyzer();
            //索引写入对象相关配置
            writerConfig=new IndexWriterConfig(version,analyzer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*
    * @return 返回版本号
    * */
    public static Version getVersion(){
        return version;
    }
    /*
    *  @return 返回用于操作索引的对象
    *  @throws IOException
    * */
    public static IndexWriter getIndexWriter(){
        IndexWriter indexWriter= null;
        try {
            indexWriter = new IndexWriter(directory,writerConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return indexWriter;
    }
    /*
    *  @return 返回用于读取索引的对象
    *  @throes IOException
    * */
    public static IndexSearcher getIndexSearcher(){
        IndexReader reader= null;
        try {
            reader = DirectoryReader.open(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        IndexSearcher indexSearcher=new IndexSearcher(reader);
        return indexSearcher;
    }
    /*
    * @returnb 返回分词器
    * */
    public static Analyzer getAnalyzer(){
        return analyzer;
    }
    /*
    * 提交并且关闭操作对象
    * */
    public static void commit(IndexWriter indexWriter) {
        if (indexWriter != null) {
            try {
                indexWriter.commit();
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    * 回滚并且关闭操作对象
    * */
    public static void rollback(IndexWriter indexWriter) {
        if (indexWriter != null) {
            try {
                indexWriter.rollback();
                indexWriter.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}
