package com.example.perguntas_e_respostas;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;

public class CreditsActivity extends Activity {

	private static final String DEBUG_TAG = "( CreditsActivity ) DEBUG";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);
		
		//language
		//strings.xml... change...
		//language
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		String langPreference = preferences.getString("language", "");
				
		Locale locale = new Locale(langPreference); 
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_credits, menu);
		return true;
	}
}
