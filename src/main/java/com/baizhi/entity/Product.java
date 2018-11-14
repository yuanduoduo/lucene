package com.baizhi.entity;

public class Product {
    private String id;
    private String name;
    private String price;
    private String desc;
    private String img;
    private String status;
    private String proDate;
    private String poace;

    public Product() {
        super();
    }

    public Product(String id, String name, String price, String desc, String img, String status, String proDate, String poace) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.img = img;
        this.status = status;
        this.proDate = proDate;
        this.poace = poace;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", desc='" + desc + '\'' +
                ", img='" + img + '\'' +
                ", status='" + status + '\'' +
                ", proDate='" + proDate + '\'' +
                ", poace='" + poace + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProDate() {
        return proDate;
    }

    public void setProDate(String proDate) {
        this.proDate = proDate;
    }

    public String getPoace() {
        return poace;
    }

    public void setPoace(String poace) {
        this.poace = poace;
    }
}
