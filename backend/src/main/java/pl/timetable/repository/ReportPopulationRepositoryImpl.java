package pl.timetable.repository;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import pl.timetable.entity.ReportPopulation;

import java.util.List;

@Repository
public class ReportPopulationRepositoryImpl extends AbstractGenericRepositoryWithSession<ReportPopulation> implements ReportPopulationRepository {

    public List<ReportPopulation> getByTimeTableDescription(Integer timeTableDescriptionId) {
        Query query = getSession().getNamedQuery("findReportPopulationByTimeTableDescription")
                .setParameter("timeTableDescriptionId", timeTableDescriptionId);
        return ((List<ReportPopulation>) query.list());
    }
}
