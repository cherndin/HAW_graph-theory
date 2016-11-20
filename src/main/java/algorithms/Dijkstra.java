package algorithms;

import helper.GraphUtil;
import org.apache.log4j.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 20.11.2016.
 */
public class Dijkstra {
    private static Logger logger = Logger.getLogger(Dijkstra.class);

    public Double distance;
    public Integer hits = 0;
    public Object[][] matrix;
    public Node[] nodes;
    public Double[] entf;
    public Node[] vorg;
    public Boolean[] ok;
    private Graph graph;
    private Node source;
    private Node target;
    private LinkedList<Node> uncheckedNodes; // TODO could be a set

    /**
     * Initialisation
     *
     * @param graph
     */
    public void init(Graph graph) {
        this.graph = graph;
        matrix = new Double[4][graph.getNodeCount()];
        nodes = (Node[]) matrix[0];
        entf = (Double[]) matrix[1];
        vorg = (Node[]) matrix[2];
        ok = (Boolean[]) matrix[3];
        setSourceAndTarget(graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
        uncheckedNodes = new LinkedList<Node>();
    }

    /**
     * starts the algorithm
     */
    public void compute() {
        logger.debug("Starting Dijkstra with " + GraphUtil.graphToString(graph, false, false));
        // Preconditions
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException();
        if (!hasWeights(graph))
            throw new IllegalArgumentException();

        // Implementation
        setUp(); // Attribute setzen und mit Standartwerten belegen

        calcNewDistance(source);

        while (!uncheckedNodes.isEmpty()) {
            logger.debug(uncheckedNodes.toString());
            // Knoten mit minimaler Distanz auswählen
            Node currentNode = withMinDistance();
            // speichere, dass dieser Knoten schon besucht wurde.
            uncheckedNodes.remove(currentNode);
            // Berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen Kantengewichtes und der Distanz.
            // Ist dieser Wert für einen Knoten kleiner als die dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten als Vorgänger.
            calcNewDistance(currentNode);

        }
        distance = target.getAttribute("Distance");
        reset();
    }

    /**
     * Returns the shortest Path from the Source to the Target
     *
     * @return the shortest Path from the Source to the Target
     */
    public List<Node> getShortestPath() {
        // TODO getShortestPath()
        return null;
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

    // === PRIVATE ===

    private void setUp() {
        // nodes
        int n = 0;
        for (Node node : nodes) {
            n++;
            nodes[n] = node;
        }
        // Entfernung
        entf[0] = 0.0;
        for (int i = 1; i < entf.length; i++) {
            entf[i] = Double.POSITIVE_INFINITY;
        }
        // Vorgänger
        vorg[0] = source;
        // OK?
        for (int i = 0; i < ok.length; i++) {
            ok[i] = false;
        }
    }

    private void calcNewDistance(@NotNull Node currNode) {
        // Berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen Kantengewichtes und der Distanz.
        Iterator<Edge> leavingEdgeIterator = currNode.getLeavingEdgeIterator();
        while (leavingEdgeIterator.hasNext()) {
            hits++;
            Edge leavingEdge = leavingEdgeIterator.next();
            // Ist dieser Wert für einen Knoten kleiner als die dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten als Vorgänger.
            String weightCurr = currNode.getAttribute("Distance").toString();
            String weightLeav = leavingEdge.getAttribute("weight").toString();
            Double newDist = (Double.parseDouble(weightCurr)) + (Double.parseDouble(weightLeav));
            Node nodeFromLeavingEdge = getRightNode(currNode, leavingEdge); // TODO ist das notwendig?
            //TODO in die Matrix einfügen
        }

    }

    @NotNull
    private Node getRightNode(@NotNull Node currNode, @NotNull Edge leavingEdge) {
        Node node;
        if (leavingEdge.getNode1().equals(currNode))
            node = leavingEdge.getNode0();
        else
            node = leavingEdge.getNode1();
        return node;
    }

    @NotNull
    private Node withMinDistance() {
        Node min = uncheckedNodes.getFirst();
        for (Node cur : uncheckedNodes) {
            if (((Double) cur.getAttribute("Distance") < ((Double) min.getAttribute("Distance"))))
                min = cur;
            hits++;
        }
        return min;
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

    private void reset() {
        for (Node node : graph.getEachNode()) {
            node.removeAttribute("Distance");
            node.removeAttribute("OK");
            node.removeAttribute("Predecessor");
        }
        source.removeAttribute("title");
        target.removeAttribute("title");
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

        DijkstraVisual.preview = true;
        DijkstraVisual dijk = new DijkstraVisual();
        dijk.init(graph);
        dijk.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        dijk.compute();
    }
}
