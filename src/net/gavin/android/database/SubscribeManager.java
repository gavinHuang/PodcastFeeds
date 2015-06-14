package net.gavin.android.database;

import java.util.ArrayList;
import java.util.List;

import net.gavin.android.model.Subscribe;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SubscribeManager extends DBManager {

	SQLiteDatabase sqliteDatabase;
	
	public SubscribeManager(Context context){
		super(context, "subscribe",null,1);
		//sqliteDatabase = this.getWritableDatabase();
	}
	
	public SubscribeManager(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	void checkPrerequisites(){
		if (sqliteDatabase == null) sqliteDatabase = this.getWritableDatabase();
		try{
			sqliteDatabase.execSQL("create table subscribe (name VARCHAR PRIMARY KEY, url VARCHAR, created varchar )");
		}catch (Exception exception){
			exception.printStackTrace();
		}		
	}
	
	public List<Subscribe> getAllSubscribe(){
		checkPrerequisites();
		try{
			Cursor cursor = sqliteDatabase.rawQuery("select * from subscribe order by created asc",null);
			List<Subscribe> result = new ArrayList<Subscribe>();
			while (cursor.moveToNext()){
				Subscribe subscribe = new Subscribe();
				subscribe.setName(cursor.getString(0));
				subscribe.setUrl(cursor.getString(1));
				subscribe.setCreated(cursor.getString(2));
				result.add(subscribe);
			}
			return result;
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return null;
		
	}
	
	public Subscribe getSubscribeByName(String name){
		checkPrerequisites();
		Cursor cursor = sqliteDatabase.rawQuery("select * from subscribe where name = ? order by created asc",new String[]{name});
		Subscribe subscribe = new Subscribe();
		if (cursor.moveToNext()){
			subscribe.setName(cursor.getString(0));
			subscribe.setUrl(cursor.getString(1));
			subscribe.setCreated(cursor.getString(2));
		}
		return subscribe;	
	}
	
	public void save(Subscribe subscribe){
		sqliteDatabase.execSQL("INSERT INTO subscribe VALUES(?, ?, ?)", new Object[]{subscribe.getName(), subscribe.getUrl(), ""});  
	}


}
