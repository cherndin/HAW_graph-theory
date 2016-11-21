package algorithms;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by MattX7 on 04.11.2016.
 * <p>
 * Ref:
 * http://www.orklaert.de/floyd-warshall-algorithmus.php
 * https://www-m9.ma.tum.de/graph-algorithms/spp-floyd-warshall/index_de.html (gute Erklärung)
 * https://www.cs.usfca.edu/~galles/visualization/Floyd.html
 */
public class FloydWarshall implements Algorithm {

    Double distance;
    Integer hits = 0;
    public int steps = -1;
    static boolean preview = true;
    private LinkedList<Node> shortestPath;
    private Graph graph;
    private Node source;
    private Node target;
    private int n;
    private double[][] distances;
    private int[][] adjacent;
    private List<Node> nodes = new LinkedList<Node>();
    // TODO (nicht von Padberg nachgefragt) negative Kanten und negative Kreise abfangen/ bearbeiten -> siehte TODOcompute()

    public void init(Graph graph) {
        // Preconditions
        if (!hasWeights(graph))
            throw new IllegalArgumentException("edges must have weights");
        if (!isDirected(graph))
            throw new IllegalArgumentException("graph has to be directed");

        this.graph = graph;
        nodes = ImmutableList.copyOf(graph.getEachNode());
        n = nodes.size();
        hits = n * n; // Zugriffe auf den Graphen TODO stimmt das noch?
        int n = graph.getNodeCount();
        distances = new double[n][n];
        adjacent = new int[n][n];
    }

    public void compute() {
        setUp();
        // Bei jeder Iteration in dieser Schleife versucht der Algorithmus alle (i, j) Wege durch Wege (i, k) und (k, j) verbessern.
        for (int k = 0; k < n; k++) { // Schritte
            for (int i = 0; i < n; i++) { // Spalte

                Double rowValue = distances[i][k];
                if (rowValue.isInfinite()) continue; // Spalte mit Inf. überspringen

                for (int j = 0; j < n; j++) { // Zeile
                    Double columnValue = distances[k][j];
                    if (columnValue.isInfinite()) continue; // Zeile mit Inf. überspringen
                    if (rowValue == 0.0 && columnValue == 0.0) continue; // Zeile==Spalte überspringen

                    distances[i][j] = min(distances[i][j], rowValue + columnValue);// Wenn kleiner, dann ersetzten
                }
            }
            // TODO negative Kreise hier abfangen: https://www-m9.ma.tum.de/graph-algorithms/spp-floyd-warshall/index_de.html
            if (preview) System.out.println("================== " + k + " ======================");
            if (preview) printMatrix();
            if (preview) System.out.println();
        }
        distance = distances[getIndex(source)][getIndex(target)];
    }

    /**
     * Sets source and target before compute()
     *
     * @param source source node
     * @param target target node
     */
    void setSourceAndTarget(@NotNull Node source,
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
    /**
     * @return shortestWay
     */
    public List<Node> getShortestPath() {
        if (hits == 0)
            throw new IllegalArgumentException("do compute before this method");
        LinkedList<Node> ShortestPath = new LinkedList<Node>();
        ShortestPath.add(target);
        Node current = target;
        while (hasPred(current)) {
            shortestPath.add(getPred(current));
            current = getPred(current);
        }
        return Lists.reverse(shortestPath);
    }

    /**
     * initialises arrays for Floyd and Warshall
     */
    private void setUp() {
        Iterator<Node> nodesForI = graph.getNodeIterator();
        for (int i = 0; i < n; i++) {
            Iterator<Node> nodesForJ = graph.getNodeIterator();
            Node NodeI = nodesForI.next();
            for (int j = 0; j < n; j++) {
                Node NodeJ = nodesForJ.next();
                if (NodeI == NodeJ) {
                    distances[i][j] = 0.0;
                } else {
                    if (NodeI.hasEdgeBetween(NodeJ)) {
                        String weight = NodeI.getEdgeBetween(NodeJ).getAttribute("weight").toString();
                        distances[i][j] = Double.parseDouble(weight);
                        adjacent[i][j] = 1;
                    } else {
                        distances[i][j] = Integer.MAX_VALUE;
                    }
                }
            } // TODO andere matrix mit aufbauen
        }
        if (preview) System.out.println("================== Start ======================");
        if (preview) printMatrix();
        if (preview) System.out.println();
    }

    /**
     * Helper method for getShortestPath()
     * check node with hasPred() before this method
     *
     * @param node to check
     * @return Predecessor from node
     * @throws IllegalArgumentException if node has no predecessor
     */
    @NotNull
    private Node getPred(Node node) throws IllegalArgumentException {
        int y = getIndex(node);
        int maxColumn = -1;
        for (int x = 1; x < n; x++) {
            if (distances[x][y] != 0.0 && Double.valueOf(distances[x][y]).isInfinite()) {
                maxColumn = x;
            }
        }
        if (maxColumn == -1)
            throw new IllegalArgumentException();
        return nodes.get(maxColumn);
    }

    /**
     * Helper method for getShortestPath()
     *
     * @param node to check
     * @return true if node has predecessor
     */
    @NotNull
    private Boolean hasPred(Node node) {
        int y = getIndex(node);
        for (int x = 1; x < n; x++) {
            if (distances[x][y] != 0.0 && Double.valueOf(distances[x][y]).isInfinite()) {
                return true;
            }
        }
        return false;

    }

    /**
     * Returns index of a Node
     *
     * @param node from we want to know the index
     * @return index
     * @throws NoSuchElementException no such node in the list
     */
    @NotNull
    private Integer getIndex(@NotNull Node node) {
        int i = nodes.indexOf(node);
        if (i < 0) throw new NoSuchElementException();
        return i;
    }

    /**
     * <b>Precondition</b> Helper Method for init()
     *
     * @return true if all edges have weights
     */
    @NotNull
    private Boolean hasWeights(@NotNull Graph graph) {
        boolean hasWeight = true;
        for (Edge edge : graph.getEachEdge()) {
            hits++;
            if (!edge.hasAttribute("weight"))
                hasWeight = false;
        }
        return hasWeight;
    }

    // TODO könnte man in eine method tuen um eine iteration zu vermeiden

    /**
     * <b>Precondition</b> Helper Method for init()
     * @return true if all edges are directed
     */
    @NotNull
    private Boolean isDirected(@NotNull Graph graph) {
        for (Edge edge : graph.getEachEdge()) {
            hits++;
            if (!edge.isDirected()) return false;
        }
        return true;
    }

    @NotNull
    private Double min(@NotNull Double x, @NotNull Double y) {
        return ((x < y) ? x : y);
    }

    /**
     * Output for the distance matrix
     */
    private void printMatrix() {
        for (Node node : nodes) { // print x nodes
            System.out.print("  \t" + node.getId() + " \t");
        }
        System.out.println();
        Iterator<Node> iterator = nodes.iterator();
        for (double[] distance1 : distances) { // print x nodes
            System.out.print(iterator.next() + " | ");
            for (int j = 0; j < distances.length; j++) { // print x nodes
                System.out.print((distance1[j] == 2.147483647E9 ? "Inf" : distance1[j]) + " \t");
            }
            System.out.println();
        }
    }

    // === MAIN ===

    public static void main(String[] args) throws Exception {
        // Graph aus den Folien
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6
        Graph bspPadberg = new SingleGraph("bspPadberg");
        bspPadberg.addNode("A");
        bspPadberg.addNode("B");
        bspPadberg.addNode("C");
        bspPadberg.addNode("D");
        bspPadberg.addNode("E");
        bspPadberg.addEdge("AB", "A", "B", true).addAttribute("weight", 2.0);
        bspPadberg.addEdge("AC", "A", "C", true).addAttribute("weight", 3.0);
        bspPadberg.addEdge("BD", "B", "D", true).addAttribute("weight", 9.0);
        bspPadberg.addEdge("CB", "C", "B", true).addAttribute("weight", 7.0);
        bspPadberg.addEdge("CE", "C", "E", true).addAttribute("weight", 5.0);
        bspPadberg.addEdge("ED", "E", "D", true).addAttribute("weight", 1.0);

        FloydWarshall floydBspPadberg = new FloydWarshall();
        floydBspPadberg.setSourceAndTarget(bspPadberg.getNode("A"), bspPadberg.getNode("E"));
        floydBspPadberg.init(bspPadberg);
        floydBspPadberg.compute();

//        // Graph from https://www-m9.ma.tum.de/graph-algorithms/spp-floyd-warshall/index_de.html
//        Graph m9 = new SingleGraph("m9");
//        m9.addNode("A");
//        m9.addNode("B");
//        m9.addNode("C");
//        m9.addNode("D");
//        m9.addEdge("AB","A","B",true).addAttribute("weight",3);
//        m9.addEdge("AC","A","C",true).addAttribute("weight",4);
//        m9.addEdge("AD","A","D",true).addAttribute("weight",9);
//        m9.addEdge("BD","B","D",true).addAttribute("weight",5);
//        m9.addEdge("CD","C","D",true).addAttribute("weight",3);
//
//        FloydWarshall floydM9 = new FloydWarshall();
//        floydM9.setSourceAndTarget(m9.getNode("A"), m9.getNode("D"));
//        floydM9.init(m9);
//        floydM9.compute();
    }
}
