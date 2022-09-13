package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDB extends SQLiteOpenHelper {

    public static final String DB_NAME="TEST_DB1.db";//this .db is must
    public static final String DB_FILE_NAME="TEST_FILE_NAME";
    public static final String COL1="ID";
    public static final String COL2="NAME";
    public static final String COL3="EMAIL";
    MyDB(Context context)
    {
        super(context,DB_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+DB_FILE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT ,NAME TEXT,EMAIL TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+DB_FILE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(String name,String email)
    {
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL2,name);
        contentValues.put(COL3,email);

        long res=sqLiteDatabase.insert(DB_FILE_NAME,null,contentValues);
        if (res==-1){

            return  false;

        }else{
            return true;
        }
    }

    public Cursor readData()
    {


        SQLiteDatabase sd= getWritableDatabase();
        String qry="select * from "+DB_FILE_NAME+" order by ?";
        return  sd.rawQuery(qry,new String[]{COL1});


    }

    public  void deleteColomByName(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_FILE_NAME, "NAME=?", new String[]{name});
        db.close();

    }

    void delColByid(int id)
    {
        String idstr=Integer.toString(id);
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        sqLiteDatabase.delete(DB_FILE_NAME,"ID=?",new String[]{idstr});
    }


    public void  deleteAll(Context context)
    {
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
         sqLiteDatabase.execSQL("DELETE FROM "+DB_FILE_NAME);
         sqLiteDatabase.close();
    }


    public int  chkSamedata(String data, String colName, MainActivity mainActivity)
    {
        int res=0;
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String str="no same email found";
        cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DB_FILE_NAME+" WHERE "+colName+" = ? ",new String[]{data});


       while (cursor.moveToNext())
        {
            int index=cursor.getColumnIndex(colName);
            str=cursor.getString(index);
            Toast.makeText(mainActivity, str, Toast.LENGTH_SHORT).show();
        }

        if (str.equals(data))
        {
            res=1;

        }
        return res;
    }
}
