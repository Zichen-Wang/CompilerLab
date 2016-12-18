package spiglet.spiglet2kanga;


public class PrintKanga {

    private static boolean pTab = false;

    public static void print(String s) {
        System.out.print(s);
    }

    public static void println(String s) {
        if(pTab) System.out.print("\t\t");
        System.out.println(s);
    }


    public static void pBegin(String procName, int argNum, int stackUse, int maxCallArg) {
        println(procName + " [" + argNum + "][" + stackUse + "][" + maxCallArg + "]");
        pTab = true;
    }


    public static void pEnd() {
        pTab = false;
        println("END");
    }
}