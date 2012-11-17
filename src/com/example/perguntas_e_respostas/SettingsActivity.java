package com.example.perguntas_e_respostas;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	private static final String DEBUG_TAG = "( SettingsActivity ) DEBUG";
	private static final String URL_SERVER = "http://soletaken.disca.upv.es:8080/WWTBAM/rest/friends";	
	private static String langPreference = "";
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println(DEBUG_TAG + " onPause");

		// guardar dados...
		save_data();
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
		System.out.println(DEBUG_TAG + " onResume");
		
		super.onResume(); 
		
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

		// guardar dados...
		save_data();
	}

	//check if web is available...
	private boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			//There are no active networks.
			return false;
		}else{
			return true;
		}
	 }
	
	private void changeLang(String country){
		Resources res = getResources();
        String current = res.getConfiguration().locale.getCountry();
        
        System.out.println(DEBUG_TAG+" LANGUAGE: "+current);
        
        if(country.equals("england")){
        	langPreference = "US";
        }else if(country.equals("spain")){
        	langPreference = "ES";
        }
        
        SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString("language", langPreference);
		editor.commit();
	      
		/*AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
		String title = "Language: ";
		if(langPreference.equals("ES")){
			title = "Idioma: ";
		}
		alertDialog.setTitle(title+langPreference);
		alertDialog.setIcon(R.drawable.background);
		alertDialog.show();*/
		
		//strings.xml... change...
		Locale locale = new Locale(langPreference); 
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
		
		Intent i = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName() );
	    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
	    finish();
	    startActivity(i);
	    
        System.out.println(DEBUG_TAG+" NEW ("+langPreference+") > LANGUAGE: "+current);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//strings.xml... change...
		//language
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		langPreference = preferences.getString("language", "");
				
		Locale locale = new Locale(langPreference); 
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

		
		//set feature progress...
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setContentView(R.layout.activity_settings);

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ajudas_possiveis,android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);

		// restaurar os dados do utilizador...
		restore_data();

		final Button send = (Button) findViewById(R.id.button1);
		send.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText text1 = (EditText) findViewById(R.id.editText2);
				String nomeAmigo = text1.getText().toString();

				EditText text2 = (EditText) findViewById(R.id.editText1);
				String nomeMeu = text2.getText().toString();
				if(nomeMeu.equals("") == true){
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
					String title = "Set User Name";
					String message = "Add our name on Settings";
					if(langPreference.equals("ES")){
						title = "Establecer nombre de usuario";
						message = "Agrega nuestro nombre en Configuraci—n";
					}
					alertDialog.setTitle(title);
					alertDialog.setMessage(message);
					alertDialog.setIcon(R.drawable.background);
					alertDialog.show();
				}

				System.out.println(DEBUG_TAG + " click POST FRIEND: " + nomeAmigo);
				
				if (isNetworkConnected()){
					
					new PostFriends().execute();
					
				}else{
					System.out.println("( SettingsActivity ): NET OFF");
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
					String title = "Net dOwn";
					if(langPreference.equals("ES")){
						title = "net abajo";
					}
					alertDialog.setTitle(title);
					alertDialog.setIcon(R.drawable.background);
					alertDialog.show();
				}
			}
		});
		
		final Button eng = (Button) findViewById(R.id.england);
		eng.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				changeLang("england");
			}
		});
		
		final Button spa = (Button) findViewById(R.id.spain);
		spa.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				changeLang("spain");
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
		getMenuInflater().inflate(R.menu.activity_settings, menu);
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

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
			// Get the layout inflater
			LayoutInflater inflater = SettingsActivity.this.getLayoutInflater();
			// Inflate and set the layout for the dialog
			// Pass null as the parent view because its going in the dialog
			// layout
			alertDialog.setView(inflater.inflate(R.layout.activity_credits,null));
			alertDialog.show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void restore_data() {
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);

		EditText text = (EditText) findViewById(R.id.editText1);
		text.setText(preferences.getString("nome_Utilizador", ""));

		Spinner sp = (Spinner) findViewById(R.id.spinner1);
		String num = preferences.getString("num_ajudas", "");
		if (num.length() == 0) {
			sp.setSelection(3);
		} else {
			sp.setSelection(Integer.parseInt(num));
		}
		
		//language...
		langPreference = preferences.getString("language", "");

		// System.out.println(DEBUG_TAG+" nome: "+text.getText().toString()+", spinner: "+num);
	}

	private void save_data() {
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);

		EditText text = (EditText) findViewById(R.id.editText1);
		
		Editor editor = preferences.edit();
		editor.putString("nome_Utilizador", text.getText().toString());
		editor.commit();

		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		String numSpinner = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

		// System.out.println(DEBUG_TAG+" nome: "+text.getText().toString()+", spinner: "+numSpinner);
		editor.putString("num_ajudas", numSpinner);
		editor.commit();
		
		//language...
		editor.putString("language", langPreference);
		editor.commit();
	}
	
	
	
	
	
	//-- async task ----------
	private class PostFriends extends AsyncTask<String, Void, Void> {
        private final HttpClient Client = new DefaultHttpClient();
        @SuppressWarnings("unused")
		private ProgressDialog Dialog = new ProgressDialog(SettingsActivity.this);
    	
     
     // enviar os nomes amigos do utilizador
    	public void postFriend() {
    		
    		EditText text = (EditText) findViewById(R.id.editText1);
    		String nomeMeu = text.getText().toString();
    				
    		EditText text2 = (EditText) findViewById(R.id.editText2);
    		String nomeAmigo = text2.getText().toString();
    		
    		HttpPost request = new HttpPost(URL_SERVER);

    		List<NameValuePair> pairs = new ArrayList<NameValuePair>();

    		pairs.add(new BasicNameValuePair("name", nomeMeu));
    		pairs.add(new BasicNameValuePair("friend_name", nomeAmigo));

    		System.out.println(DEBUG_TAG+": POST: "+nomeMeu+", "+nomeAmigo);
    		try {
    			request.setEntity(new UrlEncodedFormEntity(pairs));

    		} catch (UnsupportedEncodingException e) {
    			System.out.println(DEBUG_TAG + ": UnsupportedEncodingException");
    		}

    		HttpResponse response = null;
    		try {
    			response = Client.execute(request);
    			System.out.println(DEBUG_TAG + ": (post) resposta: " + response.toString());
    			
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			System.out.println(DEBUG_TAG + ": (post) ClientProtocolException");
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			System.out.println(DEBUG_TAG + ": (post) IOException");
    			e.printStackTrace();
    		}
    	}
    	
		protected void onPreExecute() {
            //Dialog.setMessage("Set a new friend...");
            //Dialog.show();
			
			SettingsActivity.this.setProgressBarIndeterminate(true);
        }

		protected Void doInBackground(String... params) {
        	//put the data...
			postFriend();
			
			return null;
        }
        
        protected void onPostExecute(Void unused) {
            //Dialog.dismiss();
        	SettingsActivity.this.setProgressBarIndeterminate(false);
        }

	}
}
