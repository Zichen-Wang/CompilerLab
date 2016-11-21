package minijava.typecheck;
import java.util.*;

public class PrintErrorMsg {
	public static Vector<String> error = new Vector<String>();
	public static void addError(String errorMsg, int row, int col) {
		String Msg = "row " + row + " col " + col + ": error: " + errorMsg;
		error.add(Msg);
	}
	public static void printError() {
		int len = error.size();
		for(int i = 0; i < len; i++) {
			System.out.println(error.get(i));
		}
	}
}