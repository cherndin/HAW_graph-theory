package algorithms;

import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 23.10.2016.
 */
public class BreadthFirstSearch implements Algorithm {
    private static Logger logger = Logger.getLogger(BreadthFirstSearch.class);
    // TODO Logging
    //BFS uses Queue data structure
    public int steps = -1;
    private Graph graph;
    private Node source;
    private Node target;

    public void init(Graph graph) {
        this.graph = graph;
    }

    public void compute() {
        reset();
        LinkedList<Node> queue = new LinkedList<Node>();
        queue.add(tag(source,-1));
        while(!queue.isEmpty()){
            Node node = queue.getFirst();
            queue.addAll(getUntaggedNeighborsAndTagThem(node));
            queue.remove(node);
        }
        if (isTargetTagged()){
         steps = target.getAttribute("steps");
        }

    }

    private void reset() {
        this.steps = -1;
        for (Node node:graph.getEachNode()){
            node.setAttribute("steps","-1");
        }
    }

    public void setSourceAndTarget(Node s, Node t) {
        this.source = s;
        this.target = t;
    }

    /**
     * All untagged nodes that ar neighbors.
     *
     * @param node
     * @return isEmpty if no neighbors are left.
     */
    private List<Node> getUntaggedNeighborsAndTagThem(Node node) {
        List<Node> newTaggedNeighbors = new ArrayList<Node>();
        Iterator<Node> nodeIterator = node.getNeighborNodeIterator();
        while (nodeIterator.hasNext()) {
            Node next = nodeIterator.next();
            Integer step = Integer.valueOf(node.getAttribute("steps").toString());
            if (next.getAttribute("steps").toString().equals("-1")) {
                newTaggedNeighbors.add(
                        tag(next, Integer.valueOf(node.getAttribute("steps").toString())));
            }
        }
        return newTaggedNeighbors;
    }


    private Node tag(Node node, int steps) {
        node.setAttribute("steps", steps+1);
        return node;
    }


    public boolean isTargetTagged() {
        return (target.getAttribute("steps") != null);
    }

    public static void main(String[] args) {
        Graph graph = new SingleGraph("testSave");
        graph.addNode("1");
        graph.addNode("2");
        graph.addNode("3");
        graph.addNode("4");

        graph.addEdge("12", "1", "2");
        graph.addEdge("23", "2", "3");
        graph.addEdge("34", "3", "4");
        graph.display();
    }
}
