/**
 * 
 */
package controllers;

import java.util.Date;

import models.Group;
import models.Post;
import models.User;
import models.TimelineModel;
import java.util.Vector;
import models.User;

/**
 * @author kschlosser
 *
 */
public class Groups extends OBController {

	public static void group(Long id){
		  Group group= id==null ? null : (Group) Group.findById(id);
		  User _user = user();
		  User _currentUser = user();
		  render(group,_user,_currentUser);
	}
	
	public static void newGroupPost(Long groupId, Long userId, String post_content){
		//Variable declarations/init
		User user = (User)User.findById(userId);
		Group group = (Group)Group.findById(groupId);
		Vector<User> participants = new Vector<User>();
		participants.add(user);
		
		//Post to group
		Post p = new Post((Group)Group.findById(groupId), (User)User.findById(userId), post_content).save();

		//Add TimelineEvent to Timeline
		if (user.timeline != null){
		user.timeline.addEvent(p.id, TimelineModel.Action.CREATE, participants, user.first_name + " " + user.last_name + " posted in " + group.groupName);
		user.timeline.save();
	}

		group(groupId);
	}

}
