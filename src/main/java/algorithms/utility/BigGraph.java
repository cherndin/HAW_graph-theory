package algorithms.utility;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.Random;

/**
 * Created by Neak on 22.11.2016.
 */
public class BigGraph {
    public int nodes;
    private int edges;
    public int edgeCount = 1;
    private Random random = new Random();

    public BigGraph() {
    }

    public BigGraph(final int nodes, final int edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Graph createBigGraph() {
        final Graph bigGraph = new SingleGraph("bigGraph");
        for (int i = 1; i <= nodes; i++) {
            bigGraph.addNode("" + i);
        }
        bigGraph.addEdge("1_100", "1", "100", true).addAttribute("weight", 1);
        for (int i = 2; i <= edges; i++) {
            final int x = random.nextInt(nodes - 1) + 1;
            final int y = random.nextInt(nodes - 1) + 1;
            try {
                bigGraph.addEdge(x + "_" + y + "| " + edgeCount + " |", x + "", y + "", true).addAttribute("weight", 1);
            } catch (final EdgeRejectedException o) {
                i--;
                continue;
            }
            edgeCount++;
        }
        return bigGraph;
    }


}
