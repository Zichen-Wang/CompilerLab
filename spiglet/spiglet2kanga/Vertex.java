package spiglet.spiglet2kanga;

import java.util.HashSet;

public class Vertex {
    public Integer id;

    public HashSet<Integer> def, use, IN, OUT;

    public Vertex(Integer curId) {
        id = curId;
        def = new HashSet<>();
        use = new HashSet<>();
        IN = new HashSet<>();
        OUT = new HashSet<>();
    }

}
