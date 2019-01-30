package pl.timetable.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.api.GroupRequest;
import pl.timetable.api.SubjectRequest;
import pl.timetable.api.SubjectResponse;
import pl.timetable.dto.SubjectDto;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.SubjectRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectServiceImpl extends AbstractService<SubjectDto, SubjectRequest, SubjectResponse> {

    public static final Logger LOGGER = Logger.getLogger(SubjectServiceImpl.class);

    private SubjectRepository subjectRepository;


    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public void setSubjectRepository(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public List<SubjectDto> findAll() {
        List<Subject> subjectList = subjectRepository.findAll().orElse(Collections.emptyList());
        return subjectList.stream().map(subject -> {
//            Hibernate.initialize(subject.getTeachers());
            return SubjectServiceImpl.mapEntityToDto(subject);
        }).collect(Collectors.toList());
    }

    @Override
    public SubjectResponse find(SubjectRequest request) {
        SubjectResponse subjectResponse = new SubjectResponse();
        Integer first = request.getPageNumber() * request.getSize();
        Integer max = first + request.getSize();
        List<Subject> subjectList = subjectRepository.getResult(first, max, getFilter(request)).orElse(Collections.emptyList());
        subjectResponse.setData(subjectList.stream().map(subject -> {
//            Hibernate.initialize(subject.getTeachers());
            return SubjectServiceImpl.mapEntityToDto(subject);
        }).collect(Collectors.toList()));
        subjectResponse.setTotalElements(subjectRepository.getResultSize(getFilter(request)));
        return subjectResponse;
    }

    private Criterion getFilter(SubjectRequest request){
        Conjunction conjunction = Restrictions.conjunction();
        if(StringUtils.isNotEmpty(request.getData().getName())){
            conjunction.add(Restrictions.like("name", "%"+request.getData().getName()+"%"));
        }
        return conjunction;

    }


    @Override
    public void create(SubjectRequest request) {

        subjectRepository.create(mapRequestToEntity(request));
    }


    public SubjectDto getByName(String name) throws EntityNotFoundException {
        return mapEntityToDto(
                Optional.ofNullable(subjectRepository.getSubjectByName(name)).orElseThrow(() -> new EntityNotFoundException(name , "Subject")));
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
        subjectDto.setSize(entity.getSize());
//        Hibernate.initialize(entity.getTeachers());
        subjectDto.setTeachers(new ArrayList<>(entity.getTeachers()));
        return subjectDto;
    }

    private Subject mapRequestToEntity(SubjectRequest subjectRequest) {
        Subject subject = new Subject();
        subject.setName(subjectRequest.getName());
        subject.setSize(subjectRequest.getSize());
        return subject;
    }

    public static Subject mapDtoToEntity(SubjectDto subjectDto){
        Subject subject = new Subject();
        subject.setSize(subjectDto.getSize());
        subject.setName(subjectDto.getName());
        subject.setId(subjectDto.getId());
        subject.setTeachers(new HashSet<>(subjectDto.getTeachers()));
        return subject;

    }
}
