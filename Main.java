import kanga.KangaParser;
import kanga.visitor.Kanga2MIPS;
import minijava.*;
import minijava.ParseException;
import minijava.TokenMgrError;
import minijava.typecheck.*;
import minijava.visitor.*;
import piglet.visitor.CountTmpNum;
import piglet.visitor.Piglet2Spiglet;
import piglet.*;
import spiglet.SpigletParser;
import spiglet.spiglet2kanga.AllProc;
import spiglet.visitor.*;

import java.io.*;

public class Main {
    public static void main(String [] args) {
        if(args.length != 1) {
            System.out.println("Usage: java -jar Compiler.jar *.java");
        }
        else if(!args[0].substring(args[0].lastIndexOf('.')).equals(".java")) {
            System.out.println("Usage: java -jar Compiler.jar *.java");
        }
        else {
            String fileName = args[0].substring(0, args[0].lastIndexOf('.'));
            try {
                System.setIn(new FileInputStream(args[0]));
                new MiniJavaParser(System.in);
                minijava.syntaxtree.Node rootJava = MiniJavaParser.Goal();
                AllClasses myClasses = new AllClasses();
                rootJava.accept(new BuildSymbolTable(), myClasses);
                rootJava.accept(new DeclarationCheck(), myClasses);
                rootJava.accept(new TypeCheck(), myClasses);
                if (PrintErrorMsg.error.size() > 0) {
                    System.out.println("Type error");
                    PrintErrorMsg.printError();
                } else {
                    FileOutputStream outFile = null;
                    outFile = new FileOutputStream(fileName + ".pg");
                    System.setOut(new PrintStream(outFile));
                    rootJava.accept(new Minijava2Piglet(), myClasses);

                    System.setIn(new FileInputStream(fileName + ".pg"));
                    outFile = new FileOutputStream(fileName + ".spg");
                    System.setOut(new PrintStream(outFile));
                    new PigletParser(System.in);
                    piglet.syntaxtree.Node rootPiglet = PigletParser.Goal();
                    rootPiglet.accept(new CountTmpNum(), null);
                    rootPiglet.accept(new Piglet2Spiglet(), null);

                    System.setIn(new FileInputStream(fileName + ".spg"));
                    outFile= new FileOutputStream(fileName + ".kg");
                    System.setOut(new PrintStream(outFile));
                    new SpigletParser(System.in);
                    spiglet.syntaxtree.Node rootSpiglet = SpigletParser.Goal();
                    rootSpiglet.accept(new BuildBlock(), null);
                    rootSpiglet.accept(new BuildGraph(), null);
                    AllProc.assignReg();
                    rootSpiglet.accept(new Spiglet2Kanga(), null);

                    System.setIn(new FileInputStream(fileName + ".kg"));
                    outFile = new FileOutputStream(fileName + ".s");
                    System.setOut(new PrintStream(outFile));
                    new KangaParser(System.in);
                    kanga.syntaxtree.Node rootKanga = KangaParser.Goal();
                    rootKanga.accept(new Kanga2MIPS(), null);

                }
            } catch (TokenMgrError | spiglet.ParseException | kanga.ParseException
                    | piglet.ParseException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                System.out.println(e.toString());
            }
        }
    }
}
