package com.example.perguntas_e_respostas;

import java.util.Locale;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class ScoresActivity extends TabActivity {

	private static String langPreference = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//language...
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		langPreference = preferences.getString("language", "");
		
		//strings.xml... change...
		//language
				
		Locale locale = new Locale(langPreference); 
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		
		//set feature progress...
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_scores);

		TabHost tabHost = getTabHost();

		// Tab for Photos
		String titleLocal = "Local";
		String titleFriends = "Friends";
		if(langPreference.equals("ES")){
			titleLocal = "Local";
			titleFriends = "Amigos";
		}
		
		TabSpec localspec = tabHost.newTabSpec(titleLocal);
		// setting Title and Icon for the Tab
		localspec.setIndicator(titleLocal);
		Intent localIntent = new Intent(this, LocalScores.class);
		localspec.setContent(localIntent);

		// Tab for Songs
		TabSpec friendsspec = tabHost.newTabSpec(titleFriends);
		friendsspec.setIndicator(titleFriends);
		Intent friendsIntent = new Intent(this, FriendsScores.class);
		friendsspec.setContent(friendsIntent);

		tabHost.addTab(localspec);
		tabHost.addTab(friendsspec);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_scores, menu);
		return true;
	}
}
