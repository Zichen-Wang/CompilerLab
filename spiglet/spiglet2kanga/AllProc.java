package spiglet.spiglet2kanga;

import java.util.*;

public class AllProc {

    public static Hashtable<String, ProcInfo> allProc = new Hashtable<>();


    public static void insert(String curProcName, ProcInfo curProc) {
        allProc.put(curProcName, curProc);
    }

    public static ProcInfo get(String curProcName) {
        return allProc.get(curProcName);
    }


    //Main的入口函数
    public static void assignReg() {
        for (ProcInfo curProc: allProc.values()) {
            curProc.analyseIN_OUT();
            curProc.tempSection();
            curProc.linearScan();
        }
    }

}
