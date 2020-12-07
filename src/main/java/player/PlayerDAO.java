package player;

import player.Player;

import java.util.List;

public interface PlayerDAO {
    List<Player> findBySurname(String surname);
    Player findByNumber(int number);
    List<Player> findByName(String name);
    List<Player> findByGoals(int goals);
    List<Player> findByAssists(int assists);
    List<Player> findByPosition(String position);
    void save(Player player);
    void update(Player player);
    void delete(Player player);
}
