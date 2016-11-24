package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6 // TODO gerichtet und mit negativen
        graph = new SingleGraph("graph");
        graph.addNode("v1");
        graph.addNode("v2");
        graph.addNode("v3");
        graph.addNode("v4");
        graph.addNode("v5");
        graph.addNode("v6");
        graph.addEdge("v1v2", "v1", "v2", true);
        graph.addEdge("v1v6", "v1", "v6", true);
        graph.addEdge("v2v3", "v2", "v3", true);
        graph.addEdge("v2v5", "v2", "v5", true);
        graph.addEdge("v2v6", "v2", "v6", true);
        graph.addEdge("v3v5", "v3", "v5", true);
        graph.addEdge("v3v4", "v3", "v4", true);
        graph.addEdge("v5v4", "v5", "v4", true);
        graph.addEdge("v5v6", "v5", "v6", true);
        graph.addEdge("v6v3", "v6", "v3", true);

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
        System.out.println("Hits: " + dijk.hits);
        System.out.println("Steps: " + dijk.getShortestPath());
    }

    //TODO SHORTEST PATH TEST


}