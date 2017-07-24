package com.ariondan.vendor.model;

import android.content.Context;
import android.database.Cursor;

import com.ariondan.vendor.database.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.ariondan.vendor.database.DatabaseContract.CURSOR_CUSTOMER;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_ID;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PICTURE;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PRICE;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_PRODUCT;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_QUANTITY;
import static com.ariondan.vendor.database.DatabaseContract.CURSOR_TIME;

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
    private Context context;

    public HistoryModel(Context context) {
        setContext(context);
        database = new DatabaseHelper(context);
    }

    public HistoryModel(Context context, String picture, String product, double price, int quantity, String customer, Date time) {
        setContext(context);
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

    public HistoryModel(Context context, int id, String picture, String product, double price, int quantity, String customer, Date time) {
        setContext(context);
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
                try {
                    historyModelList.add(getModelForCursor(cursor));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyModelList;
    }

    public List<HistoryModel> getHistoryForSearch(String searchField) {
        List<HistoryModel> historyModels = new ArrayList<>();
        Cursor cursor = database.getHistoryForSearch(searchField);
        if (cursor.moveToFirst()) {
            do {
                try {
                    historyModels.add(getModelForCursor(cursor));
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return historyModels;
    }

    private HistoryModel getModelForCursor(Cursor cursor) throws ParseException {
        return new HistoryModel(cursor.getInt(CURSOR_ID),
                cursor.getString(CURSOR_PICTURE),
                cursor.getString(CURSOR_PRODUCT),
                Double.parseDouble(cursor.getString(CURSOR_PRICE)),
                cursor.getInt(CURSOR_QUANTITY),
                cursor.getString(CURSOR_CUSTOMER),
                new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").parse(cursor.getString(CURSOR_TIME)));
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getTimeAsString() {
        String rawDate = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(getTime());
        return rawDate.replaceAll("/", ".").replaceAll("-", "\n");
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

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
