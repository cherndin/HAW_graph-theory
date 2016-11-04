package algorithms;

import helper.GraphUtil;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 03.11.2016.
 */
public class Dijkstra implements Algorithm {
    private static Logger logger = Logger.getLogger(Dijkstra.class);

    public static boolean preview = true;
    public Double distance;
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
        if (preview) GraphUtil.buildForDisplay(graph).display(); // TODO visualisierung des algos
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
        return null;
    }

    // === private ===

    private void calcNewDistance(Node currNode) {
        // Berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen Kantengewichtes und der Distanz.
        Iterator<Edge> leavingEdgeIterator = currNode.getLeavingEdgeIterator();
        while (leavingEdgeIterator.hasNext()) {
            Edge leavingEdge = leavingEdgeIterator.next();
            // Ist dieser Wert für einen Knoten kleiner als die dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten als Vorgänger.
            Double newDist = ((Double) currNode.getAttribute("Distance")) + ((Double) leavingEdge.getAttribute("weight"));
            Node nodeFromLeavingEdge = getRightNode(currNode, leavingEdge); // TODO ist das notwendig?

            if (!((Boolean) nodeFromLeavingEdge.getAttribute("OK")) &&
                    (newDist < (Double) nodeFromLeavingEdge.getAttribute("Distance"))) {
                nodeFromLeavingEdge.setAttribute("Distance", newDist);
                nodeFromLeavingEdge.setAttribute("Predecessor", currNode);
            }
        }
    }

    private Node getRightNode(Node currNode, Edge leavingEdge) {
        Node node;
        if (leavingEdge.getNode1().equals(currNode))
            node = leavingEdge.getNode0();
        else
            node = leavingEdge.getNode1();
        return node;
    }

    private Node withMinDistance() {
        Node min = uncheckedNodes.getFirst();
        for (Node cur : uncheckedNodes) {
            if (((Double) cur.getAttribute("Distance") < ((Double) min.getAttribute("Distance"))))
                min = cur;
        }
        return min;
    }

    private boolean hasWeights(Graph graph) {
        boolean hasWeight = true;
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.hasAttribute("weight"))
                hasWeight = false;
        }
        return hasWeight;
    }

    private void setUp() {
        for (Node node : graph.getEachNode()) {
            if (!node.equals(source)) {
                node.addAttribute("Distance", Double.POSITIVE_INFINITY);
                node.addAttribute("OK", false);
                node.addAttribute("Predecessor", 0);
                uncheckedNodes.add(node); // add all nodes
                logger.debug(node.getId() + " | Dist.: Inf. | OK: false | Pred.: 0");
            } else {
                source.setAttribute("Distance", 0.0);
                source.setAttribute("OK", true);
                source.setAttribute("Predecessor", source);
                logger.debug(source.getId() + " | Dist.: 0 | OK: true | Pred.: " + source.getId());
            }
        }

    }


    /**
     * Sets source and target before compute()
     *
     * @param source source node
     * @param target target node
     */
    public void setSourceAndTarget(@NotNull Node source,
                                   @NotNull Node target) {
        source.setAttribute("title", "");
        target.setAttribute("title", "");
        this.source = source;
        this.target = target;
        source.setAttribute("title", "source");
        target.setAttribute("title", "target");
    }

    // ====== PRIVATE =========

    private void reset() {
        for (Node node : graph.getEachNode()) {
            node.removeAttribute("Distance");
            node.removeAttribute("OK");
            node.removeAttribute("Predecessor");
        }
        source.removeAttribute("title");
        target.removeAttribute("title");
    }

    public static void main(String[] args) throws Exception {

    }
}
