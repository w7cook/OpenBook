package controllers;

import play.mvc.*;

@With(Secure.class)
public class Notes extends Controller {

    public static void index() {
        render();
    }

}
