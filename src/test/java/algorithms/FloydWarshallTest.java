package algorithms;

import org.graphstream.graph.EdgeRejectedException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Random;

import static helper.IOGraph.fromFile;
import static org.junit.Assert.assertEquals;

/**
 * Created by Neak on 18.11.2016.
 */
public class FloydWarshallTest {
    private Graph graph;
    private Graph graph3;


    @Before
    public void setUp() throws Exception {
        // Graph aus den Folien
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6
        graph = new SingleGraph("graph");
        graph.addNode("v1");
        graph.addNode("v2");
        graph.addNode("v3");
        graph.addNode("v4");
        graph.addNode("v5");
        graph.addNode("v6");
        graph.addEdge("v1v2", "v1", "v2").addAttribute("weight", 1.0);
        graph.addEdge("v1v6", "v1", "v6").addAttribute("weight", 3.0);
        graph.addEdge("v2v3", "v2", "v3").addAttribute("weight", 5.0);
        graph.addEdge("v2v5", "v2", "v5").addAttribute("weight", 2.0);
        graph.addEdge("v2v6", "v2", "v6").addAttribute("weight", 3.0);
        graph.addEdge("v3v6", "v3", "v6").addAttribute("weight", 2.0);
        graph.addEdge("v3v5", "v3", "v5").addAttribute("weight", 2.0);
        graph.addEdge("v3v4", "v3", "v4").addAttribute("weight", 1.0);
        graph.addEdge("v5v4", "v5", "v4").addAttribute("weight", 3.0);
        graph.addEdge("v5v6", "v5", "v6").addAttribute("weight", 1.0);

        graph3 = fromFile("graph3", new File("src/main/resources/input/BspGraph/graph03.gka"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphWithNoWeightTest() throws Exception {
        Graph graph9 = fromFile("graph09", new File("src/main/resources/input/BspGraph/graph09.gka"));
        FloydWarshall floyd = new FloydWarshall();
        FloydWarshall.preview = false;
        floyd.init(graph9);
        floyd.setSourceAndTarget(graph9.getNode("a"), graph9.getNode("d"));
        floyd.compute();
    }

    @Test
    public void computeSimpleGraphTest() throws Exception {
        FloydWarshall floyd = new FloydWarshall();
        FloydWarshall.preview = false;
        floyd.init(graph);
        floyd.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        floyd.compute();
        // TODO anderer Test
        assertEquals(Double.valueOf(6), floyd.distance);
        System.out.println("Hits: " + floyd.hits);
        System.out.println("Steps: " + floyd.getShortestPath());
    }

    @Test
    public void graph03test() throws Exception {
        FloydWarshall floyd = new FloydWarshall();
        FloydWarshall.preview = false;
        floyd.init(graph3);
        floyd.setSourceAndTarget(graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));
        floyd.compute();
        assertEquals(Double.valueOf(170.0), floyd.distance);
        System.out.println("Hits: " + floyd.hits);
    }

    @Test
    public void bigGraphTest() throws Exception {
        int nodes = 100;
        int edges = 2500;
        int edgeCount = 1;
        Random random = new Random();
        Graph bigGraph = new SingleGraph("bigGraph");

        for (int i = 1; i <= nodes; i++) {
            bigGraph.addNode("" + i);
        }
        bigGraph.addEdge("1_100", "1", "100").addAttribute("weight", 1);
        for (int i = 2; i <= edges; i++) {
            int x = random.nextInt(nodes - 1) + 1;
            int y = random.nextInt(nodes - 1) + 1;
            try {
                bigGraph.addEdge(x + "_" + y + "| " + edgeCount + " |", x + "", y + "").addAttribute("weight", 1);
            } catch (EdgeRejectedException o) {
                i--;
                continue;
            }
            edgeCount++;
        }
        FloydWarshall floydWarshall = new FloydWarshall();
        FloydWarshall.preview = false;
        floydWarshall.init(bigGraph);
        floydWarshall.setSourceAndTarget(bigGraph.getNode("1"), bigGraph.getNode("" + nodes));
        floydWarshall.compute();
        assertEquals(2500, edgeCount);
        assertEquals(Double.valueOf(1), floydWarshall.distance);
        System.out.println("Hits: " + floydWarshall.hits);
    }
}