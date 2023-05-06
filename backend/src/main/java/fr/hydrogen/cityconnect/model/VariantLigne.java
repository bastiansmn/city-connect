package fr.hydrogen.cityconnect.model;

import lombok.Getter;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class VariantLigne {
    
    private final Ligne ligne;
    private final int variant;
    private final List<Trajet<Station>> trajets;
    private final List<LocalTime> horairesDepart;

    /**
     * Constructor
     * @param ligne
     * @param variant
     */
    public VariantLigne(Ligne ligne, int variant){
        this.ligne = ligne;
        this.variant = variant;
        this.trajets = new ArrayList<>();
        this.horairesDepart = new ArrayList<>();
    }

    /**
     * This function adds trajet to the list of trajet
     * @param trajet
     */
    public void addTrajet(Trajet<Station> trajet) {
        this.trajets.add(trajet);
    }

    /**
     * THis function adds Horaire to the list of departure time
     * @param horaire
     * @return true if horaire is added successfully, false otherwise
     */
    public boolean addHoraire(LocalTime horaire) {
        return this.horairesDepart.add(horaire);
    }

    /**
     * This function get the number of lignr
     * @return numero, the number of the ligne
     */
    public String getNumero() {
        return ligne.getNumero();
    }

    /**
     * This function get a list of station
     * @return the list of station
     */
    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        if (!trajets.isEmpty()) {
            stations.add(trajets.get(0).getSource());
        }
        for (Trajet<Station> trajet : trajets) {
            stations.add(trajet.getDestination());
        }
        return stations;
    }

    /**
     * This function get the list of trajet
     * @return a list of trajet
     */
    public List<Trajet<Station>> getTrajets() {
        return new ArrayList<>(trajets);
    }

    /**
     * This function get the departure station
     * @return the departure station
     */
    public Station getDepart() {
        return this.trajets.get(0).getSource();
    }

    /**
     * This function returns the arrival station
     * @return the arrival station
     */
    public Station getArrivee() {
        return this.trajets.get(this.trajets.size()-1).getDestination();
    }

    /**
     * This function return the path duration time from the start to the station "station"
     * @param station
     * @return the path duration time from the first station to the station "station"
     */
    private Duration pathDurationFromStartToStation(Station station) {
        if (this.getStations().contains(station)) {
            Duration res = Duration.ZERO;
            Station currStation = getDepart();

            int i = 0;
            while (i < trajets.size() && !station.equals(currStation)) {
                Trajet<Station> trajet = trajets.get(i);
                res.plus(trajet.getTime());
                currStation = trajet.getDestination();
                i++;
            }
            return res;
        }
        throw new IllegalArgumentException();
    }
     /**
     * This function get the list of hours on the station station
     * @param station
     * @return the list of hours
     */
    public List<LocalTime> getHoraires(Station station) {
        List<LocalTime> res = new ArrayList<>();
        Collections.sort(horairesDepart);
        Duration duration = pathDurationFromStartToStation(station);
        for (LocalTime localTime : horairesDepart) {
            res.add(localTime.plus(duration));
        }
        return res;
    }

    /**
     * This function get the list of departure hours "horaire"
     * @return the list of departure hours
     */
    public List<LocalTime> getHoraires() {
        Collections.sort(horairesDepart);
        return new ArrayList<>(horairesDepart);
    }

    /**
     * This function is an Override of the function toString()
     * @return a string with the information of the object VariantLigne
     */
    @Override
    public String toString() {
        return this.getNumero() + " variant " + this.variant;
    }

}
