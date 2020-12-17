package Main;

import game.Game;
import game.GameDaoImpl;
import player.Player;
import player.PlayerDaoImpl;
import team.Team;
import team.TeamDaoImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


/**
 * Class, which does all the work with database, implements methods to make it easier to use them in UI controller
 */
public class databaseController {
    private static TeamDaoImpl teamDao = new TeamDaoImpl();
    private static PlayerDaoImpl playerDao = new PlayerDaoImpl();;
    private static GameDaoImpl gameDao = new GameDaoImpl();
    public static  String teamName = "FC Barcelona";

        //List<Player> players = teamDao.findAllPlayers();
        //players.forEach( x -> System.out.println(x.getNumber()));
//        playerDao.save(new Player("Tsyganyuk", "Nikita", 11, POSITION.MF));
//        playerDao.save(new Player("Tsyganyuk", "Nikita", 12, POSITION.MF));


    /**
     * Adds new club to database
     * @param name name of the club to add
     * @param city city of the club
     * @param year foundation year of the club
     */
    public static void addClubToDatabase(String name, String city, int year) {
        teamDao.save(new Team(name, city, year));
    }

    /**
     * Finds club from database by its name
     * @param name name of the club to find
     * @return team, which was found
     */
    public static Team getClubFromDatabase(String name) {
        System.out.println(teamDao.findByName(name).getName());
        return teamDao.findByName(name);
    }

    /**
     * @return list of all clubs
     */
    public static List<Team> getAllClubs() {
        return teamDao.findAllTeams();
    }

    /**
     * @param number number of the player to be deleted
     */
    public static void deletePlayer(int number) {
        System.out.println(number);
        Player player = playerDao.findByNumber(number);
        System.out.println(player.getSurname());
        playerDao.delete(player);
    }

    /**
     * Adds new player to database
     * @param surname surname of the player
     * @param name name of the player
     * @param number number of the player
     * @param position position where player is playing
     * @param goals goals of the player
     * @param assists assists of the player
     * @return returns added player
     */
    public static Player addPlayer(String surname, String name, int number, String position, int goals, int assists) {
        Player player = new Player(surname, name, number, position, goals, assists, getClubFromDatabase(teamName));
        playerDao.save(player);
        return player;
    }



    /**
     * Updates info of the given player
     * @param player updated player
     * @return returns updated player
     */
    public static Player updatePlayer(Player player) {
        playerDao.update(player);
        return player;
    }

    /**
     * @param name name of the players to be found
     * @return list of found player
     */
    public static List<Player> findPlayersByName(String name) {
        List<Player> player = playerDao.findByName(name);
        for (Player pl : player) {
            System.out.println(pl.getSurname());
        }
        return  player;
    }

    public static Player findByNumber(int number) {
        return playerDao.findByNumber(number);
    }

    /**
     * @param name position of the players to be found
     * @return list of found player
     */
    public static List<Player> findPlayersByPosition(String name) {
        List<Player> player = playerDao.findByPosition(name);
        for (Player pl : player) {
            System.out.println(pl.getSurname());
        }
        return  player;
    }

    /**
     * @param name surname of the players to be found
     * @return list of found player
     */
    public static List<Player> findPlayersBySurname(String name) {
        List<Player> player = playerDao.findBySurname(name);
        for (Player pl : player) {
            System.out.println(pl.getSurname());
        }
        return  player;
    }

    /**
     * @param goals amount of goals, which player scored
     * @return list of found player
     */
    public static List<Player> findPlayersByGoals(int goals) {
        List<Player> player = playerDao.findByGoals(goals);
        for (Player pl : player) {
            System.out.println(pl.getSurname());
        }
        return  player;
    }

    /**
     * @param assists amount of assists, which player scored
     * @return list of found player
     */
    public static List<Player> findPlayersByAssists(int assists) {
        List<Player> player = playerDao.findByAssists(assists);
        for (Player pl : player) {
            System.out.println(pl.getSurname());
        }
        return  player;
    }


    /**
     * Adds new game to the database
     * @param opp name of the opponent
     * @param date date and time of the game
     * @param yourGoals amount of your goals
     * @param oppGoals amount of opponent goals
     * @return added game
     */
    public static Game addGame(String opp, Calendar date, int yourGoals, int oppGoals) {

        Game game = new Game(opp, date, yourGoals, oppGoals, getClubFromDatabase(teamName));
        if (Calendar.getInstance().getTime().after(date.getTime()))
            game.setPlayed(true);
        gameDao.save(game);
        return game;
    }



    /**
     * @param date date to be found
     * @return found game
     */
    public static Game findByDate(Calendar date) {
        return gameDao.findByDateAndTime(date);
    }

    /**
     * @return loads all data from the Player table
     */
    public static Object[][] showPlayers() {
        List<Player> list = teamDao.findAllPlayers();
        Object[][] data = new Object[list.size()][6];
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).getSurname();
            data[i][1] = list.get(i).getFirstName();
            data[i][2] = list.get(i).getNumber();
            data[i][3] = list.get(i).getPosition();
            data[i][4] = list.get(i).getGoals();
            data[i][5] = list.get(i).getAssists();
        }

        return data;
    }

    /**
     * @return loads all data from the Game table
     */
    public static Object[][] showGames() {
        List<Game> list = teamDao.findAllGames();
        Object[][] data = new Object[list.size()][5];
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i).getOpponent();
            data[i][1] = dateFormat.format(list.get(i).getDateTime().getTime());
            data[i][2] = list.get(i).getYourGoals();
            data[i][3] = list.get(i).getOpponentGoals();
            data[i][4] = list.get(i).isPlayed();
        }

        return data;
    }

    /**
     * @param game game to be updated
     * @return updated game
     */
    public static Game updateGame(Game game) {
        gameDao.update(game);
        return game;
    }

    /**
     * @param date date of the game to be deleted
     */
    public static void deleteGame(Calendar date) {
        Game game = gameDao.findByDateAndTime(date);
        gameDao.delete(game);


    }


    public static List<Game> findByOpponent(String name) {
        List<Game> game = gameDao.findByOpponent(name);
        return  game;
    }

    public static List<Game> findByYourGoals(int goals) {
        List<Game> games = gameDao.findByYourGoals(goals);
        return games;
    }

    public static List<Game> findByOppGoals(int goals) {
        List<Game> games = gameDao.findByOppGoals(goals);
        return  games;
    }

    public static List<Game> findPlayedGames(boolean isPlayed) {
        List<Game> games = gameDao.playedGame(isPlayed);
        return  games;
    }
}
