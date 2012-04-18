/**
 * 
 */
package controllers;

import java.util.Date;

import models.Group;
import models.Post;
import models.User;

/**
 * @author kschlosser
 *
 */
public class Groups extends OBController {

	public static void group(Long id){
		  Group group= id==null ? null : (Group) Group.findById(id);
		  User user = user();
		  render(group,user);
	}
	
	public static void newGroupPost(Long groupId, Long userId, String post_content){
		new Post((Group)Group.findById(groupId), (User)User.findById(userId), post_content).save();
		group(groupId);
	}

}
