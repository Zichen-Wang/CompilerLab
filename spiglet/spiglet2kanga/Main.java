package spiglet.spiglet2kanga;

import spiglet.*;
import spiglet.syntaxtree.Node;
import spiglet.visitor.*;


public class Main {
    public static void main(String [] args) {
        try {
            new SpigletParser(System.in);
            Node root = SpigletParser.Goal();
            root.accept(new BuildBlock(), null);
            root.accept(new BuildGraph(), null);

            AllProc.assignReg();

            root.accept(new Spiglet2Kanga(), null);
        }
        catch (TokenMgrError e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            System.out.println(e.toString());
        }
    }
}
