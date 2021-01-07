package util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "grocery";
    private static final int DB_VERSION = 1;
    int incremt = 1;
    private SQLiteDatabase db;

    public static final String CART_TABLE = "cart";

    public static final String COLUMN_Primary = "primary_key";
    public static final String COLUMN_ID = "product_id";
    public static final String COLUMN_QTY = "qty";
    public static final String COLUMN_IMAGE = "product_image";
    public static final String COLUMN_CAT_ID = "category_id";
    public static final String COLUMN_NAME = "product_name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_REWARDS = "rewards";
    public static final String COLUMN_UNIT_VALUE = "unit_value";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_INCREAMENT = "increament";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STORE_ID = "store_id";
    public static final String COLUMN_Size = "size";
    public static final String COLUMN_Color = "color";

    Context context;
    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        String exe = "CREATE TABLE IF NOT EXISTS " + CART_TABLE
                + "(" + COLUMN_Primary + " integer primary key autoincrement, "
                + COLUMN_ID + " TEXT NOT NULL, "
                + COLUMN_QTY + " DOUBLE NOT NULL,"
                + COLUMN_IMAGE + " TEXT NOT NULL, "
                + COLUMN_CAT_ID + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PRICE + " DOUBLE NOT NULL, "
                + COLUMN_REWARDS + " DOUBLE NOT NULL, "
                + COLUMN_UNIT_VALUE + " DOUBLE NOT NULL, "
                + COLUMN_UNIT + " TEXT NOT NULL, "
                + COLUMN_INCREAMENT + " DOUBLE NOT NULL, "
                + COLUMN_STOCK + " DOUBLE NOT NULL, "
                + COLUMN_TITLE + " TEXT NOT NULL ,"
                + COLUMN_STORE_ID + " TEXT NOT NULL ,"
                + COLUMN_Color + " TEXT ,"
                + COLUMN_Size + " TEXT "
                + ")";

        db.execSQL(exe);

    }

    public boolean setCart(HashMap<String, String> map, Float Qty) {
        db = getWritableDatabase();

        if (isInCart(map.get(COLUMN_Primary))) {
            db.execSQL("update " + CART_TABLE + " set " + COLUMN_QTY + " = '" + Qty + "' " + "where " + COLUMN_Primary + "=" + map.get(COLUMN_Primary));
//            db.execSQL("update " + CART_TABLE + " set " + COLUMN_Color + " = '" + map.get(COLUMN_Color) + "' " + "where " + COLUMN_ID + "=" + map.get(COLUMN_ID));
//            db.execSQL("update " + CART_TABLE + " set " + COLUMN_Size + " = '" + map.get(COLUMN_Size) + "' " + "where " + COLUMN_ID + "=" + map.get(COLUMN_ID));
//            db.execSQL("update " + CART_TABLE + " set " + COLUMN_PRICE + " = '" + map.get(COLUMN_PRICE) + "' " + "where " + COLUMN_ID + "=" + map.get(COLUMN_ID));
//            db.execSQL("update " + CART_TABLE + " set " + COLUMN_UNIT_VALUE + " = '" + map.get(COLUMN_UNIT_VALUE) + "' " + "where " + COLUMN_ID + "=" + map.get(COLUMN_ID));

            return false;
        }
        else {

            ContentValues values = new ContentValues();
            //values.put(COLUMN_Primary, incremt);
            values.put(COLUMN_ID, map.get(COLUMN_ID));
            values.put(COLUMN_QTY, Qty);
            values.put(COLUMN_CAT_ID, map.get(COLUMN_CAT_ID));
            values.put(COLUMN_IMAGE, map.get(COLUMN_IMAGE));
            values.put(COLUMN_INCREAMENT, map.get(COLUMN_INCREAMENT));
            values.put(COLUMN_NAME, map.get(COLUMN_NAME));
            values.put(COLUMN_PRICE, map.get(COLUMN_PRICE));
            values.put(COLUMN_REWARDS, map.get(COLUMN_REWARDS));
            values.put(COLUMN_STOCK, map.get(COLUMN_STOCK));
            values.put(COLUMN_TITLE, map.get(COLUMN_TITLE));
            values.put(COLUMN_UNIT, map.get(COLUMN_UNIT));
            values.put(COLUMN_UNIT_VALUE, map.get(COLUMN_UNIT_VALUE));
            values.put(COLUMN_STORE_ID, map.get(COLUMN_STORE_ID));
            values.put(COLUMN_Size, map.get(COLUMN_Size));
            values.put(COLUMN_Color, map.get(COLUMN_Color));

            db.insert(CART_TABLE, null, values);

            return true;
        }
    }

    public boolean isInCart(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_Primary + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;

        return false;
    }

    public String getCartItemQty(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_Primary + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
    }
    public String getCartItemSize(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_Size));
    }
    public String getCartItemColor(String id) {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_ID + " = " + id;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(COLUMN_Color));
    }



    public String getInCartItemQty(String id) {
        if (isInCart(id)) {
            db = getReadableDatabase();
            String qry = "Select *  from " + CART_TABLE + " where " + COLUMN_Primary + " = " + id;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(COLUMN_QTY));
        } else {
            return "0.0";
        }
    }

    public int getCartCount() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        return cursor.getCount();
    }

    public String getTotalAmount() {
        db = getReadableDatabase();
        String qry = "Select SUM(" + COLUMN_QTY + " * " + COLUMN_PRICE + ") as total_amount  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String total = cursor.getString(cursor.getColumnIndex("total_amount"));
        if (total != null) {
            return total;
        } else {
            return "0";
        }
    }

    public ArrayList<HashMap<String, String>> getCartAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(COLUMN_Primary, cursor.getString(cursor.getColumnIndex(COLUMN_Primary)));
            map.put(COLUMN_ID, cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
            map.put(COLUMN_QTY, cursor.getString(cursor.getColumnIndex(COLUMN_QTY)));
            map.put(COLUMN_IMAGE, cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
            map.put(COLUMN_CAT_ID, cursor.getString(cursor.getColumnIndex(COLUMN_CAT_ID)));
            map.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
            map.put(COLUMN_PRICE, cursor.getString(cursor.getColumnIndex(COLUMN_PRICE)));
            map.put(COLUMN_REWARDS, cursor.getString(cursor.getColumnIndex(COLUMN_REWARDS)));
            map.put(COLUMN_UNIT_VALUE, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT_VALUE)));
            map.put(COLUMN_UNIT, cursor.getString(cursor.getColumnIndex(COLUMN_UNIT)));
            map.put(COLUMN_INCREAMENT, cursor.getString(cursor.getColumnIndex(COLUMN_INCREAMENT)));
            map.put(COLUMN_STOCK, cursor.getString(cursor.getColumnIndex(COLUMN_STOCK)));
            map.put(COLUMN_TITLE, cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            map.put(COLUMN_STORE_ID, cursor.getString(cursor.getColumnIndex(COLUMN_STORE_ID)));

            map.put(COLUMN_Size, cursor.getString(cursor.getColumnIndex(COLUMN_Size)));
            map.put(COLUMN_Color, cursor.getString(cursor.getColumnIndex(COLUMN_Color)));

            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

    public String getColumnRewards() {
        db = getReadableDatabase();
        String qry = "SELECT rewards FROM "+ CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String reward = cursor.getString(cursor.getColumnIndex("rewards"));
        if (reward != null) {

            return reward;
        } else {
            return "0";
        }
    }

    public ArrayList<String> getColumnStoreId() {
        ArrayList<String> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put(cursor.getString(cursor.getColumnIndex(COLUMN_STORE_ID)));
            list.add(cursor.getString(cursor.getColumnIndex(COLUMN_STORE_ID)));
            cursor.moveToNext();
        }
        return list;
    }

    public String getFavConcatString() {
        db = getReadableDatabase();
        String qry = "Select *  from " + CART_TABLE;
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        String concate = "";
        for (int i = 0; i < cursor.getCount(); i++) {
            if (concate.equalsIgnoreCase("")) {
                concate = cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            } else {
                concate = concate + "_" + cursor.getString(cursor.getColumnIndex(COLUMN_ID));
            }
            cursor.moveToNext();
        }
        return concate;
    }

    public void clearCart() {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE);
    }

    public void removeItemFromCart(String id) {
        db = getReadableDatabase();
        db.execSQL("delete from " + CART_TABLE + " where " + COLUMN_Primary + " = " + id);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db = getWritableDatabase();
//        String upgradeQuery = "ALTER TABLE CART_TABLE ADD COLUMN COLUMN_Size TEXT";
//        String upgradeQuery2 = "ALTER TABLE CART_TABLE ADD COLUMN COLUMN_Color TEXT";
//
//        // if (oldVersion == 1 && newVersion == 2)
//            db.execSQL(upgradeQuery);
//            db.execSQL(upgradeQuery2);
    }

}
