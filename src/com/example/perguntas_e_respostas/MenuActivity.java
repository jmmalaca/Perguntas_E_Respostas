package com.example.perguntas_e_respostas;

import java.util.Locale;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;

public class MenuActivity<CurrentActivity> extends Activity {

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
		
		setContentView(R.layout.activity_menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}
}
