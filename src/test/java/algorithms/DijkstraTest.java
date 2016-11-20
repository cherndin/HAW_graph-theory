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
 * Created by MattX7 on 20.11.2016.
 */
public class DijkstraTest {
    private Graph graph;
    private Graph graph3;

    // TODO Testen Sie für graph3 in graph3.gka dabei Floyd-Warshall gegen Dijkstra und geben Sie den k¨urzesten Weg, sowie die Anzahl der Zugriffe auf den Graphen an

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
        graph.addEdge("v1v2", "v1", "v2");
        graph.addEdge("v1v6", "v1", "v6");
        graph.addEdge("v2v3", "v2", "v3");
        graph.addEdge("v2v5", "v2", "v5");
        graph.addEdge("v2v6", "v2", "v6");
        graph.addEdge("v3v6", "v3", "v6");
        graph.addEdge("v3v5", "v3", "v5");
        graph.addEdge("v3v4", "v3", "v4");
        graph.addEdge("v5v4", "v5", "v4");
        graph.addEdge("v5v6", "v5", "v6");

        graph3 = fromFile("graph3", new File("src/main/resources/input/BspGraph/graph03.gka"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphWithNoWeightTest() throws Exception {
        // TODO INIT UND COMPUTE AUSFÜHREN STEPS UND SHORTESTPATH ANGUCKEN
        Dijkstra dijk = new Dijkstra();
        Dijkstra.preview = false;
        dijk.init(graph);
        dijk.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        dijk.compute();
    }

    @Test
    public void computeSimpleGraphTest() throws Exception {
        Dijkstra dijk = new Dijkstra();
        Dijkstra.preview = false;
        graph.getEdge("v1v2").addAttribute("weight", 1.0);
        graph.getEdge("v1v6").addAttribute("weight", 3.0);
        graph.getEdge("v2v3").addAttribute("weight", 5.0);
        graph.getEdge("v2v6").addAttribute("weight", 2.0);
        graph.getEdge("v2v5").addAttribute("weight", 3.0);
        graph.getEdge("v3v6").addAttribute("weight", 2.0);
        graph.getEdge("v3v5").addAttribute("weight", 2.0);
        graph.getEdge("v3v4").addAttribute("weight", 1.0);
        graph.getEdge("v5v4").addAttribute("weight", 3.0);
        graph.getEdge("v5v6").addAttribute("weight", 1.0);
        dijk.init(graph);
        dijk.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        dijk.compute();

        assertEquals(Double.valueOf(6), dijk.distance);
        // TODO assertEquals("[v1, v6, v3, v4]", dijk.getShortestPath().toString());
    }

    @Test
    public void graph03test() throws Exception {
        Dijkstra dijk = new Dijkstra();
        Dijkstra.preview = false;
        dijk.init(graph3);
        dijk.setSourceAndTarget(graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));
        dijk.compute();
        assertEquals(Double.valueOf(170.0), dijk.distance); //TODO not same as Floyd's
        System.out.println("Hits: " + dijk.hits);
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
        Dijkstra dijkstraVisual = new Dijkstra();
        Dijkstra.preview = false;
        dijkstraVisual.init(bigGraph);
        dijkstraVisual.setSourceAndTarget(bigGraph.getNode("1"), bigGraph.getNode("" + nodes));
        dijkstraVisual.compute();
        assertEquals(2500, edgeCount);
        assertEquals(Double.valueOf(1), dijkstraVisual.distance);
        System.out.println("Hits: " + dijkstraVisual.hits);
    }

}