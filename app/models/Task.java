package models;

import java.util.*;

import play.db.ebean.*;
import play.data.validation.Constraints.*;
import play.mvc.Controller;

import javax.persistence.*;

@Entity
public class Task extends Model{
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

  @Id
  public Long id;
  
  @Required
  public String label;
  

  public String IdUserEmail; //= "kosciesza16@wp.pl";
  
  public static Finder<Long,Task> find = new Finder<Long, Task>(
    Long.class, Task.class
  );
  
  public static List<Task> all() {
	  Object key = "email";
	  String var = Controller.session().get(key);

    return find.where().eq("IdUserEmail", var).findList();
  }
  
  public static void create(Task task) {
    task.save();
  }
  
  public static void delete(Long id) {
    find.ref(id).delete();
  }
    
}
