package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Neak on 03.11.2016.
 */
public class DijkstraTest {
    private Graph graph;

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
        Dijkstra.preview = false; // Graph nicht visualisieren
    }

    @Test(expected = IllegalArgumentException.class)
    public void graphWithNoWeightTest() throws Exception {
        // TODO INIT UND COMPUTE AUSFÜHREN STEPS UND SHORTESTPATH ANGUCKEN
        Dijkstra dijk = new Dijkstra();
        dijk.init(graph);
        dijk.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        dijk.compute();
    }

    @Test
    public void computeSimpleGraphTest() throws Exception {
        Dijkstra dijk = new Dijkstra();
        graph.getEdge("v1v2").addAttribute("weight", 1.0);
        graph.getEdge("v1v6").addAttribute("weight", 3.0);
        graph.getEdge("v2v3").addAttribute("weight", 5.0);
        graph.getEdge("v2v6").addAttribute("weight", 2.0);
        graph.getEdge("v2v5").addAttribute("weight", 3.0);
        ;
        graph.getEdge("v3v6").addAttribute("weight", 2.0);
        graph.getEdge("v3v5").addAttribute("weight", 2.0);
        graph.getEdge("v3v4").addAttribute("weight", 1.0);
        graph.getEdge("v5v4").addAttribute("weight", 3.0);
        graph.getEdge("v5v6").addAttribute("weight", 1.0);
        dijk.init(graph);
        dijk.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        dijk.compute();

        assertEquals(new Double(6), dijk.distance);
// TODO       assertEquals("[v1, v6, v3, v4]", dijk.getShortestPath().toString());
    }

}