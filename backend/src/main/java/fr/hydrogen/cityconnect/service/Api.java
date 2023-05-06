package fr.hydrogen.cityconnect.service;

import fr.hydrogen.cityconnect.model.*;
import fr.hydrogen.cityconnect.utils.*;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class Api 
{
    private final Network<Station> network;
    private Strategy strategy;
    
    /**
     * Concstructor
     */
    public Api() {
        network = Api.initGraph();
        this.strategy = new TimeStrategy();
    }

    /**
     * This method gets the network
     * @return the network
     */
    public Network<Station> getNetwork() {
        return network;
    }

    /**
     * This function uses the param in to choose if the user want the shortestPath based on time or distance
     * @param in
     */
    public void setStrategy(String in) {
        if (in.startsWith("d")) {
            this.strategy = new DistanceStrategy();
        } else {
            this.strategy = new TimeStrategy();
        }
    }

    /**
     * This method sets the strategy
     * @param strategy
     */
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * This method gets the station with the name in param
     * @param name
     * @return the station
     */
    public Station getStation(String name) {
        return this.network.getVertex(name);
    }

    /**
     * This function get the list of station with the number "numeroLigne" and the direction "sens"
     * @param numeroLigne
     * @param sens
     * @return the list of station
     */
    public List<Station> getStations(String numeroLigne, int sens) {
        Ligne ligne = this.getLigne(numeroLigne);
        if (sens == 2) {
            return ligne.getDirectedLigne(ligne.getDirection2()).getStations();
        }
        return ligne.getDirectedLigne(ligne.getDirection1()).getStations();
    }

    /**
     * This function get the list of station with directionLigne
     * @param directedLigne
     * @return the list of station
     */
    public List<Station> getStations(DirectedLigne directedLigne) {
        return directedLigne.getStations();
    }

    /**
     * This function get line "ligne" with the number "numero"
     * @param numero
     * @return line "ligne" with the number "numero"
     */
    public Ligne getLigne(String numero) {
        return this.network.getLigne(numero);
    }

    /**
     * This function get the list of Ligne
     * @return the list of Ligne
     */
    public List<Ligne> getLignes() {
        return this.network.getLignes();
    }

    /**
     * This function get the direction1 of the line "ligne" in the station "station"
     * @param ligne
     * @return the direction1 of the line "ligne" in the station "station"
     */
    public Station getDirection1(Ligne ligne) {
        return ligne.getDirectedLigne1().getArrivee();
    }

    /**
     * This function get the direction2 of the line "ligne" in the station "station"
     * @param ligne
     * @return the direction2 of the line "ligne" in the station "station"
     */
    public Station getDirection2(Ligne ligne) {
        return ligne.getDirectedLigne2().getArrivee();
    }

    /**
     * This function get the direction1 of the line with the number "numeroLigne" in the station "station"
     * @param numeroLigne
     * @return the direction1 of the line with the number "numeroLigne" in the station "station"
     */
    public Station getDirection1(String numeroLigne) {
        return this.getLigne(numeroLigne).getDirectedLigne1().getArrivee();
    }

    /**
     * This function get the direction2 of the line with the number "numeroLigne" in the station "station"
     * @param numeroLigne
     * @return the direction2 of the line with the number "numeroLigne" in the station "station"
     */
    public Station getDirection2(String numeroLigne) {
        return this.getLigne(numeroLigne).getDirectedLigne2().getArrivee();
    }

    /**
     * This function get the directedLigne of the line "ligne" and with the terminus "terminus"
     * @param ligne
     * @return the directedLigne of the line "ligne" and with the terminus "terminus"
     */
    public DirectedLigne getDirectedLigne(Ligne ligne, Station terminus) {
        return ligne.getDirectedLigne(terminus);
    }

    /**
     * This function get the directedLigne of the line with the number "numeroLigne" and with the terminus "terminus"
     * @param numeroLigne
     * @return the directedLigne of the line with the number "numeroLigne" and with the terminus "terminus"
     */
    public DirectedLigne getDirectedLigne(String numeroLigne, Station terminus) {
        return this.getLigne(numeroLigne).getDirectedLigne(terminus);
    }

    /**
     * This function get the schedules of the directedLigne "directedLigne" in the station "station"
     * @param station
     * @param directedLigne
     * @return a list of LocalTime
     */
    public List<LocalTime> getHoraires(Station station, DirectedLigne directedLigne) {
        return directedLigne.getHoraires(station);
    }


    /**
     * This function get the shortest path from the station "source" to the station "destination"
     * @param source
     * @param destination
     * @return a list of trajet
     */
    public List<Trajet<Station>> getShortestPath(Station source, Station destination) {
        List<Trajet<Station>> res = strategy.path(network, source, destination, LocalTime.now());
        System.out.println(res);
        return res;
    }

    /**
     * This function get the shortest path from the coordGPS "c1" to the coordGPS "c2"
     * @param c1
     * @param c2
     * @return
     */
    public List<Trajet<Station>> getShortestPath(CoordGPS c1, CoordGPS c2) {
        Station nearestFirstStation = strategy.getNearestStation(network,c1);
        Duration tps= Duration.ofSeconds( Walk.walkTimeinSeconds(c1, nearestFirstStation) );
        LocalTime time = LocalTime.now().plus(tps);

        Station nearestLastStation = strategy.getNearestStation(network,c2);

        return strategy.path(network, nearestFirstStation, nearestLastStation, time);
    }

    /**
     * This function get the shortest path from the station with the name "source" to the station with the name "destination"
     * @param source
     * @param destination
     * @return a list of trajet
     */
    public List<Trajet<Station>> getShortestPath(String source, String destination) {
        return this.getShortestPath(this.getStation(source),this.getStation(destination));
    }

    private static Network<Station> initGraph() {
        Network<Station> network = new Network<>();
        Parser.parseGraph("static/map_data.csv", network);
        Parser.parseTimetables("static/timetables.csv", network);

        String cheminDossier = System.getProperty("user.dir");

        // Imprimer le chemin dans la console
        System.out.println("Chemin vers le dossier actuel : " + cheminDossier);
        return network;
    }


    public static void main( String[] args ) {
        Api api = new Api();

		Scanner sc = new Scanner(System.in);
        String reponse;

        do {
			do{
				System.out.println(
					"Que voulez-vous faire ?\n"+
					"  0 -> Quitter \n"+
					"  1 -> Chercher un itinéraire \n"+
                    "  2 -> Consulter les horaires d'une station \n"
				);
				reponse = sc.nextLine();
			}while(reponse.isEmpty());

            switch (reponse) {
                case "0":
                    System.out.println("Exit");
                    break;
                
                case "1":
                    Station depart = null;
                    Station arrivee = null;
                    do {
                        System.out.println( "Départ: " );
                        depart = api.getStation(sc.nextLine());
                    } while (depart == null);
                    do {
                        System.out.println( "Arrivée: " );
                        arrivee = api.getStation(sc.nextLine());
                    } while (arrivee == null);
                    System.out.println( "temps ou distance: (temps par défault) " );
                    api.setStrategy(sc.nextLine());
                    api.getShortestPath(depart, arrivee);
                    break;
                    
                case "2":
                    System.out.println("Quelle ligne ?");
                    String numeroLigne = sc.nextLine();
                    Ligne ligne = api.getLigne(numeroLigne);
                    DirectedLigne directedLigne = null;
                    while (directedLigne == null) {
                        System.out.println("Quelle Destination ?");
                        System.out.println(api.getDirection1(ligne)+"(1) "+api.getDirection2(ligne)+"(2)");
                        String direction = sc.nextLine();
                        if (direction.equals("1")) {
                            directedLigne = api.getDirectedLigne(ligne.getNumero(), ligne.getDirection1());
                        } else if (direction.equals("2")) {
                            directedLigne = api.getDirectedLigne(ligne.getNumero(), ligne.getDirection2());
                        } else {
                            try {
                                Station terminus = api.getStation(direction);
                                directedLigne = api.getDirectedLigne(ligne, terminus);
                            } catch (IllegalArgumentException e) {
                                directedLigne = null;
                            }
                        }
                    }
                    System.out.println("Quelle station ?");
                    System.out.println(directedLigne.getStations());
                    Station station = api.getStation(sc.nextLine());
                    System.out.println(api.getHoraires(station, directedLigne));
                    break;

                default:
                    reponse = "";
            }

            sc.nextLine();

        } while (!reponse.equals("0"));
        
        sc.close();
    }

    /**
     * This function auto complete the station name entered by the final user
     * @param station
     * @return a list of candidate string that could complete the station entered by the final user
     */
    public List<String> autocomplete(String station) {
        List<String> stations = new ArrayList<>();

        var normalizedSearchInput = StringUtils.removeAccents(station.toLowerCase());

        for (Station s : this.network.getVertexSet()) {
            var normalizedStationName = StringUtils.removeAccents(s.name().toLowerCase());

            // Classic case-insensitive autocomplete
            if (normalizedStationName.contains(normalizedSearchInput) && (!stations.contains(s.name()))) {
                stations.add(s.name());
            }

            // Levenstein distance autocomplete
            if (StringUtils.levenshteinDistance(normalizedStationName, normalizedSearchInput) < 3 && (!stations.contains(s.name()))) {
                stations.add(s.name());
            }
        }

        return stations;
    }
}
