package game;

import team.Team;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Class, which represents <b>Game</b> table from database with all its fields, getters and setters
 */
@Entity
@Table(name = "game")
public class Game {
    @Id
    private int id;
    private String opponent;

    @Column(name = "datetime")
    private Calendar dateTime;
    @Column(name = "your_Goals")
    private int yourGoals;
    @Column(name = "opp_goals")
    private int opponentGoals;
    @Column(name = "is_played")
    private boolean isPlayed;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_name")
    private Team teamName;

    public Game(String opponent, Calendar dateTime, Team team) {
        this.opponent = opponent;
        this.dateTime = dateTime;
        this.teamName = team;
    }
    public Game(String opponent, Calendar dateTime,int yourGoals, int oppGoals, Team team) {
        this.opponent = opponent;
        this.dateTime = dateTime;
        this.yourGoals = yourGoals;
        this.opponentGoals = oppGoals;
        this.teamName = team;
    }
    public Game() {

    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public int getYourGoals() {
        return yourGoals;
    }

    public void setYourGoals(int yourGoals) {
        this.yourGoals = yourGoals;
    }

    public int getOpponentGoals() {
        return opponentGoals;
    }

    public void setOpponentGoals(int opponentGoals) {
        this.opponentGoals = opponentGoals;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void setPlayed(boolean played) {
        isPlayed = played;
    }

    public Team getTeamName() {
        return teamName;
    }

    public void setTeamName(Team teamName) {
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
