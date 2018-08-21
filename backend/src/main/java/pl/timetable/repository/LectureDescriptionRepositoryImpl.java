package pl.timetable.repository;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import pl.timetable.entity.LectureDescription;
import pl.timetable.entity.TimeTableDescription;

@Repository
public class LectureDescriptionRepositoryImpl extends AbstractGenericRepositoryWithSession<LectureDescription> implements LectureDescriptionRepository {

    public LectureDescription getByTimeTableDescription(TimeTableDescription timeTableDescription){
        Query query = getSession().getNamedQuery("findByTimeTableDescriptionId")
                .setParameter("timeTableId", timeTableDescription.getId());
        return ((LectureDescription)query.uniqueResult());
    }
}
