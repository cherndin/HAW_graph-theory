package algorithms.optimal_ways;

import algorithms.utility.BigGraph;
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
 * Test for {@link ShortestWay}
 */
public class ShortestWayTest {
    private Graph graph;
    private Graph graph3;
    private Graph graph9;
    private Graph test;
    private Graph negGraph;
    private List<Node> list = new ArrayList<Node>();

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
        // 02_GKA-Optimale Wege.pdf Folie 2 und 6
        graph = new SingleGraph("graph"); // TODO gerichtet und mit negativen
        graph.addNode("v1");
        graph.addNode("v2");
        graph.addNode("v3");
        graph.addNode("v4");
        graph.addNode("v5");
        graph.addNode("v6");
        graph.addEdge("v1v2", "v1", "v2", true).addAttribute("weight", 1.0);
        graph.addEdge("v1v6", "v1", "v6", true).addAttribute("weight", 3.0);
        graph.addEdge("v2v3", "v2", "v3", true).addAttribute("weight", 5.0);
        graph.addEdge("v2v5", "v2", "v5", true).addAttribute("weight", 2.0);
        graph.addEdge("v2v6", "v2", "v6", true).addAttribute("weight", 3.0);
        graph.addEdge("v3v5", "v3", "v5", true).addAttribute("weight", 2.0);
        graph.addEdge("v3v4", "v3", "v4", true).addAttribute("weight", 1.0);
        graph.addEdge("v5v4", "v5", "v4", true).addAttribute("weight", 3.0);
        graph.addEdge("v5v6", "v5", "v6", true).addAttribute("weight", 1.0);
        graph.addEdge("v6v3", "v6", "v3", true).addAttribute("weight", 2.0);

        negGraph = new SingleGraph("negGraph");
        negGraph.addNode("v1");
        negGraph.addNode("v2");
        negGraph.addNode("v3");
        negGraph.addNode("v4");
        negGraph.addNode("v5");

        negGraph.addEdge("v1v2", "v1", "v2", true).addAttribute("weight", -3.0);
        negGraph.addEdge("v2v3", "v2", "v3", true).addAttribute("weight", -11.0);
        negGraph.addEdge("v3v4", "v3", "v4", true).addAttribute("weight", 4.0);
        negGraph.addEdge("v4v2", "v4", "v2", true).addAttribute("weight", 3.0);
        negGraph.addEdge("v5v4", "v5", "v4", true).addAttribute("weight", -2.0);

        graph3 = fromFile("graph3", new File("src/main/resources/input/BspGraph/graph03.gka"));
        graph9 = fromFile("graph09", new File("src/main/resources/input/BspGraph/graph09.gka"));
    }

    /**
     * Test for graph without weights.
     */
    @Test(expected = IllegalArgumentException.class)
    public void graphWithNoWeightTest() throws Exception {
        ShortestWay dijk = new ShortestWay(new Dijkstra());
        ShortestWay floyd = new ShortestWay(new FloydWarshall());

        dijk.executeStrategy(graph9, graph9.getNode("a"), graph9.getNode("d"));
        floyd.executeStrategy(graph9, graph9.getNode("a"), graph9.getNode("d"));
    }

    /**
     * Test for shortest path.
     */
    @Test
    public void getShortestPathTest() { //TODO rausziehen
        Dijkstra dijk = new Dijkstra();
        FloydWarshall floyd = new FloydWarshall();

        dijk.init(test, test.getNode("0"), test.getNode("7"));
        floyd.init(test, test.getNode("0"), test.getNode("7"));

        dijk.compute();
        floyd.compute();

        assertEquals(dijk.getDistance(), floyd.getDistance());

        assertEquals(list, dijk.getShortestPath());
        assertEquals(list, floyd.getShortestPath());

        System.out.println("Dijkstra Hits: " + dijk.getHits());
        System.out.println("Floyd Warshall Hits: " + floyd.getHits());
    }

    @Test(expected = IllegalArgumentException.class)
    public void graph03test() throws Exception {
        ShortestWay dijk = new ShortestWay(new Dijkstra());
        ShortestWay floyd = new ShortestWay(new FloydWarshall());

        dijk.executeStrategy(graph3, graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));
        floyd.executeStrategy(graph3, graph3.getNode("Hamburg"), graph3.getNode("Lübeck"));
    }

    @Test
    public void bigGraphTest() throws Exception {
        BigGraph bigGraph = new BigGraph(100, 2500);
        Graph big = bigGraph.createBigGraph();

        FloydWarshall floyd = new FloydWarshall();
        Dijkstra dijk = new Dijkstra();

        Dijkstra.preview = false;
        FloydWarshall.preview = false;

        dijk.init(big, big.getNode("1"), big.getNode("" + bigGraph.nodes));
        floyd.init(big, big.getNode("1"), big.getNode("" + bigGraph.nodes));

        dijk.compute();
        floyd.compute();

        assertEquals(2500, bigGraph.edgeCount);

        assertEquals(Double.valueOf(1), dijk.distance);
        assertEquals(Double.valueOf(1), floyd.distance);
        assertEquals(floyd.distance, dijk.distance);

        System.out.println("Dijkstra Hits: " + dijk.hits);
        System.out.println("Floyd Warshall Hits: " + floyd.hits);
    }

}
