package fr.hydrogen.cityconnect.model.graph;

import java.util.*;

public interface Graph<T extends Vertex, E extends Edge<T>> {

    /**
     * This function add a vertex to the graph.
     * @param vertex
     * @return true if the vertex is added successfully, false otherwise.
     */
    public boolean addVertex(T vertex);

    /**
     * This function add an edge to the graph.
     * @param edge
     * @return true if the edge is added successfully, false otherwise.
     */
    public boolean addEdge(E edge);

    /**
     * This function returns the vertex with the name given in parameter.
     * @param name
     * @return the vertex with the name given in parameter.
     */
    public T getVertex(String name);

    /**
     * This function returns the edge with the source and the destination given in parameter.
     * @param vertex
     * @return the edge with the source and the destination given in parameter.
     */
    public List<T> getAdjacents(T vertex);

    /**
     * This function returns the list of edges adjacent to the vertex given in parameter of the graph.
     * @param vertex
     * @return the list of edges adjacent to the vertex given in parameter of the graph.
     */
    public List<E> getAdjacentEdges(T vertex);

    /**
     * This function returns the list of edges of the graph.
     * @return the list of edges of the graph.
     */
    public Set<T> getVertexSet();

}
