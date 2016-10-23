package algorithms;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 23.10.2016.
 */
public class BreadthFirstSearch implements Algorithm {
    //BFS uses Queue data structure
    private int steps = -1;
    private Graph graph;
    private Node source;
    private Node target;


    public void init(Graph graph) {
        this.graph = graph;
        this.source = graph.getNode(0);
        this.target = graph.getNode(graph.getEdgeCount() - 1);
    }

    public void compute() {
        /*
         * Man kennzeichne den Knoten s mit 0 und setze i = 0.
         */
        reset();
        tag(source, 0);
        int lvl = 1;
        List<Node> newTaggedNeighbors;
        /* Man ermittle alle nichtgekennzeichneten Knoten in G, die zu dem mit i
         * gekennzeichneten Knoten benachbart sind.
         *  Falls es derartige Knoten nicht gibt, ist t nicht mit s ¨uber einen Weg
         * verbunden.
         * Falls es derartige Knoten gibt, sind sie mit i + 1 zu kennzeichnen.
        */
        newTaggedNeighbors = getUntaggedNeighborsAndTagThem(source, lvl);
        if (!isTargetTagged()) {
            for (Node node : newTaggedNeighbors) {
                newTaggedNeighbors.addAll(getUntaggedNeighborsAndTagThem(node, lvl));
                if (newTaggedNeighbors.isEmpty() && !isTargetTagged()) { // only if t and s could be connected
                    System.err.println("Target can not be reached!");
                    break;
                }

            }
            lvl++;
        }else
            steps = lvl;
    }

    private void reset() {
        this.steps = -1;
        for (Node node:graph.getEachNode()){
            node.setAttribute("BFS",-1);
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
    private List<Node> getUntaggedNeighborsAndTagThem(Node node, int lvl) {
        List<Node> newTaggedNeighbors = new ArrayList<Node>() {
        };
        Iterator<Node> nodeIterator = node.getNeighborNodeIterator();
        while (nodeIterator.hasNext()) {
            Node next = nodeIterator.next();
            if (next.getAttribute("BFS") == null) {
                tag(next, lvl);
                newTaggedNeighbors.add(next);
            }
        }
        return newTaggedNeighbors;
    }

    /**
     * Tags a node.
     *
     * @param node
     * @param lvl
     */
    private void tag(Node node, int lvl) {
        node.setAttribute("BFS", lvl);
    }


    public boolean isTargetTagged() {
        return (target.getAttribute("BFS") != null);
    }
}
