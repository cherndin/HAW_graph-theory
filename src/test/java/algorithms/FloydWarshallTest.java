package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

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
        graph = new SingleGraph("graph"); // TODO gerichtet und mit negativen
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
        FloydWarshall.preview = true;
        floyd.init(graph);
        floyd.setSourceAndTarget(graph.getNode("v1"), graph.getNode("v4"));
        floyd.compute();
        assertEquals(Double.valueOf(6), floyd.distance);
        System.out.println("Hits: " + floyd.hits);
//        System.out.println("Steps: " + floyd.getShortestPath());
    }
}