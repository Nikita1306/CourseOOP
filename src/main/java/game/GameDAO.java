package game;

import team.Team;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public interface GameDAO {
    Game findByDateAndTime(Calendar calendar);
    List<Game> findByOpponent(String opponent);
    List<Game> findByYourGoals(int goals);
    List<Game> findByOppGoals(int goals);
    List<Game> playedGame(boolean isPlayed);
    void save(Game game);
    void update(Game game);
    void delete(Game game);
}
