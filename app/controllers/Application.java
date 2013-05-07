package controllers;

import play.mvc.*;
import play.data.*;
import models.*;
import static play.data.Form.*;


public class Application extends Controller {
	// static Form< Task> taskForm = Form.form(Task.class);
	 static Form< Label> labelForm = Form.form(Label.class);
	 static Boolean is_setCookie=false;
	 static String user = null;
	 static User userAut=null;
	 
	 
	    // -- Authentication
	    public static class Login {
	        
	        public String email;
	        public String password;

	  
	        public String validate() {    
	            if((userAut = User.authenticate(email, password)) == null) {
	            	return "  Invalid email or password";
	            }       
	            return null;
	        }
	        
	    }
	    

	 // -- Registration
	    public static class Regist {
	        
	        public String email;
	        public String password;
	        public String passwordConfirmation;
	  
	        public String validate() {    
	            return User.registrate(email, password, passwordConfirmation);
	        }
	        
	    }

	    /**
	     * Home page.
	     */
		  public static Result index() {
			  if( user == null) { 
				  
				  user = session("connected");
				  
				  session("connected", "is_cookie_set"); 
					  is_setCookie=true;
			  }else{
				  is_setCookie=false; 
			  }
			  
		  return ok(
	        		views.html.login.render(form(Login.class), is_setCookie)
	        );
		  }
	    

	    
	    /**
	     * Handle registration form submission.
	     */
	    public static Result authenticate() {
	        Form<Login>  loginForm = form(Login.class).bindFromRequest();
	        if(loginForm.hasErrors()){	
	        	return badRequest(views.html.login.render(loginForm, is_setCookie));
	        } else {
	            session("email", loginForm.get().email);
	            if(userAut.role == 0){
		            return redirect(
		            		routes.Application.admin()
		     		);
	            }
	            return redirect(
	            		routes.Application.tasks()
	            );
	        }
	    }
	    
	    
	    public static Result registration() {
	        Form<Regist> registForm = form(Regist.class).bindFromRequest();
	        	return badRequest(views.html.register.render(registForm));
	    }
	    
	    /**
	     * Logout and clean the session.
	     */
	    public static Result logout() {
	        session().clear();
	        flash("success_login", "You've been logged out");
	        return redirect(
	            routes.Application.index()
	        );
	    }
	 ///---------------------------------------   

	  
	  public static Result tasks() {
		  return ok(views.html.task.render(Task.all() , labelForm, userAut.email));
	 }
	  
	  public static Result newTask() {

		Form<Label> filledForm = labelForm.bindFromRequest();
		  if(filledForm.hasErrors()) {
			return badRequest(
			  views.html.task.render(Task.all(), labelForm, userAut.email)
			);
		  } else {
			  Task ts = new Task();
			  ts.label = filledForm.get().label;
			  ts.IdUserEmail = userAut.email;
			Task.create(ts);
			return redirect(routes.Application.tasks());  
		  }
	  }
	  
	  public static Result deleteTask(Long id) {
		  Task.delete(id);
		return redirect(routes.Application.tasks());
	  }
	  
	  
	 //----------------------------------------------
	  public static Result admin() {
		  return ok(views.html.admin.render(User.findAll()) );
	 }
	  
	  public static Result deleteUser(String email) {
		  User.findByEmail(email).delete();
		return redirect(routes.Application.admin());
	  }
  
	  
	  
}
