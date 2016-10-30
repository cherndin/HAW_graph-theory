package helper;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

/**
 * Created by MattX7 on 30.10.2016.
 */
public class GraphUtil {
    private GraphUtil() {
    }

    /**
     * build with CSS
     *
     * @param graph
     */
    public static void buildForDisplay(Graph graph) {
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        for (Node node : graph) {
            if (node.hasAttribute("title"))
                node.addAttribute("ui.label", node.getAttribute("title") + "[" + node.getId() + "]");
            else
                node.addAttribute("ui.label", node.getId());
        }
    }

    /**
     * Thread.sleep
     */
    public static void sleepLong() {
        sleep(5000);
    }

    public static void sleepShort() {
        sleep(1000);
    }

    public static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (Exception e) {
        }
    }


    public static String styleSheet =
            "node {" +
                    "	fill-color: black;" +
                    "}" +
                    "node.marked {" +
                    "	fill-color: blue;" +
                    "}" +
                    "node.actual {" +
                    "fill-color: red;" +
                    "}";


    /**
     * Converts an Edge to String.
     * <p>
     *
     * @param edge         edge we wanna create.
     * @param enableName   name should be added?
     * @param enableWeight weight should be added?
     * @return name node1 [ arrow name node2] [(edge name)] [: edgeweight];
     */
    public static String edgeToLine(@NotNull Edge edge,
                                    @NotNull Boolean enableName,
                                    @NotNull Boolean enableWeight) {
        String name = "";
        String weight = "";
        String arrow = " -- ";

        if (enableWeight) {
            if (edge.getAttribute("weight") == null)
                throw new IllegalArgumentException("Edge has no attribute with the key <weight>");
            weight = " : " + edge.getAttribute("weight");
        }
        if (edge.isDirected()) arrow = " -> ";
        if (enableName) name = " (" + edge.getId() + ")";

        return edge.getNode0() + arrow + edge.getNode1() + name + weight + ";";
    }

    public static String graphToString(@NotNull Graph graph,
                                       @NotNull Boolean enableName,
                                       @NotNull Boolean enableWeight) {
        String string = "";
        for (Edge edge : graph.getEachEdge()) {
            string = string + "\n" + edgeToLine(edge, enableName, enableWeight);
        }
        return string;
    }
}
