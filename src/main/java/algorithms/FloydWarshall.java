package algorithms;

import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 04.11.2016.
 *
 * Ref:
 * http://www.orklaert.de/floyd-warshall-algorithmus.php
 * https://www.cs.usfca.edu/~galles/visualization/Floyd.html
 */
public class FloydWarshall implements Algorithm {

    public Double distance;
    private Graph graph;
    private Node source;
    private Node target;
    private  double[][] distances;
    private List<Node> nodes = new LinkedList<Node>();

    public void init(Graph graph) {
        if (!hasWeights(graph))
            throw new IllegalArgumentException();
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException();

        this.graph = graph;
        setSourceAndTarget(graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
        for (Node node : graph.getEachNode()) {
            nodes.add(node);
        }

        int n = graph.getNodeCount();
        distances = new double[n][n];

        Iterator<Node> nodesForI = graph.getNodeIterator();
        Iterator<Node> nodesForJ = graph.getNodeIterator();
        for (int i = 0; i < n; i++) {
            Node NodeI = nodesForI.next();
            for (int j = 0; j < n; j++) {
                Node NodeJ = nodesForJ.next();
                if (NodeI == NodeJ) {
                    distances[i][j] = 0.0;
                } else if (NodeI.hasEdgeBetween(NodeJ)) {
                    distances[i][j] = NodeI.getEdgeBetween(NodeJ).getAttribute("weight");
                } else {
                    distances[i][j] = Integer.MAX_VALUE;
                }
            }
            nodesForJ = graph.getNodeIterator();
        }
    }

    public void compute() {
        Iterator<Node> nodeIter = graph.getNodeIterator();

        while (nodeIter.hasNext()) { // Über alle Nodes iterieren
            Node curr = nodeIter.next(); // Aktuellen Node setzten
            if (hasIncomingEdges(curr)) { // Wenn dieser eingehende Edges hat....
                List<Node> incomingNodes = getIncomingNodes(curr); // ...dann finde alle eingehenden Knoten
                List<Node> getTargetNodes = getTargetNodes(curr); // ... magic
                for (Node incomingNode : incomingNodes) {
                    double incomeWeight = (double) Integer.MAX_VALUE;
                    if (incomingNode != curr) { //check if not the same
                        incomeWeight = incomingNode.getEdgeBetween(curr).getAttribute("weight");
                    }
                    for (Node outgoingNode : getTargetNodes) {
                        double outWeight = (double) Integer.MAX_VALUE;
                        if (outgoingNode != curr) { //check if not the same
                            outWeight = curr.getEdgeBetween(outgoingNode).getAttribute("weight");
                        }
                        double pathWeight = incomeWeight + outWeight;
                        insertIfSmaller(incomingNode, outgoingNode, pathWeight);
                    }
                }
            }
        }
        distance = distances[source.getIndex()][target.getIndex()];
    }

    /**
     * Sets source and target before compute()
     *
     * @param source source node
     * @param target target node
     */
    public void setSourceAndTarget(@NotNull Node source,
                                   @NotNull Node target) {
        if (this.source != null && this.source.hasAttribute("title"))
            this.source.removeAttribute("title");
        if (this.target != null && this.target.hasAttribute("title"))
            this.target.removeAttribute("title");
        this.source = source;
        this.target = target;
        source.setAttribute("title", "source");
        target.setAttribute("title", "target");
    }

    @NotNull
    private Boolean hasWeights(@NotNull Graph graph) {
        boolean hasWeight = true;
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.hasAttribute("weight"))
                hasWeight = false;
        }
        return hasWeight;
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
                                 @NotNull Double value) {
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

    // === MAIN ===

    public static void main(String[] args) throws Exception {
// Graph aus den Folien
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6
        Graph graph = new SingleGraph("graph");

        graph.addNode("v1");
        graph.addNode("v2");
        graph.addNode("v3");
        graph.addNode("v4");
        graph.addNode("v5");
        graph.addNode("v6");

        graph.addEdge("v1v2", "v1", "v2").addAttribute("weight", 1.0);
        graph.addEdge("v1v6", "v1", "v6").addAttribute("weight", 3.0);
        graph.addEdge("v2v3", "v2", "v3").addAttribute("weight", 5.0);
        graph.addEdge("v2v5", "v2", "v5").addAttribute("weight", 2.0);
        graph.addEdge("v2v6", "v2", "v6").addAttribute("weight", 3.0);
        graph.addEdge("v3v6", "v3", "v6").addAttribute("weight", 2.0);
        graph.addEdge("v3v5", "v3", "v5").addAttribute("weight", 2.0);
        graph.addEdge("v3v4", "v3", "v4").addAttribute("weight", 1.0);
        graph.addEdge("v5v4", "v5", "v4").addAttribute("weight", 3.0);
        graph.addEdge("v5v6", "v5", "v6").addAttribute("weight", 1.0);

        FloydWarshall floyd = new FloydWarshall();
        floyd.init(graph);
        floyd.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        floyd.compute();
    }
}
