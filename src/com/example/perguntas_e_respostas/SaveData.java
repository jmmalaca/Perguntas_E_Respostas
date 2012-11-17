package com.example.perguntas_e_respostas;

import java.util.List;
import java.io.IOException;

public class SaveData {
	
	public static void guardarScores(List<HighScore> Pessoas) throws IOException{
    	
		FileData FILE=new FileData();
		FILE.abreEscrita();		
		try{
			FILE.escreveObjectoArray(Pessoas);
			System.out.println("SYSTEM: lista de utilizadores guardada no ficheiro.");
			
		}catch (Exception e){
			System.out.println("ERROR(guardarUtilizadoresFicheiro):"+e.getMessage());
		}
		FILE.fecharEscrita();
    }
	
	public static List<HighScore> lerScores(List<HighScore> Pessoas) throws IOException{
		FileData FILE=new FileData();
		FILE.abreLeitura();
		try{
			Pessoas=FILE.lerObjectoArray();
		}catch (Exception e){
			System.out.println(e);
		}
		FILE.fecharLeitura();
		
		return Pessoas;
	}
	
}
