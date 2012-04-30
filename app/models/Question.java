package models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.data.validation.Required;

@Entity
public class Question extends Post{

	@Required
	@ManyToOne
	@JoinTable(name="QuestionsToUser")
	public User owner;
	
	@Required
	@OneToMany(mappedBy="question")
	public Set<Answer> answers;

	public Question(User author, String question) {
		super(author, author, question);
		this.owner = author;
	}
	
	public void addUserAnswer(User user, Answer answer){
		if(!answer.usersWhoAnswered.contains(user))
			answer.addUserChoice(user);
		this.save();
	}

}
