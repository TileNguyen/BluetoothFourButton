package com.example.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: CP9
 * Date: 6/1/14
 * Time: 12:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MyDatabases extends SQLiteOpenHelper {



    /* ==============================VARIABLE============================================ */

    // Database version.
    private static final int DATABASE_VERSION = 1;

    // Database Name.
    private static final String DATABASE_NAME = "four_button";

    /*----------------------------- TABLE NAME ---------------------------------------- */
	private static final String TABLE_NAME_PASSWORD = "password";
    private static final String TABLE_NAME_BUTTON_INFO = "button_info";

/*--------------------------- END TABLE NAME ---------------------------------------- */



    // Common column names table password
	private static final String _ID_PASSWORD = "id";
	private static final String _VALUE = "pass";




    /** **************************** TABLE INFO ***************************** */

    private static final String _ID = "id";
    private static final String _NAME = "name";
    /** ************************** END TABLE INFO ***************************** */



    private static final String CREATE_TABLE_BUTTON_INFO ="CREATE TABLE "+TABLE_NAME_BUTTON_INFO
            +" (" + _ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+ _NAME + " TEXT" +")";

    private static final String CREATE_TABLE_PASSWORD ="CREATE TABLE "+TABLE_NAME_PASSWORD
            +" ("+_ID_PASSWORD+" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "+_VALUE+" TEXT"+")";


	/* ==============================END VARIABLE============================================ */




    public MyDatabases(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BUTTON_INFO);
        db.execSQL(CREATE_TABLE_PASSWORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BUTTON_INFO);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME_PASSWORD);
    }
    /**
     * get all name device on databases.
     * @return
     */
    public List<DeviceName> getAllName(){
        List<DeviceName> list = new ArrayList<DeviceName>();

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = " SELECT * FROM " + TABLE_NAME_BUTTON_INFO;

        try{
            /* Query all DeviceName */
            Cursor c = db.rawQuery(sql, null);
            if(c.moveToFirst()){
                do{
                    DeviceName fb = new DeviceName();
                    int i = c.getInt(0);
                    String str = c.getString(1);
                    fb.set_id(i);
                    fb.set_name(str);

                    list.add(fb);

                }while(c.moveToNext());
            }

        }catch(Exception e){
            Log.d("getOneItem",""+e.getMessage());
        }

        return list;
    }

    /**
     * Hàm trả về một {@link DeviceName} với tham số id.
     * @param id
     * @return
     */
    public DeviceName getOneItem(int id){
        DeviceName fb = new DeviceName();

        SQLiteDatabase db = this.getReadableDatabase();

        String sql = " SELECT * FROM "+TABLE_NAME_BUTTON_INFO+" WHERE "+_ID+"="+id;

        try{
            Cursor c = db.rawQuery(sql, null);
            if(c.moveToFirst()){
                fb.set_id(c.getInt(0));
                fb.set_name(c.getString(1));
            }
        }catch(Exception e){
             Log.d("getOneItem",""+e.getMessage());
        }

        return fb;
    }


    /**
     * Lưu mặc định 4 nút.
     */
    public void installDefault(){

        for(int i = 1; i < 5; i++){
            DeviceName fb = new DeviceName();

            fb.set_name("DEVICE " + i);

//            DeviceName(fb);
        }


    }



    public void installFourButton(DeviceName fb){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(_NAME, fb.get_name());

        db.insert(TABLE_NAME_BUTTON_INFO, null, values);

    }


    public void updateFourButton(DeviceName fb){

        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "UPDATE "+ TABLE_NAME_BUTTON_INFO + " SET "+_NAME+"='"+fb.get_name()+ "' WHERE "+_ID+"="+fb.get_id();


        ContentValues values = new ContentValues();

        values.put(_NAME, fb.get_name());

//        db.insert(TABLE_NAME_BUTTON_INFO, null, values);
//        db.update(TABLE_NAME_BUTTON_INFO, values, where, null);
        db.execSQL(sql);
    }














}
