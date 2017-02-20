package algorithms.optimal_ways;

import algorithms.utility.GraphUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import static algorithms.utility.GraphPreconditions.mustHaveDirectedEdges;
import static algorithms.utility.GraphPreconditions.mustHaveWeights;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Floyd Warshall algorithm for shortest way in a graph with weights.
 * <p>
 * Ref:
 * http://www.orklaert.de/floyd-warshall-algorithmus.php
 * https://www-m9.ma.tum.de/graph-algorithms/spp-floyd-warshall/index_de.html (gute Erklärung)
 * https://www.cs.usfca.edu/~galles/visualization/Floyd.html
 */
public class FloydWarshall implements ShortestWayStrategy {
    private static final Logger LOG = Logger.getLogger(FloydWarshall.class);
    static boolean preview = false;

    Double distance;
    Integer hits;

    private Graph graph;
    private Node source;
    private Node target;
    private int n;
    private Double[][] distances;
    private Integer[][] transits;
    private List<Node> nodes = new LinkedList<Node>();

    @Override
    public void init(@NotNull final Graph graph,
                     @NotNull final Node source,
                     @NotNull final Node target) throws IllegalArgumentException {
        checkNotNull(graph, "graph has to be not null!");
        checkNotNull(source, "source has to be not null!");
        checkNotNull(target, "target has to be not null!");
        mustHaveWeights(graph);
        mustHaveDirectedEdges(graph);

        this.graph = graph;
        this.source = source;
        this.target = target;
        this.nodes = ImmutableList.copyOf(graph.getEachNode());

        this.n = nodes.size();
        this.hits = n * n; // Zugriffe auf den Graphen TODO stimmt das noch?

        int n = graph.getNodeCount();
        this.distances = new Double[n][n];
        this.transits = new Integer[n][n];
    }

    @Override
    public void compute() {
        // GraphPreconditions
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException("Attributes are missing");
        // Implementation
        setUp();
        // Bei jeder Iteration in dieser Schleife versucht der Algorithmus alle (i, j) Wege durch Wege (i, k) und (k, j) verbessern.
        for (int k = 0; k < n; k++) { // Schritte
            for (int i = 0; i < n; i++) { // Spalte

                if (i == k) continue; // Kann mit sich selbst addiert nicht kleiner sein
                final Double rowValue = distances[i][k];
                if (rowValue.isInfinite()) continue; // Spalte mit Inf. überspringen

                for (int j = 0; j < n; j++) { // Zeile

                    if (j == k) continue;
                    final Double columnValue = distances[k][j];
                    if (columnValue.isInfinite()) continue; // Zeile mit Inf. überspringen

                    final double oldDistance = distances[i][j];
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

    public List<Node> getShortestPath() {
        if (hits == 0)
            throw new IllegalArgumentException("do compute before this method");

        final LinkedList<Node> shortestPath = new LinkedList<>();

        shortestPath.add(target);
        Node current = target;
        while (hasPred(current)) {
            shortestPath.add(getPred(current));
            current = getPred(current);
        }
        shortestPath.add(source);
        return Lists.reverse(shortestPath);
    }

    @Nullable
    Double getDistance() {
        return distance;
    }

    Integer getHits() {
        return hits;
    }

    /**
     * initialises arrays for FloydWarshall
     */
    private void setUp() {
        final Iterator<Node> nodesForI = graph.getNodeIterator();
        for (int i = 0; i < n; i++) {
            final Iterator<Node> nodesForJ = graph.getNodeIterator();
            final Node NodeI = nodesForI.next();
            for (int j = 0; j < n; j++) {
                final Node NodeJ = nodesForJ.next();
                transits[i][j] = -1;
                if (NodeI == NodeJ) {
                    distances[i][j] = 0.0;
                } else {
                    if (NodeI.hasEdgeBetween(NodeJ)) {
                        final String weight = NodeI.getEdgeBetween(NodeJ).getAttribute("weight").toString();
                        distances[i][j] = Double.parseDouble(weight);
                    } else {
                        distances[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }
        }
        if (preview) {
            System.out.println("================== Start ======================");
            GraphUtil.printMatrix(graph, distances);
            GraphUtil.printMatrix(graph, transits);
            System.out.println();
        }
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
    private Node getPred(final Node node) throws IllegalArgumentException {
        return nodes.get(transits[getIndex(source)][getIndex(node)]);
    }

    /**
     * Helper method for getShortestPath()
     *
     * @param node to check
     * @return true if node has predecessor
     */
    @NotNull
    private Boolean hasPred(final Node node) {
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
    private Integer getIndex(@NotNull final Node node) {
        final int i = nodes.indexOf(node);
        if (i < 0) throw new NoSuchElementException();
        return i;
    }

    @NotNull
    private Double min(@NotNull final Double x, @NotNull final Double y) {
        return ((x < y) ? x : y);
    }

    // === MAIN ===
    public static void main(final String[] args) throws Exception {
        final Graph test = new SingleGraph("test");
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

        final FloydWarshall floydTest = new FloydWarshall();
        floydTest.init(test, test.getNode("0"), test.getNode("7"));
        floydTest.compute();


    }
}
