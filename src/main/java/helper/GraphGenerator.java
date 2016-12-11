package helper;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
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

    public static Graph createGritNetworkGraph(int n) {
        Graph graph = new SingleGraph("Random");
        Generator gen = new GridGenerator(false, false, true, true);
        gen.addSink(graph);
        gen.begin();

        for (int i = 0; i < n; i++) {
            gen.nextEvents();
        }

        gen.end();

        // Nodes already have a position.
        graph.display(false);
        return graph;
    }


    public static void main(String[] args) {
        Graph graph = createGritNetworkGraph(1);
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
