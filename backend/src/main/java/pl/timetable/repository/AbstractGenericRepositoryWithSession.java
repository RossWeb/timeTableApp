package pl.timetable.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class AbstractGenericRepositoryWithSession<T extends Object> {

    private static final org.jboss.logging.Logger LOG = LoggerFactory.logger(AbstractGenericRepositoryWithSession.class);

    @Autowired
    private SessionFactory sessionFactory;

    public T create(T entity) {
        getSession().save(entity);
        getSession().flush();
        return entity;
    }

    public T update(T entity) {
        getSession().saveOrUpdate(entity);
        getSession().flush();
        return entity;
    }

    public void remove(T entity) {
        getSession().delete(entity);
        getSession().flush();
    }

    public T getById(Integer id){
        return (T) getSession().get((Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0], id);
    }

    public Optional<List<T>> findAll(){
        String tableName = getClass().getSimpleName().replace("RepositoryImpl", "");
        return Optional.of(getSession().createQuery( "from " + tableName).list());
    }

    protected Session getSession() {
        Session currentSession = sessionFactory.getCurrentSession();
        if (currentSession == null) {
            LOG.info("Session is null!");
        }
        return currentSession;
    }
}
