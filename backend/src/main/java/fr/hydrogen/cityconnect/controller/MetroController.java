package fr.hydrogen.cityconnect.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.hydrogen.cityconnect.model.*;
import fr.hydrogen.cityconnect.service.Api;
import fr.hydrogen.cityconnect.utils.Walk;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Controller for the metro API
 */
@RestController
@RequiredArgsConstructor
public class MetroController {

    private final Api api;

    /**
     * Handle the request to the API
     * @param metro1 the first metro station
     * @param metro2 the second metro station
     * @return the shortest path between the two stations
     */
    @GetMapping("/api/metro")
    public String handleRequest(@RequestParam String metro1, @RequestParam String metro2) throws JsonProcessingException {

        try{

            List<Trajet<Station>> shortPath = api.getShortestPath(api.getStation(metro1),api.getStation(metro2));

            // Créer un objet ObjectMapper pour convertir les données en JSON
            ObjectMapper mapper = new ObjectMapper();
    
            // Créer un ArrayNode pour stocker les stations dans le chemin le plus court
            ArrayNode stations = mapper.createArrayNode();
    
            ObjectNode stationFirst = mapper.createObjectNode();
            stationFirst.put("name", shortPath.get(0).getSource().toString());
            stationFirst.put("lat", shortPath.get(0).getSourceGPS().getX());
            stationFirst.put("lon", shortPath.get(0).getSourceGPS().getY());
            stationFirst.put("ligne", shortPath.get(0).getVariantLigne().getNumero());
    
            stations.add(stationFirst);
    
            // Ajouter chaque station dans le chemin le plus court à l'ArrayNode
            for (Trajet<Station> trajet : shortPath) {
                ObjectNode station = mapper.createObjectNode();
                station.put("name", trajet.getDestination().toString());
                station.put("lat", trajet.getDestGPS().getX());
                station.put("lon", trajet.getDestGPS().getY());
                station.put("ligne", trajet.getVariantLigne().getNumero());
    
                stations.add(station);
            }
    
            // Convertir l'ArrayNode en JSON et le renvoyer en tant que réponse HTTP
            return mapper.writeValueAsString(stations);

        // TODO: différencier l'erreur lorsqu'on appelle la requete avec la meme station et quand on appelle la requete avec des coordonnées

        // } catch (IndexOutOfBoundsException e) {
        //     e.printStackTrace(System.err);
        //     ObjectMapper mapper = new ObjectMapper();
        //     ArrayNode stations = mapper.createArrayNode();
        //     return mapper.writeValueAsString(stations);

        } catch (Exception e){
            e.printStackTrace(System.err);
            String[] latLng1Split = metro1.split(" ");
            String[] latLng2Split = metro2.split(" ");

            Double lat1 = Double.parseDouble(latLng1Split[0]);
            Double lng1 = Double.parseDouble(latLng1Split[1]);
            Double lat2 = Double.parseDouble(latLng2Split[0]);
            Double lng2 = Double.parseDouble(latLng2Split[1]);

            CoordGPS c1=new CoordGPS(lng1, lat1);
            CoordGPS c2=new CoordGPS(lng2, lat2);

            List<Trajet<Station>> shortPath = api.getShortestPath(c1, c2);

            // Créer un objet ObjectMapper pour convertir les données en JSON
            ObjectMapper mapper = new ObjectMapper();

            // Créer un ArrayNode pour stocker les stations dans le chemin le plus court
            ArrayNode stations = mapper.createArrayNode();

            ObjectNode walkFirst = mapper.createObjectNode();
            walkFirst.put("name", "Marchez "+Walk.walkTimeInMin(lat1,lng1,shortPath.get(0).getSourceGPS().getY(),shortPath.get(0).getSourceGPS().getX())+"min");
            walkFirst.put("lat", lng1);
            walkFirst.put("lon", lat1);
            walkFirst.put("ligne", "AP");

            stations.add(walkFirst);

            ObjectNode stationFirst = mapper.createObjectNode();
            stationFirst.put("name", shortPath.get(0).getSource().toString());
            stationFirst.put("lat", shortPath.get(0).getSourceGPS().getX());
            stationFirst.put("lon", shortPath.get(0).getSourceGPS().getY());
            stationFirst.put("ligne", shortPath.get(0).getVariantLigne().getNumero());

            stations.add(stationFirst);

            // Ajouter chaque station dans le chemin le plus court à l'ArrayNode


            for (Trajet<Station> trajet : shortPath) {
                ObjectNode station = mapper.createObjectNode();
                station.put("name", trajet.getDestination().toString());
                station.put("lat", trajet.getDestGPS().getX());
                station.put("lon", trajet.getDestGPS().getY());
                station.put("ligne", trajet.getVariantLigne().getNumero());

                stations.add(station);
            }

            int size = shortPath.size();
            System.out.println(shortPath.get(size-1).getSourceGPS().getY());

            ObjectNode walkSecond = mapper.createObjectNode();
            walkSecond.put("name", "Marchez "+Walk.walkTimeInMin(lat2,lng2,shortPath.get(size-1).getDestGPS().getY(),shortPath.get(size-1).getDestGPS().getX())+"min");
            walkSecond.put("lat", lng2);
            walkSecond.put("lon", lat2);
            walkSecond.put("ligne", "AP");

            stations.add(walkSecond);

            // Convertir l'ArrayNode en JSON et le renvoyer en tant que réponse HTTP
            return mapper.writeValueAsString(stations);
            
        }

        
    }

    /**
     * This method is used to get the schedules of the line of direction in the station 
     * @param station the station
     * @param ligneString the line
     * @param direction the direction
     * @return the schedules of the line
     */
    @GetMapping("/api/horaires")
    public String metroHoraires(@RequestParam String station, @RequestParam String ligneString, @RequestParam String direction) throws JsonProcessingException{
        	ObjectMapper mapper = new ObjectMapper();
        	ArrayNode horaires = mapper.createArrayNode();


        	Station s = api.getStation(station);
            System.out.println(s);

            Ligne ligne = api.getLigne(ligneString);
            System.out.println(api.getDirection1(ligne)+"(1) "+api.getDirection2(ligne)+"(2)");

            //String terminusString = ""; // TODO
        	Station terminus = api.getStation(direction);
            System.out.println(terminus);

            DirectedLigne directedLigne = api.getDirectedLigne(ligne, terminus);
            //System.out.println(api.getHoraires(s, directedLigne));
            //System.out.println(s.getDirection1());

            ObjectNode stationNode = mapper.createObjectNode();
            stationNode.put("name", s.toString());
            stationNode.put("lat", s.getCoordGPS(directedLigne).getX());
            stationNode.put("lon", s.getCoordGPS(directedLigne).getY());

            horaires.add(stationNode);

            List<LocalTime> getHoraire = api.getHoraires(s, directedLigne);

            for (LocalTime hor : getHoraire) {
                ObjectNode horraire = mapper.createObjectNode();
                horraire.put("horraire", hor.toString());
                horaires.add(horraire);
            }
        	return mapper.writeValueAsString(horaires);
    }

    /**
     * This method is used to get the metro line of the station
     * @param station the station
     * @return the metro line of the station
     */
    @GetMapping("/api/Ligne")
    public String metroLigne(@RequestParam String station) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode lignes = mapper.createArrayNode();

        Station s = api.getStation(station);
        System.out.println(s);

        Set<VariantLigne> getLigne = s.getVariantsLigne();

        // Use a Set to store unique line numbers
        Set<String> uniqueLignes = new HashSet<>();

        // Collect unique line numbers
        for (VariantLigne ligne : getLigne) {
            uniqueLignes.add(ligne.getNumero());
        }

        // Add unique line numbers to ArrayNode
        for (String numeroLigne : uniqueLignes) {
            ObjectNode ligneNode = mapper.createObjectNode();
            ligneNode.put("ligne", numeroLigne);
            lignes.add(ligneNode);
        }

        return mapper.writeValueAsString(lignes);
    }

    /**
     * This method is used to get the direction of the metro line
     * @param ligneString the metro line
     * @return the direction of the metro line
     */
    @GetMapping("/api/Direction")
    public String metroDirection(@RequestParam String ligneString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode directions = mapper.createArrayNode();

        Ligne ligne = api.getLigne(ligneString);
        System.out.println(api.getDirection1(ligne)+"(1) "+api.getDirection2(ligne)+"(2)");

        ObjectNode direction1 = mapper.createObjectNode();
        direction1.put("direction", api.getDirection1(ligne).toString());
        directions.add(direction1);

        ObjectNode direction2 = mapper.createObjectNode();
        direction2.put("direction", api.getDirection2(ligne).toString());
        directions.add(direction2);

        return mapper.writeValueAsString(directions);
    }

    /**
     * This method handles the walk request
     * @param latLng1 the first coordinate
     * @param latLng2 the second coordinate
     * @return the walk time between the two coordinates on string format
     */
    @GetMapping("/api/walk")
    public String handleWalkRequest(@RequestParam String latLng1, @RequestParam String latLng2){
        System.out.println(latLng1);
        System.out.println(latLng2);
        String[] latLng1Split = latLng1.split(" ");
        String[] latLng2Split = latLng2.split(" ");

        Double lat1 = Double.parseDouble(latLng1Split[0]);
        Double lng1 = Double.parseDouble(latLng1Split[1]);
        Double lat2 = Double.parseDouble(latLng2Split[0]);
        Double lng2 = Double.parseDouble(latLng2Split[1]);

        System.out.println("lat1: "+lat1);
        System.out.println("lng1: "+lng1);
        System.out.println("lat2: "+lat2);
        System.out.println("lng2: "+lng2);
        CoordGPS c1 = new CoordGPS(lat1, lng1);
        CoordGPS c2 = new CoordGPS(lat2, lng2);

        double walkTime = Walk.walkTimeinSeconds(c1, c2)/60;

        System.out.println("walkTime: "+walkTime);

        return String.valueOf(walkTime);
    }

    /**
     * This method autocompletes the station name when the user is typing
     * @param station the station
     * @return the list of candidate names for the station name
     */
    @GetMapping("/api/autocomplete")
    public List<String> autocomplete(@RequestParam String station) {
    	return api.autocomplete(station);
    }
}