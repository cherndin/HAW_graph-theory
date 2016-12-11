package algorithms;

import helper.GraphUtil;
import helper.IOGraph;
import org.apache.log4j.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

/**
 * Created by MattX7 on 11.12.2016.
 */
public class BreadthFirstSearch {
    private static Logger logger = Logger.getLogger(algorithms.BreadthFirstSearch.class);
    public static boolean preview = true;
    public int steps = -1;
    private Graph graph;
    private Node source;
    private Node target;
    private boolean noTargetFound;

    public void init(Graph graph) {
        this.graph = graph;
        setSourceAndTarget(graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
    }

    public void compute() {
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException();
        reset();
        logger.debug("Starting BFS with graph:" + GraphUtil.graphToString(graph, false, false));
        Queue<Node> queue = new LinkedList<Node>();
        queue.add(tag(source, -1)); // start with source

        while (!queue.isEmpty()) {
            Node next = queue.peek();

            queue.addAll(getUntaggedNeighborsAndTagThem(next));
            if (isTargetTagged()) {
                steps = target.getAttribute("hits");
                break;
            }
            logger.debug("queue:" + queue.toString());
            queue.remove(next);
        }
        if (!isTargetTagged()) {
            logger.error("Target not found!");
            noTargetFound = true;
        } else {
            logger.info("Target found!");
            noTargetFound = false;
        }
    }

    @NotNull
    public List<Node> getShortestPath() {
        if (noTargetFound)
            return Collections.emptyList();

        LinkedList<Node> shortestWay = new LinkedList<Node>();
        shortestWay.add(target);
        while (!shortestWay.getLast().getAttribute("hits").equals(0)) { // TODO noch eine Abbruchbedingung
            Node next = getShortestNode(shortestWay.getLast()); // TODO Nullable
            shortestWay.add(next);
        }
        Collections.reverse(shortestWay);
        return shortestWay;
    }

    @Nullable
    private Node getShortestNode(Node node) {
        Iterator<Node> nodeIterator = node.getNeighborNodeIterator();
        while (nodeIterator.hasNext()) {
            Node next = nodeIterator.next();
            if (next.getAttribute("hits").equals((((Integer) node.getAttribute("hits")) - 1))) {
                return next;
            }
        }
        return null;
    }

    /**
     * Sets source and target before compute()
     *
     * @param s source node
     * @param t target node
     */
    public void setSourceAndTarget(@NotNull Node s, @NotNull Node t) {
        this.source = s;
        this.target = t;
    }

    // ====== PRIVATE =========

    /**
     * All untagged shortestWay that ar neighbors.
     *
     * @param node
     * @return isEmpty if no neighbors are left.
     */
    @NotNull
    private List<Node> getUntaggedNeighborsAndTagThem(@NotNull Node node) {
        List<Node> newTaggedNeighbors = new ArrayList<Node>();
        Iterator<Edge> edgeIterator = node.getLeavingEdgeIterator();
        while (edgeIterator.hasNext()) {
            Edge nextEdge = edgeIterator.next();
            Node nextNode;
            if (node != nextEdge.getNode1())
                nextNode = nextEdge.getNode1();
            else
                nextNode = nextEdge.getNode0();
            if (nextNode.getAttribute("hits").toString().equals("-1")) {
                newTaggedNeighbors.add(
                        tag(nextNode, Integer.valueOf(node.getAttribute("hits").toString())));
            }

        }
        return newTaggedNeighbors;
    }

    private void reset() {
        this.steps = -1;
        for (Node node : graph.getEachNode()) {
            node.setAttribute("hits", -1);
        }
    }

    /**
     * tag Node
     *
     * @param node
     * @param steps
     * @return the node param for inline use
     */
    @NotNull
    private Node tag(@NotNull Node node, @NotNull Integer steps) {
        node.setAttribute("hits", steps + 1);
        return node;
    }

    @NotNull
    private Boolean isTargetTagged() {
        return (!target.getAttribute("hits").equals(-1));
    }


    public static void main(String[] args) throws Exception {
        Graph graph = IOGraph.fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph02.gka"));

        BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(graph);
        bfs.compute();
        System.out.println(bfs.getShortestPath().toString());
        System.out.println("Steps: " + bfs.steps);

    }


}


