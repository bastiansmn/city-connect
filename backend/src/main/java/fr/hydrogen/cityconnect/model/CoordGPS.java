package fr.hydrogen.cityconnect.model;

public record CoordGPS(double x, double y) {

    /**
     * Constructor
     * @param o
     */
    public CoordGPS(CoordGPS o) {
        this(o.x, o.y);
    }

    /**
     * This function returns true if the GPS coordinate is equal to the object, false otherwise
     * @param obj the object to compare
     * @return true if the GPS coordinate is equal to the object, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CoordGPS) {

            return this.x == ((CoordGPS) (obj)).x && this.y == ((CoordGPS) (obj)).y;
        }
        return false;
    }

    /**
     * This function returns the x coordinate of the GPS coordinate
     * @return the coordinate x
     */
    public double getX() {
        return this.x;
    }

    /**
     * This function returns the y coordinate of the GPS coordinate
     * @return the coordinate y
     */
    public double getY() {
        return this.y;
    }

    /**
     * This function returns the string representation of the GPS coordinate
     * @return the string representation of the GPS coordinate
     */
    @Override
    public String toString() {
        return "(" + this.x + "," + this.y + ")";
    }

}
