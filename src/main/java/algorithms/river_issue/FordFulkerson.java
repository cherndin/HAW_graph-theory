package algorithms.river_issue;

import algorithms.Preconditions;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.Algorithm;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 * Created by MattX7 on 25.11.2016.
 */
public class FordFulkerson implements Algorithm {
    private static Logger logger = Logger.getLogger(FordFulkerson.class);
    static boolean preview = true;

    private Graph graph;
    private Node source;
    private Node sink;

    /**
     * Initialization of the algorithm. This method has to be called before the
     * {@link #compute()} method to initialize or reset the algorithm according
     * to the new given graph.
     *
     * @param graph The graph this algorithm is using.
     */
    public void init(Graph graph) throws IllegalArgumentException {
        Preconditions.noNegativeWeights(graph);
        Preconditions.noNonDirectedEdges(graph);
        // TODO schwach zusammenhängend
        // TODO schlicht
        // TODO simple directed graph

        //Implementation
        this.graph = graph;
    }

    /**
     * Run the algorithm. The {@link #init(Graph)} method has to be called
     * before computing.
     *
     * @see #init(Graph)
     */
    public void compute() {

    }
}
