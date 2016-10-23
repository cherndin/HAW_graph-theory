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
    public void compute() throws Exception {
        Graph testGraph = new SingleGraph("testSave");
        testGraph.addNode("1");
        testGraph.addNode("2");
        testGraph.addNode("3");
        testGraph.addNode("4");

        testGraph.addEdge("12", "1", "2");
        testGraph.addEdge("23", "2", "3");
        testGraph.addEdge("34", "3", "4");
        testGraph.display();


        BreadthFirstSearch.init(testGraph);


        assertEquals({3,3}, shortTraverse(testGraph)); // {shortestWay, anzKanten}

}