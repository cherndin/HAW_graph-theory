package algorithms;

import helper.GraphUtil;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by MattX7 on 03.11.2016.
 */
public class Dijkstra implements Algorithm {
    private static Logger logger = Logger.getLogger(Dijkstra.class);

    public static boolean preview = true;
    public int steps = -1;
    private Graph graph;
    private Node source;
    private Node target;
    private LinkedList<Node> nodes;

    public void init(Graph graph) {
        this.graph = graph;
        setSourceAndTarget(graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
        nodes = new LinkedList<Node>();
    }

    public void compute() {
        logger.debug("Starting Dijskra with " + GraphUtil.graphToString(graph, false, false));

        // Preconditions
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException();

        boolean hasWeight = true;
        for (Edge edge : graph.getEachEdge()) {
            if (!edge.hasAttribute("weight"))  //Check if all Edges have the weight attribute
                hasWeight = false;
        }
        if (!hasWeight)
            throw new IllegalArgumentException();

        // Implementation
        if (preview) GraphUtil.buildForDisplay(graph).display();
        logger.debug("Starting Dijkstra with graph:" + GraphUtil.graphToString(graph, false, false));
        setUp();
        while (!nodes.isEmpty()) {
            // Knoten mit minimaler Distanz auswählen
            Node currentNode = withMinDistance();
            // speichere, dass dieser Knoten schon besucht wurde.
            nodes.remove(currentNode);
            // Berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen Kantengewichtes und der Distanz.
            // Ist dieser Wert für einen Knoten kleiner als die dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten als Vorgänger.
            calcNewDistance(currentNode);
        }
        reset();
    }

    public List<Node> getShortestPath() {
        return null;
    }

    // === private ===

    private void calcNewDistance(Node current) {
        ArrayList<Node> neighbors = new ArrayList<Node>();
        // TODO Berechne für alle noch unbesuchten Nachbarknoten die Summe des jeweiligen Kantengewichtes und der Distanz.

        Iterator<Node> neighborNodeIterator = current.getNeighborNodeIterator();
        while (neighborNodeIterator.hasNext()) {
            Node next = neighborNodeIterator.next();
            // TODO Ist dieser Wert für einen Knoten kleiner als die dort gespeicherte Distanz, aktualisiere sie und setze den aktuellen Knoten als Vorgänger.
            Double newD = ((Double) next.getAttribute("Distance")) + ((Double) current.getAttribute("Distance"));
            if (newD < (Double) next.getAttribute("Distance"))
                next.setAttribute("Distance", newD);
        }


    }

    private Node withMinDistance() {
        Node min = nodes.getFirst();
        for (Node cur : nodes) {
            if (((Double) cur.getAttribute("Distance") < ((Double) min.getAttribute("Distance")))) ;
            min = cur;
        }
        return min;
    }


    private void setUp() {
        for (Node node : graph.getEachNode()) {
            if (!node.equals(source)) {
                node.setAttribute("Distance", Double.POSITIVE_INFINITY);
                node.setAttribute("OK", false);
                node.setAttribute("Predecessor", null);
                nodes.add(node);
            } else {
                source.setAttribute("Distance", 0);
                source.setAttribute("OK", true);
                source.setAttribute("Predecessor", source);
            }
        }
    }


    /**
     * Sets source and target before compute()
     *
     * @param s source node
     * @param t target node
     */
    public void setSourceAndTarget(@NotNull Node s, @NotNull Node t) {
        if (source != null) source.setAttribute("title", "");
        if (target != null) target.setAttribute("title", "");
        this.source = s;
        this.target = t;
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
