package piglet.piglet2spiglet;

import java.util.*;

public class PrintSpiglet {
	//记录每个过程内的寄存器的当前可以使用的标号
	public static Hashtable<String, Integer> TmpNum = new Hashtable<String, Integer>();
	
	private static boolean pTab = false;
	
	public static void print(String s) {
		System.out.print(s);
	}
	
	public static void println(String s) {
		if(pTab) System.out.print("\t\t");
		System.out.println(s);
	}
	
	public static void pMain() {
		println("MAIN");
		pTab = true;
	}
	
	public static void pBegin() {
		println("BEGIN");
		pTab = true;
	}
	
	public static void pReturn() {
		print("RETURN");
		System.out.println();
	}
	
	public static void pEnd() {
		pTab = false;
		println("END");
	}
}