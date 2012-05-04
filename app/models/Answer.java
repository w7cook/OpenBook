package models;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.data.validation.Required;

@Entity
public class Answer extends Model {

	public enum Visibility {PRIVATE, FRIENDS, PUBLIC};

	@Required
	public Visibility visibility;
	
	@Required
	public String answer;
	
	@ManyToOne
	public Question question;
	
	@Required
	@JoinTable(name="answers_table")
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<User> usersWhoAnswered;
	
	public Answer(Question question, String answer){
		this.answer = answer;
		this.question = question;
	}
	
	public Answer addUserChoice(User user){
		for(Answer a : question.answers)
			if(a.answeredBy(user))
				return null;
		usersWhoAnswered.add(user);
		user.userAnswers.add(this);
		this.save();
		return this;
	}
	
	public boolean answeredBy(User user){
		return usersWhoAnswered.contains(user);
	}
	
	public int numTimesAnswered(){
		return usersWhoAnswered.size();
	}
	
	public boolean visible(User user) {
		User owner = User.findById(question.owner.id);
	    if(visibility == Visibility.PRIVATE)
	      return user.equals(owner);
	    if(visibility == Visibility.FRIENDS)
	      return user.equals(owner) || user.isFriendsWith(owner);
	    return true;
	  }
	
}
