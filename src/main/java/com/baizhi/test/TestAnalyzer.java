package com.baizhi.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.io.StringReader;

public class TestAnalyzer {
    public String text="如果那天你迷路了，记得站在最高的地方等我，我来找你";

    /*
    * 单字分词器
    * */

    @Test
    public void testStandardAndlyzar()throws IOException{
        test(new StandardAnalyzer(Version.LUCENE_44),text);
        System.out.println(StandardAnalyzer.STOP_WORDS_SET);
    }

    @Test
    public void testChineseAnalyzer() throws IOException{
        test(new ChineseAnalyzer(),text);
    }
    
    /*
    * 中日韩   分词器 二分分词
    * */

    @Test
    public void testCJKAnalyzer() throws IOException{
        test(new CJKAnalyzer(Version.LUCENE_44),text);
    }

    @Test
    public void testSmartChineseAnalyzer() throws IOException {
        test(new SmartChineseAnalyzer(Version.LUCENE_44),text);
    }
    
    /*
    * 剖丁解牛  设置 关键字  设置 停用词 
    * */

    @Test
    public void testIKAnalyzer() throws IOException {
        test(new IKAnalyzer(),text);
    }



    /*
     * 此方法就是用来验证 分词器的分次规则
     *
     * */
    public static void test(Analyzer analyzer, String text) throws IOException {

        System.out.println("当前分词器:--->" + analyzer.getClass().getName());

        TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(text));

        tokenStream.addAttribute(CharTermAttribute.class);

        tokenStream.reset();
        while (tokenStream.incrementToken()) {
            CharTermAttribute attribute = tokenStream.getAttribute(CharTermAttribute.class);
            System.out.println(attribute.toString());
        }

        tokenStream.end();
    }
}
