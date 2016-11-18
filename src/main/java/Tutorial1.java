import helper.GraphUtil;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

/**
 * Created by MattX7 on 18.10.2016.
 */
public class Tutorial1 {
    public static void main(String args[]) {
        Graph graph = new SingleGraph("graph");
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
        graph.addEdge("v3v6", "v3", "v6", true);
        graph.addEdge("v3v5", "v3", "v5", true);
        graph.addEdge("v3v4", "v3", "v4", true);
        graph.addEdge("v5v4", "v5", "v4", true);
        graph.addEdge("v5v6", "v5", "v6", true);
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
        GraphUtil.buildForDisplay(graph);

    }
}
