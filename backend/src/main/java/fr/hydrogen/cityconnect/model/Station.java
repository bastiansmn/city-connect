package fr.hydrogen.cityconnect.model;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.hydrogen.cityconnect.model.graph.Vertex;

public record Station(String name, Network<Station> network) implements Vertex {

    /**
     * This function get the coordGPS of the variantLigne at the station
     * @param directedLigne
     * @return a coordGPS
     */
    public CoordGPS getCoordGPS(DirectedLigne directedLigne) {
        for (VariantLigne variantLigne : directedLigne.getVariantLignes()) {
            try {
                return this.getCoordGPS(variantLigne);
            } catch (RuntimeException e) {
                
            }
        }
        throw new RuntimeException("La station n'est pas sur la ligne");
    }

    /**
     * This function get the variantLigne at the station
     * @return a set of variantLigne
     */
    public CoordGPS getCoordGPS(VariantLigne variantLigne) {
        if (variantLigne.getDepart().equals(this)) {
            return variantLigne.getTrajets().get(0).getSourceGPS();
        }
        for (Trajet<Station> trajet : variantLigne.getTrajets()) {
            if (trajet.getDestination().equals(this)) {
                return trajet.getDestGPS();
            }
        }
        throw new RuntimeException("La station n'est pas sur le variant de ligne");
    }

    /**
     * This function get all the coordGPS of the variantLigne at the station
     * @return a set of coordGPS
     */
    public Set<CoordGPS> getAllCoordGPS() {
        Set<CoordGPS> res = new HashSet<>();
        for (VariantLigne variantLigne : this.getVariantsLigne()) {
            res.add(getCoordGPS(variantLigne));
        }
        return res;
    }

    /**
     * This function get the variantLigne at the station
     * @return a set of variantLigne
     */
    public Set<VariantLigne> getVariantsLigne() {
        Set<VariantLigne> res = new HashSet<>();
        for (Trajet<Station> trajet : network.getOutgoingEdges(this)) {
            res.add(trajet.getVariantLigne());
        }
        for (Trajet<Station> trajet : network.getIncomingEdges(this)) {
            res.add(trajet.getVariantLigne());
        }
        return res;
    }

    /**
     * This function get the schedule of the variantLigne at the station
     * @param variantLigne
     * @return a list of LocalTime
     */
    public List<LocalTime> getHoraires(VariantLigne variantLigne) {
        return variantLigne.getHoraires(this);
    }

    /**
     * This function returns the string representation of the station
     * @return the string representation of the station
     */
    @Override
    public String toString() {
        return this.name;
    }

    /**
     * This function returns true if the station is equal to the object, false otherwise
     * @param obj the object to compare
     * @return true if the station is equal to the object, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Station other) {
            return this.name.equals(other.name);
        }
        return false;
    }

    /**
     * This function returns the hashcode of the station
     * @return the hashcode of the station
     */
    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
