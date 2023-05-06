package fr.hydrogen.cityconnect.model;

import fr.hydrogen.cityconnect.model.graph.Vertex;

public class VertexTestImpl implements Vertex{
    
    String name;
    public VertexTestImpl(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        VertexTestImpl other = (VertexTestImpl)obj;
        return this.name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

}
