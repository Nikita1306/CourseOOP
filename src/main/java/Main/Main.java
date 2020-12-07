package Main;

import game.GameDaoImpl;
import player.Player;
import player.PlayerDaoImpl;
import team.Team;
import team.TeamDaoImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {



        //List<Player> players = teamDao.findAllPlayers();
        //players.forEach( x -> System.out.println(x.getNumber()));
//        playerDao.save(new Player("Tsyganyuk", "Nikita", 11, POSITION.MF));
//        playerDao.save(new Player("Tsyganyuk", "Nikita", 12, POSITION.MF));
        new UI();
    }



}
