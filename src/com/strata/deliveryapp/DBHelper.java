package com.strata.deliveryapp;

import java.util.ArrayList;

import com.strata.deliveryapp.arrays.LockedArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	   public static final String DATABASE_NAME = "DeliveryAppDB.db";
	   public static final String[] TABLE_NAME = {"MemberOrder","IndOrder"};
	   public static final String CONTACTS_COLUMN_ID = "id";
	   public static final String COLUMN_MEMBER_DATA = "MemberData";
	   public static final String COLUMN_SLOT = "Slot";
	   public static final String COLUMN_MEMBER_NO = "MembershipNo";
	   public static final String COLUMN_ORDER = "OrderData";
	   public static final String COLUMN_TYPE ="Type";
	   public static final String COLUMN_SELECTED ="Selected";

	   public DBHelper(Context context)
	   {
	      super(context, DATABASE_NAME , null, 2);
	   }

	   @Override
	   public void onCreate(SQLiteDatabase db) {
	      db.execSQL("create table "+TABLE_NAME[0]+"(id integer primary key autoincrement, "+COLUMN_MEMBER_DATA+" text, "+COLUMN_SLOT+" text)");
	      db.execSQL("create table "+TABLE_NAME[1]+"(id integer primary key autoincrement, "+COLUMN_ORDER+" text, "+COLUMN_MEMBER_NO+" text, "+COLUMN_TYPE+" text, "+COLUMN_SELECTED+" text DEFAULT \'false\')");}

	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME[0]);
	      db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME[1]);
	      onCreate(db);
	   }
	   public void deleteAllData (){
		   SQLiteDatabase db = this.getWritableDatabase();
		   db.delete(TABLE_NAME[0], null, null);
		   db.delete(TABLE_NAME[1], null, null);
	   }
	   public void insertAllData (String[] member_data, String slot){
		   SQLiteDatabase db = this.getWritableDatabase();
		   int count_m = member_data.length;
		   for(int i=0; i < count_m; i++){
		      ContentValues contentValues = new ContentValues();
		      contentValues.put(COLUMN_MEMBER_DATA, member_data[i]);
		      contentValues.put(COLUMN_SLOT, slot);
		      db.insert(TABLE_NAME[0], null, contentValues);
		   }
	   }
	   
	   public void insertAllOrder (String[] order, String membership_no, String type){
		   SQLiteDatabase db = this.getWritableDatabase();
		   
		   int count_o = order.length;
		   for(int i=0; i < count_o; i++){
		      ContentValues contentValues = new ContentValues();
		      contentValues.put(COLUMN_ORDER, order[i]);
		      contentValues.put(COLUMN_MEMBER_NO, membership_no);
		      contentValues.put(COLUMN_TYPE, type);
		      db.insert(TABLE_NAME[1], null, contentValues);
		   }
		   
	   }
	   
	   public ArrayList<String> getAllData(String slot)
	   {
	      ArrayList<String> array_list = new ArrayList<String>();
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME[0]+" where "+COLUMN_SLOT+"="+slot, null );
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
		      array_list.add(res.getString(res.getColumnIndex(COLUMN_MEMBER_DATA)));
		      res.moveToNext();
	      }
	      return array_list;
	   }
	   
	   public ArrayList<String> getAllOrder(String membership_no, String type)
	   {
	      ArrayList<String> array_list = new ArrayList<String>();
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "SELECT * FROM "+TABLE_NAME[1]+" WHERE "+COLUMN_MEMBER_NO+"=? AND "+COLUMN_TYPE+"=?",new String[] {membership_no, type});
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
		      array_list.add(res.getString(res.getColumnIndex(COLUMN_ORDER)));
		      res.moveToNext();
	      }
	      return array_list;
	   }
	   
	   public ArrayList<LockedArray> getAllSelected(String membership_no, String type){
	      ArrayList<LockedArray> array_list = new ArrayList<LockedArray>();
	      //hp = new HashMap();
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "SELECT * FROM "+TABLE_NAME[1]+" WHERE "+COLUMN_MEMBER_NO+"=? AND "+COLUMN_TYPE+"=?",new String[] {membership_no, type});
	      res.moveToFirst();
	      while(res.isAfterLast() == false){
	    	  LockedArray array_single_item = new LockedArray();
	    	  array_single_item.setSelect(res.getString(res.getColumnIndex(COLUMN_SELECTED)));
	    	  array_single_item.setId(res.getInt(res.getColumnIndex("id")));
	    	  array_list.add(array_single_item);
		      res.moveToNext();
	      }
	      return array_list;
	   }
	   
	   public boolean setNewSelected (int id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put(COLUMN_SELECTED, "true");
	      db.update(TABLE_NAME[1], contentValues, "id = ? ", new String[] { Integer.toString(id) } );
	      return true;
	   }
	   
	   public Cursor getData(int id , int tab){
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+TABLE_NAME[tab]+" where id="+id+"", null );
	      return res;
	   }
	   public int numberOfRows( int tab){
	      SQLiteDatabase db = this.getReadableDatabase();
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME[tab]);
	      return numRows;
	   }
	   public boolean updateContact (Integer id, String data, int tab)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put(COLUMN_MEMBER_DATA, data);
	      db.update(TABLE_NAME[tab], contentValues, "id = ? ", new String[] { Integer.toString(id) } );
	      return true;
	   }

	   public Integer deleteContact (Integer id , int tab)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete(TABLE_NAME[tab], "id = ? ", new String[] { Integer.toString(id) });
	   }
	   
	}

