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
		//Post to group
		Post p = new Post((Group)Group.findById(groupId), (User)User.findById(userId), post_content).save();

		User user = (User)User.findById(userId);
		Group group = (Group)Group.findById(groupId);
		//Add TimelineEvent to Timeline
		if (user.timeline != null)
		    user.timeline.addEvent(user.id, group, TimelineModel.Action.CREATE, new Vector<User>(), user.first_name + " " + user.last_name + " posted in " + group.groupName);
	
		group(groupId);
	}

}
