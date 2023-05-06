package fr.hydrogen.cityconnect.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DirectedLigne {

    private final Ligne ligne;
    private final List<VariantLigne> variantLignes;
    private final LinkedList<Station> stations;

    /**
     * Constructor
     * @param ligne
     */
     public DirectedLigne(Ligne ligne) {
        this.ligne = ligne;
        this.variantLignes = new ArrayList<>();
        this.stations = new LinkedList<>();
    }

    /**
     * This function returns the departure station of the directed line
     * @return the departure station of the directed line
     */
    public Station getDepart() {
        List<Station> stations = getStations();
        return stations.get(0);
    }

    /**
     * This function returns the arrival station of the directed line
     * @return the arrival station of the directed line
     */
    public Station getArrivee() {
        List<Station> stations = getStations();
        return stations.get(stations.size()-1);
    }

    /**
     * This function returns true if the parameter variant is contained in the directed line, false otherwise
     * @param variant
     * @return true if the parameter variant is contained in the directed line, false otherwise
     */
    public boolean containsVariant(int variant) {
        return this.variantLignes.stream().anyMatch(v->v.getVariant() == variant);
    }

    /**
     * This function returns true if the parameter variantLigne is in the same direction as the directed line, false otherwise
     * @param variantLigne
     * @return true if the parameter variantLigne is in the same direction as the directed line, false otherwise
     */
    public boolean memeSens(VariantLigne variantLigne) {
        if (this.variantLignes.size() == 0) {
            return true;
        }
        boolean res = false;
        List<Station> variantLigneStations = variantLigne.getStations();
        for (Station station : this.stations) {
            if (variantLigneStations.contains(station)) {
                try {
                    Station nextStation1 = variantLigneStations.get(variantLigneStations.indexOf(station)+1);
                    Station nextStation2 = this.stations.get(this.stations.indexOf(station)+1);
                    res = nextStation1.equals(nextStation2);
                } catch (Exception ignored) {
                }
                break;
            }
        }
        if (!res) {
            for (Station station : variantLigneStations) {
                if (this.stations.contains(station)) {
                    try {
                        Station nextStation1 = variantLigneStations.get(variantLigneStations.indexOf(station)+1);
                        Station nextStation2 = this.stations.get(this.stations.indexOf(station)+1);
                        res = nextStation1.equals(nextStation2);
                    } catch (Exception ignored) {
                    }
                    break;
                }
            }
        }
        if (!res) {
            for (Station station : variantLigneStations) {
                for (VariantLigne vl : this.getVariantLignes()) {
                    if (vl.getStations().contains(station)) {
                        try {
                            Station nextStation1 = variantLigneStations.get(variantLigneStations.indexOf(station)+1);
                            Station nextStation2 = vl.getStations().get(vl.getStations().indexOf(station)+1);
                            res = res || nextStation1.equals(nextStation2);
                        } catch (Exception ignored) {
                        }
                        break;
                    }
                }
            }
        }
        return res;
    }
    
    /**
     * This function adds the parameter variantLigne to the directed line
     * @param variantLigne
     */
    public void addVariant(VariantLigne variantLigne) {
        if (!this.memeSens(variantLigne)) {
            return;
        }
        this.variantLignes.add(variantLigne);
        for (Trajet<Station> trajet : variantLigne.getTrajets()) {
            if (!this.stations.contains(trajet.getSource()) && this.stations.contains(trajet.getDestination())) {
                this.stations.addFirst(trajet.getSource());
            }
            else if (this.stations.contains(trajet.getSource()) && !this.stations.contains(trajet.getDestination())) {
                try {
                    this.stations.add(this.stations.indexOf(trajet.getSource())+1,trajet.getDestination());
                } catch (IndexOutOfBoundsException e) {
                    this.stations.addLast(trajet.getDestination());
                }
            }
            else if (!this.stations.contains(trajet.getSource()) && !this.stations.contains(trajet.getDestination())) {
                this.stations.addFirst(trajet.getDestination());
                this.stations.addFirst(trajet.getSource());
            }
        }
    }

    /**
     * This function returns the list of the variants of the directed line
     * @return the list of the variants of the directed line
     */
    public List<VariantLigne> getVariantLignes() {
        return variantLignes;
    }

    /**
     * This function returns the list of hours of passage of the directed line at the parameter station
     * @param station
     * @return the list of hours of passage of the directed line at the parameter station
     */
    public List<LocalTime> getHoraires(Station station) {
        List<LocalTime> res = new ArrayList<>();
        for (VariantLigne variantLigne : variantLignes) {
            if (variantLigne.getStations().contains(station)) {
                res.addAll(variantLigne.getHoraires(station));
            }
        }
        Collections.sort(res);
        return res;
    }

}
