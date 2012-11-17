package com.example.perguntas_e_respostas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper_Friends extends SQLiteOpenHelper {

	private static final String DEBUG_TAG = "( DBHelper_Friends ) DEBUG";

	private static final String DATABASE_NAME_FRIENDS = "Scores_Local";

	public static final String KEY_NAME = "name";
	public static final String KEY_SCORE = "score";

	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "MyDBHelper";

	DBHelper_Friends(Context context) {
		super(context, DATABASE_NAME_FRIENDS, null, DATABASE_VERSION);
		System.out.println(DEBUG_TAG + " builder called ");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + DATABASE_NAME_FRIENDS
				+ " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_NAME
				+ " TEXT NOT NULL, " + KEY_SCORE + " INTEGER NOT NULL);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
				+ newVersion + ", which will destroy all old data");
	}
}
