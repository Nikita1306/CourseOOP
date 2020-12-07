package player;

import team.Team;

import javax.persistence.*;
@Entity
@Table(name = "player")
public class Player {
    private String surname;
    @Column(name = "firstname")
    private String firstName;
    @Id
    @Column(name = "number")
    private int number;
    @Column(name = "position")
    private String position;
    private int goals;
    private int assists;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name")
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }


    public Player() {

    }

    public Player(String surname, String firstName, int number, String position) {
        this.surname = surname;
        this.firstName = firstName;
        this.number = number;
        this.position = position;
        this.goals = 0;
        this.assists = 0;
    }
    public Player(String surname, String firstName, int number, String position, int goals, int assists, Team team) {
        this.surname = surname;
        this.firstName = firstName;
        this.number = number;
        this.position = position;
        this.goals = goals;
        this.assists = assists;
        this.team = team;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getAssists() {
        return assists;
    }

    public void setAssists(int assists) {
        this.assists = assists;
    }

    public Object[] fieldsToArray() {
        return new Object[] {getSurname(),getFirstName(),getNumber(),getPosition(),getGoals(),getAssists()};
    }
}

