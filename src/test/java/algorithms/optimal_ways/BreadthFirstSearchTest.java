package algorithms.optimal_ways;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BreadthFirstSearch}
 */
public class BreadthFirstSearchTest {
    @Before
    public void setUp() throws Exception {
        BreadthFirstSearch.preview = false;
    }

    @Test
    public void computeTest() throws Exception {
        //Ein Kreis Graph
        final Graph circle = new SingleGraph("circle");
        circle.addNode("a");
        circle.addNode("b");
        circle.addNode("c");
        circle.addNode("d");

        circle.addEdge("ab", "a", "b");
        circle.addEdge("bc", "b", "c");
        circle.addEdge("cd", "c", "d");
        circle.addEdge("da", "d", "a");

        //Ein Pentagram with Circle Graph
        final Graph pentaCircle = new SingleGraph("pentaCircle");
        pentaCircle.addNode("a");
        pentaCircle.addNode("b");
        pentaCircle.addNode("c");
        pentaCircle.addNode("d");
        pentaCircle.addNode("e");

        pentaCircle.addEdge("ab", "a", "b");
        pentaCircle.addEdge("ac", "a", "c");
        pentaCircle.addEdge("ae", "a", "e");
        pentaCircle.addEdge("ad", "a", "d");
        pentaCircle.addEdge("bd", "b", "d");
        pentaCircle.addEdge("bc", "b", "c");
        pentaCircle.addEdge("be", "b", "e");
        pentaCircle.addEdge("ed", "e", "d");
        pentaCircle.addEdge("ec", "e", "c");
        pentaCircle.addEdge("cd", "c", "d");

        //Ein Pentagram Graph
        final Graph penta = new SingleGraph("pentaCircle");
        penta.addNode("a");
        penta.addNode("b");
        penta.addNode("c");
        penta.addNode("d");
        penta.addNode("e");

        penta.addEdge("ab", "a", "b");
        penta.addEdge("ac", "a", "c");
        penta.addEdge("de", "d", "e");
        penta.addEdge("ec", "e", "c");
        penta.addEdge("bd", "b", "d");

        BreadthFirstSearch.preview = false;

        final BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(circle, circle.getNode("a"), circle.getNode("c"));
        bfs.compute();

        final BreadthFirstSearch bfs2 = new BreadthFirstSearch();
        bfs2.init(pentaCircle, pentaCircle.getNode("a"), pentaCircle.getNode("c"));
        bfs2.compute();

        final BreadthFirstSearch bfs3 = new BreadthFirstSearch();
        bfs3.init(penta, penta.getNode("a"), penta.getNode("e"));
        bfs3.compute();

        assertEquals(2, bfs.steps);
        assertEquals("[a, d, c]", bfs.getShortestPath().toString());
        assertEquals(1, bfs2.steps);
        assertEquals("[a, c]", bfs2.getShortestPath().toString());
        assertEquals(2, bfs3.steps);
        assertEquals("[a, c, e]", bfs3.getShortestPath().toString());

        //assertEquals(bfs.sumWeight, 3); // {shortestWay, anzKanten}
        //Graph graph1 = IOGraph.fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph05.gka"));
        //bfs.init(graph1);
        //bfs.setSourceAndTarget(graph1.getNode("v1"), graph1.getNode("v5"));
        //bfs.compute();
        //assertEquals(bfs.sumWeight, 1);
    }

    @Test
    public void bigGraphTest() throws Exception {
        final int edges = 1000;
        final Graph bigGraph = new SingleGraph("bigGraph");
        bigGraph.addNode("0");
        for (int i = 1; i <= edges; i++) {
            bigGraph.addNode("" + i);
            bigGraph.addEdge("" + (i - 1) + i, "" + (i - 1), "" + i);
        }
        BreadthFirstSearch.preview = false;
        final BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(bigGraph, bigGraph.getNode("0"), bigGraph.getNode("" + edges));
        bfs.compute();
        assertEquals(edges, bfs.steps);
    }
}