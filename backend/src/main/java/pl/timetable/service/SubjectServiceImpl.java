package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.SubjectRequest;
import pl.timetable.dto.SubjectDto;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.SubjectRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectServiceImpl extends AbstractService<SubjectDto, SubjectRequest> {

    public static final Logger LOGGER = Logger.getLogger(SubjectServiceImpl.class);

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<SubjectDto> findAll() {
        List<Subject> subjectList = subjectRepository.findAll().orElse(Collections.emptyList());
        return subjectList.stream().map(SubjectServiceImpl::mapEntityToDto).collect(Collectors.toList());
    }

    @Override
    public void create(SubjectRequest request) {

        subjectRepository.create(mapRequestToEntity(request));
    }

    @Override
    public SubjectDto update(SubjectRequest request, int id) {
        Subject entity = mapRequestToEntity(request);
        entity.setId(id);
        return mapEntityToDto(subjectRepository.update(entity));
    }


    @Override
    public void delete(int id) throws EntityNotFoundException {
        Subject entity = Optional.ofNullable(subjectRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Subject"));
        subjectRepository.remove(entity);
    }

    @Override
    public SubjectDto get(int id) throws EntityNotFoundException {
        Subject subject = Optional.ofNullable(subjectRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Subject"));
        return mapEntityToDto(subject);
    }

    public static SubjectDto mapEntityToDto(Subject entity) {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(entity.getId());
        subjectDto.setName(entity.getName());
        return subjectDto;
    }

    private Subject mapRequestToEntity(SubjectRequest subjectRequest) {
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        return subject;
    }
}
