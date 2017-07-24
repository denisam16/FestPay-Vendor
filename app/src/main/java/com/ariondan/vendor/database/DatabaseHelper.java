package com.ariondan.vendor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.ariondan.vendor.database.DatabaseContract.CURSOR_ID;

/**
 * Created by Akitektuo on 22.07.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_QUERY = "CREATE TABLE " + DatabaseContract.HistoryContractEntry.TABLE_NAME + " (" +
            DatabaseContract.HistoryContractEntry.COLUMN_ID + " NUMBER," +
            DatabaseContract.HistoryContractEntry.COLUMN_PICTURE + " TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT + " TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_PRICE + "TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY + "NUMBER," +
            DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER + " TEXT," +
            DatabaseContract.HistoryContractEntry.COLUMN_TIME + "TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addHistory(String picture, String product, double price, int quantity, String customer, Date time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_ID, getGeneratedId());
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_PICTURE, picture);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT, product);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_PRICE, String.valueOf(price));
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER, customer);
        contentValues.put(DatabaseContract.HistoryContractEntry.COLUMN_TIME, new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss").format(time));
        getWritableDatabase().insert(DatabaseContract.HistoryContractEntry.TABLE_NAME, null, contentValues);
    }

    public Cursor getHistory() {
        String[] list = {DatabaseContract.HistoryContractEntry.COLUMN_ID,
                DatabaseContract.HistoryContractEntry.COLUMN_PICTURE,
                DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT,
                DatabaseContract.HistoryContractEntry.COLUMN_PRICE,
                DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY,
                DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER,
                DatabaseContract.HistoryContractEntry.COLUMN_TIME};
        return getReadableDatabase().query(DatabaseContract.HistoryContractEntry.TABLE_NAME, list, null, null, null, null, null);
    }

    private int getGeneratedId() {
        int id = 0;
        Cursor cursor = getHistory();
        if (cursor.moveToLast()) {
            id = cursor.getInt(CURSOR_ID);
        }
        cursor.close();
        return ++id;
    }

    public Cursor getHistoryForSearch(String searchField) {
        String[] results = {DatabaseContract.HistoryContractEntry.COLUMN_ID,
                DatabaseContract.HistoryContractEntry.COLUMN_PICTURE,
                DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT,
                DatabaseContract.HistoryContractEntry.COLUMN_PRICE,
                DatabaseContract.HistoryContractEntry.COLUMN_QUANTITY,
                DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER,
                DatabaseContract.HistoryContractEntry.COLUMN_TIME};
        String selection = DatabaseContract.HistoryContractEntry.COLUMN_PRODUCT + " LIKE ? OR " +
                DatabaseContract.HistoryContractEntry.COLUMN_CUSTOMER + " LIKE ?";
        String[] selectionArgs = {"%" + searchField + "%", "%" + searchField + "%"};
        return getReadableDatabase().query(DatabaseContract.HistoryContractEntry.TABLE_NAME, results, selection, selectionArgs, null, null, null);
    }
}
