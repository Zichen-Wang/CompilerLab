package kanga.kanga2mips;


import kanga.*;
import kanga.syntaxtree.*;
import kanga.visitor.*;

public class Main {
    public static void main(String [] args) {
        try {
            new KangaParser(System.in);
            Node root = KangaParser.Goal();
            root.accept(new Kanga2MIPS(), null);
        }
        catch (TokenMgrError e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            System.out.println(e.toString());
        }
    }
}
