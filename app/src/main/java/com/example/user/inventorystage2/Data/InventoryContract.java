package com.example.user.inventorystage2.Data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class InventoryContract {


    public static final String CONTENT_AUTHORITY = "com.example.user.inventorystage2";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_PRODUCTS = "products";


    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {
    }

    public static final class ProductEntry implements BaseColumns {

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        //Type: INTEGER  contant _ID already included in BaseColumns as primary key

        //Type: TEXT
        public final static String TABLE_NAME = "products";

        //Type: TEXT
        public final static String PRODUCT_NAME = "productName";

        //Type: REAL
        public final static String PRICE = "Price";

        //Type: INTEGER
        public final static String QUANTITY = "quantity";

        //Type: TEXT
        public final static String SUPPLIER_NAME = "supplierName";

        //Type: TEXT
        public final static String SUPPLIER_PHONE_NUMBER = "supplierPhoneNumber";

    }
}

