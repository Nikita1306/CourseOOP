package team;

import game.Game;
import player.Player;
import team.Team;

import java.util.List;

public interface TeamDAO {
    Team findByName(String name);
    Team findByCity(String city);
    List<Player> findAllPlayers();
    List<Game> findAllGames();
    List<Team> findAllTeams();
    void save(Team team);
    void update(Team team);
    void delete(Team team);
}
