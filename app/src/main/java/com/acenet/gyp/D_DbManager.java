package com.acenet.gyp;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by AceNet on 12/31/2015.
 */
public class D_DbManager extends B_Config {
    static final String NAMA_DB = "sqlite_gyp";
    static final String NAMA_TABEL_TANAMAN = "tanaman";
    static final String NAMA_TABEL_NOTIFIKASI= "notifikasi";
    static final int DB_VERSION = 1;

//    static final String ROW_ID = "id";
    static final String ROW_ID_COUNTER = "_id";
//    static final String ROW_NAMA = "nama_tanaman";
//    static final String ROW_JENIS = "jenis";
//    static final String ROW_DESKRIPSI = "deskripsi";

    static final String CREATE_TABLE = "create table " + NAMA_TABEL_NOTIFIKASI
            +" ("+TAG_ID +" integer PRIMARY KEY autoincrement, "+
                TAG_NAMA_TANAMAN+" text, "+TAG_JUDUL+" text, "+TAG_INTERVAL_HARI+" int, "+TAG_BERAPA_KALI+" int" +
            ")";
    static final String CREATE_TABLE_TANAMAN = "create table " + NAMA_TABEL_TANAMAN
            +" ("+TAG_ID +" text PRIMARY KEY , "+
            TAG_NAMA_TANAMAN+" text"+
            ")";
//    static final String CREATE_TABLE_NOTIFIKASI = "create table " + NAMA_TABEL_NOTIFIKASI
//            +" ("+ ROW_ID+" integer PRIMARY KEY autoincrement, " +
//            ROW_NAMA+" text, "+ROW_JENIS+" text, "+ROW_DESKRIPSI+" text)";

    private final Context context;
    private DatabaseOpenHelper dbHelper;
    private SQLiteDatabase db;

    public D_DbManager(Context context){
        this.context = context;
        dbHelper = new DatabaseOpenHelper(context);
        db = dbHelper.getWritableDatabase();
    }
    private static class DatabaseOpenHelper extends SQLiteOpenHelper{
        public DatabaseOpenHelper(Context context){
            super(context, NAMA_DB, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE); //db.execSQL(D_DbManager.init_data);
            db.execSQL(CREATE_TABLE_TANAMAN);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXIST " + NAMA_DB);
            onCreate(db);
        }
    }

    public void close(){dbHelper.close();}

    public  void addTanaman(String nama){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_ID, nama.replace(" ","_"));
        contentValues.put(TAG_NAMA_TANAMAN, nama);
        try {
            db.insert(NAMA_TABEL_TANAMAN, null, contentValues);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DB ERROR", e.toString());
//            Toast.makeText(context, "DB ERROR" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public void addRow(String nama, String judul, int interval, int berapa_kali){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TAG_NAMA_TANAMAN, nama);
        contentValues.put(TAG_JUDUL, judul);
        contentValues.put(TAG_INTERVAL_HARI, interval);
        contentValues.put(TAG_BERAPA_KALI, berapa_kali);
        try {
            db.insert(NAMA_TABEL_NOTIFIKASI, null, contentValues);
        }catch (Exception e){
            e.printStackTrace();
            Log.e("DB ERROR", e.toString());
            Toast.makeText(context, "DB ERROR" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    public ArrayList<ArrayList<Object>> ambilTanaman(){
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cursor;
        try {
            cursor=db.query(
                    NAMA_TABEL_TANAMAN,
                    new String[]{ TAG_NAMA_TANAMAN},
                    null, null,
                    null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()){
                do {
                    ArrayList<Object> dataList =new ArrayList<Object>();
                    dataList.add(cursor.getString(0));
                    Log.e("Tanaman", "" + cursor.getString(0));
                    dataArray.add(dataList);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Ambil-Semua-Tanaman ERROR:"+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return dataArray;
    }
    public ArrayList<ArrayList<Object>> ambilTanamanNotifikasi(String nama_){
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cursor;
        try {
            cursor=db.query(
                    NAMA_TABEL_NOTIFIKASI,
                    new String[]{ TAG_NAMA_TANAMAN, TAG_JUDUL, TAG_INTERVAL_HARI, TAG_BERAPA_KALI, TAG_ID},
                    null, null,
                    null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()){
                do {
                    if (nama_.equals(cursor.getString(0))){
                        ArrayList<Object> dataList = new ArrayList<Object>();
                        dataList.add(cursor.getString(0));
                        dataList.add(cursor.getString(1));
                        dataList.add(cursor.getInt(2) + "");
                        dataList.add(cursor.getInt(3) + "");
                        dataList.add(cursor.getInt(4) + "");
                        Log.e("SuksesAmbil", cursor.getString(1) + " " + cursor.getInt(2));

                        dataArray.add(dataList);
                    }
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Ambil-Semua-Data-Notif ERROR: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return dataArray;
    }


    public ArrayList<ArrayList<Object>> ambilSemuaDataNotifikasi(){
        ArrayList<ArrayList<Object>> dataArray = new ArrayList<ArrayList<Object>>();
        Cursor cursor;
        try {
            cursor=db.query(
                    NAMA_TABEL_NOTIFIKASI,
                    new String[]{TAG_ID, TAG_NAMA_TANAMAN, TAG_JUDUL, TAG_INTERVAL_HARI, TAG_BERAPA_KALI},
                    null, null,
                    null, null, null);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()){
                do {
                    ArrayList<Object> dataList =new ArrayList<Object>();
                    dataList.add(cursor.getLong(0));
                    dataList.add(cursor.getString(1));
                    dataList.add(cursor.getString(2));
                    dataList.add(cursor.getInt(3));
                    dataList.add(cursor.getInt(4));

                    dataArray.add(dataList);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "Ambil-Semua-Data-Notif ERROR: "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return dataArray;
    }

    ///---------------------------GAK DIPAKAI -------------------////

    public boolean checkIsDataAlreadyIn(String nama, String judul){
        String Query = "SELECT * FROM "+NAMA_TABEL_NOTIFIKASI +" WHERE "+TAG_NAMA_TANAMAN + " = '"+nama+"' AND "+ TAG_JUDUL +" ='"+judul+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount()<=0){
            return false;
        }
        return true;
    }
    public ArrayList<Object> ambilBaris(String nama_tanaman){
        ArrayList<Object> arrayList = new ArrayList<Object>();
//        SQLiteDatabase sqldb = this.sqli
        Cursor cursor;
        try {
            cursor = db.query(NAMA_TABEL_NOTIFIKASI, new String[]{
                             TAG_NAMA_TANAMAN}, TAG_NAMA_TANAMAN+ "='"+nama_tanaman+"'", null, null, null,
                    null, null
            );
            cursor.moveToFirst();
            if (!cursor.isAfterLast()){
                do {
                    arrayList.add(cursor.getString(0)); //ID
                    Log.e("Isi Debe",""+cursor.getString(0));
                }while (cursor.moveToNext());
                String r = String.valueOf(arrayList);
                Log.e("haha ",r);
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("hihi ",e.toString());
            Toast.makeText(context, "hihi "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        return arrayList;
    }

    /*
    public void updateBaris(long rowId, String nama, String jenis, String deskripsi){
        ContentValues cv = new ContentValues();
        cv.put(ROW_NAMA, nama);
        cv.put(ROW_JENIS, jenis);
        cv.put(ROW_DESKRIPSI, deskripsi);
        try {
            db.update(NAMA_TABEL_TANAMAN, cv, ROW_ID + "=" + rowId, null);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "UPDATE ERROR: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void deleteBaris(long id){
        try {
            db.delete(NAMA_TABEL_TANAMAN, ROW_ID + "=" + id, null);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, "DeLeTe ERROR: "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }
*/
    public long getJumlahTanaman(){
        long numRows = DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+NAMA_TABEL_NOTIFIKASI, null);
        return numRows;
    }
}

