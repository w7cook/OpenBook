package controllers;

import java.util.HashSet;
import java.util.Set;

import models.Answer;
import models.Post;
import models.Question;

public class Questions extends OBController{

	public static void addQuestion(String question, String[] answers){
		Question q = new Question(user(), question);
		q.save();
		for(String s : answers)
			new Answer(q, s).save();
		redirect("/");
	}
}
