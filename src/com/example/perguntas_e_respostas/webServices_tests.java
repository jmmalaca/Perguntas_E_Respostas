package com.example.perguntas_e_respostas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class webServices_tests extends AsyncTask<Void, Integer, Boolean> {
	// En el caso de que NO se requiera que el hilo de ejecucion en paralelo
	// modifique
	// la interfaz grafica, la manera mas eficiente de gestionar su ejecucion es
	// a traves
	// de un nuevo thread

	private static final String DEBUG_TAG = "( Worker_forWebServices ) DEBUG: ";
	private HttpClient client;

	webServices_tests() {
		client = new DefaultHttpClient();
	}

	// enviar os nomes amigos do utilizador
	public void post(String URL_SERVER, String nomeMeu, String nomeAmigo) {
		System.out.println(DEBUG_TAG+": POST: "+nomeMeu+", "+nomeAmigo);
		
		HttpPost request = new HttpPost(URL_SERVER);

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();

		pairs.add(new BasicNameValuePair("name,", nomeMeu));
		pairs.add(new BasicNameValuePair("friend_name", nomeAmigo));

		try {
			request.setEntity(new UrlEncodedFormEntity(pairs));

		} catch (UnsupportedEncodingException e) {
			System.out.println(DEBUG_TAG + ": UnsupportedEncodingException");
		}

		HttpResponse response = null;
		try {
			response = client.execute(request);
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
	
	
	//put enviar o score
	public void put(String URL_SERVER, String nomeMeu, String score) {
		System.out.println(DEBUG_TAG+": PUT: "+nomeMeu+", "+score);
		
		HttpPut request = new HttpPut(URL_SERVER);

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();

		pairs.add(new BasicNameValuePair("name,", nomeMeu));
		pairs.add(new BasicNameValuePair("scoring", score));

		try {
			request.setEntity(new UrlEncodedFormEntity(pairs));

		} catch (UnsupportedEncodingException e) {
			System.out.println(DEBUG_TAG + ": UnsupportedEncodingException");
		}

		HttpResponse response = null;
		try {
			response = client.execute(request);
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

	
	// get... para obter scores dos amigos...
	public List<HighScore> get(String URL_SERVER, String nomeMeu) {
		System.out.println(DEBUG_TAG+": GET: "+nomeMeu);
		@SuppressWarnings("unused")
		String nomeTeste = "ddandres";
		
		
		List<HighScore> dados = null;

		HttpGet request = new HttpGet(URL_SERVER+"?name="+nomeMeu);
		request.setHeader("Accept", "application/json");
		
		//HttpGet request = new HttpGet(URL_SERVER);
		//request.setHeader("Accept", "application/json");
		//request.setHeader("name", nomeMeu);
		
		
		System.out.println(DEBUG_TAG+": GET: URL: "+request.toString());
		
		HttpResponse response = null;
		try {
			response = client.execute(request);

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
			System.out.println(DEBUG_TAG + ": (get) resposta: " + responseString);
			System.out.println(DEBUG_TAG + ": (get) verif: " + responseString.equals("null"));
			
			if(responseString.equals("null")){

				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				JSONObject json = null;
	
				try {
					json = new JSONObject(responseString);
	
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println(DEBUG_TAG + ": (get) JSONException");
					e.printStackTrace();
				}
	
				HighScoreList object = gson.fromJson(json.toString(), HighScoreList.class);
				//System.out.println(DEBUG_TAG + ": (get) object obtido: "+ object.toString());
				//System.out.println(DEBUG_TAG + "Teste: Object 1: "+ object.getScores().get(0).getName());
				
				dados = object.getScores();
			}
		}
		
		return dados;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}
}
