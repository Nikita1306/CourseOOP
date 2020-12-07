package team;

import game.Game;
import player.Player;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
public class Team {
    @Id
    private String name;
    @Column(name = "city")
    private String city;
    @Column(name = "found_year")
    private int foundationYear;
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    private List<Player> players;
    @OneToMany(mappedBy = "teamName", orphanRemoval = true)
    private List<Game> games;

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }


    public Team() {
        players = new ArrayList<>();
    }

    public Team(String name, String city, int foundationYear) {
        this.name = name;
        this.city = city;
        this.foundationYear = foundationYear;
        players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        player.setTeam(this);
        players.add(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFoundationYear() {
        return foundationYear;
    }

    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
