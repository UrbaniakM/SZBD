package Entities;

public class Position {
    private String name;
    private Integer wage;
    private Integer id;

    public Position(){

    }

    public Position(Position position){
        this.id = position.getId();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
