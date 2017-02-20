package algorithms.utility;

import com.google.common.base.Preconditions;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Utility class for graph algorithms.
 */
public class GraphUtil {
    private GraphUtil() {
    }

    /**
     * build with CSS
     *
     * @param graph Not null.
     * @return graph for inline use
     */
    public static Graph buildForDisplay(@NotNull final Graph graph) {
        Preconditions.checkNotNull(graph, "graph has to be not null!");

        final String styleSheet = "graph {" +
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

        graph.addAttribute("ui.stylesheet", styleSheet);
        graph.setAutoCreate(true);
        graph.setStrict(false);
        for (final Node node : graph) {
            node.addAttribute("ui.size", "2gu");
            if (node.hasAttribute("title"))
                node.addAttribute("ui.label", node.getAttribute("title") + "[" + node.getId() + "]");
            else
                node.addAttribute("ui.label", node.getId());
        }
        return graph;
    }

    /**
     * Converts an Edge to String.
     * <p>
     *
     * @param edge         edge we wanna create.
     * @param enableName   name should be added?
     * @param enableWeight weight should be added?
     * @return name node1 [ arrow name node2] [(edge name)] [: edgeweight];
     */
    static String edgeToLine(@NotNull final Edge edge,
                             @NotNull final Boolean enableName,
                             @NotNull final Boolean enableWeight) {
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

    public static String graphToString(@NotNull final Graph graph) {
        String string = "";
        for (final Edge edge : graph.getEachEdge()) {
            string = string + " " + edgeToLine(edge, false, false);
        }
        return string;
    }

    /**
     * Output for the distance matrix
     */
    public static void printMatrix(@NotNull final Graph graph,
                                   @NotNull final Double[][] matrix) {
        for (final Node node : graph.getEachNode()) { // print x nodes
            System.out.print("  \t" + node.getId() + " \t");
        }
        System.out.println();
        final Iterator<Node> iterator = graph.getNodeIterator();
        for (final Number[] array : matrix) { // print x nodes
            System.out.print(iterator.next() + " | ");
            for (final Number number : array) { // print x nodes
                System.out.print((Double.valueOf(number.doubleValue()).isInfinite() ? "Inf" : number) + " \t");
            }
            System.out.println();
        }
    }

    /**
     * Output for the distance matrix
     */
    public static void printMatrix(@NotNull final Graph graph,
                                   @NotNull final Integer[][] matrix) {
        System.out.println();
        for (final Node node : graph.getEachNode()) { // print x nodes
            System.out.print("  \t" + node.getId() + " \t");
        }
        System.out.println();
        final Iterator<Node> iterator = graph.getNodeIterator();
        for (final Number[] array : matrix) { // print x nodes
            System.out.print(iterator.next() + " | ");
            for (final Number number : array) { // print x nodes
                System.out.print((Double.valueOf(number.doubleValue()).isInfinite() ? "Inf" : number) + " \t\t");
            }
            System.out.println();
        }
    }
}
