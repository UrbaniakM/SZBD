package Entities;

public class Position {
    private int id;
    private String name;
    private int minWage;
    private int maxWage;

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

    public int getMinWage() {
        return minWage;
    }

    public void setMinWage(int minWage) {
        this.minWage = minWage;
    }

    public int getMaxWage() {
        return maxWage;
    }

    public void setMaxWage(int maxWage) {
        this.maxWage = maxWage;
    }
}
