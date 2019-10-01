package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pl.timetable.dto.GeneticInitialData;
import pl.timetable.dto.SubjectDto;
import pl.timetable.dto.TeacherDto;
import pl.timetable.entity.Subject;
import pl.timetable.entity.Teacher;

import java.util.List;
import java.util.Objects;

@Service
public class InitialGenotypeCriteria {

    private static final Logger LOGGER = Logger.getLogger(InitialGenotypeCriteria.class);


    public boolean checkData(GeneticInitialData geneticInitialData) {
        boolean isValid = true;
        isValid = checkEnoughLectureToSubjectPerSemester(geneticInitialData);
        isValid = isValid && checkEnoughRoomsForGroup(geneticInitialData);
        isValid = isValid && checkSubjectHasTeacher(geneticInitialData);
        isValid = isValid && checkTeacherHasEnoughSubjectLecture(geneticInitialData);
        return isValid;
    }

    private boolean checkEnoughLectureToSubjectPerSemester(GeneticInitialData geneticInitialData) {
        LOGGER.info("Checking enough lecture to subject per semester");
        Integer roomSize = geneticInitialData.getRoomDtoList().size();
        Integer lectureSizePerWeek = geneticInitialData.getLectureDescriptionDto().getNumberPerDay() * geneticInitialData.getLectureDescriptionDto().getDaysPerWeek();
        Integer spaceSizePerSemester = geneticInitialData.getGroupDtoList().size() * lectureSizePerWeek * geneticInitialData.getLectureDescriptionDto().getWeeksPerSemester();
        Integer subjectSizePerSemester = geneticInitialData.getGroupDtoList().stream().mapToInt(groupDto -> groupDto.getCourse().getSubjectSet().stream().mapToInt(Subject::getSize).sum()).sum();
        boolean enough = spaceSizePerSemester >= subjectSizePerSemester;
        if (!enough) {
            LOGGER.error("Number lecture isn't enough to subject size");
        }

        return enough;
    }

    private boolean checkEnoughRoomsForGroup(GeneticInitialData geneticInitialData){
        LOGGER.info("Checking enoguh rooms for group");
        Integer roomSize = geneticInitialData.getRoomDtoList().size();
        Integer groupSize = geneticInitialData.getGroupDtoList().size();
        boolean enough =  roomSize >= groupSize;
        if(!enough){
            LOGGER.error("Number rooms isn't enough for group min group size == rooms size");
        }
        return enough;
    }

    private boolean checkSubjectHasTeacher(GeneticInitialData geneticInitialData){
        LOGGER.info("Checking all subjects has teachers");
        List<SubjectDto> subjectDtoList = geneticInitialData.getSubjectDtoList();
        boolean isNoTeacher = subjectDtoList.stream().anyMatch(subjectDto -> subjectDto.getTeachers().isEmpty());
        if(isNoTeacher){
            LOGGER.error("Any subject has no teacher");
        }
        return !isNoTeacher;
    }

    private boolean checkTeacherHasEnoughSubjectLecture(GeneticInitialData geneticInitialData){
        List<TeacherDto> teacherDtoList = geneticInitialData.getTeacherDtoList();
        List<SubjectDto> subjectDtoList = geneticInitialData.getSubjectDtoList();
//        Integer lectureSizePerWeek = geneticInitialData.getLectureDescriptionDto().getNumberPerDay() * geneticInitialData.getLectureDescriptionDto().getDaysPerWeek();
        Integer subjectSizePerSemester = geneticInitialData.getCourseDtoList().stream().mapToInt(courseDto -> courseDto.getSubjectSet().stream().mapToInt(Subject::getSize).sum()).sum();
        Integer subjectSizePerTeacher = 0;
        for (SubjectDto subjectDto : subjectDtoList) {
            subjectSizePerTeacher = subjectSizePerSemester + subjectDto.getTeachers().stream().mapToInt(teacher -> {
                if (subjectDto.getTeachers().size() == 1) {
                    return subjectDto.getSize();
                } else {
//                    boolean anyTeacherIsOnlyForThisSubject = subject.getTeachers().stream().anyMatch(teacher -> {
//                        return teacher.getSubjectSet().size() == 1;
//                    });
//                    if(anyTeacherIsOnlyForThisSubject) {
//                        return subject.getSize();
                    if(subjectDto.getTeachers().size() > 1){
                        int subjectPerTeacher = subjectDto.getSize() / subjectDto.getTeachers().size();
                        if(subjectPerTeacher == 0){
                            return 0;
                        }
                        Teacher teacherWithMaxSubject = getTeacherWithMaxSubject(subjectDto);
                        int restSubject = subjectDto.getSize() - subjectDto.getTeachers().size() * subjectPerTeacher;
                        if(restSubject != 0 && teacherWithMaxSubject.getId().equals(teacher.getId()) ){
                            return subjectPerTeacher + restSubject;
                        }else{
                            return subjectPerTeacher;
                        }

                    }
                    return 0;
                }
            }).sum();

        }
        return subjectSizePerTeacher >= subjectSizePerSemester;
    }

    private Teacher getTeacherWithMaxSubject(SubjectDto subject) {
        Teacher teacherHasMaxSubject = null;
        for (Teacher teacher :  subject.getTeachers()) {
            if(Objects.isNull(teacherHasMaxSubject)){
                teacherHasMaxSubject = teacher;
            }else if(teacher.getSubjectSet().size() > teacherHasMaxSubject.getSubjectSet().size()){
               teacherHasMaxSubject = teacher;
            }
        }
        return teacherHasMaxSubject;
    }


}


