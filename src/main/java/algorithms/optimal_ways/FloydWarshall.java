package algorithms.optimal_ways;

import algorithms.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import helper.GraphUtil;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
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
    private static Logger logger = Logger.getLogger(FloydWarshall.class);
    static boolean preview = true;

    Double distance;
    Integer hits = 0;

    private Graph graph;
    private Node source;
    private Node target;
    private int n;
    private Double[][] distances;
    private Integer[][] transits;
    private List<Node> nodes = new LinkedList<Node>();

    /**
     * Initialization of the algorithm. This method has to be called before the
     * {@link #compute()} method to initialize or reset the algorithm according
     * to the new given graph.
     *
     * @param graph The graph this algorithm is using.
     */
    public void init(Graph graph) throws IllegalArgumentException {
        Preconditions.mustHaveWeights(graph);
        Preconditions.mustHaveDirectedEdges(graph);

        this.graph = graph;
        nodes = ImmutableList.copyOf(graph.getEachNode());
        n = nodes.size();
        hits = n * n; // Zugriffe auf den Graphen TODO stimmt das noch?
        int n = graph.getNodeCount();
        distances = new Double[n][n];
        transits = new Integer[n][n];
    }

    /**
     * Run the algorithm. The {@link #init(Graph)} method has to be called
     * before computing.
     *
     * @see #init(Graph)
     */
    public void compute() {
        // Preconditions
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException("Attributes are missing");
        // Implementation
        setUp();
        // Bei jeder Iteration in dieser Schleife versucht der Algorithmus alle (i, j) Wege durch Wege (i, k) und (k, j) verbessern.
        for (int k = 0; k < n; k++) { // Schritte
            for (int i = 0; i < n; i++) { // Spalte

                if (i == k) continue; // Kann mit sich selbst addiert nicht kleiner sein
                Double rowValue = distances[i][k];
                if (rowValue.isInfinite()) continue; // Spalte mit Inf. überspringen

                for (int j = 0; j < n; j++) { // Zeile

                    if (j == k) continue;
                    Double columnValue = distances[k][j];
                    if (columnValue.isInfinite()) continue; // Zeile mit Inf. überspringen

                    double oldDistance = distances[i][j];
                    distances[i][j] = min(distances[i][j], rowValue + columnValue);// Wenn kleiner, dann ersetzten

                    if (oldDistance != distances[i][j]) // Falls Dij verändert wurde, setzt Tij := k
                        transits[i][j] = k;

                    if (distances[i][i] < 0) // Kreis mit negativer Länge gefunden
                        throw new IllegalArgumentException("Graph has negative circles");
                }
            }
            if (preview) System.out.println("================== " + k + " ======================");
            if (preview) GraphUtil.printMatrix(graph, distances);
            if (preview) GraphUtil.printMatrix(graph, transits);
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
        this.source = source;
        this.target = target;
    }

    /**
     * @return shortestWay
     */
    List<Node> getShortestPath() {
        if (hits == 0)
            throw new IllegalArgumentException("do compute before this method");

        LinkedList<Node> shortestPath = new LinkedList<Node>();

        shortestPath.add(target);
        Node current = target;
        while (hasPred(current)) {
            shortestPath.add(getPred(current));
            current = getPred(current);
        }
        shortestPath.add(source);
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
                transits[i][j] = -1;
                if (NodeI == NodeJ) {
                    distances[i][j] = 0.0;
                } else {
                    if (NodeI.hasEdgeBetween(NodeJ)) {
                        String weight = NodeI.getEdgeBetween(NodeJ).getAttribute("weight").toString();
                        distances[i][j] = Double.parseDouble(weight);
                    } else {
                        distances[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
        if (preview) System.out.println("================== Start ======================");
        if (preview) GraphUtil.printMatrix(graph, distances);
        if (preview) GraphUtil.printMatrix(graph, transits);
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
        return nodes.get(transits[getIndex(source)][getIndex(node)]);
    }

    /**
     * Helper method for getShortestPath()
     *
     * @param node to check
     * @return true if node has predecessor
     */
    @NotNull
    private Boolean hasPred(Node node) {
        return transits[getIndex(source)][getIndex(node)] >= 0;
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

    @NotNull
    private Double min(@NotNull Double x, @NotNull Double y) {
        return ((x < y) ? x : y);
    }

    // === MAIN ===
    public static void main(String[] args) throws Exception {
        // Graph aus den Folien
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6
//        Graph bspPadberg = new SingleGraph("bspPadberg");
//        bspPadberg.addNode("A");
//        bspPadberg.addNode("B");
//        bspPadberg.addNode("C");
//        bspPadberg.addNode("D");
//        bspPadberg.addNode("E");
//        bspPadberg.addEdge("AB", "A", "B", true).addAttribute("weight", 2.0);
//        bspPadberg.addEdge("AC", "A", "C", true).addAttribute("weight", 3.0);
//        bspPadberg.addEdge("BD", "B", "D", true).addAttribute("weight", 9.0);
//        bspPadberg.addEdge("CB", "C", "B", true).addAttribute("weight", 7.0);
//        bspPadberg.addEdge("CE", "C", "E", true).addAttribute("weight", 5.0);
//        bspPadberg.addEdge("ED", "E", "D", true).addAttribute("weight", 1.0);
//
//        FloydWarshall floydBspPadberg = new FloydWarshall();
//        floydBspPadberg.setSourceAndTarget(bspPadberg.getNode("A"), bspPadberg.getNode("E"));
//        floydBspPadberg.init(bspPadberg);
//        floydBspPadberg.compute();

        // Graph from https://www-m9.ma.tum.de/graph-algorithms/spp-floyd-warshall/index_de.html
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

        Graph test = new SingleGraph("test");
        test.addNode("0");
        test.addNode("1");
        test.addNode("2");
        test.addNode("3");
        test.addNode("4");
        test.addNode("5");
        test.addNode("6");
        test.addNode("7");
        test.addEdge("01", "0", "1", true).addAttribute("weight", 3);
        test.addEdge("03", "0", "3", true).addAttribute("weight", 2);
        test.addEdge("10", "1", "0", true).addAttribute("weight", 2);
        test.addEdge("15", "1", "5", true).addAttribute("weight", 3);
        test.addEdge("16", "1", "6", true).addAttribute("weight", 8);
        test.addEdge("26", "2", "6", true).addAttribute("weight", 8);
        test.addEdge("42", "4", "2", true).addAttribute("weight", 3);
        test.addEdge("46", "4", "6", true).addAttribute("weight", 1);
        test.addEdge("53", "5", "3", true).addAttribute("weight", 0);
        test.addEdge("56", "5", "6", true).addAttribute("weight", 2);
        test.addEdge("67", "6", "7", true).addAttribute("weight", 1);

//        GraphUtil.buildForDisplay(test).display();
        FloydWarshall floydTest = new FloydWarshall();
        floydTest.setSourceAndTarget(test.getNode("0"), test.getNode("7"));
        floydTest.init(test);
        floydTest.compute();


    }

    public Double getDistance() {
        return distance;
    }

    public Integer getHits() {
        return hits;
    }
}
