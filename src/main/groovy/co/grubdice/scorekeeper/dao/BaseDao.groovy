package co.grubdice.scorekeeper.dao

import org.hibernate.HibernateException
import org.hibernate.Session

interface BaseDao<T> {
    public Session getSession() throws HibernateException
    public void save(T obj)
}
