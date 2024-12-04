package iset.dsi.tp7;

import java.io.Serializable;

public class Cours implements Serializable {
    private int id;  // Add an ID field
    private String name;
    private float nbHeures;
    private String type;
    private int teacherId;  // Foreign key for Teacher

    // Constructor
    public Cours(int id, String name, float nbHeures, String type, int teacherId) {
        this.id = id;
        this.name = name;
        this.nbHeures = nbHeures;
        this.type = type;
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", nbHeures=" + nbHeures +
                ", type='" + type + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNbHeures() {
        return nbHeures;
    }

    public void setNbHeures(float nbHeures) {
        this.nbHeures = nbHeures;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }


}
