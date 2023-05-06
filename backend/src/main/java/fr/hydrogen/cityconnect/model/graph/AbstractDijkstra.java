package fr.hydrogen.cityconnect.model.graph;

import java.util.*;

public abstract class AbstractDijkstra<T extends Vertex,E extends Edge<T>> {
    
    /**
     * This function initialize the distance of each vertex 
     * @param graph
     * @param source
     * @return an HashMap with the distance of each vertex
     */
    protected HashMap<T,Double> initializationDist(Graph<T,E> graph, T source){
        HashMap<T,Double> dist = new HashMap<>();
        graph.getVertexSet().forEach(k -> dist.put(k,-1.));
        dist.replace(source,0.);
        return dist;
    }

    /**
     * This function initialize the list of vertex
     * @param graph
     * @return a list of vertex
     */
    protected List<T> initializationP(Graph<T,E> graph){
        return new LinkedList<>(graph.getVertexSet());
    }

    /**
     * This function initialize the predecessor of each vertex
     * @param graph
     * @return an HashMap with the predecessor of each vertex
     */
    protected HashMap<T,E> initializationPredecessor(Graph<T,E> graph){
        HashMap<T,E> predecessor=new HashMap<>();
        graph.getVertexSet().forEach(k -> predecessor.put(k,null));
        return predecessor;
    }

    /**
     * This function find the minimum distance in the list
     * @param list
     * @param dist
     * @return the vertex with the minimum distance
     */
    protected T find_min(List<T> list, HashMap<T,Double> dist){
        if(list.isEmpty()){
            return null;
        }
        Double min=dist.get(list.get(0));
        T res=list.get(0);
        for(int i=1; i<list.size(); i++){
            if(min == -1. || (dist.get(list.get(i))!= -1. && dist.get(list.get(i)) < min)){
                min=dist.get(list.get(i));
                res=list.get(i);
            }
        }
        return res;
    }

}
