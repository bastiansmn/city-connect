package fr.hydrogen.cityconnect.model;

import java.time.Duration;

import fr.hydrogen.cityconnect.model.graph.Edge;
import fr.hydrogen.cityconnect.model.graph.Vertex;
import lombok.Getter;

@Getter
public class Trajet<T extends Vertex> extends Edge<T> {

    private final CoordGPS sourceGPS;
    private final CoordGPS destGPS;
    private final VariantLigne variantLigne;
    private final Duration time;
    private final double distance;

    /**
     * Constructor
     * @param source
     * @param sourceGPS
     * @param destination
     * @param destGPS
     * @param variantLigne
     * @param seconds
     * @param distance
     */
    public Trajet(T source, CoordGPS sourceGPS, T destination, CoordGPS destGPS, VariantLigne variantLigne, long seconds, double distance) {
        super(source, destination);
        this.sourceGPS = sourceGPS;
        this.destGPS = destGPS;
        this.variantLigne = variantLigne;
        this.time = Duration.ofSeconds(seconds);
        this.distance = distance;
    }

    /**
     * Constructor
     * @param source
     * @param sourceGPS
     * @param destination
     * @param destGPS
     * @param variantLigne
     * @param duration
     * @param distance
     */
    public Trajet(T source, CoordGPS sourceGPS, T destination, CoordGPS destGPS, VariantLigne variantLigne, Duration duration, double distance) {
        this(source, sourceGPS, destination, destGPS, variantLigne, duration.toSeconds(), distance);
    }
    
    /**
     * This function is an Override of the function toString()
     * @return a string with the information of the object Trajet
     */
    @Override
    public String toString() {
        return "(" + super.getSource().toString() + " -> " + super.getDestination().toString()
                // + ")";
                + "(" + variantLigne.getNumero() + ")" + ")";
                // + ", t=" + time + ", d=" + distance + ")";
    }

    /**
     * This function returns true if the arguments are equal to each other and false otherwise.
     * @param obj
     * @return true if the arguments are equal to each other and false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Trajet<?> other) {
            return super.equals(obj)
                && this.getVariantLigne().equals(other.getVariantLigne());
        }
        return false;
    }

    /**
     * This function returns the hash code of a non-null object and 0 for a null object.
     * @return the hash code of a non-null object and 0 for a null object.
     */
    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 31 * hash + this.getVariantLigne().hashCode();
        return hash;
    }

}
