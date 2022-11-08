package com.asm.productmanager;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {
    //Seri..  lưu trữ và chuyển đổi trạng thái của 1 đối tượng (Object) vào 1 byte stream sao cho byte stream này
    // có thể chuyển đổi ngược trở lại thành một Object.
    private int id;
    private String name;
    private String provider;
    private int price;
    private String type;
    private int quantity;

    public Product(int id, String name, String provider, int price, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.price = price;
        this.type = type;
        this.quantity = quantity;
    }
    public Product() {

    }


    public int getId() {
        return id;
    } public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    } public void setName(String name) {
        this.name = name;
    }
    public String getProvider() {
        return provider;
    } public void setProvider(String provider) {
        this.provider = provider;
    }
    public int getPrice() {
        return price;
    } public void setPrice(int price) {
        this.price = price;
    }
    public String getType() {
        return type;
    } public void setType(String type) {
        this.type = type;
    }
    public int getQuantity() {
        return quantity;
    } public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    @Override
    public String toString(){
        return id+", "+name+", "+provider+", "+price+", "+type+", "+quantity;
    }
}


