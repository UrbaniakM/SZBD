package Entities;

public class Position {
    private int id;
    private String name;
    private float minWage;
    private float maxWage;

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

    public float getMinWage() {
        return minWage;
    }

    public void setMinWage(float minWage) {
        this.minWage = minWage;
    }

    public float getMaxWage() {
        return maxWage;
    }

    public void setMaxWage(float maxWage) {
        this.maxWage = maxWage;
    }
}
