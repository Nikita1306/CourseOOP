package Main;

import game.GameDaoImpl;
import org.apache.log4j.Level;
import player.Player;
import player.PlayerDaoImpl;
import team.Team;
import team.TeamDaoImpl;

import java.util.List;
import org.apache.log4j.Logger;

/**
 * @author Nikita Tsyganyuk, group 8305
 */
public class Main {
    private static final Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) {
        new UI();
    }

}
