package helper;

import algorithms.BreadthFirstSearch;
import org.apache.log4j.Logger;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.algorithm.generator.WattsStrogatzGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;

/**
 * Created by Neak on 22.11.2016.
 */
public class GraphGenerator {
    private static final Logger LOG = Logger.getLogger(GraphGenerator.class);

    private static Random random;

    private GraphGenerator() {
    }


    public static Graph createNetworkGraph(int nodes, int edges) {
        random = new Random();
        Graph NetworkGraph = new SingleGraph("NetworkGraph");
        for (int i = 1; i <= nodes; i++) {
            NetworkGraph.addNode("" + i);
        }
        NetworkGraph.addEdge("1_" + nodes, "1", nodes + "", true).addAttribute("capacity", 1.0);
        for (int i = 2; i <= edges; i++) {
            int x = random.nextInt(nodes - 1) + 1;
            int y = random.nextInt(nodes - 1) + 1;
            try {
                NetworkGraph.addEdge(x + "_" + y, x + "", y + "", true).addAttribute("capacity", 1.0);
            } catch (EdgeRejectedException o) {
                i--;
                continue;
            }
        }
        return NetworkGraph;
    }

    public static Graph createGritNetworkGraph(int nodes) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new GridGenerator(false, false, true, true);
        gen.addSink(graph);
        gen.begin();
        for (int i = 0; i < Math.sqrt(nodes); i++) {
            gen.nextEvents();
        }
        gen.end();

        BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(graph);
        bfs.compute();
        for (Edge edge : graph.getEachEdge()) {
            edge.setAttribute("capacity", Double.valueOf(edge.getTargetNode().getAttribute("hits").toString()));
        }
        return graph;
    }

    public static int gritNetworkEdges(int nodes) {
        return (int) Math.pow((nodes + Math.sqrt(nodes)), 2) / 4;
    }

    public static Graph createSmallWorldGraph(int n, int k, double beta) {
        Graph graph = new SingleGraph("This is a small world!");
        Generator gen = new WattsStrogatzGenerator(n, k, beta);

        gen.addSink(graph);
        gen.begin();
        while (gen.nextEvents()) {
        }
        gen.end();

        graph.display(false); // Node position is provided.
        return graph;
    }

    public static Graph createRandomNetwork(int nodes, int maxEdges) {
        Graph graph = new SingleGraph("Random");
        int averageDegree = maxEdges / nodes;
        RandomGenerator gen = new RandomGenerator(averageDegree);
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

    public static void main(String[] args) {
        Graph graph = createRandomNetwork(50, 300);
        for (Node node : graph.getEachNode()) {
            System.out.println("Node:");
            System.out.println(node + "\n");
        }
        for (Edge edge : graph.getEachEdge()) {
            System.out.println("Edge:");
            System.out.println(edge + "\n");
        }
    }

}
