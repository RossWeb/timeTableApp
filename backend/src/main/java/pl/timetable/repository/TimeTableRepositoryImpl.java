package pl.timetable.repository;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pl.timetable.entity.TimeTable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class TimeTableRepositoryImpl extends AbstractGenericRepositoryWithSession<TimeTable> implements TimeTableRepository {

    public List<TimeTable> getTimeTableRowsByDays(List<Integer> days){
        return getSession().getNamedQuery("findTimeTableByDays").setParameterList("days", days).list();
    }

    public List<TimeTable> getTimeTableResult(Integer timeTableId, Integer firstResult, Integer maxResult, Integer groupId){
        return (List<TimeTable>) getSession().createCriteria(TimeTable.class)
                .add(Restrictions.eq("timeTableDescription.id", timeTableId))
                .add(Restrictions.eq("group.id", groupId))
                .add(Restrictions.in("day", IntStream.range(firstResult, maxResult+1).boxed().collect(Collectors.toList())))
                .addOrder(Order.asc("lectureNumber"))
                .list();
    }

    public Integer getTimeTableCount(Integer timeTableId, Integer groupId){
        return getSession().createCriteria(TimeTable.class)
                .add(Restrictions.eq("timeTableDescription.id", timeTableId))
                .add(Restrictions.eq("group.id", groupId))
                .setProjection(Projections.distinct(Projections.property("day")))
                .list().size();
    }
}
