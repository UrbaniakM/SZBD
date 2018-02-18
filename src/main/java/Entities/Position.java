package Entities;

public class Position {
    private String name;
    private Integer wage;

    public Position(){

    }

    public Position(Position position){
        this.name = position.getName();
        this.wage = position.getWage();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWage() {
        return wage;
    }

    public void setWage(Integer wage) {
        this.wage = wage;
    }
}
