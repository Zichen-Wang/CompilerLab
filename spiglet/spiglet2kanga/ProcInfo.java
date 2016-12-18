package spiglet.spiglet2kanga;



import minijava.syntaxtree.IntegerLiteral;

import java.util.*;

public class ProcInfo {

    public int stmtNum = 0;

    public int argNum, stackUse = 0, maxCallArg = 0;

    public Hashtable<String, Integer> labelInfo = new Hashtable<>();
    public Graph graph = new Graph();
    public Vector<Integer> callStmt = new Vector<>();

    public Hashtable<Integer, Section> tempSec = new Hashtable<>();


    //临时变量的位置
    public Hashtable<Integer, Integer> regT = new Hashtable<>();
    public Hashtable<Integer, Integer> regS = new Hashtable<>();
    public Hashtable<Integer, Integer> stack = new Hashtable<>();


    //当前活跃的区间，按照结束点从小到大排序
    public PriorityQueue<Section> active;

    //寄存器堆
    public Vector<Boolean> setT;
    public Vector<Boolean> setS;
    public int regSNum = 0;


    //分析curProc过程中每个语句块的出入口活性信息
    public void analyseIN_OUT() {

        HashSet<Vertex> isChanged = new HashSet<>();

        for (Vertex curVer : this.graph.verMap.values()) {
            if(curVer.id != this.stmtNum - 1) {//not END stmt
                isChanged.add(curVer);
            }
        }

        while(isChanged.size() > 0) {

            HashSet<Vertex> newChanged = new HashSet<>();

            for (Vertex curVer : isChanged) {

                curVer.OUT = new HashSet<>();
                for (Vertex curSuccVer : this.graph.succEdgeMap.get(curVer)) {
                    curVer.OUT.addAll(curSuccVer.IN);
                }

                HashSet<Integer> newIN = new HashSet<>();

                newIN.addAll(curVer.OUT);
                newIN.removeAll(curVer.def);
                newIN.addAll(curVer.use);

                if(!newIN.equals(curVer.IN)) {
                    curVer.IN = newIN;
                    for (Vertex curPredVer : this.graph.predEdgeMap.get(curVer)) {
                        newChanged.add(curPredVer);
                    }
                }
            }

            isChanged = newChanged;
        }
    }

    //注：某个TEMP的活性区间会变成好多段，这里为了方便，取最早出现在IN的语句标号和最后一次出现在IN的语句标号的寄存器作为活性区间
    public void tempSection() {

        //只需要看IN即可
        for (Vertex curVer : this.graph.verMap.values()) {
            for (Integer curTempNum : curVer.IN) {
                this.tempSec.get(curTempNum).start = Integer.min(this.tempSec.get(curTempNum).start, curVer.id);
                this.tempSec.get(curTempNum).end = Integer.max(this.tempSec.get(curTempNum).end, curVer.id);
            }
        }

        //记录跨函数调用的临时变量，优先分配到s寄存器中
        for (Integer curCallStmt : this.callStmt) {
            for (Section curTempSection : this.tempSec.values()) {
                if(curTempSection.start <= curCallStmt && curCallStmt < curTempSection.end)
                    curTempSection.hasCrossedCall = true;
            }
        }
    }

    private void allocateRegT(Section curSection, Integer regNum) {
        regT.put(curSection.tempID, regNum);
        setT.set(regNum, false);
    }

    private void allocateRegS(Section curSection, Integer regNum) {
        regS.put(curSection.tempID, regNum);
        setS.set(regNum, false);
    }

    private void freeRegT(Integer regName) {
        setT.set(regName, true);
    }
    
    private void freeRegS(Integer regName) {
        setS.set(regName, true);
    }

    private Integer getMinNumRegT() {
        for(int i = 0; i < setT.size(); i++)
            if(setT.get(i))
                return i;
        return null;
    }

    private Integer getMinNumRegS() {
        for(int i = 0; i < setS.size(); i++)
            if(setS.get(i))
                return i;
        return null;
    }


    //检查是否有结束的活跃区间
    private void expireOldInterval(Section curSection) {
        while(!active.isEmpty()) {
            Section earliestSec = active.peek();
            if(earliestSec.end >= curSection.start)
                return;

            active.poll();

            if(regT.containsKey(earliestSec.tempID)) freeRegT(regT.get(earliestSec.tempID));
            else if(regS.containsKey((earliestSec.tempID))) freeRegS(regS.get(earliestSec.tempID));
        }
    }

    //需要溢出，溢出了一定是8个s寄存器都用满了
    //故为了计算栈中的偏移量，存入的位置为stackUse + 8
    private void spillAtInterval(Section curSection) {
        //寻找最迟结束的
        int latest = curSection.tempID;
        for (Section actSec : active) {
            if(tempSec.get(latest).end < actSec.end) {
                latest = actSec.tempID;
            }
        }

        if(latest == curSection.tempID) {
            stack.put(curSection.tempID, stackUse + 8);
            stackUse++;
        }
        else {
            if(regT.containsKey(latest)) {
                allocateRegT(curSection, regT.get(latest));
                active.add(curSection);

                active.remove(tempSec.get(latest));
                regT.remove(latest);
                stack.put(latest, stackUse + 8);
                stackUse++;

            }
            else if(regS.containsKey(latest)) {
                allocateRegS(curSection, regS.get(latest));
                active.add(curSection);

                active.remove(tempSec.get(latest));
                regS.remove(latest);
                stack.put(latest, stackUse + 8);
                stackUse++;
            }
        }
    }

    //线性扫描寄存器分配
    public void linearScan() {
        ArrayList<Section> sections = new ArrayList<>();
        for (Section curSection : this.tempSec.values()) {
            sections.add(curSection);
        }
        sections.sort(Section::compareAsStart);


        active = new PriorityQueue<>(Section::compareAsEnd);
        setT = new Vector<>();
        setS = new Vector<>();
        for (int i = 0; i < 10; i++) {
            setT.add(true);
        }

        for (int i = 0; i < 8; i++) {
            setS.add(true);
        }


        for (Section curSection : sections) {

            expireOldInterval(curSection);
            Integer minT = getMinNumRegT(), minS = getMinNumRegS();
            if(curSection.hasCrossedCall) {
                if(minS == null)
                    spillAtInterval(curSection);
                else {
                    allocateRegS(curSection, minS);
                    active.add(curSection);
                    regSNum = Integer.max(minS + 1, regSNum);
                }
            }
            else {
                if(minT == null) {
                    if(minS == null) {
                        spillAtInterval(curSection);
                    }
                    else {
                        allocateRegS(curSection, minS);
                        active.add(curSection);
                        regSNum = Integer.max(minS + 1, regSNum);
                    }
                }
                else {
                    allocateRegT(curSection, minT);
                    active.add(curSection);
                }
            }
        }

        stackUse += regSNum;
    }

}
