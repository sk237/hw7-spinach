package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IEdge;
import datastructures.interfaces.IDisjointSet;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.Sorter;
import misc.exceptions.NoPathExistsException;


/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends IEdge<V> & Comparable<E>> {
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated than usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've constrained Graph
    //   so that E *must* always be an instance of IEdge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the IEdge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask ASAP either on Piazza or during office hours.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * Note that each edge in 'edges' represents a unique edge. For example, if 'edges'
     * contains an entry for '(A,B)' and for '(B,A)', that means there are two parallel
     * edges between vertex 'A' and vertex 'B'.
     *
     * @throws IllegalArgumentException if any edges have a negative weight
     * @throws IllegalArgumentException if any edges connect to a vertex not present in 'vertices'
     * @throws IllegalArgumentException if 'vertices' or 'edges' are null or contain null
     * @throws IllegalArgumentException if 'vertices' contains duplicates
     */
    ISet<V> vSet;
    IList<E> eSet;
    IDictionary<V, IList<E>> adjacency;
    public Graph(IList<V> vertices, IList<E> edges) {
        if (vertices == null || edges == null) {
            throw new IllegalArgumentException();
        }
        vSet = new ChainedHashSet<>();
        eSet = new DoubleLinkedList<>();
        adjacency = new ChainedHashDictionary<>();
        for (V v : vertices) {
            if (v == null || vSet.contains(v)) {
                throw new IllegalArgumentException();
            }
            vSet.add(v);
            adjacency.put(v, new DoubleLinkedList<>());
        }
        for (E e : edges) {
            if (e.getWeight() < 0) {
                throw new IllegalArgumentException();
            }
            if (!vSet.contains(e.getVertex1()) || !vSet.contains(e.getVertex2())) {
                throw new IllegalArgumentException();
            }
            if (e == null) {
                throw new IllegalArgumentException();
            }
            eSet.add(e);
            adjacency.get(e.getVertex1()).add(e);
            adjacency.get(e.getVertex2()).add(e);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     * @throws IllegalArgumentException if vertices or edges are null or contain null
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        if (set == null) {
            throw new IllegalArgumentException();
        }
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return vSet.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return eSet.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     *
     * If there exists multiple valid MSTs, return any one of them.
     *
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        IList<E> sorted = Sorter.topKSort(eSet.size(), eSet);
        IDisjointSet<V> set = new ArrayDisjointSet<>();
        ISet<E> output = new ChainedHashSet<>();
        for (V v :vSet) {
            set.makeSet(v);
        }
        for (E e : sorted) {
            if (set.findSet(e.getVertex1()) != set.findSet(e.getVertex2())) {
                output.add(e);
                set.union(e.getVertex1(), e.getVertex2());
            }
        }
        return output;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     *
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     *
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException  if there does not exist a path from the start to the end
     * @throws IllegalArgumentException if start or end is null or not in the graph
     */
    // public IList<E> findShortestPathBetween(V start, V end) {
    //     if (start == null || end == null || !vSet.contains(start)) {
    //         throw new IllegalArgumentException();
    //     }
    //     double inf = Double.POSITIVE_INFINITY;
    //     IPriorityQueue<ComparableVertex<V, E>> que = new ArrayHeap<>();
    //     IDictionary<V, ComparableVertex<V, E>> list = new ChainedHashDictionary<>();
    //     for (V v : vSet) {
    //         ComparableVertex<V, E> temp;
    //         if (v.equals(start)) {
    //             temp = new ComparableVertex<>(v, 0.0);
    //         } else {
    //             temp = new ComparableVertex<>(v, inf);
    //         }
    //         que.add(temp);
    //         list.put(v, temp);
    //     }
    //     while (!que.isEmpty() && !que.peekMin().name.equals(end)) {
    //         ComparableVertex<V, E> vertex = que.removeMin();
    //         V v = vertex.name;
    //         double distance = vertex.distance;
    //         for (E e : adjacency.get(v)) {
    //             V otherV = e.getOtherVertex(v);
    //             ComparableVertex<V, E> oldV;
    //             oldV = list.get(otherV);
    //             double oldD = oldV.distance;
    //             double newD = distance + e.getWeight();
    //             if (newD < oldD) {
    //                 IList<E> path = new DoubleLinkedList<>();
    //                 for (E prevPath : vertex.path) {
    //                     path.add(prevPath);
    //                 }
    //                 ComparableVertex<V, E> newV;
    //                 newV = new ComparableVertex<>(otherV, newD);
    //                 path.add(e);
    //                 newV.path = path;
    //                 que.remove(oldV);
    //                 que.add(newV);
    //                 list.put(otherV, newV);
    //             }
    //         }
    //     }
    //     ComparableVertex<V, E> v = list.get(end);
    //     if (v.distance == inf) {
    //         throw new NoPathExistsException();
    //     }
    //     return v.path;
    // }
    //
    // private static class ComparableVertex<V, E> implements Comparable<ComparableVertex<V, E>> {
    //     IList<E> path;
    //     final V name;
    //     final double distance;
    //
    //     public ComparableVertex(V vertex, Double distance) {
    //         this.path = new DoubleLinkedList<>();
    //         this.name = vertex;
    //         this.distance = distance;
    //     }
    //
    //     public int compareTo(ComparableVertex<V, E> vertex){
    //         return (int) (this.distance - vertex.distance);
    //     }
    // }
    //
    public IList<E> findShortestPathBetween(V start, V end) {
        if (start == null || end == null || !vSet.contains(start)) {
            throw new IllegalArgumentException();
        }
        double inf = Double.POSITIVE_INFINITY;
        IPriorityQueue<ComparableVertex<V, E>> que = new ArrayHeap<>();
        IDictionary<V, ComparableVertex<V, E>> list = new ChainedHashDictionary<>();
        IList<E> output = new DoubleLinkedList<>();
        for (V v : vSet) {
            ComparableVertex<V, E> temp;
            if (v.equals(start)) {
                temp = new ComparableVertex<>(v, 0.0, null);
            } else {
                temp = new ComparableVertex<>(v, inf, null);
            }
            que.add(temp);
            list.put(v, temp);
        }
        while (!que.isEmpty() && !que.peekMin().name.equals(end)) {
            ComparableVertex<V, E> vertex = que.removeMin();
            V v = vertex.name;
            double distance = vertex.distance;
            for (E e : adjacency.get(v)) {
                V otherV = e.getOtherVertex(v);
                ComparableVertex<V, E> oldV;
                oldV = list.get(otherV);
                double oldD = oldV.distance;
                double newD = distance + e.getWeight();
                if (newD < oldD) {
                    ComparableVertex<V, E> newV;
                    newV = new ComparableVertex<>(otherV, newD, e);
                    que.remove(oldV);
                    que.add(newV);
                    list.put(otherV, newV);
                }
            }
        }
        ComparableVertex<V, E> pathFinder = list.get(end);
        if (pathFinder.distance == inf) {
            throw new NoPathExistsException();
        }
        while (pathFinder.edge != null) {
            V v = pathFinder.name;
            E e = pathFinder.edge;
            V preV = e.getOtherVertex(v);
            pathFinder = list.get(preV);
            output.insert(0, e);
        }
        return output;

    }

    private static class ComparableVertex<V, E> implements Comparable<ComparableVertex<V, E>> {
        final E edge;
        final V name;
        final double distance;

        public ComparableVertex(V vertex, Double distance, E edge) {
            this.name = vertex;
            this.distance = distance;
            this.edge = edge;
        }

        public int compareTo(ComparableVertex<V, E> vertex){
            return (int) (this.distance - vertex.distance);
        }
    }
}
