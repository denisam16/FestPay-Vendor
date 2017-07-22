package com.ariondan.vendor.model;

import android.content.Context;
import android.database.Cursor;

import com.ariondan.vendor.database.DatabaseContract;
import com.ariondan.vendor.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ariondan.vendor.database.DatabaseContract.CURSOR_ID;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PICTURE;

/**
 * Created by Akitektuo on 22.07.2017.
 */

public class HistoryModel {

    //TODO: Insert history when object is created AND make a method getEntireHistory() which returns a List<History>

    private int id;
    private String picture;
    private String product;
    private double price;
    private int quantity;
    private String customer;
    private Date time;

    private DatabaseHelper database;

    public HistoryModel(Context context, String picture, String product, double price, int quantity, String customer, Date time) {
        database = new DatabaseHelper(context);
        database.addHistory(picture, product, price, quantity, customer, time);
    }

    public HistoryModel(int id, String picture, String product, double price, int quantity, String customer, Date time) {
        setId(id);
        setPicture(picture);
        setProduct(product);
        setPrice(price);
        setQuantity(quantity);
        setCustomer(customer);
        setTime(time);
    }

    public List<HistoryModel> getEntireHistory() {
        List<HistoryModel> historyModelList = new ArrayList<>();
        Cursor cursor = database.getHistory();
        if (cursor.moveToFirst()) {
            do {
//                historyModelList.add(new HistoryModel(cursor.getInt(CURSOR_ID), cursor.getString(CURSOR_PICTURE), cursor.getString()));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyModelList;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
