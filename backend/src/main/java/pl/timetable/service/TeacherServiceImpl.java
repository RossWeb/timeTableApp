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
import pl.timetable.api.*;
import pl.timetable.api.TeacherRequest;
import pl.timetable.api.TeacherResponse;
import pl.timetable.dto.TeacherDto;
import pl.timetable.dto.TeacherDto;
import pl.timetable.entity.Teacher;
import pl.timetable.entity.Subject;
import pl.timetable.exception.EntityDuplicateFoundException;
import pl.timetable.exception.EntityNotFoundException;
import pl.timetable.repository.TeacherRepository;
import pl.timetable.repository.SubjectRepository;
import pl.timetable.repository.TeacherRepository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherServiceImpl extends AbstractService<TeacherDto, TeacherRequest, TeacherResponse> {

    public static final Logger LOGGER = Logger.getLogger(TeacherServiceImpl.class);

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<TeacherDto> findAll() {
        List<Teacher> entityTeachers = teacherRepository.findAll().orElse(Collections.emptyList());
        return entityTeachers.stream().map(course -> {
//            Hibernate.initialize(course.getSubjectSet());
            return mapEntityToDto(course);
        }).collect(Collectors.toList());
    }

    @Override
    public TeacherResponse find(TeacherRequest request) {
        TeacherResponse teacherResponse = new TeacherResponse();
        Integer first = request.getPageNumber() * request.getSize();
        Integer max = first + request.getSize();
        List<Teacher> teacherList = teacherRepository.getResult(first, max,getFilter(request)).orElse(Collections.emptyList());
        teacherResponse.setData(teacherList.stream().map(teacher -> {
//            Hibernate.initialize(teacher.getSubjectSet());
            return mapEntityToDto(teacher);
        }).collect(Collectors.toList()));
        teacherResponse.setTotalElements(teacherRepository.getResultSize(getFilter(request)));
        return teacherResponse;
    }

    private Criterion getFilter(TeacherRequest request){
        Conjunction conjunction = Restrictions.conjunction();
        if(StringUtils.isNotEmpty(request.getData().getName())){
            conjunction.add(Restrictions.like("name", "%"+request.getData().getName()+"%"));
        }
        if(Objects.nonNull(request.getData().getSubject())){
            conjunction.add(Restrictions.like("subject", "%"+request.getData().getSubject()+"%"));
        }
        if(StringUtils.isNotEmpty(request.getData().getSurname())){
            conjunction.add(Restrictions.like("surname", "%"+request.getData().getSurname()+"%"));
        }
        return conjunction;

    }

    @Override
    public void create(TeacherRequest request) throws EntityNotFoundException {
        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        if (Objects.nonNull(request.getSubject())) {
            teacher.getSubjectSet().add(getSubject(request.getSubject()));
        }
        teacherRepository.create(teacher);
    }

    @Override
    public TeacherDto update(TeacherRequest request, int id) throws EntityNotFoundException {
        Teacher teacher = new Teacher();
        teacher.setName(request.getName());
        teacher.setSurname(request.getSurname());
        teacher.getSubjectSet().add(getSubject(request.getSubject()));
        teacher.setId(id);
        return mapEntityToDto(teacherRepository.update(teacher));
    }

    @Override
    public void delete(int id) throws EntityNotFoundException {
        Teacher teacher = Optional.ofNullable(teacherRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Teacher"));
        teacherRepository.remove(teacher);
    }

    public void deleteParameter(int id, int parameterId) throws EntityNotFoundException {
        Teacher teacher = Optional.ofNullable(teacherRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Teacher"));
        Subject subjectFounded = teacher.getSubjectSet().stream().filter(subject -> parameterId == subject.getId()).findAny()
                .orElseThrow(() -> new EntityNotFoundException(parameterId, "Subject"));
        teacher.getSubjectSet().remove(subjectFounded);
        teacherRepository.update(teacher);
    }

    public void addParameter(int id, int parameterId) throws EntityNotFoundException, EntityDuplicateFoundException {
        Teacher teacher = Optional.ofNullable(teacherRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Teacher"));
        teacher.getSubjectSet().stream()
            .filter(subject -> subject.getId() == parameterId).findAny().ifPresent(
                    subject -> new EntityDuplicateFoundException(parameterId, "Subject")
            );
        Subject subjectToAdd = subjectRepository.getById(parameterId);
        teacher.getSubjectSet().add(subjectToAdd);
        teacherRepository.update(teacher);

    }

    @Override
    public TeacherDto get(int id) throws EntityNotFoundException {
        Teacher teacher = Optional.ofNullable(teacherRepository.getById(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "Teacher"));
        Hibernate.initialize(teacher.getSubjectSet());
        return mapEntityToDto(teacher);
    }

    public List<Subject> getParameters(int id) throws EntityNotFoundException {
        return Optional.ofNullable(teacherRepository.getParameters(id))
                .orElseThrow(() -> new EntityNotFoundException(id, "SubjectList"));
    }

    public List<Subject> findParameters(TeacherRequest request){
        Integer first = request.getPageNumber() * request.getSize();
        Integer max = first + request.getSize();
        return Optional.ofNullable(teacherRepository.findParameters(request.getId(), first, max))
                .orElse(Collections.emptyList());
    }

    public Integer findParametersCount(){
        return subjectRepository.findAll().get().size();
    }

    private Subject getSubject(Integer subjectId) throws EntityNotFoundException {
        return Optional.ofNullable(subjectRepository.getById(subjectId))
                .orElseThrow(() -> new EntityNotFoundException(subjectId, "Subject"));
    }

    public static TeacherDto mapEntityToDto(Teacher teacher) {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setName(teacher.getName());
        teacherDto.setSurname(teacher.getSurname());
        teacherDto.setSubjectSet(teacher.getSubjectSet());
        return teacherDto;
    }

    public static Teacher mapDtoToEntity(TeacherDto teacherDto){
        Teacher teacher = new Teacher();
        teacher.setName(teacherDto.getName());
        teacher.setSurname(teacherDto.getSurname());
        teacher.setSubjectSet(teacherDto.getSubjectSet());
        teacher.setId(teacherDto.getId());
        return teacher;
    }
}
