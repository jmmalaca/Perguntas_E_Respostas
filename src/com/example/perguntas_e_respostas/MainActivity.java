package com.example.perguntas_e_respostas;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private static final String DEBUG_TAG = "( MainActivity ) DEBUG";

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println(DEBUG_TAG + " onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println(DEBUG_TAG + " onRestart");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(DEBUG_TAG + " onResume");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		System.out.println(DEBUG_TAG + " onStart");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println(DEBUG_TAG + " onStop");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//strings.xml... change...
		//language
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		String langPreference = preferences.getString("language", "");
				
		Locale locale = new Locale(langPreference); 
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		
				
		setContentView(R.layout.activity_main);

		// listenner... botao 1
		final Button play = (Button) findViewById(R.id.button1);
		play.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(DEBUG_TAG + " click PLAY");
				startActivity(new Intent(MainActivity.this, PlayActivity.class));
			}
		});

		// listenner... botao 2
		final Button scores = (Button) findViewById(R.id.button2);
		scores.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(DEBUG_TAG + " click SCORES");
				startActivity(new Intent(MainActivity.this,
						ScoresActivity.class));
			}
		});

		// listenner... botao 3
		final Button settings = (Button) findViewById(R.id.button3);
		settings.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println(DEBUG_TAG + " click SETTINGS");
				startActivity(new Intent(MainActivity.this,
						SettingsActivity.class));
			}
		});
	}

	@Override
	public boolean isFinishing() {
		// TODO Auto-generated method stub
		System.out.println(DEBUG_TAG + " isFinishing");
		return super.isFinishing();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println(DEBUG_TAG + " onDestroy");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.credits_menu:
			System.out.println(DEBUG_TAG + " CREDITS MENU Click");
			// DialogFragment newFragment = myDialog.newInstance();
			// newFragment.show(newFragment.getFragmentManager(), "credits");

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					MainActivity.this);
			// Get the layout inflater
			LayoutInflater inflater = MainActivity.this.getLayoutInflater();
			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			alertDialog.setView(inflater.inflate(R.layout.activity_credits,
					null));
			alertDialog.show();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
