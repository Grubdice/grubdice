package co.grubdice.scorekeeper.dao

import org.hibernate.HibernateException
import org.hibernate.Session
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired

import javax.transaction.Transactional

class BaseDaoImpl<T> implements BaseDao<T> {

    @Autowired
    SessionFactory sessionFactory

    @Transactional
    public Session getSession() throws HibernateException {
        try {
            return sessionFactory.getCurrentSession();
        } catch (HibernateException e) {
            return sessionFactory.openSession();
        }
    }

    @Transactional
    public void save(T obj) {
        getSession().saveOrUpdate(obj)
    }

}
