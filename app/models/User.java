package models;

import java.util.*;

import javax.persistence.*;

import java.math.BigInteger;
import java.security.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;




/**
 * User entity managed by Ebean
 */
@Entity 
@Table(name="account")
public class User extends Model {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
    @Constraints.Required
    @Formats.NonEmpty
    public String email;
    
    @Constraints.Required
    public String password;

    @Constraints.Required
    public int role;
    
    // -- Queries
    
    public static Model.Finder<String,User> find = new Model.Finder(String.class, User.class);
    
    /**
     * Retrieve all users.
     */
    public static List<User> findAll() {
        return find.all();
    }
    
    /**
     * Retrieve a User from email.
     */
    public static User findByEmail(String email) {
        return find.where().eq("email", email).findUnique();
    }
    
    /*
    *  Add user to database
    */
    public static void create(User user) {
        user.save();
    }
    
    /**
     * Authenticate a User.
     */
    public static User authenticate(String email, String password) {
    	String hashword = null;
    	try {
    			MessageDigest md = MessageDigest.getInstance("MD5");
    		   md.update(password.getBytes());
    		    BigInteger hash = new BigInteger(1, md.digest());
    		   hashword = hash.toString(16);
    		} catch (NoSuchAlgorithmException nsae) {
    		// ignore
    		}
    	
        return find.where()
            .eq("email", email)
            .eq("password", hashword)
            .findUnique();
    }
    
    /**
     * Registrate a User.
     */
    public static String registrate(String email, String password, String passwordConfitmation) {
    	if(find.where().eq("email", email).findUnique() != null) return "This email already been used";
    	if(!password.equals(passwordConfitmation)) return "Invalid confirmation password";
    	if(password.length() < 7) return "Password have to more than 7 characters";
    	
    	String hashword = null;
    		try{
    			MessageDigest md = MessageDigest.getInstance("MD5");
    			md.update(password.getBytes());
    			BigInteger hash = new BigInteger(1, md.digest());
    			hashword = hash.toString(16);
    		} catch (NoSuchAlgorithmException nsae) {
    				// ignore
    		}
		
    	User u = new User();
    	u.email=email;
    	u.password= hashword;
    	u.role = 1;
    	//  send email
    	
    	create(u);
     return "Thank You for registration";
    }
    
    // --
    
    public String toString() {
        return "User(" + email + ")";
    }

}
