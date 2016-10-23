package algorithms;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 23.10.2016.
 */
public class BreadthFirstSearch implements Algorithm {
    //BFS uses Queue data structure
    private static List<Node> nodes = new LinkedList<Node>();
    private Graph theGraph;
    private Node first;
    private Node last;

    public void init(Graph graph) {
        this.theGraph = graph;

    }

    public void compute() {

    }

    public void setSourceAndTarget(Node source, Node target){

    }
}
