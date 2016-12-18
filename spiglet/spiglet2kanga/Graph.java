package spiglet.spiglet2kanga;


import java.util.Hashtable;
import java.util.Vector;

public class Graph {

    public Hashtable<Integer, Vertex> verMap = new Hashtable<>();

    public Hashtable<Vertex, Vector<Vertex>> succEdgeMap = new Hashtable<>();
    public Hashtable<Vertex, Vector<Vertex>> predEdgeMap = new Hashtable<>();


    public void insertNode(int num) {
        Vertex curVertex = new Vertex(num);
        verMap.put(num, curVertex);
        succEdgeMap.put(curVertex, new Vector<>());
        predEdgeMap.put(curVertex, new Vector<>());
    }

    public void insertEdge(int S, int T) {
        Vertex curS = verMap.get(S), curT = verMap.get(T);
        succEdgeMap.get(curS).add(curT);
        predEdgeMap.get(curT).add(curS);
    }

    public void insertDef(int num, int curLeftTemp) {
        Vertex curVertex = verMap.get(num);
        curVertex.def.add(curLeftTemp);
    }

    public void insertUse(int num, int curRightTemp) {
        Vertex curVertex = verMap.get(num);
        curVertex.use.add(curRightTemp);
    }


}
