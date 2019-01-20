package pl.timetable.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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
        return Optional.ofNullable((List<T>) getSession().createQuery( "from " + tableName).list());
    }

    public Integer getResultSize(Criterion filter){
        return ((Long) getSession().createCriteria((Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
                .add(filter)
                .setProjection(Projections.rowCount()).uniqueResult()).intValue();
    }

    public Optional<List<T>> getResult(Integer first, Integer max, Criterion filter){
//        String tableName = getClass().getSimpleName().replace("RepositoryImpl", "");
        return Optional.ofNullable((getSession().createCriteria((Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0])
                .add(filter)
                .setFirstResult(first).setMaxResults(max).list()));
    }

    protected Session getSession() {
        Session currentSession = sessionFactory.getCurrentSession();
        if (currentSession == null) {
            LOG.info("Session is null!");
        }
        return currentSession;
    }
}
