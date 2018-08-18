package com.example.user.inventorystage2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InventoryDBHelper extends SQLiteOpenHelper {

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "inventory.db";

    private static final int DATABASE_VERSION = 1;

    public InventoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRODUCTS_TABLE = "CREATE TABLE " + InventoryContract.ProductEntry.TABLE_NAME + " ("
                + InventoryContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryContract.ProductEntry.PRODUCT_NAME + " TEXT NOT NULL, "
                + InventoryContract.ProductEntry.PRICE + " REAL DEFAULT 0, "
                + InventoryContract.ProductEntry.QUANTITY + " INTEGER NOT NULL DEFAULT 0, "
                + InventoryContract.ProductEntry.SUPPLIER_NAME + " TEXT, "
                + InventoryContract.ProductEntry.SUPPLIER_PHONE_NUMBER+ " TEXT)";

        db.execSQL(SQL_CREATE_PRODUCTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
