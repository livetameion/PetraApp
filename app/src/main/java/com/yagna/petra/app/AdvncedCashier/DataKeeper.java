package com.yagna.petra.app.AdvncedCashier;

import com.yagna.petra.app.Model.Product.Products;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.List;

public  class DataKeeper {
    public static List<String> old_tagsArray = new ArrayList<>();
    public static String searchTag="";
    public static String recentTag="";
    public static List<String> product_id_selected = new ArrayList<>();
    public static List<String> product_name_selected = new ArrayList<>();
    public static ArrayList<Products.Product> products_array=new ArrayList<>();
    public static Products.Product detail_product;
    public static ArrayList<Products.Product> search_product_array =new ArrayList<>();
    public static ArrayList<Products.Product> all_products_array =new ArrayList<>();

}
