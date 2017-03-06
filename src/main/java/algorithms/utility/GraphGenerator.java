package algorithms.utility;

import algorithms.optimal_ways.BreadthFirstSearch;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;

/**
 * Generates graphs.
 */
public class GraphGenerator {
    private static final Logger LOG = Logger.getLogger(GraphGenerator.class);

    private static Random random;

    private GraphGenerator() {
    }


    public static Graph createNetworkGraph(final int nodes, final int edges) {
        random = new Random();
        final Graph NetworkGraph = new SingleGraph("NetworkGraph");
        for (int i = 1; i <= nodes; i++) {
            NetworkGraph.addNode("" + i);
        }
        NetworkGraph.addEdge("1_" + nodes, "1", nodes + "", true).addAttribute("capacity", 1.0);
        for (int i = 2; i <= edges; i++) {
            final int x = random.nextInt(nodes - 1) + 1;
            final int y = random.nextInt(nodes - 1) + 1;
            try {
                NetworkGraph.addEdge(x + "_" + y, x + "", y + "", true).addAttribute("capacity", 1.0);
            } catch (final EdgeRejectedException o) {
                i--;
            }
        }
        return NetworkGraph;
    }

    public static Graph createGritNetworkGraph(final int nodes) {
        final Graph graph = new SingleGraph("Random");
        final Generator gen = new GridGenerator(false, false, true, true);
        gen.addSink(graph);
        gen.begin();
        for (int i = 0; i < Math.sqrt(nodes); i++) {
            gen.nextEvents();
        }
        gen.end();

        final BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(graph, graph.getNode(0), graph.getNode(graph.getNodeCount() - 1));
        bfs.compute();
        for (final Edge edge : graph.getEachEdge()) {
            edge.setAttribute("capacity", Double.valueOf(edge.getTargetNode().getAttribute("hits").toString()));
        }
        return graph;
    }

    public static int gritNetworkEdges(final int nodes) {
        return (int) Math.pow((nodes + Math.sqrt(nodes)), 2) / 4;
    }


    private static Graph createRandomNetwork(final int nodes, final int maxEdges) {
        final Graph graph = new SingleGraph("Random");
        final int averageDegree = maxEdges / nodes;
        final RandomGenerator gen = new RandomGenerator(averageDegree);
        gen.addSink(graph);
        gen.addEdgeAttribute("capacity");
        gen.setDirectedEdges(true, false);
        gen.begin();
        for (int i = 0; i < nodes; i++) {
            gen.nextEvents();
        }
        gen.end();
        return graph;
    }

    public static void main(final String[] args) {
        final Graph graph = createRandomNetwork(50, 300);
        for (final Node node : graph.getEachNode()) {
            System.out.println("Node:");
            System.out.println(node + "\n");
        }
        for (final Edge edge : graph.getEachEdge()) {
            System.out.println("Edge:");
            System.out.println(edge + "\n");
        }
    }

}
