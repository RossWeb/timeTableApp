package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.SubjectRequest;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.SubjectRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SubjectServiceImpl extends AbstractService<Subject, SubjectRequest> {

    public static final Logger LOGGER = Logger.getLogger(SubjectServiceImpl.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> findAll() {
        Optional<List<Subject>> subjectList = subjectRepository.findAll();
        return subjectList.orElse(Collections.emptyList());
    }

    @Override
    public void create(SubjectRequest request) {

        subjectRepository.create(mapRequestToEntity(request));
    }

    @Override
    public Subject update(SubjectRequest request, int id) {
        Subject entity = mapRequestToEntity(request);
        entity.setId(id);
        return subjectRepository.update(entity);
    }


    @Override
    public void delete(int id) throws EntityNotFoundException {
        Subject entity = get(id);
        subjectRepository.remove(entity);
    }

    @Override
    public Subject get(int id) throws EntityNotFoundException {
        return Optional.ofNullable(subjectRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Subject"));
    }

    private Subject mapRequestToEntity(SubjectRequest subjectRequest) {
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        return subject;
    }
}
