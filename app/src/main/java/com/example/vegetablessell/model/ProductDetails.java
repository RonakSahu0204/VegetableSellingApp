package com.example.vegetablessell.model;

public class ProductDetails {

    private String productName;
    private int price;
    private int discount;
    private int quantity;
    private String description;
    private String sellerShopName;
    private String sellerEmail;
    private String imageUrl;
    private String id;

    public ProductDetails() {}

    public ProductDetails(String id, String productName, int price, int discount, int quantity, String description, String sellerShopName, String sellerEmail, String imageUrl) {
        this.productName = productName;
        this.price = price;
        this.discount = discount;
        this.quantity = quantity;
        this.description = description;
        this.sellerShopName = sellerShopName;
        this.sellerEmail = sellerEmail;
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSellerShopName() {
        return sellerShopName;
    }

    public void setSellerShopName(String sellerShopName) {
        this.sellerShopName = sellerShopName;
    }

    public String getSellerEmail() {
        return sellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        this.sellerEmail = sellerEmail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ProductDetails{" +
                "productName='" + productName + '\'' +
                ", price=" + price +
                ", discount=" + discount +
                ", quantity=" + quantity +
                ", description='" + description + '\'' +
                ", sellerShopName='" + sellerShopName + '\'' +
                ", sellerEmail='" + sellerEmail + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
