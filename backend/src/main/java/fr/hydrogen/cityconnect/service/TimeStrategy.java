package fr.hydrogen.cityconnect.service;

import java.time.LocalTime;

import fr.hydrogen.cityconnect.model.CoordGPS;
import fr.hydrogen.cityconnect.model.Network;
import fr.hydrogen.cityconnect.model.Station;
import fr.hydrogen.cityconnect.model.Trajet;
import fr.hydrogen.cityconnect.model.VariantLigne;
import fr.hydrogen.cityconnect.model.graph.AbstractDijkstra;
import fr.hydrogen.cityconnect.model.graph.Graph;
import fr.hydrogen.cityconnect.utils.Walk;

import java.util.*;

public class TimeStrategy extends AbstractDijkstra<Station, Trajet<Station>> implements Strategy {

    /**
     * This function is used to find the shortest path between two stations in a network using the Dijkstra algorithm and the time as weight.
     * @param network
     * @param source
     * @param destination
     * @param localTime
     * @return the shortest path between two stations in a network using the Dijkstra algorithm and the time as weight.
     */
    @Override
    public List<Trajet<Station>> path(Network<Station> network, Station source, Station destination, LocalTime localTime) {
        LinkedList<Trajet<Station>> path=new LinkedList<>();
        HashMap<Station, Trajet<Station>> predecessor=dijkstra(network, source, localTime);
        if (predecessor==null) {
            return List.of();
        }
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
                if( Walk.walkTimeinSeconds(c1, c2) < min){
                    System.out.println("coucou");
                    res=test;
                    min=Walk.walkTimeinSeconds(c1, c2);
                }
            }
        }
        return res;
    }

    // public List<Trajet<Station>> exPathWithCoord(Graph<Station, Trajet<Station>> graph, CoordGPS c1, CoordGPS c2){
    //     Trajet<Station> t1 = this.getnearestsation(graph,c1);
    //     Station source=t1.getSource();
    //     double tps= Walk.walkTimeinSeconds(c1, t1.getSourceGPS());
    //     LocalTime add=LocalTime.ofSecondOfDay((long)tps);
    //     Trajet<Station> t2 = this.getnearestsation(graph,c2);
    //     Station destination=t2.getSource();
    //     LocalTime time = LocalTime.now();
    //     time = time.plusHours((long)add.getHour());
    //     time = time.plusMinutes((long)add.getMinute());
    //     time = time.plusSeconds((long)add.getSecond());

    //     LinkedList<Trajet<Station>> path=new LinkedList<>();
    //     HashMap<Station, Trajet<Station>> predecessor=dijkstra(graph, source, LocalTime.now());
    //     if (predecessor==null) {
    //         return List.of();
    //     }
    //     Trajet<Station> mem=predecessor.get(destination);
    //     while(mem!=null){
    //         path.push(mem);
    //         mem=predecessor.get(mem.getSource());
    //     }
    //     return path;
    // }

    /**
     * This function is used to find the waiting time penalty for a given edge and a given start time.
     * @param edge
     * @param start
     * @return the waiting time penalty for a given edge and a given start time.
     */
    public double get_wait_penalty(Trajet<Station> edge, double start){
        VariantLigne vld = edge.getVariantLigne();
        double min=1000000000;
        List<LocalTime> list = ((Station) edge.getSource()).getHoraires(vld);
        for (LocalTime t : list){
            double test = t.toSecondOfDay();
            if(test-start>0 && test-start<min){
                min=test-start;
            }
        }
        return min;
    }


    /**
     * This function is used to update the distance.
     * @param dist
     * @param predecessor
     * @param edge
     * @param starttime
     */
    private void updateDistance(HashMap<Station,Double> dist, HashMap<Station, Trajet<Station>> predecessor, Trajet<Station> edge, LocalTime starttime){
        Trajet<Station> previoustrajet = predecessor.get(edge.getSource());
        VariantLigne vls;
        VariantLigne vld;
        double w;
        if(previoustrajet == null){
            double min=get_wait_penalty(edge, starttime.toSecondOfDay());
            w = min + edge.getTime().toSeconds();
        } else {
            vls = previoustrajet.getVariantLigne();
            vld = edge.getVariantLigne();
            if( vls.getVariant()==vld.getVariant() && vls.getLigne().getNumero().equals(vld.getLigne().getNumero())){
                w = edge.getTime().toSeconds();
            } else {
                double min = get_wait_penalty(edge, starttime.toSecondOfDay()+dist.get(edge.getSource())+Walk.walkTimeinSeconds(predecessor.get(edge.getSource()).getDestGPS(), edge.getSourceGPS()));
                w = min + edge.getTime().toSeconds();
            }
        }
        if ( (dist.get(edge.getSource())!=-1. && dist.get(edge.getDestination()) == -1.) || 
            (dist.get(edge.getSource())!=-1. && dist.get(edge.getDestination()) > dist.get(edge.getSource()) + w) ){
            dist.replace(edge.getDestination(),dist.get(edge.getSource())+ w);
            predecessor.replace(edge.getDestination(),edge);
        }
    }

    /**
     * This function applies the dijkstra algorithm to a given graph, a given source and a given start time.
     * @param graph
     * @param source
     * @param lt
     * @return a HashMap containing the predecessor of each station.
     */
    private HashMap<Station, Trajet<Station>> dijkstra(Graph<Station, Trajet<Station>> graph, Station source, LocalTime lt){
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
                updateDistance(dist,predecessor,adjacentEdge,lt);
            }
        }
        return predecessor;
    }

}