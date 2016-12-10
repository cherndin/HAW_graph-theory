package helper;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;

/**
 * Created by Neak on 22.11.2016.
 */
public class NetworkGraph {
    public int nodes;
    public int edges;
    public int edgeCount = 1;
    private Random random = new Random();

    public NetworkGraph(int nodes, int edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Graph createNetworkGraph() {
        Graph NetworkGraph = new SingleGraph("NetworkGraph");
        for (int i = 1; i <= nodes; i++) {
            NetworkGraph.addNode("" + i);
        }
        NetworkGraph.addEdge("1_" + nodes, "1", nodes + "", true).addAttribute("capacity", 1.0);
        for (int i = 2; i <= edges; i++) {
            int x = random.nextInt(nodes - 1) + 1;
            int y = random.nextInt(nodes - 1) + 1;
            try {
                NetworkGraph.addEdge(x + "_" + y + "| " + edgeCount + " |", x + "", y + "", true).addAttribute("capacity", 1.0);
            } catch (EdgeRejectedException o) {
                i--;
                continue;
            }
            edgeCount++;
        }
        return NetworkGraph;
    }


}
