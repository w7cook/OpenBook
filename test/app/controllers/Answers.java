package controllers;

import models.Answer;

public class Answers extends OBController {

	public static void chooseAnswer(Long answerId){
		Answer answer = Answer.findById(answerId);
		if(answer == null)
			notFound();
		answer.addUserChoice(user());
		redirect("/");
	}
	
	public static void answers(Long answerId){
		Answer answer = Answer.findById(answerId);
		if(answer == null)
			notFound();
		render(user(), answer);
	}
}
