package controllers;

import models.*;

public class Security extends Secure.Security {
    public static String connected() {
        return Secure.Security.connected();
    }
}
