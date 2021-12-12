package org.acme;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PassageFinder {

    public static final String END = "end";
    public static final String START = "start";
    private DirectedGraph<String, DefaultEdge> graph;
    private Stack<String> currentPath = new Stack<>();
    private List<List<String>> simplePaths = new ArrayList<>();
    private final Function<String, Boolean> stoppingCondition;

    public PassageFinder(final boolean part2) {
        if(part2){
            this.stoppingCondition = this::stoppingConditionPart2;
        }else{
            this.stoppingCondition = this::stoppingConditionPart1;
        }

    }

    private static void log(final String s) {
        if(isLogEnabled()){
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return false;
    }

    public int solve(final List<String> strings){
        createGraph(strings);
        findPaths(START);
        log(String.format("Found %d Paths:\n%s", simplePaths.size(), simplePaths.stream().map(Object::toString).sorted().collect(Collectors.joining("\n"))));
        return simplePaths.size();
    }

    private void createGraph(List<String> strings) {
        graph = new DefaultDirectedGraph<>(DefaultEdge.class);
        for (final String line : strings) {
            final String[] split = line.split("-");
            graph.addVertex(split[0]);
            graph.addVertex(split[1]);
            graph.addEdge(split[0], split[1]);
            graph.addEdge(split[1], split[0]);
        }
        log(graph.toString());
    }

    private void findPaths(final String startPoint) {
        if (stoppingCondition.apply(startPoint)) {
            return;
        }
        currentPath.push(startPoint);
        if (isEnd(startPoint)) {
            addPathToPathCollection();
        } else {
            visitChildren(startPoint);
        }
        currentPath.pop();
    }

    private void addPathToPathCollection() {
        log("found endpoint: " + currentPath);
        simplePaths.add(new ArrayList<>(currentPath));
    }

    private void visitChildren(final String startPoint) {
        final Set<DefaultEdge> outgoingEdges = graph.outgoingEdgesOf(startPoint);
        log(String.format("Outgoing Edges of node %s: %s", startPoint, outgoingEdges.toString()));
        for (final DefaultEdge edge : outgoingEdges) {
            findPaths(graph.getEdgeTarget(edge));
        }
    }

    private boolean stoppingConditionPart1(final String startPoint) {
        return isSmallCave(startPoint) && currentPath.contains(startPoint);
    }

    private boolean isSmallCave(final String startPoint) {
        return Character.isLowerCase(startPoint.charAt(0));
    }

    private boolean stoppingConditionPart2(final String s) {
        final List<String> smallCaves = currentPath.stream().filter(this::isSmallCave).collect(Collectors.toList());
        if(hasPathVisitedSmallCaveTwice(smallCaves)){
            return true;
        }else{
           return (isEnd(s) || (isStart(s))) && smallCaves.contains(s);
        }
    }

    private boolean hasPathVisitedSmallCaveTwice(final List<String> smallCaves) {
        final int size = smallCaves.size();
        final long count = smallCaves.stream().distinct().count();
        return size > count + 1;
    }

    private boolean isEnd(final String s) {
        return END.equals(s);
    }

    private boolean isStart(final String s) {
        return START.equals(s);
    }

}
