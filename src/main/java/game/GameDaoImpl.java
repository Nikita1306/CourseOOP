package game;

import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Class for implementing methods which work with game table
 */
public class GameDaoImpl implements GameDAO {
    /**
     *
     * @param calendar exact date and time to find game
     * @return game which was found by method
     */
    @Override
    public Game findByDateAndTime(Calendar calendar) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        //Game game = session.get(Game.class, calendar);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(formatter.format(calendar.getTime()));
        Game game = (Game)session.createQuery("from Game where datetime = '" + formatter.format(calendar.getTime()) + "'").list().get(0);
        session.close();
        return game;
    }

    /**
     *
     * @param opponent Name of the opponent to find
     * @return List of games which were found by method
     */
    @Override
    public List<Game> findByOpponent(String opponent) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE opponent = '" + opponent + "'").list();
        session.close();
        return game;
    }


    /**
     *
     * @param goals Goals of your team to find
     * @return List of games which were found by method
     */
    @Override
    public List<Game> findByYourGoals(int goals) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE yourGoals = " + goals).list();
        session.close();
        return game;
    }


    /**
     *
     * @param goals Goals of opponent to find
     * @return List of games which were found by method
     */
    @Override
    public List<Game> findByOppGoals(int goals) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE opponentGoals = " + goals).list();
        session.close();
        return game;
    }


    /**
     *
     * @param isPlayed if the game is played or not
     * @return List of games which were found by method
     */
    @Override
    public List<Game> playedGame(boolean isPlayed) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE isPlayed = " + isPlayed ).list();
        session.close();
        return game;
    }


    /**
     *
     * @param game Game to be added to database
     */
    @Override
    public void save(Game game) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.save(game);
        transaction.commit();
        session.close();
    }


    /**
     *
     * @param game Game to be updated in database
     */
    @Override
    public void update(Game game) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(game);
        transaction.commit();
        session.close();
    }


    /**
     *
     * @param game Game to be deleted from database
     */
    @Override
    public void delete(Game game) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(game);
        transaction.commit();
        session.close();
    }
}
