package com.example.mohammed.ScanNSav;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Notes.db";
    public static final String Note_TABLE_NAME = "save_notes";

    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_NAME = "name";

    public static final String COlUMN_PRICE = "price";
    public static final String COLUMN_IMAGE = "img";
   // public static final String CONTACTS_COLUMN_CITY = "place";
   // public static final String CONTACTS_COLUMN_PHONE = "phone";
    private HashMap hp;
    Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table save_notes " +
                        "(barcode text,name text,price text ,img text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS save_notes");

        onCreate(db);
    }

    public boolean deleteNote (String sub) throws SQLiteException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Note_TABLE_NAME,"subject ='"+sub+"'",null) > 0;
    }
    public boolean insertNote (String barcode, String name, String price, String img) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BARCODE, barcode);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COlUMN_PRICE, price);
        contentValues.put(COLUMN_IMAGE, img);
        db.insert(Note_TABLE_NAME, null, contentValues);
        return true;
    }
    public void delAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note_TABLE_NAME, null, null);
    }

    public Cursor getNoteData(String sub) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from save_notes where TRIM(subject)= '"+sub.trim()+"'", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Note_TABLE_NAME);
        return numRows;
    }



    /*public boolean updateData (String sub, String note, String created, String old) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_Subject, sub);
        contentValues.put(COLUMN_Notes, note);
        contentValues.put(COlUMN_Created, created);

        db.update("save_notes", contentValues, "subject='"+old+"'",null);
        return true;
    }
   */
    public Integer deleteData () {
        SQLiteDatabase db = this.getWritableDatabase();
        int data1,data2;
         data1=db.delete("save_notes","1", null);
         data2=db.delete("save_images","1", null);
        if(data1>0 && data2>0)
        {
            return 1;
        }
        else
            return 0;
    }

    public ArrayList<savedItem> getAllSubject() {
        ArrayList<savedItem> array_list = new ArrayList<savedItem>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from save_notes", null );
        res.moveToFirst();

        savedItem item=new savedItem();

        while(res.isAfterLast() == false){
            item.setBarcode(res.getString(res.getColumnIndex(COLUMN_BARCODE)));
            item.setName(res.getString(res.getColumnIndex(COLUMN_NAME)));
            item.setPrice(res.getString(res.getColumnIndex(COlUMN_PRICE)));
            item.setStore(res.getString(res.getColumnIndex(COLUMN_IMAGE)));
            array_list.add(item);
            res.moveToNext();
        }
        return array_list;
    }

    public boolean ValidateRecord(String code)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from save_notes Where barcode ="+code, null );
        res.moveToFirst();

        if(res.getCount() <= 0){
            res.close();
            return false;
        }

        res.close();
        return true;
    }

}