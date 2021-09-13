package com.general.quizapp.modell;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResultsItem{

	@SerializedName("difficulty")
	private String difficulty;

	@SerializedName("question")
	private String question;

	@SerializedName("correct_answer")
	private String correctAnswer;

	@SerializedName("incorrect_answers")
	private List<String> incorrectAnswers;

	@SerializedName("category")
	private String category;

	@SerializedName("type")
	private String type;

	public void setDifficulty(String difficulty){
		this.difficulty = difficulty;
	}

	public String getDifficulty(){
		return difficulty;
	}

	public void setQuestion(String question){
		this.question = question;
	}

	public String getQuestion(){
		return question;
	}

	public void setCorrectAnswer(String correctAnswer){
		this.correctAnswer = correctAnswer;
	}

	public String getCorrectAnswer(){
		return correctAnswer;
	}

	public void setIncorrectAnswers(List<String> incorrectAnswers){
		this.incorrectAnswers = incorrectAnswers;
	}

	public List<String> getIncorrectAnswers(){
		return incorrectAnswers;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}
}