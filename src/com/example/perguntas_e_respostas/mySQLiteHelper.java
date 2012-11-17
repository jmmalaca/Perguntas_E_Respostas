package com.example.perguntas_e_respostas;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class mySQLiteHelper {

	private static final String DEBUG_TAG = "( mySQLiteHelper ) DEBUG";

	private static final String DATABASE_NAME_LOCAL = "Scores_Local";
	private static final String DATABASE_NAME_FRIENDS = "Scores_Local";

	public static final String KEY_NAME = "name";
	public static final String KEY_SCORE = "score";

	private final Context context;
	private DBHelper_Local DBHelper_Local;
	private DBHelper_Friends DBHelper_Friends;

	private SQLiteDatabase db;

	public mySQLiteHelper(Context ctx) {
		this.context = ctx;
		DBHelper_Local = new DBHelper_Local(context);
		DBHelper_Friends = new DBHelper_Friends(context);

		System.out.println(DEBUG_TAG + " builder called ");
	}

	// ---opens the database---
	public mySQLiteHelper open(String dados) throws SQLException {
		if (dados.equals("local")) {
			db = DBHelper_Local.getWritableDatabase();
			System.out.println(DEBUG_TAG + " local path: " + db.getPath());
			return this;
		} else {
			db = DBHelper_Friends.getWritableDatabase();
			System.out.println(DEBUG_TAG + " friends path: " + db.getPath());
			return this;
		}
	}

	// ---closes the database---
	public void close(String dados) {
		if (dados.equals("local")) {
			DBHelper_Local.close();
		} else {
			DBHelper_Friends.close();
		}
	}

	// ---insert a score---
	public void insert_Score(String nome, int score, String dados) {
		System.out.println(DEBUG_TAG + " INSERT called: Nome: " + nome
				+ ", score: " + score);

		if (dados.equals("local")) {
			open("local");

			if (db.equals(null) != true) {
				System.out.println(DEBUG_TAG + " Data Base NOT NULL");

				// one way...
				// ContentValues values = new ContentValues();
				// values.put(KEY_NAME, nome);
				// values.put(KEY_SCORE, score);
				// db.insert(DATABASE_NAME_LOCAL, null, values);

				// other way...
				Cursor cursor = db.rawQuery("SELECT * from "
						+ DATABASE_NAME_LOCAL, null);

				if (cursor.getCount() > 0) {
					cursor.close();
					cursor = db.rawQuery("SELECT MAX(ID) from "
							+ DATABASE_NAME_LOCAL, null);
					cursor.moveToFirst();
					// System.out.println("tamanho da tabela: "+tamanho.getInt(0));
					db.execSQL("INSERT INTO " + DATABASE_NAME_LOCAL
							+ " VALUES (?, ?, ?)",
							new String[] {
									String.valueOf(cursor.getInt(0) + 1), nome,
									String.valueOf(score) });
				} else {
					cursor.close();
					db.execSQL(
							"INSERT INTO " + DATABASE_NAME_LOCAL
									+ " VALUES (?, ?, ?)",
							new String[] { String.valueOf(0), nome,
									String.valueOf(score) });
				}
				cursor.close();
			}

			close("local");
		} else {
			open("friends");

			if (db.equals(null) != true) {
				System.out.println(DEBUG_TAG + " Data Base NOT NULL");

				// one way...
				// ContentValues values = new ContentValues();
				// values.put(KEY_NAME, nome);
				// values.put(KEY_SCORE, score);
				// db.insert(DATABASE_NAME_FRIENDS, null, values);

				// other way...
				Cursor cursor = db.rawQuery("SELECT * from "
						+ DATABASE_NAME_FRIENDS, null);

				if (cursor.getCount() > 0) {
					cursor.close();
					cursor = db.rawQuery("SELECT MAX(ID) from "
							+ DATABASE_NAME_FRIENDS, null);
					cursor.moveToFirst();
					// System.out.println("tamanho da tabela: "+tamanho.getInt(0));
					db.execSQL("INSERT INTO " + DATABASE_NAME_FRIENDS
							+ " VALUES (?, ?, ?)",
							new String[] {
									String.valueOf(cursor.getInt(0) + 1), nome,
									String.valueOf(score) });
				} else {
					cursor.close();
					db.execSQL(
							"INSERT INTO " + DATABASE_NAME_FRIENDS
									+ " VALUES (?, ?, ?)",
							new String[] { String.valueOf(0), nome,
									String.valueOf(score) });
				}
				cursor.close();
			}

			close("friends");
		}
	}

	// ---deletes a particular score---
	public void delete_Score(String nome, String dados) {
		if (dados.equals("local")) {
			open("local");
			db.delete(DATABASE_NAME_LOCAL, KEY_NAME + "=" + nome, null);
			close("local");
		} else {
			open("friends");
			db.delete(DATABASE_NAME_FRIENDS, KEY_NAME + "=" + nome, null);
			close("friends");
		}
	}

	// ---deletes all scores---
	public void delete_all(String dados) {
		if (dados.equals("local")) {
			open("local");
			db.delete(DATABASE_NAME_LOCAL, null, null);
			// db.execSQL("DELETE FROM "+DATABASE_NAME_LOCAL);
			close("local");
		} else {
			open("friends");
			db.delete(DATABASE_NAME_FRIENDS, null, null);
			// db.execSQL("DELETE FROM "+DATABASE_NAME_LOCAL);
			close("friends");
		}
	}

	// ---retrieves all scores---
	public ArrayList<Score> get_All_Scores(String dados) {
		ArrayList<Score> list = new ArrayList<Score>();

		if (dados.equals("local")) {
			open("local");

			// onve way...
			// Cursor cursor = db.query(DATABASE_NAME_LOCAL, new String[] {
			// KEY_NAME, KEY_SCORE }, null, null, null, null, null);

			// other way...
			Cursor cursor = db.rawQuery("SELECT * FROM " + DATABASE_NAME_LOCAL,
					null);
			if (cursor.moveToFirst()) {
				do {
					Score novo = new Score(cursor.getString(1),
							cursor.getString(2));
					list.add(novo);
				} while (cursor.moveToNext());
			}
			cursor.close();

			close("local");
		} else {
			open("friends");

			// onve way...
			// Cursor cursor = db.query(DATABASE_NAME_FRIENDS, new String[] {
			// KEY_NAME, KEY_SCORE }, null, null, null, null, null);

			// other way...
			Cursor cursor = db.rawQuery("SELECT * FROM "
					+ DATABASE_NAME_FRIENDS, null);
			if (cursor.moveToFirst()) {
				do {
					Score novo = new Score(cursor.getString(1),
							cursor.getString(2));
					list.add(novo);
				} while (cursor.moveToNext());
			}
			cursor.close();

			close("friends");
		}
		return list;
	}

	// ---retrieves a particular score---
	public Score get_Score(String nome, String dados) throws SQLException {
		Score novo = null;

		if (dados.equals("local")) {
			open("local");

			Cursor mCursor = db.query(true, DATABASE_NAME_LOCAL, new String[] {
					KEY_NAME, KEY_SCORE }, KEY_NAME + "=" + nome, null, null,
					null, null, null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

			novo = new Score(mCursor.getString(0), mCursor.getString(1));
			close("local");
		} else {
			open("friends");

			Cursor mCursor = db.query(true, DATABASE_NAME_FRIENDS,
					new String[] { KEY_NAME, KEY_SCORE },
					KEY_NAME + "=" + nome, null, null, null, null, null);

			if (mCursor != null) {
				mCursor.moveToFirst();
			}

			novo = new Score(mCursor.getString(0), mCursor.getString(1));
			close("friends");
		}
		return novo;
	}

	// ---updates a score---
	public void update_Score(String nome, String score, String dados) {
		if (dados.equals("local")) {
			open("local");

			ContentValues args = new ContentValues();
			args.put(KEY_NAME, nome);
			args.put(KEY_SCORE, score);
			db.update(DATABASE_NAME_LOCAL, args, KEY_NAME + "=" + nome, null);

			close("local");
		} else {

			open("friends");

			ContentValues args = new ContentValues();
			args.put(KEY_NAME, nome);
			args.put(KEY_SCORE, score);
			db.update(DATABASE_NAME_FRIENDS, args, KEY_NAME + "=" + nome, null);

			close("friends");
		}
	}
}
