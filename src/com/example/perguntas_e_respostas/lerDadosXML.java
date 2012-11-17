package com.example.perguntas_e_respostas;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class lerDadosXML {

	private static final String DEBUG_TAG = "( PlayActivity ) DEBUG";
	public static ArrayList<Question> perguntas = null;

	public void lerDados(InputStream inputStream) {
		perguntas = new ArrayList<Question>();
		// on create primeiro de tudo temos de ler as perguntas do ficheiro
		// XML...

		XmlPullParser parser = null;
		try {
			parser = XmlPullParserFactory.newInstance().newPullParser();

		} catch (XmlPullParserException e) {
			System.out.println(DEBUG_TAG + " ERROR: opening PULLPARSER");
		}

		try {
			parser.setInput(inputStream, null);
		} catch (XmlPullParserException e) {
			System.out.println(DEBUG_TAG + " ERROR: SET_INPUT on parser");
		}

		int eventType = XmlPullParser.START_DOCUMENT;
		while (eventType != XmlPullParser.END_DOCUMENT) {

			if (eventType == XmlPullParser.START_TAG) {
				Question nova = new Question();
				nova.setAnswer1(parser.getAttributeValue(null, "answer1"));
				nova.setAnswer2(parser.getAttributeValue(null, "answer2"));
				nova.setAnswer3(parser.getAttributeValue(null, "answer3"));
				nova.setAnswer4(parser.getAttributeValue(null, "answer4"));
				nova.setAudience(parser.getAttributeValue(null, "audience"));
				nova.setFifty1(parser.getAttributeValue(null, "fifty1"));
				nova.setFifty2(parser.getAttributeValue(null, "fifty2"));
				nova.setNumber(parser.getAttributeValue(null, "number"));
				nova.setPhone(parser.getAttributeValue(null, "phone"));
				nova.setRight(parser.getAttributeValue(null, "right"));
				nova.setText(parser.getAttributeValue(null, "text"));
				// com a pergunta criada, adicionar ao array de perguntas...
				perguntas.add(nova);
			}

			try {
				eventType = parser.next();
			} catch (XmlPullParserException e) {
				System.out
						.println(DEBUG_TAG
								+ " ERROR: XmlPullParserException on reading some question");
			} catch (IOException e) {
				System.out.println(DEBUG_TAG
						+ " ERROR: IOException on reading some question");
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			System.out.println(DEBUG_TAG
					+ " ERROR: closing inputStream from xml file");
		}

		// verificar os dados lidos...
		// verificarDados();
	}

	@SuppressWarnings("unused")
	private void verificarDados() {
		System.out.println("Perguntas Lidas: ");
		if (perguntas.size() > 0) {
			for (int i = 1; i < perguntas.size(); i++) {
				System.out.println("- Pergunta[ " + i + " ]:");
				System.out.println("pergunta.getAnswer1: "
						+ perguntas.get(i).getAnswer1());
				System.out.println("pergunta.getAnswer2: "
						+ perguntas.get(i).getAnswer2());
				System.out.println("pergunta.getAnswer3: "
						+ perguntas.get(i).getAnswer3());
				System.out.println("pergunta.getAnswer4: "
						+ perguntas.get(i).getAnswer4());
				System.out.println("pergunta.getAudience: "
						+ perguntas.get(i).getAudience());
				System.out.println("pergunta.getFifty1: "
						+ perguntas.get(i).getFifty1());
				System.out.println("pergunta.getFifty2: "
						+ perguntas.get(i).getFifty2());
				System.out.println("pergunta.getNumber: "
						+ perguntas.get(i).getNumber());
				System.out.println("pergunta.getPhone: "
						+ perguntas.get(i).getPhone());
				System.out.println("pergunta.getRight: "
						+ perguntas.get(i).getRight());
				System.out.println("pergunta.getText: "
						+ perguntas.get(i).getText());
			}
		}
	}
}
