package com.example.perguntas_e_respostas;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LocalScores extends Activity implements OnClickListener {

	private static final String DEBUG_TAG = "( LocalScoresActivity ) DEBUG";
	private static int numeroLinha = 0;
	private static TableLayout table = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_local_scores);
				
		table = (TableLayout) findViewById(R.id.localTable);

		// ir buscar os dados...
		mySQLiteHelper db = new mySQLiteHelper(this);

		ArrayList<Score> dados = db.get_All_Scores("local");

		// inserir dinamicamente...
		if(dados == null){
			// nome...
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocalScores.this);
			alertDialog.setTitle("Erro:");
			alertDialog.setMessage("Scores from server = NULL");
			alertDialog.setIcon(R.drawable.background);
			alertDialog.show();
			
			TableRow row = new TableRow(this);
			TextView tv = new TextView(this);
			tv.setText("...");
			tv.setGravity(Gravity.CENTER);
			row.setClickable(true);
			row.setOnClickListener(this);
			row.addView(tv);
			// score...
			tv = new TextView(this);
			tv.setText("...");
			tv.setGravity(Gravity.CENTER);
			row.setClickable(true);
			row.setOnClickListener(this);
			row.addView(tv);

			table.addView(row);
			
		}else{
			
			for (int i = 0; i < dados.size(); i++) {
				//System.out.println(DEBUG_TAG + " DADO: nome: "+ dados.get(i).getId() + ", score: "+ dados.get(i).getScore());
	
				// nome...
				TableRow row = new TableRow(this);
				TextView tv = new TextView(this);
	
				// nome...
				tv.setText(dados.get(i).getId());
				tv.setGravity(Gravity.CENTER);
				row.addView(tv);
				// score...
				tv = new TextView(this);
				tv.setText(dados.get(i).getScore());
				tv.setGravity(Gravity.CENTER);
				row.addView(tv);
	
				// add to table...
				table.addView(row);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_local_scores, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.local_delete:
			table.removeAllViews();
			table.refreshDrawableState();

			mySQLiteHelper db = new mySQLiteHelper(this);
			db.delete_all("local");

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View pView) {
		System.out.println(DEBUG_TAG + " linha ESCOLHIDA: " + numeroLinha);

	}
}
