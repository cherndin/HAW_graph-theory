package helper;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import static helper.IOGraph.fromFile;
import static org.junit.Assert.*;

/**
 * Created by MattX7 on 23.10.2016.
 * Edited by Neak on 23.10.2016.
 */
public class IOGraphTest {
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void inandout() throws Exception {

        //Initialize graph
        Graph testGraph = new SingleGraph("testSave");
        testGraph.addNode("1");
        testGraph.addNode("2");
        testGraph.addNode("3");
        testGraph.addNode("4");

        testGraph.addEdge("12", "1", "2");
        testGraph.addEdge("23", "2", "3");
        testGraph.addEdge("34", "3", "4");
        testGraph.display();

        //Save initialized Graph
        IOGraph.save(testGraph,"testSave");
        File file = new File("src/main/resources/output/testSave.gka");
        assertTrue(file.exists());

        //Get saved File
        Graph chooser = fromFile("chooser", file);

        //Compare intitialized File with saved File
        for (Edge testedge: testGraph.getEachEdge()) {
            Edge chooseredge = chooser.getEdge(testedge.getId());
            assertTrue(chooseredge!=null);
            assertTrue(testedge.getNode0().getId().equals(chooseredge.getNode0().getId()));
            assertTrue(testedge.getNode1().getId().equals(chooseredge.getNode1().getId()));
        }

    }

}