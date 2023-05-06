package fr.hydrogen.cityconnect.service;

import fr.hydrogen.cityconnect.model.CoordGPS;
import fr.hydrogen.cityconnect.model.Network;
import fr.hydrogen.cityconnect.model.Station;
import fr.hydrogen.cityconnect.model.Trajet;
import fr.hydrogen.cityconnect.model.VariantLigne;
import fr.hydrogen.cityconnect.model.graph.AbstractDijkstra;
import fr.hydrogen.cityconnect.model.graph.Graph;
import fr.hydrogen.cityconnect.utils.Walk;

import java.time.LocalTime;
import java.util.*;

public class DistanceStrategy extends AbstractDijkstra<Station, Trajet<Station>> implements Strategy {

    /**
     * This function returns the path between two stations in a network at a given time using the Dijkstra algorithm and the distance as weight.
     * @param network
     * @param source
     * @param destination
     * @param localTime
     * @return the path between two stations in a network at a given time using the Dijkstra algorithm and the distance as weight.
     */
    @Override
    public List<Trajet<Station>> path(Network<Station> network, Station source, Station destination, LocalTime localTime) {
        LinkedList<Trajet<Station>> path=new LinkedList<>();
        HashMap<Station, Trajet<Station>> predecessor=dijkstra(network, source);
        Trajet<Station> mem=predecessor.get(destination);
        while(mem!=null){
            path.push(mem);
            mem=predecessor.get(mem.getSource());
        }
        return path;
    }

    /**
     * This function is used to find the nearest station from a given coordinate in a network.
     * @param network
     * @param c1
     * @return the nearest station from a given coordinate in a network.
     */
    @Override
    public Station getNearestStation(Network<Station> network, CoordGPS c1){
        Station res=null;
        double min=1000000;
        for(Station test : network.getVertexSet()){
            for( Trajet<Station> trajettest : network.getAdjacentEdges(test)){
                CoordGPS c2 = trajettest.getSourceGPS();
                if( Walk.distanceInKm(c1.getX(),c1.getY(), c2.getX(), c2.getY()) < min){
                    res=test;
                    min=Walk.distanceInKm(c1.getX(),c1.getY(), c2.getX(), c2.getY());
                }
            }
        }
        return res;
    }

    /**
     * This function updates the distance.
     * @param dist
     * @param predecessor
     * @param edge
     * @param w
     */
    private void update_distance(HashMap<Station,Double> dist, HashMap<Station, Trajet<Station>> predecessor, Trajet<Station> edge, double w){
        Trajet<Station> previoustrajet = predecessor.get(edge.getSource());
        VariantLigne vls;
        VariantLigne vld;
        if(previoustrajet != null){
            vls = previoustrajet.getVariantLigne();
            vld = edge.getVariantLigne();
            if( vls.getVariant()!=vld.getVariant() || !(vls.getLigne().getNumero().equals(vld.getLigne().getNumero()))){
                CoordGPS c1 = predecessor.get(edge.getSource()).getDestGPS();
                CoordGPS c2 = edge.getSourceGPS();
                w += Walk.distanceInKm(c1.getX(),c1.getY(),c2.getX(),c2.getY());
            }
        }
        if ( (dist.get(edge.getSource())!=-1. && dist.get(edge.getDestination()) == -1.) || 
            (dist.get(edge.getSource())!=-1. && dist.get(edge.getDestination()) > dist.get(edge.getSource()) + w) )
        {
            dist.replace(edge.getDestination(),dist.get(edge.getSource())+ w);
            predecessor.replace(edge.getDestination(),edge);
        }
    }

    /**
     * This function applies the Dijkstra algorithm to find the shortest path between two stations in a network using the distance as weight.
     * @param graph
     * @param source
     * @return the shortest path between two stations in a network using the distance as weight.
     */
    private HashMap<Station, Trajet<Station>> dijkstra(Graph<Station, Trajet<Station>> graph, Station source){
        if (!graph.getVertexSet().contains(source)) {
            return null;
        }
        List<Station> P=initializationP(graph);
        HashMap<Station, Trajet<Station>> predecessor=initializationPredecessor(graph);
        HashMap<Station, Double> dist=initializationDist(graph, source);
        while(!P.isEmpty()){
            Station t1=find_min(P,dist);
            P.remove(t1);
            List <Trajet<Station>> adjacentEdges = graph.getAdjacentEdges(t1);
            for (Trajet<Station> adjacentEdge: adjacentEdges){
                update_distance(dist,predecessor,adjacentEdge,adjacentEdge.getDistance());
            }
        }
        return predecessor;
    }
    
}
