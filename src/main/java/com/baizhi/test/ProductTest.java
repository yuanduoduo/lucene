package com.baizhi.test;

import com.baizhi.dao.ProductDao;
import com.baizhi.entity.Product;
import org.junit.Test;

import java.util.List;

public class ProductTest {
    @Test
    public void test1(){
        ProductDao productDao = new ProductDao();
        List<Product> q = productDao.searcheIndex("袁");
        for (Product product : q) {
            System.out.println(product);
        }
    }
}
