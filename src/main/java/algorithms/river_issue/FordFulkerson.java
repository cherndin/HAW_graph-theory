package algorithms.river_issue;

import algorithms.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class FordFulkerson implements Algorithm {
    private static Logger logger = Logger.getLogger(FordFulkerson.class);
    static boolean preview = true;
    public int hits = 0;

    private Graph graph;
    private Node source;
    private Node sink;

    private Double capacity[][]; // capacity
    private Double flow[][]; // flow
    private Node pred[]; // pred
    private Double delta[]; // gedissel
    private Boolean positive[];
    private Boolean inspected[];

    private List<Node> nodes = new LinkedList<Node>();

    /**
     * Initialization of the algorithm. This method has to be called before the
     * {@link #compute()} method to initialize or reset the algorithm according
     * to the new given graph.
     *
     * @param graph The graph this algorithm is using.
     */
    public void init(Graph graph) throws IllegalArgumentException {
        Preconditions.isNetwork(graph);
        //Implementation
        this.graph = graph;
        nodes = ImmutableList.copyOf(graph.getEachNode());
        int size = nodes.size();

        capacity = new Double[size][size]; // capacity matrix
        flow = new Double[size][size]; // flow matrix
        pred = new Node[size]; // flow matrix

        // Initialize empty flow & capacity.
        Iterator<Node> iIterator = nodes.iterator();
        for (int i = 0; i < size; i++) {
            Node nodeI = iIterator.next();
            Iterator<Node> jIterator = nodes.iterator();
            for (int j = 0; j < size; j++) {
                Node nodeJ = jIterator.next();
                capacity[i][j] = nodeI.getEdgeBetween(nodeJ).getAttribute("capacity");
                flow[i][j] = 0.0;
            }
        }

        // Markiere q mit (undef, Inf.)
        pred[indexOf(source)] = source;
        delta[indexOf(source)] = Double.POSITIVE_INFINITY;


    }

    /**
     * Run the algorithm. The {@link #init(Graph)} method has to be called
     * before computing.
     *
     * @see #init(Graph)
     */
    public void compute() {
        Node curr = getBeliebigenMarkiertenAberNichtInspiziertenKnoten();

        while (!queue.isEmpty()) { // beliebigen markierten, aber noch nicht inspizierten Knoten
            Node curr = queue.peek(); // VERTEXi
            Integer i = indexOf(curr);


            for (Edge leavingEdge : curr.getEachLeavingEdge()) {   // EDGEij elemOf Output(Vi)
                Node targetNode = leavingEdge.getTargetNode();

                if (!queue.contains(targetNode)) { // unmarkierter Knoten Vj
                    Integer j = indexOf(targetNode);

                    if (flow[i][j] < capacity[i][j]) { // f(EDGEij) < c(EDGEij)
                        queue.add(targetNode); // markiere VERTEXj

                        pred[j] = curr;
                        positive[j] = true;
                        delta[j] = min(delta[j], capacity[i][j] - flow[i][j]);

                    }
                }
            }

            for (Edge enteringEdge : curr.getEachEnteringEdge()) {   // EDGEji elemOf Input(Vi)
                Node sourceNode = enteringEdge.getSourceNode();

                if (!queue.contains(sourceNode)) {
                    Integer j = indexOf(sourceNode);

                    if (flow[j][i] > 0) { // f(Eji) > 0
                        queue.add(sourceNode); // markiere VERTEXj

                        pred[j] = curr;
                        positive[j] = false;
                        delta[j] = min(delta[j], flow[i][j]);
                    }
                }
            }

            queue.remove();
        }

    }

    /**
     * @return null if everything is inspected
     */
    @Nullable
    private Node getBeliebigenMarkiertenAberNichtInspiziertenKnoten() {
        for (Node node : nodes) {
            if (isMarked(node) && !inspected[indexOf(node)]) {
                return node;
            }
        }
        return null;
    }

    /**
     * Returns index of a Node
     *
     * @param node from we want to know the index
     * @return index
     * @throws NoSuchElementException no such node in the list
     */
    @NotNull
    private Integer indexOf(@NotNull Node node) {
        int i = nodes.indexOf(node);
        if (i < 0) throw new NoSuchElementException();
        return i;
    }

    // TODO caoacityOf, deltaOf,...

    @NotNull
    private Double min(double x, double y) {
        return x < y ? x : y;  // returns minimum of x and y
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
        if (this.sink != null && this.sink.hasAttribute("title"))
            this.sink.removeAttribute("title");
        this.source = source;
        this.sink = target;
        source.setAttribute("title", "source");
        target.setAttribute("title", "target");
    }

    @NotNull
    private Boolean isMarked(@NotNull Node node) {
        return (delta[indexOf(node)] != 0.0 && pred[indexOf(node)] != null);
    }

    // === MAIN ===
    public static void main(String[] args) throws Exception {
        Graph test = new SingleGraph("test");
        test.addNode("q");
        test.addNode("v1");
        test.addNode("v2");
        test.addNode("v3");
        test.addNode("v5");
        test.addNode("s");
        test.addEdge("qv5", "q", "v5", true).addAttribute("capacity", 1.0);
        test.addEdge("qv1", "q", "v1", true).addAttribute("capacity", 5.0);
        test.addEdge("qv2", "q", "v2", true).addAttribute("capacity", 4.0);
        test.addEdge("v2v3", "v2", "v3", true).addAttribute("capacity", 2.0);
        test.addEdge("v1v3", "v1", "v3", true).addAttribute("capacity", 1.0);
        test.addEdge("v1s", "v1", "s", true).addAttribute("capacity", 3.0);
        test.addEdge("v1v5", "v1", "v5", true).addAttribute("capacity", 1.0);
        test.addEdge("v5s", "v5", "s", true).addAttribute("capacity", 3.0);

        FordFulkerson ford = new FordFulkerson();
        ford.init(test);
        ford.setSourceAndTarget(test.getNode("q"), test.getNode("s"));
        ford.compute();

    }
}
