package fr.hydrogen.cityconnect.utils;

import fr.hydrogen.cityconnect.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;

public class Parser {

    /**
     * This function parses the time from a string to seconds
     * @param timeStr
     * @return the time in seconds
     */
    private static long parseTime(String timeStr) {
        String[] parts = timeStr.split(":");
        long minutes = Long.parseLong(parts[0]);
        long seconds = Long.parseLong(parts[1]);
        return (minutes * 60 + seconds);
    }


    /**
     * This function parses the network from the file "filename" to a graph
     * @param filename
     * @param network
     */
    public static void parseGraph(String filename, Network<Station> network) {
        ClassLoader classLoader = Parser.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(filename);
        if (in == null) {
            throw new IllegalArgumentException("file not found! " + filename);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;

            // Ligne de métro
            String prevVariantString = null;
            Ligne ligne = null;
            VariantLigne variantLigne = null;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                Station source = network.getVertex( values[0] );
                if (source == null) {
                    network.addVertex(new Station(values[0], network));
                    source = network.getVertex( values[0] );
                }

                Station destination = network.getVertex( values[2] );
                if (destination == null) {
                    network.addVertex(new Station(values[2], network));
                    destination = network.getVertex( values[2] );
                }

                String[] sourceGPSSplit = values[1].split(",");
                CoordGPS sourceGPS = new CoordGPS(
                    Double.parseDouble(sourceGPSSplit[0]), 
                    Double.parseDouble(sourceGPSSplit[1])
                );
                
                String[] destGPSSplit = values[3].split(",");
                CoordGPS destGPS = new CoordGPS(
                    Double.parseDouble(destGPSSplit[0]), 
                    Double.parseDouble(destGPSSplit[1])
                );

                // Ligne de métro
                if (!values[4].equals(prevVariantString)) {
                    if (variantLigne != null) {
                        ligne.addVariantLigne(variantLigne);
                    }
                    
                    String[] ligneInfos = values[4].split(" variant ");
                    try {
                        ligne = network.getLigne(ligneInfos[0]);
                    } catch (Exception e) {
                        ligne = new Ligne(ligneInfos[0]);
                        network.addLigne(ligne);
                    }
                    variantLigne = new VariantLigne(ligne, Integer.parseInt(ligneInfos[1]));
                }

                Trajet<Station> trajet = new Trajet<Station>(
                    source, sourceGPS, 
                    destination, destGPS, 
                    variantLigne, (long)parseTime(values[5]), Double.parseDouble(values[6])
                );
                variantLigne.addTrajet(trajet);
                network.addEdge(trajet);

                prevVariantString = (values[4]);
            }
            // Add last variant
            ligne.addVariantLigne(variantLigne);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This function parses the time from the file "filename" and added them the graph "network"
     * @param filename
     * @param network
     */
    public static void parseTimetables(String filename, Network<Station> network){
        ClassLoader classLoader = Parser.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(filename);
        if (in == null) {
            throw new IllegalArgumentException("file not found! " + filename);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                boolean added = false;
                String[] values = line.split(";");
                
                // Parse
                String horaireString = values[2];
                if (horaireString.split(":")[0].length() == 1) {
                    horaireString = "0"+horaireString;
                }
                LocalTime horaire = LocalTime.parse(horaireString);

                // Add
                String numero = values[0];
                int variant = Integer.parseInt(values[3]);
                Station station = network.getVariantLigne(numero, variant).getDepart();
                if (station.name().equals(values[1])) {
                    added = network.addHoraire(numero, variant, horaire);
                }

                if (!added) {
                    // TODO: parsing error handling
                    System.err.println("Parsing error: timetables line"+lineCount);
                }
                ++lineCount;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
