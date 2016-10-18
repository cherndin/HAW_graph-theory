import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by MattX7 on 18.10.2016.
 */
public class IOGraph {
    private static BufferedWriter bw;

    public static void output(Graph graph, String filename) throws IOException{

        bw = new BufferedWriter(new FileWriter(filename +".gka"));

        for(Node n:graph) {
            System.out.println(n.getId());
        }

        for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }

        bw.write("test test test");
        bw.write("tset tset tset");

        bw.close();
    }
}
