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
		Post thePost= new Post((User)User.findById(userId), new Date().toString(), post_content).save();
		Group theGroup= Group.findById(groupId);
		theGroup.addPost(thePost);
		System.err.println("Posts: "+theGroup.getPosts().size());
		theGroup.save();
		group(groupId);
	}

}
