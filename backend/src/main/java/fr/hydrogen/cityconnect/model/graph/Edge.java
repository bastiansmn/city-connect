package fr.hydrogen.cityconnect.model.graph;

public abstract class Edge <T extends Vertex>{

    private final T source;
    private final T destination;

    /**
     * Constructor of Edge
     * @param source
     * @param destination
     */
    public Edge(T source, T destination){
        this.source = source;
        this.destination = destination;
    }

    /**
     * This function returns the source of the edge.
     * @return the source of the edge.
     */
    public T getSource() {
        return source;
    }

    /**
     * This function returns the destination of the edge.
     * @return the destination of the edge.
     */
    public T getDestination() {
        return destination;
    }

    /**
     * This function return the string that represents the edge.
     * @return the string that represents the edge.
     */
    @Override
    public String toString() {
        return "(" + source.toString() + " -> " + destination.toString() + ")";
    }

    /**
     * This function compare two edges.
     * @param obj
     * @return true if the edges are equals, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Edge) {
            return this.getSource().equals(((Edge<?>)(obj)).getSource())
                && this.getDestination().equals(((Edge<?>)(obj)).getDestination());
        }
        return false;
    }

    /**
     * This function returns the hashcode of the edge.
     * @return the hashcode of the edge.
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.getSource().hashCode();
        hash = 31 * hash + this.getDestination().hashCode();
        return hash;
    }

}
