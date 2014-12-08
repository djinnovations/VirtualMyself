package com.dj.virtualmyself_1;

import java.util.HashSet;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MyDb extends SQLiteOpenHelper {
	
	static final String dbName="Myself";
	static final String create_query="CREATE TABLE Virtual(QuestionId INTEGER PRIMARY KEY,Question TEXT,Answer TEXT)"; 
	String exception;
	public MyDb(Context context){
		super(context,dbName,null,1);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(create_query);
		
		
	}
	
	public void writeDb(String query){
		
		SQLiteDatabase database = this.getWritableDatabase();
		database.execSQL(query);
	}
	
	public HashSet<String> readDbQues(String query){
		
			SQLiteDatabase database = this.getReadableDatabase();
			HashSet<String> question=new HashSet<String>();
			Cursor hold=database.rawQuery(query, null);
			int index=hold.getColumnIndex("Question");
			if(hold.moveToFirst()){
				
				do{
					question.add(hold.getString(index));
					
				}while(hold.moveToNext());
					
			}
			
			return question;
		
	}
	
	public String readDbAns(String query){
			
			SQLiteDatabase database = this.getReadableDatabase();
			Cursor hold=database.rawQuery(query, null);
			hold.moveToFirst();
			int index=hold.getColumnIndex("Answer");
			String text=hold.getString(index);
			return text;
		}
	
	public String[] readDbArr(String query){
		
		SQLiteDatabase database = this.getReadableDatabase();
		Cursor hold=database.rawQuery(query, null);
		hold.moveToFirst();
		int index0=hold.getColumnIndex("QuestionId");
		int index1=hold.getColumnIndex("Question");
		int index2=hold.getColumnIndex("Answer");
		String text0=String.valueOf( hold.getInt(index0));
		String text1=hold.getString(index1);
		String text2=hold.getString(index2);
		String[] textArr=new String[3];
		textArr[0]=text0;
		textArr[1]=text1;
		textArr[2]=text2;
		return textArr;
		
		
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		
	}

}
