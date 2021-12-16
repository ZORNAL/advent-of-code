package org.acme;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RiddleSolver {

    private static boolean logEnabled = true;

    private static boolean part2 = false;

    private static void log(final String s) {
        if (isLogEnabled()) {
            System.out.println(s);
        }
    }

    public static boolean isLogEnabled() {
        return logEnabled;
    }

    public long solve(List<String> lines) {
        Integer[][] map = createMap(lines);

        if (part2) {
            map = extendMap(map);
        }
        int size = map.length;
        DefaultDirectedWeightedGraph<String, DefaultEdge> graph = createGraph(map, size);
        List<String> shortestPath = calculateShortestPath(graph, size);
        double weight = calculateWeight(graph, shortestPath);
        printPath(map, shortestPath);
        return (long) weight;
    }

    private void printPath(Integer[][] map, List<String> shortestPath) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (!shortestPath.contains(i + "-" + j)) {
                    map[i][j] = 0;
                }
            }
        }
        printMap(map);
    }

    private DefaultDirectedWeightedGraph<String, DefaultEdge> createGraph(Integer[][] map, int size) {
        DefaultDirectedWeightedGraph<String, DefaultEdge> graph = new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < map[0].length; x++) {
                graph.addVertex(createVertexName(y, x));
            }
        }
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < map[0].length; x++) {
                String vertexName = createVertexName(y, x);
                if (x + 1 < map[0].length) {
                    createEdge(map[y][x + 1], graph, graph.addEdge(vertexName, createVertexName(y, x + 1)));
                }
                if (y + 1 < size) {
                    createEdge(map[y + 1][x], graph, graph.addEdge(vertexName, createVertexName(y + 1, x)));
                }
            }
        }
        return graph;
    }

    private void printMap(Integer[][] map) {
        log(Arrays.stream(map).map(Arrays::toString).map(s -> Arrays.stream(s.split(", ")).collect(Collectors.joining())).collect(Collectors.joining("\n")));
    }

    private Integer[][] createMap(List<String> lines) {
        int size = lines.size();
        Integer[][] map = new Integer[size][lines.get(0).length()];
        for (int y = 0; y < size; y++) {
            String row = lines.get(y);
            for (int x = 0; x < size; x++) {
                map[y][x] = Integer.valueOf("" + row.charAt(x));
            }
        }
        return map;
    }

    private int calc(int value, int increment) {
        int result = value + increment;
        if (result >= 10) {
            return result - 9;
        }
        return result;
    }

    private Integer[][] extendMap(Integer[][] map) {
        Integer[][] extendedMap = new Integer[map.length * 5][map[0].length * 5];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[0].length; x++) {
                extendedMap[y][x] = map[y][x];
                extendedMap[y][x + map[0].length] = calc(map[y][x], 1);
                extendedMap[y][x + map[0].length * 2] = calc(map[y][x], 2);
                extendedMap[y][x + map[0].length * 3] = calc(map[y][x], 3);
                extendedMap[y][x + map[0].length * 4] = calc(map[y][x], 4);
            }
        }

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < extendedMap[0].length; x++) {
                extendedMap[y + map.length][x] = calc(extendedMap[y][x], 1);
                extendedMap[y + map.length * 2][x] = calc(extendedMap[y][x], 2);
                extendedMap[y + map.length * 3][x] = calc(extendedMap[y][x], 3);
                extendedMap[y + map.length * 4][x] = calc(extendedMap[y][x], 4);
            }
        }
        return extendedMap;
    }

    private double calculateWeight(DefaultDirectedWeightedGraph<String, DefaultEdge> graph, List<String> shortestPath) {
        double weight = 0;
        for (int index = 0; index < shortestPath.size() - 1; index++) {
            DefaultEdge edge = graph.getEdge(shortestPath.get(index), shortestPath.get(index + 1));
            weight += graph.getEdgeWeight(edge);
        }
        return weight;
    }

    private List<String> calculateShortestPath(DefaultDirectedWeightedGraph<String, DefaultEdge> graph, int size) {
        DijkstraShortestPath dijkstraShortestPath
                = new DijkstraShortestPath(graph);
        int i = size - 1;
        GraphPath path = dijkstraShortestPath
                .getPath("0-0", i + "-" + i);
        List<String> shortestPath = path.getVertexList();
        log(shortestPath.toString());
        return shortestPath;
    }

    private void createEdge(Integer weight, DefaultDirectedWeightedGraph<String, DefaultEdge> graph, DefaultEdge edge) {
        DefaultEdge defaultEdge = edge;
        graph.setEdgeWeight(defaultEdge, weight);
    }

    private String createVertexName(int y, int x) {
        return "" + y + "-" + x;
    }

    public void setPart2(boolean b) {
        part2 = b;
    }
}
