package algorithms;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by MattX7 on 04.11.2016.
 *
 * Ref:
 * http://www.orklaert.de/floyd-warshall-algorithmus.php
 * https://www.cs.usfca.edu/~galles/visualization/Floyd.html
 */
public class FloydWarshall implements Algorithm {
    private Graph graph;
    private int[][] distances;
    private List<Node> nodes;

    public void init(Graph graph) {
        this.graph = graph;
        for (Node node : graph.getEachNode()) {
            this.nodes.add(node);
        }

        int n = graph.getNodeCount();
        distances = new int[n][n];

        Iterator<Node> nodesForI = graph.getNodeIterator();
        Iterator<Node> nodesForJ = graph.getNodeIterator();
        for (int i = 0; i <= n; i++) {
            Node NodeI = nodesForI.next();
            for (int j = 0; j <= n; j++) {
                Node NodeJ = nodesForJ.next();
                if (NodeI == NodeJ) {
                    distances[i][j] = 0;
                } else if (NodeI.hasEdgeBetween(NodeJ)) {
                    distances[i][j] = NodeI.getEdgeBetween(NodeJ).getAttribute("weight");
                } else {
                    distances[i][j] = Integer.MAX_VALUE;
                }
            }
        }
    }

    public void compute() {
        Iterator<Node> nodeIter = graph.getNodeIterator();

        while (nodeIter.hasNext()) { // Über alle Nodes iterieren
            Node curr = nodeIter.next(); // Aktuellen Node setzten
            if (hasIncomingEdges(curr)) { // Wenn dieser eingehende Edges hat....
                List<Node> incomingNodes = getIncomingNodes(curr); // ...dann finde alle eingehenden Knoten
                List<Node> getTargetNodes = getTargetNodes(curr); // ...
                for (Node incomingNode : incomingNodes) {
                    int incomeWeight = incomingNode.getEdgeBetween(curr).getAttribute("weight");
                    for (Node outgoingNode : getTargetNodes) {
                        int outWeight = curr.getEdgeBetween(outgoingNode).getAttribute("weight");
                        int pathWeight = incomeWeight + outWeight;
                        insertIfSmaller(incomingNode, outgoingNode, pathWeight);
                    }
                }
            }
        }
    }

    /**
     * Inserts a value if it is smaller
     *
     * @param fromNode
     * @param toNode
     * @param value
     */
    @NotNull
    private void insertIfSmaller(@NotNull Node fromNode,
                                 @NotNull Node toNode,
                                 @NotNull Integer value) {
        int x = getIndex(fromNode);
        int y = getIndex(toNode);
        if (value < distances[x][y])
            distances[x][y] = value;

    }

    /**
     * Returns true if node has incoming Edges
     *
     * @param node
     * @return
     */
    @NotNull
    private Boolean hasIncomingEdges(Node node) {
        for (Edge edge : node.getEachEnteringEdge()) {
            if (edge.getSourceNode() == node)
                return true;
        }
        return false;
    }

    /**
     * Returns incoming Nodes
     *
     * @param node
     * @return
     */
    @NotNull
    private List<Node> getIncomingNodes(Node node) {
        List<Node> nodes = new ArrayList<Node>();
        for (Edge edge : node.getEachEnteringEdge()) {
            nodes.add(edge.getSourceNode());
        }
        return nodes;
    }

    /**
     * Return target Nodes
     *
     * @param node
     * @return
     */
    @NotNull
    private List<Node> getTargetNodes(Node node) {
        List<Node> nodes = new ArrayList<Node>();
        for (Edge edge : node.getEachLeavingEdge()) {
            nodes.add(edge.getTargetNode());
        }
        return nodes;
    }

    /**
     * Returns index of a Node
     *
     * @param node
     * @return
     */
    private int getIndex(Node node) {
        int i = nodes.indexOf(node);
        if (i < 0) throw new IllegalArgumentException();
        return i;
    }
}
