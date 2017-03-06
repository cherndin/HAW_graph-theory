package algorithms.optimal_ways;

import algorithms.utility.GraphUtil;
import algorithms.utility.IOGraph;
import org.apache.log4j.Logger;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of the breath-first search algorithm.
 */
public class BreadthFirstSearch implements ShortestWayStrategy {
    private static final Logger LOG = Logger.getLogger(BreadthFirstSearch.class);
    public static boolean preview = false;

    int steps = -1;
    private Graph graph;
    private Node source;
    private Node target;
    private boolean noTargetFound;

    public void init(@NotNull final Graph graph,
                     @NotNull final Node source,
                     @NotNull final Node target) {
        this.graph = checkNotNull(graph, "graph has to be not null!");
        this.source = checkNotNull(source, "source has to be not null!");
        this.target = checkNotNull(target, "target has to be not null!");
    }

    public void compute() {
        if (graph == null || source == null || target == null) // have to be set
            throw new IllegalArgumentException("Do init() before compute()");

        reset();
        LOG.debug("Starting BFS with graph:" + GraphUtil.graphToString(graph));
        final Queue<Node> queue = new LinkedList<>();
        queue.add(tag(source, -1)); // start with source

        while (!queue.isEmpty()) {
            final Node next = queue.peek();

            queue.addAll(getUntaggedNeighborsAndTagThem(next));
            if (isTargetTagged()) {
                steps = target.getAttribute("hits");
                break;
            }
            LOG.debug("queue:" + queue.toString());
            queue.remove(next);
        }
        if (!isTargetTagged()) {
            LOG.error("Target not found!");
            noTargetFound = true;
        } else {
            LOG.info("Target found!");
            noTargetFound = false;
        }
    }

    @NotNull
    public List<Node> getShortestPath() {
        if (noTargetFound)
            return Collections.emptyList();
        // TODO was wenn compute noch nicht ausgeführt wurde?
        final LinkedList<Node> shortestWay = new LinkedList<>();
        shortestWay.add(target);
        while (!shortestWay.getLast().getAttribute("hits").equals(0)) { // TODO noch eine Abbruchbedingung
            final Node next = getShortestNode(shortestWay.getLast()); // TODO Nullable
            shortestWay.add(next);
        }
        Collections.reverse(shortestWay);
        return shortestWay;
    }

    @Nullable
    private Node getShortestNode(final Node node) {
        final Iterator<Node> nodeIterator = node.getNeighborNodeIterator();
        while (nodeIterator.hasNext()) {
            final Node next = nodeIterator.next();
            if (next.getAttribute("hits").equals((((Integer) node.getAttribute("hits")) - 1))) {
                return next;
            }
        }
        return null;
    }

    // ====== PRIVATE =========

    /**
     * All untagged shortestWay that ar neighbors.
     *
     * @param node not null.
     * @return isEmpty if no neighbors are left.
     */
    @NotNull
    private List<Node> getUntaggedNeighborsAndTagThem(@NotNull final Node node) {
        final List<Node> newTaggedNeighbors = new ArrayList<>();
        final Iterator<Edge> edgeIterator = node.getLeavingEdgeIterator();
        while (edgeIterator.hasNext()) {
            final Edge nextEdge = edgeIterator.next();
            final Node nextNode;
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
        for (final Node node : graph.getEachNode()) {
            node.setAttribute("hits", -1);
        }
    }

    /**
     * tag Node
     *
     * @param node not null.
     * @param steps not null.
     * @return the node param for inline use
     */
    @NotNull
    private Node tag(@NotNull final Node node, @NotNull final Integer steps) {
        checkNotNull(node, "node has to be not null!");
        checkNotNull(steps, "steps has to be not null!");

        node.setAttribute("hits", steps + 1);
        return node;
    }

    @NotNull
    private Boolean isTargetTagged() {
        return (!target.getAttribute("hits").equals(-1));
    }


    public static void main(final String[] args) throws Exception {
        final Graph graph = IOGraph.fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph02.gka"));

        final BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(graph, graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
        bfs.compute();
        System.out.println(bfs.getShortestPath().toString());
        System.out.println("Steps: " + bfs.steps);

    }


}


