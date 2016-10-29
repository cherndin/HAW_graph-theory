package algorithms;

import helper.IOGraph;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by Neak on 23.10.2016.
 */
public class BreadthFirstSearchTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void computeTest() throws Exception {
        Graph graph = new SingleGraph("testSave");
        graph.addNode("a");
        graph.addNode("b");
        graph.addNode("c");
        graph.addNode("d");

        graph.addEdge("ab", "a", "b");
        graph.addEdge("bc", "b", "c");
        graph.addEdge("cd", "c", "d");
//        graph.display();

        BreadthFirstSearch bfs = new BreadthFirstSearch();
        bfs.init(graph);
        bfs.setSourceAndTarget(graph.getNode("a"),graph.getNode("d"));
        bfs.compute();

        assertEquals(bfs.steps, 3); // {shortestWay, anzKanten}
        Graph graph1 = IOGraph.fromFile("MyGraph", new File("src/main/resources/input/BspGraph/graph05.gka"));
        bfs.init(graph1);
        bfs.setSourceAndTarget(graph1.getNode("v1"), graph1.getNode("v5"));
        bfs.compute();
        assertEquals(bfs.steps, 1);
    }
}