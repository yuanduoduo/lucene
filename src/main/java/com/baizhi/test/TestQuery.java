package com.baizhi.test;

import com.baizhi.util.LuceneUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.IOException;

public class TestQuery {

    @Test
    public void test1() {
        Query query = new TermQuery(new Term("content", "橘子"));
        testSearcherIndex(query);
    }

    @Test
    public void test2() throws ParseException {
        // 可以基于一句话进行查询    可以基于多个列进行查询
        String[] fields = {"title", "author", "content"};
        // 参数1：版本  参数2：基于哪些进行查询  参数3：检索索引需要的分词器
        MultiFieldQueryParser multiFieldQueryParser = new MultiFieldQueryParser(Version.LUCENE_44, fields, LuceneUtil.getAnalyzer());
        Query query = multiFieldQueryParser.parse("你在这里不动，我去买个橘子");
        testSearcherIndex(query);
    }

    /*
     *匹配所有文章(查所有)
     * */
    @Test
    public void test3() {
        Query query = new MatchAllDocsQuery();
        testSearcherIndex(query);
    }

    /*
    * 应用场景
    * 价格筛选   100-200
    * */
    @Test
    public void test4() {
        NumericRangeQuery<Integer> query = NumericRangeQuery.newIntRange("age", 1, 14, true, false);
        testSearcherIndex(query);
    }
    /*
    * 通配符查询
    * ?代表一个字符  * 代表多个字符
    * */
    @Test
    public void test5() {
        WildcardQuery author = new WildcardQuery(new Term("author", "朱*"));
        testSearcherIndex(author);
    }

    @Test
    public void test6() {
        BooleanQuery booleanClauses = new BooleanQuery();
        NumericRangeQuery<Integer> query=NumericRangeQuery.newIntRange("age",1,9,true,true);
        NumericRangeQuery<Integer> query1=NumericRangeQuery.newIntRange("age",2,5,true,true);
        booleanClauses.add(query, BooleanClause.Occur.SHOULD);
        booleanClauses.add(query1,BooleanClause.Occur.MUST_NOT);
        testSearcherIndex(booleanClauses);
    }

    /*
    * 模糊查询
    * */

    @Test
    public void test7() {
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term("content", "橘花"), 2);
        testSearcherIndex(fuzzyQuery);
    }

    /*
    * 添加内容
    * */
    @Test
    public void testCreteIndex() {
        IndexWriter indexWriter = LuceneUtil.getIndexWriter();
        for (int i = 15; i < 16; i++) {
            Document document = new Document();
            document.add(new StringField("id", String.valueOf(i), Field.Store.YES));
            document.add(new StringField("title", "背影" + i, Field.Store.YES));
            document.add(new StringField("author", "朱自清" + i, Field.Store.YES));
            document.add(new IntField("age", i, Field.Store.YES));
            //document.add(new TextField("content", "你在这里不动，我去买个橘子" + i, Field.Store.YES));
            TextField content=new TextField("content", "你在这里不动，我去买个橘子" + i, Field.Store.YES);
            content.setBoost(10F);
            document.add(content);
            try {
                indexWriter.addDocument(document);
            } catch (IOException e) {
                e.printStackTrace();
                LuceneUtil.rollback(indexWriter);
            }
        }
        LuceneUtil.commit(indexWriter);
    }

    public static void testSearcherIndex(Query query){
        //int page=1;//页数
        //int rows=5;//条数
        /*
        * 高亮
        * */
        //样式 给关键字设置的颜色
        Formatter formatter = new SimpleHTMLFormatter("<font color=red>","</font>");
        //关键字
        QueryScorer scorer = new QueryScorer(query);
        /*
        * 参数1：高亮成什么颜色
        *
        * 参数2：哪些词高亮
        * */
        Highlighter highlighter = new Highlighter(formatter, scorer);

        try {
            IndexSearcher indexSearcher = LuceneUtil.getIndexSearcher();
            TopDocs topDocs = indexSearcher.search(query, 100);  //page*rows
            //相关度的排序
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (int i = 0; i < scoreDocs.length; i++) {       //(page-1)*rows
                ScoreDoc scoreDoc = scoreDocs[i];
                int doc = scoreDoc.doc;
                float score = scoreDocs[i].score;
                //根据索引编号拿到document
                Document document = indexSearcher.doc(doc);
                String title = document.get("title");
                String author = document.get("author");
                String content = document.get("content");
                System.out.println(title);
                System.out.println(author);
                System.out.println(content);
                System.out.println(score);
                //应用高亮度
                /*String title1 = highlighter.getBestFragment(LuceneUtil.getAnalyzer(), "title", document.get("title"));
                if(title1==null){
                    System.out.println(title);
                }else{
                    System.out.println("高亮后的" + title1);
                }
                String author1= highlighter.getBestFragment(LuceneUtil.getAnalyzer(), "author", document.get("author"));
                if(author1==null){
                    System.out.println(author);
                }else{
                    System.out.println("高亮后的" + author1);
                }
                String content1=highlighter.getBestFragment(LuceneUtil.getAnalyzer(),"content",document.get("content"));
                if(content1==null){
                    System.out.println(content);
                }else{
                    System.out.println("高亮后的" + content1);
                }*/



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test                                                      
    public void test8() {                                      
        IndexWriter indexWriter = LuceneUtil.getIndexWriter(); 
        Term term = new Term("id", "15");                      
        try {                                                  
            indexWriter.deleteDocuments(term);                 
            LuceneUtil.commit(indexWriter);                    
        } catch (IOException e) {                              
            LuceneUtil.rollback(indexWriter);                  
            e.printStackTrace();                               
        }                                                      
    }                                                          
}
