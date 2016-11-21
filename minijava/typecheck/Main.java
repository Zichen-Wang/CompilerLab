package minijava.typecheck;

import minijava.*;
import minijava.syntaxtree.Node;
import minijava.visitor.*;

public class Main {
	public static void main(String [] args) {
		try {
			new MiniJavaParser(System.in);
			Node root = MiniJavaParser.Goal();
			AllClasses myClasses = new AllClasses();
			root.accept(new BuildSymbolTable(), myClasses);
			root.accept(new DeclarationCheck(), myClasses);
			root.accept(new TypeCheck(), myClasses);
			if(PrintErrorMsg.error.size() > 0) {
				System.out.println("Type error");
				PrintErrorMsg.printError();
			}
			else {
				System.out.println("Program type checked successfully");
			}
		}
		catch (TokenMgrError e) {
			e.printStackTrace();
		}
	    catch (ParseException e) {
	        System.out.println(e.toString());
	    }
	}
}