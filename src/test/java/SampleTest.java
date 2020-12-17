import game.Game;
import game.GameDaoImpl;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import player.Player;
import player.PlayerDaoImpl;
import team.Team;
import team.TeamDaoImpl;
import utils.HibernateSessionFactoryUtil;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SampleTest {

    @Test
    public void isPlayerAdded() throws SQLException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();

        try {
            TeamDaoImpl teamDao = new TeamDaoImpl();
            Team team = teamDao.findByName("FC Barcelona");
            PlayerDaoImpl playerDao = new PlayerDaoImpl();
            Player player = new Player("Tsyganyuk", "Nikita", 10, "STR", 10, 11, team);
            playerDao.save(player);
            Player testPlayer = playerDao.findByNumber(player.getNumber());
            Assertions.assertEquals(testPlayer.getNumber(), player.getNumber());
            playerDao.delete(player);
        }
        finally {
            transaction.rollback();
            session.close();
        }

    }

    @Test
    public void isGameDeleted() throws SQLException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();

        try {
            TeamDaoImpl teamDao = new TeamDaoImpl();
            Team team = teamDao.findByName("FC Barcelona");
            GameDaoImpl gameDao = new GameDaoImpl();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            System.out.println(formatter.format(date));
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            Game game = new Game("Real Madrid", cal, 0, 0, team);
            gameDao.save(game);
            gameDao.delete(game);
            Assertions.assertNull(gameDao.findByDateAndTime(cal));
        }
        finally {
            transaction.rollback();
            session.close();
        }

    }
    @Test
    public void isGameAdded() throws SQLException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();

        try {
            TeamDaoImpl teamDao = new TeamDaoImpl();
            Team team = teamDao.findByName("FC Barcelona");
            GameDaoImpl gameDao = new GameDaoImpl();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            System.out.println(formatter.format(date));
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 1);
            Game game = new Game("Real Madrid", cal, 0, 0, team);
            gameDao.save(game);
            Game testGame = gameDao.findByDateAndTime(cal);
                    //gameDao.delete(game);
            Assertions.assertEquals(testGame.getDateTime(), game.getDateTime());
        }
        finally {
            transaction.rollback();
            session.close();
        }

    }

    @Test
    public void isPlayerDeleted() throws SQLException {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();

        try {
            TeamDaoImpl teamDao = new TeamDaoImpl();
            Team team = teamDao.findByName("FC Barcelona");
            PlayerDaoImpl playerDao = new PlayerDaoImpl();
            Player player = new Player("Tsyganyuk", "Nikita", 10, "STR", 10, 11, team);
            playerDao.save(player);
            playerDao.delete(player);
            Assertions.assertNull(playerDao.findByNumber(player.getNumber()));
        }
        finally {
            transaction.rollback();
            session.close();
        }

    }
}
