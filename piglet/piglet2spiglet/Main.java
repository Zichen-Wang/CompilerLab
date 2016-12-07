package piglet.piglet2spiglet;


import piglet.*;
import piglet.syntaxtree.Node;
import piglet.visitor.*;

public class Main {
	public static void main(String [] args) {
		try {
			new PigletParser(System.in);
			Node root = PigletParser.Goal();
			root.accept(new CountTmpNum(), null);
			root.accept(new Piglet2Spiglet(), null);
		}
		catch (TokenMgrError e) {
			e.printStackTrace();
		}
	    catch (ParseException e) {
	        System.out.println(e.toString());
	    }
	}
}