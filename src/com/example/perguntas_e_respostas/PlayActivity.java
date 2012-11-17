package com.example.perguntas_e_respostas;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class PlayActivity extends Activity {

	private static final String DEBUG_TAG = "( PlayActivity ) DEBUG";
	private static final int[] valoresPerguntas = { 0, 100, 200, 300, 500,1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000,500000, 1000000 };
	private int numPerguntas = 15;
	private int numPergunta = 1;
	private int numVezes50 = 0;
	private static int money = 0;
	private static String nome_Utilizador = "";
	private int contador_ajudas = 0;
	private String numero_Ajudas = "";
	private boolean restaurar = false;
	private static String langPreference = "";

	private static final String URL_SERVER = "http://soletaken.disca.upv.es:8080/WWTBAM/rest/highscores";

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

		// restaurar dados...
		if(restaurar){
			//restore_data();
		}
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

	private void acertei_resposta() {
		// actualizar o numero da pergunta
		TextView guito = (TextView) findViewById(R.id.textView3);
		money = Integer.parseInt(guito.getText().toString());
		money = money + valoresPerguntas[numPergunta - 1];
		guito.setText(String.valueOf(money));

		// actualizar a pontuacao
		TextView question = (TextView) findViewById(R.id.textView6);
		numPergunta++;
		question.setText(String.valueOf(numPergunta));

		// actualizar o texto da proxima pergunta
		TextView pergunta = (TextView) findViewById(R.id.textView4);
		pergunta.setText(lerDadosXML.perguntas.get(numPergunta).getText());

		// actualizar o texto nas respostas...
		final Button resposta1 = (Button) findViewById(R.id.resposta1);
		resposta1.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer1());
		final Button resposta2 = (Button) findViewById(R.id.resposta2);
		resposta2.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer2());
		final Button resposta3 = (Button) findViewById(R.id.resposta3);
		resposta3.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer3());
		final Button resposta4 = (Button) findViewById(R.id.resposta4);
		resposta4.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer4());
		
		save_data();
	}

	private void guardar_BD() {
		// base de dados...
		mySQLiteHelper db = new mySQLiteHelper(this);
		db.insert_Score(nome_Utilizador, money, "local");

		// based de dados da web
		System.out.println(DEBUG_TAG + " click PUT MY SCORE... ");

		if (isNetworkConnected()){
			
			new PutScore().execute();
			
		}else{
			System.out.println("( PlayActivity ): NET OFF");
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayActivity.this);
			String title = "Net dOwn";
			if(langPreference.equals("ES")){
				title = "net abajo";
			}
			alertDialog.setTitle(title);
			alertDialog.setIcon(R.drawable.background);
			alertDialog.show();
		}
	}

	private void ganhei() {
		// ganhei...
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayActivity.this);
		String title = "YOU WIN! :D";
		if(langPreference.equals("ES")){
			title = "usted puede ganar! :D";
		}
		alertDialog.setTitle(title);
		alertDialog.setIcon(R.drawable.background);
		alertDialog.show();

		// acabar com o jogo... guardar mais um record...
		guardar_BD();

		// voltar ao menu principal...
		startActivity(new Intent(PlayActivity.this, MainActivity.class));
	}

	private void perdi() {
		// perdi...
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayActivity.this);
		String title = "YOU LOOSE! :D";
		if(langPreference.equals("ES")){
			title = "pierdes! :(";
		}
		alertDialog.setTitle(title);
		alertDialog.setIcon(R.drawable.background);
		alertDialog.show();

		// acabar com o jogo... guardar mais um record...
		guardar_BD();

		// voltar ao menu principal...
		startActivity(new Intent(PlayActivity.this, MainActivity.class));
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		System.out.println(DEBUG_TAG + " onCreate");

		super.onCreate(savedInstanceState);
		
		//set feature progress...
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		//strings.xml... change...
		//language
		SharedPreferences preferences = getSharedPreferences("MyFile",Context.MODE_PRIVATE);
		String langPreference = preferences.getString("language", "");
				
		Locale locale = new Locale(langPreference); 
		Locale.setDefault(locale);
		
		Configuration config = new Configuration();
		config.locale = locale;
		
		getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

		setContentView(R.layout.activity_play);

		// saber o nome do User e quantas ajudas pode utilizar
		nome_Utilizador = preferences.getString("nome_Utilizador", "");
		numero_Ajudas = preferences.getString("num_ajudas", "");
		
		//start... backup data...
		save_data();
		
		System.out.println(DEBUG_TAG + " USER " + nome_Utilizador+ " START, ajudas: " + numero_Ajudas);

		if(nome_Utilizador.equals("") == true){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayActivity.this);
			String title = "Set User Name";
			String message = "Add our name on Settings";
			if(langPreference.equals("ES")){
				title = "pierdes! :(";
				message = "Agrega nuestro nombre en Configuraci—n";
			}
			alertDialog.setTitle(title);
			alertDialog.setMessage(message);
			alertDialog.setIcon(R.drawable.background);
			alertDialog.show();
			
		}else{
			// ler as perguntas do ficheiro xml
			lerDadosXML perguntas = new lerDadosXML();
			InputStream inputStream = null;
			
			if(langPreference.equals("ES")){
				inputStream = getResources().openRawResource(R.raw.questions0002);
			}else{
				inputStream = getResources().openRawResource(R.raw.questions0001);
			}
			perguntas.lerDados(inputStream);
	
			// perguntas lidas...
			// colocar os dados das perguntas no programa...
			TextView pergunta = (TextView) findViewById(R.id.textView4);
			pergunta.setText(lerDadosXML.perguntas.get(numPergunta).getText());
	
			// colocar os botao a funcionar e as respectivas resposta em cada um...
			// listenner... resposta 1
			final Button resposta1 = (Button) findViewById(R.id.resposta1);
			resposta1.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer1());
			resposta1.setOnClickListener(new View.OnClickListener() {
	
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(DEBUG_TAG + " click RESPOSTA 1");
					if (lerDadosXML.perguntas.get(numPergunta).getRight() == 1) {
						if (numPergunta == numPerguntas) {
							ganhei();
						} else {
							acertei_resposta();
						}
					} else {
						perdi();
					}
				}
			});
	
			//jogo vai comecar... preparar um restauro possivel...
			restaurar = true;
			
			
			
			//botoes para jogar...
			final Button resposta2 = (Button) findViewById(R.id.resposta2);
			resposta2.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer2());
			resposta2.setOnClickListener(new View.OnClickListener() {
	
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(DEBUG_TAG + " click RESPOSTA 2");
					if (lerDadosXML.perguntas.get(numPergunta).getRight() == 2) {
						if (numPergunta == numPerguntas) {
							ganhei();
						} else {
							acertei_resposta();
						}
					} else {
						perdi();
					}
				}
			});
	
			final Button resposta3 = (Button) findViewById(R.id.resposta3);
			resposta3.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer3());
			resposta3.setOnClickListener(new View.OnClickListener() {
	
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(DEBUG_TAG + " click RESPOSTA 3");
					if (lerDadosXML.perguntas.get(numPergunta).getRight() == 3) {
						if (numPergunta == numPerguntas) {
							ganhei();
						} else {
							acertei_resposta();
						}
					} else {
						perdi();
					}
				}
			});
	
			final Button resposta4 = (Button) findViewById(R.id.resposta4);
			resposta4.setText(lerDadosXML.perguntas.get(numPergunta).getAnswer4());
			resposta4.setOnClickListener(new View.OnClickListener() {
	
				public void onClick(View v) {
					// TODO Auto-generated method stub
					System.out.println(DEBUG_TAG + " click RESPOSTA 4");
					if (lerDadosXML.perguntas.get(numPergunta).getRight() == 4) {
						if (numPergunta == numPerguntas) {
							ganhei();
						} else {
							acertei_resposta();
						}
					} else {
						perdi();
					}
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_play, menu);
		return true;
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

	private String resposta(int num) {
		if (num == 1) {
			return lerDadosXML.perguntas.get(numPergunta).getAnswer1();
		} else if (num == 2) {
			return lerDadosXML.perguntas.get(numPergunta).getAnswer2();
		} else if (num == 3) {
			return lerDadosXML.perguntas.get(numPergunta).getAnswer3();
		} else if (num == 4) {
			return lerDadosXML.perguntas.get(numPergunta).getAnswer4();
		} else {
			return "";
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.ajuda_phone:
			if (contador_ajudas < Integer.parseInt(numero_Ajudas)) {
				System.out.println(DEBUG_TAG + " ajuda PHONE: "+ lerDadosXML.perguntas.get(numPergunta).getPhone());
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(PlayActivity.this);
				String title = "Help Phone";
				String message = "Choose: ";
				if(langPreference.equals("ES")){
					title = "TelŽfono de ayuda";
					message = "Elegir: ";
				}
				alertDialog.setTitle(title);
				alertDialog.setMessage(message+resposta(lerDadosXML.perguntas.get(numPergunta).getPhone()));
				alertDialog.setIcon(R.drawable.phone);
				alertDialog.show();
				contador_ajudas++;
			} else {
				AlertDialog.Builder alertDialog6 = new AlertDialog.Builder(PlayActivity.this);
				String title = "Help Phone";
				String message = "No more Help";
				if(langPreference.equals("ES")){
					title = "TelŽfono de ayuda";
					message = "Ayuda no m‡s";
				}
				alertDialog6.setTitle(title);
				alertDialog6.setMessage(message);
				alertDialog6.setIcon(R.drawable.phone);
				alertDialog6.show();
			}
			return true;
		case R.id.ajuda_50:
			if (contador_ajudas < Integer.parseInt(numero_Ajudas)) {
				if (numVezes50 == 0) {
					System.out.println(DEBUG_TAG+ " ajuda 50%: "+ lerDadosXML.perguntas.get(numPergunta).getFifty1());
					AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(PlayActivity.this);
					String title = "Help 50%";
					String message = "Choose: ";
					if(langPreference.equals("ES")){
						title = "Ayuda 50%";
						message = "Elegir: ";
					}
					alertDialog2.setTitle(title);
					alertDialog2.setMessage(message+resposta(lerDadosXML.perguntas.get(numPergunta).getFifty1()));
					alertDialog2.setIcon(R.drawable.help50);
					alertDialog2.show();
					numVezes50++;
				} else if (numVezes50 == 1) {
					System.out.println(DEBUG_TAG+ " ajuda 50%: "+ lerDadosXML.perguntas.get(numPergunta).getFifty2());
					AlertDialog.Builder alertDialog3 = new AlertDialog.Builder(PlayActivity.this);
					String title = "Help 50%";
					String message = "Choose: ";
					if(langPreference.equals("ES")){
						title = "Ayuda 50%";
						message = "Elegir: ";
					}
					alertDialog3.setTitle(title);
					alertDialog3.setMessage(message+resposta(lerDadosXML.perguntas.get(numPergunta).getFifty2()));
					alertDialog3.setIcon(R.drawable.help50);
					alertDialog3.show();
					numVezes50++;
				} else {
					System.out.println(DEBUG_TAG + " ajuda 50%: acabou !");
					AlertDialog.Builder alertDialog4 = new AlertDialog.Builder(PlayActivity.this);
					String title = "Help 50%";
					String message = "50% help is Over ";
					if(langPreference.equals("ES")){
						title = "Ayuda 50%";
						message = "Ayuda es m‡s";
					}
					alertDialog4.setTitle(title);
					alertDialog4.setMessage(message);
					alertDialog4.setIcon(R.drawable.help50);
					alertDialog4.show();
				}
				contador_ajudas++;
			} else {
				AlertDialog.Builder alertDialog7 = new AlertDialog.Builder(PlayActivity.this);
				String title = "Help 50%";
				String message = "50% help is Over ";
				if(langPreference.equals("ES")){
					title = "Ayuda 50%";
					message = "Ayuda es m‡s";
				}
				alertDialog7.setTitle(title);
				alertDialog7.setMessage(message);
				alertDialog7.setIcon(R.drawable.help50);
				alertDialog7.show();
			}
			return true;
		case R.id.ajuda_audience:
			if (contador_ajudas < Integer.parseInt(numero_Ajudas)) {
				System.out.println(DEBUG_TAG + " ajuda AUDIENCE: "+ lerDadosXML.perguntas.get(numPergunta).getAudience());
				AlertDialog.Builder alertDialog5 = new AlertDialog.Builder(PlayActivity.this);
				String title = "Help Audience";
				String message = "Choose: ";
				if(langPreference.equals("ES")){
					title = "Ayuda Pœblico";
					message = "Elegir: ";
				}
				alertDialog5.setTitle(title);
				alertDialog5.setMessage(message+ resposta(lerDadosXML.perguntas.get(numPergunta).getAudience()));
				alertDialog5.setIcon(R.drawable.audience);
				alertDialog5.show();
				contador_ajudas++;
			} else {
				AlertDialog.Builder alertDialog8 = new AlertDialog.Builder(PlayActivity.this);
				String title = "Help Audience";
				String message = "No more Help";
				if(langPreference.equals("ES")){
					title = "Ayuda Pœblico";
					message = "Ayuda no m‡s";
				}
				alertDialog8.setTitle(title);
				alertDialog8.setMessage(message);
				alertDialog8.setIcon(R.drawable.audience);
				alertDialog8.show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void restore_data() {
		SharedPreferences preferences = getSharedPreferences("MyGame",Context.MODE_PRIVATE);
		
		TextView text2 = (TextView) findViewById(R.id.textView3);
		text2.setText(preferences.getString("money", ""));
		
		TextView text = (TextView) findViewById(R.id.textView6);
		text.setText(preferences.getString("numPergunta", ""));
		
		numVezes50 = Integer.getInteger(preferences.getString("numVezes50", ""));
		contador_ajudas = Integer.getInteger(preferences.getString("contador_ajudas", ""));
		
		String num = preferences.getString("num_ajudas", "");
		System.out.println(DEBUG_TAG + " restaurar ajudas: " + num);
	}

	private void save_data() {
		//save data just to prevent
		SharedPreferences preferences = getSharedPreferences("MyGame",Context.MODE_PRIVATE);
		Editor editor = preferences.edit();

		TextView guito = (TextView) findViewById(R.id.textView3);
		editor.putString("money", guito.getText().toString());
		editor.commit();

		TextView question = (TextView) findViewById(R.id.textView6);
		editor.putString("numPergunta", question.getText().toString());
		editor.commit();
		
		editor.putString("numVezes50", Integer.toString(numVezes50));
		editor.commit();
		editor.putString("contador_ajudas", Integer.toString(contador_ajudas));
		editor.commit();
		editor.putString("numero_Ajudas", numero_Ajudas);
		editor.commit();
		
	}
	
	
	
	
	
	//-- async task ----------
		private class PutScore extends AsyncTask<String, Void, Void> {
	        private final HttpClient Client = new DefaultHttpClient();
	        @SuppressWarnings("unused")
			private ProgressDialog Dialog = new ProgressDialog(PlayActivity.this);
	    	
	     
	        //put enviar o score
	    	public void putScores() {
	    		HttpPut request = new HttpPut(URL_SERVER);

	    		List<NameValuePair> pairs = new ArrayList<NameValuePair>();

	    		pairs.add(new BasicNameValuePair("name", nome_Utilizador));
	    		pairs.add(new BasicNameValuePair("scoring", Integer.toString(money)));
	    		System.out.println(DEBUG_TAG+": PUT: "+nome_Utilizador+", "+money);
	    		
	    		try {
	    			request.setEntity(new UrlEncodedFormEntity(pairs));

	    		} catch (UnsupportedEncodingException e) {
	    			System.out.println(DEBUG_TAG + ": UnsupportedEncodingException");
	    		}

	    		HttpResponse response = null;
	    		try {
	    			response = Client.execute(request);
	    			System.out.println(DEBUG_TAG + ": (put) resposta: " + response.toString());
	    			
	    		} catch (ClientProtocolException e) {
	    			// TODO Auto-generated catch block
	    			System.out.println(DEBUG_TAG + ": (put) ClientProtocolException");
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			System.out.println(DEBUG_TAG + ": (put) IOException");
	    			e.printStackTrace();
	    		}
	    	}
	    	
			protected void onPreExecute() {
	            //Dialog.setMessage("Set a new Score...");
	            //Dialog.show();
				
				PlayActivity.this.setProgressBarIndeterminate(true);
	        }

			protected Void doInBackground(String... params) {
	        	//put the data...
				putScores();
				
				return null;
	        }
	        
	        protected void onPostExecute(Void unused) {
	            //Dialog.dismiss();
	        	
	        	PlayActivity.this.setProgressBarIndeterminate(false);
	        }

		}
}
