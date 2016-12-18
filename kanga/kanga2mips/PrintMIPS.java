package kanga.kanga2mips;

public class PrintMIPS {

    private static boolean pTab = false;


    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        if(pTab) System.out.print("\t\t\t");
        System.out.println(s);
    }


    public static void pBegin(String procName) {
        pTab = true;
        println(".text");
        println(".globl " + procName);
        pTab = false;
        println(procName + ":");
        pTab = true;
    }


    public static void pEnd() {
        pTab = false;
        println("");
    }
}