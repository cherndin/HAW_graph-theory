package algorithms.optimal_ways;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static algorithms.utility.IOGraph.fromFile;
import static org.junit.Assert.assertEquals;

/**
 * Created by MattX7 on 20.11.2016.
 */
public class DijkstraTest {
    private Graph graph;
    private Graph graph3;
    private Graph test;
    private List<Node> list = new ArrayList<Node>();

    // TODO Testen Sie für graph3 in graph3.gka dabei Floyd-Warshall gegen Dijkstra und geben Sie den k¨urzesten Weg, sowie die Anzahl der Zugriffe auf den Graphen an

    @Before
    public void setUp() throws Exception {

        test = new SingleGraph("test");
        test.addNode("0");
        test.addNode("1");
        test.addNode("2");
        test.addNode("3");
        test.addNode("4");
        test.addNode("5");
        test.addNode("6");
        test.addNode("7");
        test.addEdge("01", "0", "1", true).addAttribute("weight", 3);
        test.addEdge("03", "0", "3", true).addAttribute("weight", 2);
        test.addEdge("10", "1", "0", true).addAttribute("weight", 2);
        test.addEdge("15", "1", "5", true).addAttribute("weight", 3);
        test.addEdge("16", "1", "6", true).addAttribute("weight", 8);
        test.addEdge("26", "2", "6", true).addAttribute("weight", 8);
        test.addEdge("42", "4", "2", true).addAttribute("weight", 3);
        test.addEdge("46", "4", "6", true).addAttribute("weight", 1);
        test.addEdge("53", "5", "3", true).addAttribute("weight", 0);
        test.addEdge("56", "5", "6", true).addAttribute("weight", 2);
        test.addEdge("67", "6", "7", true).addAttribute("weight", 1);

        list.add(test.getNode(0));
        list.add(test.getNode(1));
        list.add(test.getNode(5));
        list.add(test.getNode(6));
        list.add(test.getNode(7));

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

    @Test
    public void computeSimpleGraphTest() throws Exception {
        Dijkstra dijk = new Dijkstra();
        Dijkstra.preview = false;
        graph.getEdge("v1v2").addAttribute("weight", 1.0);
        graph.getEdge("v1v6").addAttribute("weight", 3.0);
        graph.getEdge("v2v3").addAttribute("weight", 5.0);
        graph.getEdge("v2v6").addAttribute("weight", 2.0);
        graph.getEdge("v2v5").addAttribute("weight", 3.0);
        graph.getEdge("v3v5").addAttribute("weight", 2.0);
        graph.getEdge("v3v4").addAttribute("weight", 1.0);
        graph.getEdge("v5v4").addAttribute("weight", 3.0);
        graph.getEdge("v5v6").addAttribute("weight", 1.0);
        graph.getEdge("v6v3").addAttribute("weight", 2.0);
        dijk.init(graph, graph.getNode("v1"), graph.getNode("v4"));
        dijk.compute();

        assertEquals(Double.valueOf(6), dijk.distance);
        System.out.println("Hits: " + dijk.hits);
        System.out.println("Steps: " + dijk.getShortestPath());
    }



}