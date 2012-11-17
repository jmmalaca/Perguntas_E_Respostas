package com.example.perguntas_e_respostas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class FriendsScores extends Activity implements OnClickListener {

	private static final String DEBUG_TAG = "( FriendsScoresActivity ) DEBUG";
	private static int numeroLinha = 0;
	private static TableLayout table = null;
	private static final String URL_SERVER = "http://soletaken.disca.upv.es:8080/WWTBAM/rest/highscores";
	private static String nome = "";
	private static List<HighScore> dados=null;

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
	
	private void data(){
		
		if(nome.equals("") == true){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(FriendsScores.this);
			alertDialog.setTitle("Set User Name");
			alertDialog.setMessage("Add our name on Settings");
			alertDialog.setIcon(R.drawable.background);
			alertDialog.show();
		}else{
			
			if (isNetworkConnected()){
				System.out.println("( FriendsScores ): NET ON");
				/*
				try {
					SaveData.guardarScores(dados);
				} catch (IOException e) {
					System.out.println("( FriendsScores ): ERROR save on data file");
				}*/
				
				//inserir dinamicamente...
				table = (TableLayout) findViewById(R.id.fiendsTable);
				if(dados == null){
					// nome...
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(FriendsScores.this);
					alertDialog.setTitle("Erro:");
					alertDialog.setMessage("Scores from server = NULL");
					alertDialog.setIcon(R.drawable.background);
					alertDialog.show();
					
					//name...
					TableRow row = new TableRow(this);
					TextView tv = new TextView(this);
					tv.setText("...");
					tv.setGravity(Gravity.CENTER);
					//row.setClickable(true);
					//row.setOnClickListener(this);
					row.addView(tv);
					
					// score...
					tv = new TextView(this);
					tv.setText("...");
					tv.setGravity(Gravity.CENTER);
					//row.setClickable(true);
					//row.setOnClickListener(this);
					row.addView(tv);
	
					table.addView(row);
					
				}else{
					System.out.println("( FriendsScores ): GET my friends: "+dados.size());
					
					for(int i=0; i<dados.size(); i++){
						// define nome...
						TableRow row = new TableRow(this);
						TextView tv = new TextView(this);
						tv.setText(dados.get(i).getName());
						tv.setGravity(Gravity.CENTER);
						row.addView(tv);
						
						// define score...
						tv = new TextView(this);
						tv.setText(Integer.toString(dados.get(i).getScoring()));
						tv.setGravity(Gravity.CENTER);
						row.addView(tv);
			
						//add to table...
						table.addView(row);
					}
				}
				
			}else{
				System.out.println("( FriendsScores ): NET OFF");
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(FriendsScores.this);
				alertDialog.setTitle("Net dOwn");
				alertDialog.setIcon(R.drawable.background);
				alertDialog.show();
			}
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//set feature progress...
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
				
		setContentView(R.layout.activity_friends_scores);

		// ir buscar o nome do user...
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		nome = preferences.getString("nome_Utilizador", "");
		
		new GrabScores().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_friends_scores, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.friends_delete:
			table.removeAllViews();
			table.refreshDrawableState();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onClick(View v) {
		System.out.println(DEBUG_TAG + " linha ESCOLHIDA: " + numeroLinha);

	}
	
	
	
	
	
	
	//-- async task ----------
	private class GrabScores extends AsyncTask<Void, Void, Void> {
        private final HttpClient Client = new DefaultHttpClient();
        @SuppressWarnings("unused")
		private ProgressDialog Dialog = new ProgressDialog(FriendsScores.this);
    	
        // get... para obter scores dos amigos...
        public List<HighScore> getScores() {

    		HttpGet request = new HttpGet(URL_SERVER+"?name="+nome);
    		request.setHeader("Accept", "application/json");
    		System.out.println(DEBUG_TAG+": GET: "+nome);
    		
    		//HttpGet request = new HttpGet(URL_SERVER);
    		//request.setHeader("Accept", "application/json");
    		//request.setHeader("name", nome);
    		
    		System.out.println(DEBUG_TAG+": GET: URL: "+request.getURI());
    		
    		HttpResponse response = null;
    		try {
    			response = Client.execute(request);
    			
    		} catch (ClientProtocolException e) {
    			// TODO Auto-generated catch block
    			System.out.println(DEBUG_TAG + ": (get) ClientProtocolException");
    			e.printStackTrace();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			System.out.println(DEBUG_TAG + ": (get) IOException 1");
    			e.printStackTrace();
    		}

    		HttpEntity entity = response.getEntity();

    		if (entity != null) {
    			InputStream stream = null;

    			try {
    				stream = entity.getContent();

    			} catch (IllegalStateException e) {
    				// TODO Auto-generated catch block
    				System.out.println(DEBUG_TAG + ": (get) IllegalStateException");
    				e.printStackTrace();
    				
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				System.out.println(DEBUG_TAG + ": (get) IOException 2");
    				e.printStackTrace();
    			}

    			BufferedReader reader = new BufferedReader(new InputStreamReader(
    					stream));

    			StringBuilder sb = new StringBuilder();
    			String line = null;

    			try {
    				while ((line = reader.readLine()) != null) {
    					sb.append(line + "\n");
    				}
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				System.out.println(DEBUG_TAG + ": (get) IOException 3");
    				e.printStackTrace();
    			}

    			try {
    				stream.close();
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				System.out.println(DEBUG_TAG + ": (get) IOException 4");
    				e.printStackTrace();
    			}

    			String responseString = sb.toString();
    			
    			System.out.println(DEBUG_TAG + ": (get) return data: " + responseString.equals("null"));
    			if(responseString.equals("null") != true){

    				GsonBuilder builder = new GsonBuilder();
    				Gson gson = builder.create();
    				JSONObject json = null;
    				
    				try {
    					System.out.println(DEBUG_TAG + ": (get) resposta: " + responseString);
    					json = new JSONObject(responseString);
    	
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					System.out.println(DEBUG_TAG + ": (get) JSONException");
    					e.printStackTrace();
    				}
    	
    				HighScoreList object = gson.fromJson(json.toString(), HighScoreList.class);
    				dados = object.getScores();
    				
    				System.out.println(DEBUG_TAG + ": (get) dados size: "+ dados.size());
    			}
    		}
			return null;
    	}
        
        @Override
		protected void onPreExecute() {
            //Dialog.setMessage("Downloading Friends Scores..");
            //Dialog.show();
			
			FriendsScores.this.setProgressBarIndeterminate(true);
        }

		@Override
		protected Void doInBackground(Void... params) {
        	
			//get the data...
			getScores();
        	
			//forcar o update dos dados no ecra...
			publishProgress();
			return null;
        }
        
		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			
			//Dialog.dismiss();
        	FriendsScores.this.setProgressBarIndeterminate(false);
        	
        	//show data...
        	data();
        	
		}
	}
}
