package helper;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

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
     * @return graph for inline use
     */
    public static Graph buildForDisplay(Graph graph) {
        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        for (Node node : graph) {
            node.addAttribute("ui.size", "2gu");
            if (node.hasAttribute("title"))
                node.addAttribute("ui.label", node.getAttribute("title") + "[" + node.getId() + "]");
            else
                node.addAttribute("ui.label", node.getId());
        }
        return graph;
    }

    /**
     * Thread.sleep
     */
    public static void sleepLong() {
        sleep(3000);
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
            "graph {" +
                    "   text-style:bold;" +
                    "}" +
                    "node {" +
                    "   size: 13px;" +
                    "   shape: circle;" +
                    "	fill-mode: plain;" +
                    "	fill-color: black;" +
                    "	stroke-mode: plain;" +
                    "   stroke-color: black;" +
                    "   stroke-width: 2px;" +
                    "}" +
                    "node.markBlue {" +
                    "	fill-color: blue;" +
                    "}" +
                    "node.markRed {" +
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
            string = string + " " + edgeToLine(edge, enableName, enableWeight);
        }
        return string;
    }

    /**
     * Output for the distance matrix
     */
    public static void printMatrix(@NotNull Graph graph,
                                   @NotNull Double[][] matrix) {
        for (Node node : graph.getEachNode()) { // print x nodes
            System.out.print("  \t" + node.getId() + " \t");
        }
        System.out.println();
        Iterator<Node> iterator = graph.getNodeIterator();
        for (Number[] array : matrix) { // print x nodes
            System.out.print(iterator.next() + " | ");
            for (Number number : array) { // print x nodes
                System.out.print((Double.valueOf(number.doubleValue()).isInfinite() ? "Inf" : number) + " \t");
            }
            System.out.println();
        }
    }

    /**
     * Output for the distance matrix
     */
    public static void printMatrix(@NotNull Graph graph,
                                   @NotNull Integer[][] matrix) {
        System.out.println();
        for (Node node : graph.getEachNode()) { // print x nodes
            System.out.print("  \t" + node.getId() + " \t");
        }
        System.out.println();
        Iterator<Node> iterator = graph.getNodeIterator();
        for (Number[] array : matrix) { // print x nodes
            System.out.print(iterator.next() + " | ");
            for (Number number : array) { // print x nodes
                System.out.print((Double.valueOf(number.doubleValue()).isInfinite() ? "Inf" : number) + " \t\t");
            }
            System.out.println();
        }
    }
}
