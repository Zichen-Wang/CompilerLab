package minijava.minijava2piglet;

public class PrintPiglet {
	private static int pTab = 0;
	
	public static void print(String s) {
		System.out.print(s);
	}
	
	public static void println(String s) {
		System.out.println(s);
		for(int i = 0; i < pTab; i++)
			System.out.print("\t");
	}
	
	public static void pMain() {
		println("MAIN");
		pTab++;
	}
	
	public static void pBegin() {
		print("BEGIN");
		pTab++;
		println("");
	}
	
	public static void pBeginProcedure(String curClassName, String curMethodName, int curParaNum) {
		if(curParaNum > 20) curParaNum = 20;
		print(curClassName + "_" + curMethodName + " [ " + curParaNum + " ] ");
		pTab++;
		println("");
	}
	
	public static void pReturn() {
		print("RETURN ");
		pTab--;
	}
	
	public static void pEndProcedure() {
		pTab--;
		println("END");
		println("");
	}
	
	public static void pEnd() {
		print("END");
	}
	
}