package algorithms;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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

    }
}