package controllers;

import models.*;

public class Security extends Secure.Security {
    public static String connected() {
        return Secure.Security.connected();
    }
    
    public static boolean authenticate(String username, String password) {
    	if (User.connect(username, password) == null) {
    		return false;
    	}
    	return true;
    }
}
