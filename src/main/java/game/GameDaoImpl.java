package game;

import org.hibernate.Session;
import org.hibernate.Transaction;
import player.Player;
import utils.HibernateSessionFactoryUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameDaoImpl implements GameDAO {
    @Override
    public Game findByDateAndTime(Calendar calendar) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Game game = session.get(Game.class, calendar);
        session.close();
        return game;
    }

    @Override
    public List<Game> findByOpponent(String opponent) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE opponent = '" + opponent + "'").list();
        session.close();
        return game;
    }

    @Override
    public List<Game> findByYourGoals(int goals) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE yourGoals = " + goals).list();
        session.close();
        return game;
    }

    @Override
    public List<Game> findByOppGoals(int goals) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE opponentGoals = " + goals).list();
        session.close();
        return game;
    }

    @Override
    public List<Game> playedGame(boolean isPlayed) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Game> game = (List<Game>)session.createQuery("FROM Game WHERE isPlayed = " + isPlayed ).list();
        session.close();
        return game;
    }


    @Override
    public void save(Game game) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.save(game);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Game game) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(game);
        transaction.commit();
        session.close();
    }

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
