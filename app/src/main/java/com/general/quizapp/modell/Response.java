package com.general.quizapp.modell;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("response_code")
	private int responseCode;

	@SerializedName("results")
	private List<ResultsItem> results;

	public int getResponseCode(){
		return responseCode;
	}

	public List<ResultsItem> getResults(){
		return results;
	}
}