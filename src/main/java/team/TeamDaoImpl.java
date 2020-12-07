package team;

import game.Game;
import org.hibernate.Session;
import org.hibernate.Transaction;
import player.Player;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class TeamDaoImpl implements TeamDAO {
    @Override
    public Team findByName(String name) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Team team = session.get(Team.class, name);
        session.close();
        return team;
    }

    @Override
    public Team findByCity(String city) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Team team = session.get(Team.class, city);
        session.close();
        return team;
    }

    @Override
    public List<Team> findAllTeams() {
        return (List<Team>)HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("From Team").list();
    }

    @Override
    public List<Player> findAllPlayers() {
        return (List<Player>)HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("From Player").list();
    }

    @Override
    public List<Game> findAllGames() {
        return (List<Game>)HibernateSessionFactoryUtil.getSessionFactory()
                .openSession().createQuery("From Game").list();
    }

//    @Override
//    public List<Game> findAllGames() {
//        return (List<Game>)HibernateSessionFactoryUtil.getSessionFactory()
//                .openSession().createQuery("From Game").list();
//    }

    @Override
    public void save(Team team) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.save(team);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Team team) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.update(team);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Team team) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(team);
        transaction.commit();
        session.close();
    }
}
