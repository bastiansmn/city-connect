package fr.hydrogen.cityconnect.utils;

import fr.hydrogen.cityconnect.model.CoordGPS;
import fr.hydrogen.cityconnect.model.Station;

public class Walk {

    public static void main(String[] args) {
        double lat1 = 48.8576887395772; // latitude de départ
        double lon1 = 2.347759259772123; // longitude de départ
        double lat2 = 48.85757442237991; // latitude d'arrivée
        double lon2 = 2.35154698561547; // longitude d'arrivée

        double distanceInKm = distanceInKm(lat1, lon1, lat2, lon2);
        double walkSpeedInKmPerHour = 5.0; // vitesse de marche moyenne en km/h
        double walkTimeInHours = distanceInKm / walkSpeedInKmPerHour;
        int walkTimeInSeconds = (int) (walkTimeInHours * 3600);
        int walkTimeInMinutes = (int)(walkTimeInHours * 60);

        System.out.println("Temps de marche: " + walkTimeInMinutes + " minutes");
        System.out.println("Temps de marche: " + walkTimeInSeconds + " secondes");
    }

    /**
     * This function returns the distance in Km between two points 
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return the distance in Km between two points
     */
    public static double distanceInKm(double lat1, double lon1, double lat2, double lon2) {
        int radiusOfEarthInKm = 6371;

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (radiusOfEarthInKm * c);
    }

    /**
     * This function returns the wallking time in minutes between two points 
     * @param lat1
     * @param lat2
     * @param lon1
     * @param lon2
     * @return the wallking time in minutes between two points
     */
    public static int walkTimeInMin(double lat1, double lon1, double lat2, double lon2){
        double distanceInKm = Walk.distanceInKm(lat1, lon1, lat2, lon2);
        double walkSpeedInKmPerHour = 5.0; // vitesse de marche moyenne en km/h
        double walkTimeInHours = distanceInKm / walkSpeedInKmPerHour;
        int walkTimeInMinutes = (int)(walkTimeInHours * 60);

        return walkTimeInMinutes;
    }

    /**
     * This function returns the wallking time in seconds between two coordGPS
     * @param c1
     * @param c2
     * @return the wallking time in seconds between two coordGPS
     */
    public static long walkTimeinSeconds(CoordGPS c1, CoordGPS c2){
        double lat1=c1.getX();
        double lon1=c1.getY();
        double lat2=c2.getX();
        double lon2=c2.getY();
        double distanceInKm = distanceInKm(lat1, lon1, lat2, lon2);
        double walkSpeedInKmPerHour = 5.0; // vitesse de marche moyenne en km/h
        double walkTimeInHours = distanceInKm / walkSpeedInKmPerHour;
        double walkTimeInSeconds = (walkTimeInHours * 3600);

        return (long) walkTimeInSeconds;
    }

    /**
     * This function returns the wallking time in seconds between the coordGPS and the station
     * @param coordGPS
     * @param station
     * @return the wallking time in seconds between the coordGPS and the station
     */
    public static long walkTimeinSeconds(CoordGPS coordGPS, Station station){
        long walkTimeInSeconds = 0;
        for (CoordGPS stationGPS : station.getAllCoordGPS()) {
            walkTimeInSeconds = Long.max(walkTimeInSeconds, walkTimeinSeconds(coordGPS, stationGPS));
        }
        return walkTimeInSeconds;
    }

}
