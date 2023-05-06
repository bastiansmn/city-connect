package fr.hydrogen.cityconnect.service;

import java.time.LocalTime;
import java.util.List;

import fr.hydrogen.cityconnect.model.CoordGPS;
import fr.hydrogen.cityconnect.model.Network;
import fr.hydrogen.cityconnect.model.Station;
import fr.hydrogen.cityconnect.model.Trajet;

public interface Strategy {

    /**
     * This function returns the path between two stations in a network at a given time.
     * @param network
     * @param source
     * @param destination
     * @param localTime
     * @return the path between two stations in a network at a given time.
     */
    public List<Trajet<Station>> path(Network<Station> network, Station source, Station destination, LocalTime localTime);

    /**
     * This function returns the nearest station from a given GPS coordinate in a network.
     * @param network
     * @param coordGPS
     * @return the nearest station from a given GPS coordinate in a network.
     */
    public Station getNearestStation(Network<Station> network, CoordGPS coordGPS);
    
}
