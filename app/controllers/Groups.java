/**
 * 
 */
package controllers;

import models.Group;
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

}
