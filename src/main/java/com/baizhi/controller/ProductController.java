package com.baizhi.controller;

import com.baizhi.dao.ProductDao;
import com.baizhi.entity.Product;
import com.baizhi.util.UploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("product")
public class ProductController {
    @Autowired
    private ProductDao productDao;
    @ResponseBody
    @RequestMapping("query")
    public List<Product> query(String param){
        System.out.println(param);
        List<Product> products = productDao.searcheIndex(param);
        for (Product product : products) {
            System.out.println(product);
        }
        return products;
    }
    @RequestMapping("save")
    public String save(Product product, MultipartFile imgPath, HttpServletRequest request){
        product.setId(UUID.randomUUID().toString());
        String img = UploadUtil.upload(imgPath, request, "img");
        System.out.println(img+"========");
        product.setImg(img);
        System.out.println(product);
        productDao.addIndex(product);
        return "index";
    }
    @RequestMapping("update")
    public String update(Product product){
        productDao.updateIndex(product);
        return "index";
    }
    @RequestMapping("delete")
    public String delete(String id){
        productDao.deleteIndex(id);
        return "index";
    }
}
