package algorithms.river_issue;

import algorithms.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.Graphs;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by MattX7 on 25.11.2016.
 *
 */
public class FordFulkerson implements Algorithm {
    private static Logger logger = Logger.getLogger(FordFulkerson.class);
    public static boolean preview = true;

    private Graph graph;
    private List<Node> nodes = new LinkedList<Node>();
    private Node source;
    private Node sink;

    private Double capacity[][]; // capacity
    private Double flow[][]; // flow
    private Node predecessor[]; // predecessor
    private Double delta[]; // delta
    private boolean forward[];
    private boolean inspected[];

    public Set<Edge> maxFlowMinCut = new HashSet<Edge>();

    /**
     * Initialization of the algorithm. This method has to be called before the
     * {@link #compute()} method to initialize or reset the algorithm according
     * to the new given graph.
     *
     * @param graph The graph this algorithm is using.
     */
    public void init(Graph graph) throws IllegalArgumentException {
        Preconditions.isNetwork(graph);

        nodes = ImmutableList.copyOf(graph.getEachNode());
        int size = nodes.size();
        setSourceAndTarget(nodes.get(0), nodes.get(size - 1));
        this.graph = Graphs.clone(graph); // copy so we can make a residualgraph without editing the original

        capacity = new Double[size][size];
        flow = new Double[size][size];
        predecessor = new Node[size];
        delta = new Double[size];
        forward = new boolean[size];
        inspected = new boolean[size];

        // Initialize empty flow & capacity.
        Iterator<Node> iIterator = nodes.iterator();
        for (int i = 0; i < size; i++) {
            Node nodeI = iIterator.next();
            Iterator<Node> jIterator = nodes.iterator();
            for (int j = 0; j < size; j++) {
                Node nodeJ = jIterator.next();
                Edge edgeBetween = nodeI.getEdgeBetween(nodeJ);
                if (edgeBetween != null) {
                    capacity[i][j] = edgeBetween.getAttribute("capacity");
                }
                flow[i][j] = 0.0;
            }
            delta[i] = Double.POSITIVE_INFINITY;  // TODO right?
        }

        // Markiere q mit (undef, Inf.)
//        predecessor[indexOf(source)] = source; TODO ist das richtig?
        delta[indexOf(source)] = Double.POSITIVE_INFINITY;
        mark(indexOf(source), null, true, delta[indexOf(source)]);  //changed this
    }

    /**
     * Run the algorithm. The {@link #init(Graph)} method has to be called
     * before computing.
     *
     * @see #init(Graph)
     */
    public void compute() { // TODO no computer after cut because residualgraph
        /* (2) Inspektion und Markierung */
        // current setzten wir auch einen bel. markierten aber nicht inspizierten wert
        Node node = getMarkedButNotInspected(); // V_i
        Integer i = indexOf(node);
        // OUTPUT
        for (Edge leavingEdge : node.getEachLeavingEdge()) {   // E_ij elemOf Output(V_i)
            Node targetNode = leavingEdge.getTargetNode();

            if (!isMarked(targetNode)) { // nur unmarkierte Knoten markieren V_j
                Integer j = indexOf(targetNode);

                if (flow[i][j] < capacity[i][j]) { // f(E_ij) < c(E_ij)
                    // markiere VERTEX_j
                    mark(j, node, true, Math.min(delta[j], capacity[i][j] - flow[i][j]));
                }
            }
        }
        // INPUT
        for (Edge enteringEdge : node.getEachEnteringEdge()) {   // E_ji elemOf Input(V_i)
            Node sourceNode = enteringEdge.getSourceNode();

            if (!isMarked(sourceNode)) { // nur unmarkierter Knoten V_j
                Integer j = indexOf(sourceNode);

                if (flow[j][i] > 0) { // f(E_ji) > 0
                    // markiere V_j
                    mark(j, node, false, Math.min(delta[j], flow[i][j]));
                }
            }
        }

        inspected[indexOf(node)] = true;

        if (areAllMarkedNodesInspected()) {
            compute_Cut(); // (4) Es gibt keinen vergrößernden Weg
        } else if (isMarked(sink)) {
            compute_AugmentedPath(); // (3) Vergrößerung der Flussstärke
            compute();
        } else {
            compute(); // (2) Inspektion und Markierung
        }
    }

    /* (3) Vergrößerung der Flussstärke */
    private void compute_AugmentedPath() {
        Node current = sink;
        while (hasPred(current)) {
            int j = indexOf(current);
            Node pred = predecessor[j];
            Edge edge = pred.getEdgeBetween(current);
            if (edge == null)
                edge = current.getEdgeBetween(pred);

            if (edge.getTargetNode() == current) { // Vorwärtskante
                int i = indexOf(edge.getSourceNode());
                // f(E_ij) um DELTA_s erhöht
                flow[i][j] += delta[indexOf(sink)]; // TODO i und j vertauschen?
            } else if (edge.getSourceNode() == current) { // Rückwärtskante
                int i = indexOf(edge.getTargetNode());
                // wird f (E_ji ) um DELTA_s vermindet
                flow[i][j] -= delta[indexOf(sink)];
            } else {
                throw new IllegalArgumentException("WTF: Something impossible went wrong");
            }
            current = pred;
        }
        deleteAllMarks();
    }

    /* (4) Es gibt keinen vergrößernden Weg ( Max-Flow-Min-Cut-Theorem ) */
    // from: https://de.wikipedia.org/wiki/Max-Flow-Min-Cut-Theorem
    private void compute_Cut() {
        residualNetwork(graph); // Residualnetzwerk(G)

        Set<Node> nodesS = new HashSet<Node>();
        Set<Node> nodesT = new HashSet<Node>();

        for (Node v : nodes) {
            if (v.getEdgeBetween(source) != null) { // Wenn ein Pfad(s,v) in G existiert...
                nodesS.add(v);
            } else {
                nodesT.add(v);
            }
        }

        Set<Edge> cut = new HashSet<Edge>();

        for (Edge e : graph.getEachEdge()) { // Für jede Kante e aus E
            if (nodesS.contains(e.getSourceNode()) && nodesT.contains(e.getTargetNode())) { // Wenn startNode(e) aus S und endNode(e) aus T liegt...
                cut.add(e);
            }
        }

        maxFlowMinCut = cut;
    }

    private void residualNetwork(Graph graph) {
        for (Edge e : graph.getEachEdge()) {
            double currCapacity = e.getAttribute("capacity");
            double currFlow = flow[indexOf(e.getSourceNode())][indexOf(e.getTargetNode())];
            if (currFlow > 0) {
                graph.addEdge(
                        e.getTargetNode().getId() + e.getSourceNode().getId(),
                        e.getTargetNode().getId(),
                        e.getSourceNode().getId(),
                        true).setAttribute("capacity", currFlow);
                graph.getEdge(
                        e.getSourceNode().getId() + e.getTargetNode().getId()
                ).setAttribute("capacity", (currCapacity - currFlow));
            }
        }
    }

    /**
     * @return true if all marked nodes are inspected
     */
    private boolean areAllMarkedNodesInspected() {
        for (Node node : nodes) {
            if (isMarked(node) && !inspected[indexOf(node)])
                return false;
        }
        return true;
    }

    /**
     * Returns marked but not inspected node
     *
     * @return marked but not inspected node
     */
    @NotNull
    private Node getMarkedButNotInspected() {
        for (Node node : nodes) {
            if (isMarked(node) && !inspected[indexOf(node)]) {
                return node;
            }
        }
        throw new IllegalArgumentException("no node found");
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

    private boolean hasPred(Node node) {
        return (predecessor[indexOf(node)] != null);
    }

    /**
     * Sets source and target before compute()
     *
     * @param source source node
     * @param target target node
     */
    public void setSourceAndTarget(@NotNull Node source,
                                   @NotNull Node target) {
        this.source = source;
        this.sink = target;
    }

    /**
     * returns true if given node is marked;
     */
    @NotNull
    private Boolean isMarked(@NotNull Node node) {
        int i = indexOf(node);
        return node == source || (delta[i] != Double.POSITIVE_INFINITY && predecessor[i] != null);
    }

    /**
     * Marks an Node and sets values in the arrays predecessor[], forward[] and delta[]
     *
     * @param idx     Index
     * @param pred    Predecessor
     * @param forward True if Edge goes forward
     * @param delta   Delta value
     */
    private void mark(@NotNull Integer idx,
                      @Nullable Node pred,
                      @NotNull Boolean forward,
                      @NotNull Double delta) {
        this.predecessor[idx] = pred;
        this.forward[idx] = forward;
        this.delta[idx] = delta;
    }

    /**
     * Reset of the arrays for the mark
     */
    private void deleteAllMarks() {
        for (int i = 0; i < nodes.size(); i++) {
            inspected[i] = false;
            if (i != indexOf(source)) {
                mark(i, null, false, 0.0);
            }
        }
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
