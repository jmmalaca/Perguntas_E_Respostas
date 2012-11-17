package com.example.perguntas_e_respostas;

public class Score {
	private String nome;
	private String score;

	public Score(String nom, String sc) {
		nome = nom;
		score = sc;
	}

	public String getId() {
		return nome;
	}

	public void setId(String nom) {
		this.nome = nom;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String sc) {
		this.score = sc;
	}

	public String toString() {
		return "Name: " + nome + ", Score:" + score;
	}
}
