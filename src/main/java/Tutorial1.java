import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

/**
 * Created by MattX7 on 18.10.2016.
 */
public class Tutorial1 {
    public static void main(String args[]) {
        Graph graph = new SingleGraph("Tutorial 1");
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");
        graph.display();
        System.out.println("Nodes");
        for(Node n:graph) {
            System.out.println(n.getId());
        }
        System.out.println("Graph");
        for(Edge e:graph.getEachEdge()) {
            System.out.println(e.getId());
        }
    }
}
