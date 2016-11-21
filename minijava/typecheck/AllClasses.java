package minijava.typecheck;
import java.util.*;

public class AllClasses extends AllType {
	public Hashtable<String, MyClass> classes = new Hashtable<String, MyClass>();
	public String insertClass(String className, MyClass obj) {
		String errorMsg = null;
		if(classes.get(className) != null) {
			errorMsg = "insert Class Error";
		}
		else {
			classes.put(className, obj);
		}
		return errorMsg;
	}
	public MyClass getMyClass(String className) {
		return classes.get(className);
	}
	
}