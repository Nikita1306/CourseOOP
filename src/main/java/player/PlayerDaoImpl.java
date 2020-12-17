package player;

import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactoryUtil;

import java.util.List;

public class PlayerDaoImpl  implements PlayerDAO {
    @Override
    public List<Player> findBySurname(String surname) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        //Правильное использование
        List<Player> pl = (List<Player>)session.createQuery("from Player where surname = '" + surname + "'").list();
        session.close();
        return pl;
    }

    @Override
    public Player findByNumber(int number) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Player pl = (Player)session.createQuery("from Player where number = '" + number + "'").list().get(0);
        session.close();
        return pl;
    }

    @Override
    public List<Player> findByName(String name) {

        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        //Правильное использование
        List<Player> pl = (List<Player>)session.createQuery("from Player where firstname = '" + name + "'").list();
        session.close();
        return pl;
    }

    @Override
    public List<Player> findByGoals(int goals) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Player> pl = (List<Player>)session.createQuery("FROM Player WHERE goals = " + goals).list();
        session.close();
        return pl;
    }

    @Override
    public List<Player> findByAssists(int assists) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Player> pl = (List<Player>)session.createQuery("FROM Player WHERE assists = " + assists).list();
        session.close();
        return pl;
    }

    @Override
    public List<Player> findByPosition(String position) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        List<Player> pl = (List<Player>)session.createQuery("FROM Player WHERE position = '" + position + "'").list();
        session.close();
        return pl;
    }



    @Override
    public void save(Player player) {

        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.save(player);
        transaction.commit();
        session.close();
    }

    @Override
    public void update(Player player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        //session.saveOrUpdate(player);
        session.update(player);
        transaction.commit();
        session.close();
    }

    @Override
    public void delete(Player player) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory()
                .openSession();
        Transaction transaction = session.beginTransaction();
        session.delete(player);
        transaction.commit();
        session.close();
    }
}
