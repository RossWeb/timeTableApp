package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.timetable.dto.*;
import pl.timetable.entity.Subject;
import pl.timetable.entity.Teacher;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Transactional
public class GenotypeService {

    private HardGenotypeCriteria hardGenotypeCriteria;

    private static final Logger LOGGER = Logger.getLogger(GenotypeService.class);

    @Autowired
    public GenotypeService(@Qualifier("hardGenotypeCriteria") HardGenotypeCriteria hardGenotypeCriteria) {
        this.hardGenotypeCriteria = hardGenotypeCriteria;
    }

    public void setHardGenotypeCriteria(HardGenotypeCriteria hardGenotypeCriteria) {
        this.hardGenotypeCriteria = hardGenotypeCriteria;
    }

    public void mapHintGenotype(Genotype genotype) {
        genotype.setRoomBySubject(new HashMap<>());
        genotype.setRoomByLecture(new HashMap<>());
        genotype.setLectureByTeacher(new HashMap<>());
        for (int i = 0; i < genotype.getGenotypeTable().length; i++) {
            GroupDto groupDto = genotype.getGenotypeTable()[i][0].getGroupDto();
            genotype.getRoomByGroup().put(groupDto, new LinkedList<>());
            for (int j = 0; j < genotype.getGenotypeTable()[i].length; j++) {
                Cell cell = genotype.getGenotypeTable()[i][j];
                if (Objects.nonNull(cell)) {
                    genotype.getRoomByGroup().get(groupDto).add(cell.getRoomDto());
                    if (Objects.nonNull(cell.getRoomDto())) {
                        genotype.getRoomByLecture().putIfAbsent(cell.getLecture(), new LinkedList<>());
                        genotype.getRoomByLecture().get(cell.getLecture()).add(cell.getRoomDto());
                        if (Objects.nonNull(cell.getSubjectDto())) {
                            genotype.getRoomBySubject().putIfAbsent(cell.getSubjectDto().getId(), new ArrayList<>());
                            genotype.getRoomBySubject().get(cell.getSubjectDto().getId()).add(cell.getRoomDto());
                        }
                        if (Objects.nonNull(cell.getTeacherDto())) {
                            genotype.getLectureByTeacher().putIfAbsent(cell.getTeacherDto().getId(), new ArrayList<>());
                            genotype.getLectureByTeacher().get(cell.getTeacherDto().getId()).add(cell.getLecture());
                        }

                    }
                }

            }

        }

//
    }

    public Genotype createInitialGenotype(GeneticInitialData geneticInitialData) {

        int lectureSize = geneticInitialData.getLectureDescriptionDto().getNumberPerDay() * geneticInitialData.getLectureDescriptionDto().getDaysPerWeek() * geneticInitialData.getLectureDescriptionDto().getWeeksPerSemester();

//        int roomIncrementNumber = 0;
        GenotypeServiceDto genotypeServiceDto = new GenotypeServiceDto();

        fillRoomListPerLecture(geneticInitialData, lectureSize, genotypeServiceDto);
        Genotype genotype = new Genotype(geneticInitialData.getGroupDtoList().size(), lectureSize);
//        genotype.getRoomByLecture().put(lectureIncrementNumber, new ArrayList<>());
//        genotype.getLectureByRoom().put(geneticInitialData.getRoomDtoList().get(roomIncrementNumber), new ArrayList<>());
//        Integer putRoomPosition = getRandomIntegerBetweenRange(0, geneticInitialData.getRoomDtoList().size());
        //group iteration
        boolean initialGenotypeValid = false;
        while (!initialGenotypeValid) {
            createInitialGenotypesWithRoom(geneticInitialData, lectureSize, genotypeServiceDto, genotype);
            initialGenotypeValid = hardGenotypeCriteria.hasNoGroupDuplicatesByRoom(genotype.getGenotypeTable());
        }
        addSubject(genotype);
        addTeacher(genotype);
//        mapHintGenotype(genotype);
        Arrays.stream(genotype.getGenotypeTable()).forEach(group -> {
            if (Arrays.stream(group).filter(cell -> Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())).count() < 25) {
                LOGGER.info("Rooms not enough");
            }
        });
        LOGGER.info(genotype);
        return genotype;
    }

    public void mutateGenotype(Genotype genotype, GeneticInitialData geneticInitialData) {
        Arrays.stream(genotype.getGenotypeTable()).forEach(group -> {
            IntStream.range(0, group.length).forEach(value -> {
                if (new Random().doubles(0, 1).findFirst().getAsDouble()
                        < geneticInitialData.getMutationValue()) {
                    if (new Random().nextBoolean()) {
                        CourseDto courseDto = group[0].getCourseDto();
                        List<Subject> subjectList = new ArrayList<>(courseDto.getSubjectSet());
                        SubjectDto randomSubject = SubjectServiceImpl.mapEntityToDto(
                                subjectList.get(getRandomIntegerBetweenRange(0, subjectList.size())));
                        RoomDto roomDto = geneticInitialData.getRoomDtoList().get(getRandomIntegerBetweenRange(0, geneticInitialData.getRoomDtoList().size()));
                        TeacherDto teacherDto = TeacherServiceImpl.mapEntityToDto(
                                randomSubject.getTeachers().get(getRandomIntegerBetweenRange(0, randomSubject.getTeachers().size())));
                        group[value].setSubjectDto(randomSubject);
                        group[value].setTeacherDto(teacherDto);
                        group[value].setRoomDto(roomDto);
                    } else {
                        group[value].setSubjectDto(null);
                        group[value].setTeacherDto(null);
                        group[value].setRoomDto(null);
                    }
                }
            });
        });
    }

    private void createInitialGenotypesWithRoom(GeneticInitialData geneticInitialData, int lectureSize, GenotypeServiceDto genotypeServiceDto, Genotype genotype) {
        int lectureIncrementNumber = 1;
        for (int i = 0; i < genotype.getGenotypeTable().length; ) {
            GroupDto groupDto = geneticInitialData.getGroupDtoList().get(i);
//            Map<RoomDto, List<Integer>> lectureByRoomTemporary = new HashMap<>();
            CourseDto courseDto = CourseServiceImpl.mapEntityToDto(groupDto.getCourse());
            Integer subjectSize = getSubjectPerGroup(courseDto);
            genotypeServiceDto.getRoomByGroupTemporary().put(groupDto, new LinkedList<>());
//            lectureByRoomTemporary.put(geneticInitialData.getRoomDtoList().get(0), new ArrayList<>());
            fillInitialValueGenotypeTable(geneticInitialData, genotype, i, groupDto, courseDto);
            //lecture iteration per group
            for (int actualLecture = 0; actualLecture < genotype.getGenotypeTable()[i].length; actualLecture++) {

                //end of iteration when enough room to subject by group
                if (subjectSize <= genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).stream().filter(Objects::nonNull).count()) {
                    int lectureToFinalCheck = actualLecture;
                    if (actualLecture == subjectSize + 1) {
                        lectureToFinalCheck = actualLecture - 1;
                    }
                    GentoypeCriteriaDto gentoypeCriteriaDto =
                            new GentoypeCriteriaDto(geneticInitialData, lectureIncrementNumber, genotypeServiceDto, groupDto, lectureToFinalCheck)
                                    .checkCriteriaAndContinueIfPossible();
                    lectureIncrementNumber = gentoypeCriteriaDto.getLectureIncrementNumber();
                    actualLecture = gentoypeCriteriaDto.getActualLecture();
                    if (lectureIncrementNumber == 1) {
                        break;
                    }
                }
                RoomDto roomFounded = getRoomByRandomNumberWithNull(genotypeServiceDto.getRoomListPerLecture().get(actualLecture));
//                LOGGER.debug("Actual lecture " + actualLecture + " for group " + i + " " + roomFounded);
                genotype.getRoomByLecture().putIfAbsent(actualLecture, new LinkedList<>());
                genotype.getGenotypeTable()[i][actualLecture].setRoomDto(roomFounded);
                genotypeServiceDto.getRoomByLectureTemporary().putIfAbsent(actualLecture, new LinkedList<>());
                genotypeServiceDto.getRoomByLectureTemporary().get(actualLecture).add(roomFounded);
                genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).add(roomFounded);
                List<RoomDto> listToCheck = genotypeServiceDto.getRoomByGroupTemporary().get(groupDto);
//                if (Arrays.stream(genotype.getGenotypeTable()[i]).filter(cell -> Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())).count() == 26) {
//                    LOGGER.error("To much cell for group " + i);
//                }
                try {
                    if (genotype.getGenotypeTable()[i][actualLecture].getRoomDto() != genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).get(actualLecture)) {
                        LOGGER.info("Different cell for group " + i + "and lecture " + actualLecture);
                    }
                } catch (IndexOutOfBoundsException e) {
                    LOGGER.error("Index out of bound");
                }
                //check lecture group per day
                if (lectureIncrementNumber == geneticInitialData.getLectureDescriptionDto().getNumberPerDay() || actualLecture == genotype.getGenotypeTable()[i].length - 1) {
                    GentoypeCriteriaDto gentoypeCriteriaDto =
                            new GentoypeCriteriaDto(geneticInitialData, lectureIncrementNumber, genotypeServiceDto, groupDto, actualLecture)
                                    .checkCriteriaAndContinueIfPossible();
                    lectureIncrementNumber = gentoypeCriteriaDto.getLectureIncrementNumber();
                    actualLecture = gentoypeCriteriaDto.getActualLecture();
//                    LOGGER.debug(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto));
                    if (!genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).containsAll(listToCheck)) {
                        LOGGER.error("Not equal");
                    }
                } else {
                    lectureIncrementNumber++;
                }
            }

//            if (!genotype.getRoomByLecture().get(1).isEmpty()) {
//                genotype.getRoomByLecture().forEach((integer, roomDtos) -> genotypeServiceDto.getRoomByLectureTemporary().get(integer).addAll(roomDtos));
//            }
//            if (genotype.getRoomByGroup().size() > 1) {
//                genotype.getRoomByGroup().forEach((groupDto, integers) -> roomByGroupTemporary.get(groupDto).addAll(integers));
//                roomByGroupTemporary.get(geneticInitialData.getGroupDtoList().get(i)).addAll(

//                        genotype.getRoomByGroup().get(geneticInitialData.getGroupDtoList().get(i)));
//            }
            boolean isValidCriteria = hardGenotypeCriteria.hasNoRoomDuplicatesByLecture(genotypeServiceDto.getRoomByLectureTemporary());
//                    &&
//                    hardGenotypeCriteria.hasNoEmptyLectureByGroup(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto)
//                            , geneticInitialData.getLectureDescriptionDto().getNumberPerDay());
//            hardGenotypeCriteria.hasEnoughSizeLectureToSubject(
//                    ((Long) genotypeServiceDto.getRoomByLectureTemporary().values().stream().filter(roomDtos -> !roomDtos.isEmpty()).count()).intValue()
//                    , subjectSize)
            if (isValidCriteria) {
                List<Cell> cellWithEmptyRoomList = Arrays.stream(genotype.getGenotypeTable()[i])
                        .filter(cell -> Objects.isNull(cell.getRoomDto())).collect(Collectors.toList());
                while (subjectSize > ((Long) genotypeServiceDto.getRoomByLectureTemporary().values().stream().filter(roomDtos -> !roomDtos.isEmpty()).count()).intValue()) {

                    int cellPosition = getRandomIntegerBetweenRange(0, cellWithEmptyRoomList.size());
                    Cell cellFound = cellWithEmptyRoomList.get(cellPosition);
                    RoomDto roomFound = getRoomByRandomNumber(genotypeServiceDto.getRoomListPerLecture()
                            .get(cellFound.getLecture()));
                    cellWithEmptyRoomList.remove(cellPosition);
                    if (Objects.nonNull(roomFound)) {
                        genotypeServiceDto.getRoomByGroupTemporary().get(groupDto)
                                .addAll(Arrays.asList(new RoomDto[lectureSize - genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).size()]));
                        genotype.getGenotypeTable()[i][cellFound.getLecture()].setRoomDto(roomFound);
                        genotypeServiceDto.getRoomByLectureTemporary().putIfAbsent(cellFound.getLecture(), new LinkedList<>());
                        genotypeServiceDto.getRoomByLectureTemporary().get(cellFound.getLecture()).add(roomFound);
                        genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).set(cellFound.getLecture(), roomFound);
                        genotypeServiceDto.getRoomListPerLecture().get(cellFound.getLecture()).remove(roomFound);
                    }
                }
                genotype.setRoomByLecture(genotypeServiceDto.getRoomByLectureTemporary());
                genotype.setRoomByGroup(genotypeServiceDto.getRoomByGroupTemporary());
                i++;
                //czy potrzebne?
//                genotype.getLectureByRoom().putAll(lectureByRoomTemporary);
            } else {
                for (int j = 0; j < genotypeServiceDto.getRoomByGroupRecovery().size(); j++) {
                    if (Objects.nonNull(genotypeServiceDto.getRoomByGroupRecovery().get(j))) {
                        genotypeServiceDto.getRoomListPerLecture().get(j).add(genotypeServiceDto.getRoomByGroupRecovery().get(j));
                        genotypeServiceDto.getRoomByLectureTemporary().get(j).remove(genotypeServiceDto.getRoomByGroupRecovery().get(j));
                    }

                }
                genotype.getGenotypeTable()[i] = new Cell[lectureSize];
            }

        }
    }

    private void fillInitialValueGenotypeTable(GeneticInitialData geneticInitialData, Genotype genotype, int i, GroupDto groupDto, CourseDto courseDto) {
        for (int j = 0; j < genotype.getGenotypeTable()[i].length; j++) {
            genotype.getGenotypeTable()[i][j] = new Cell(
                    j,
                    courseDto,
                    groupDto, j / geneticInitialData.getLectureDescriptionDto().getNumberPerDay() + 1);
        }
    }

    private void fillRoomListPerLecture(GeneticInitialData geneticInitialData, int lectureSize, GenotypeServiceDto genotypeServiceDto) {
        for (int i = 0; i < lectureSize; i++) {
            genotypeServiceDto.getRoomListPerLecture().put(i, new ArrayList<>(geneticInitialData.getRoomDtoList()));
        }
    }


    private int getSubjectPerGroup(CourseDto courseDto) {
        return courseDto.getSubjectSet().stream().map(Subject::getSize).mapToInt(value -> value).sum();
    }

    private void addTeacher(Genotype genotype) {
        Map<Integer, Set<Teacher>> mapTeacherBySubject = new HashMap<>();
        Arrays.asList(genotype.getGenotypeTable()).forEach(columnGroup -> {
            Arrays.asList(columnGroup).forEach(rowDay -> {
                SubjectDto subjectDto = rowDay.getSubjectDto();
                if (Objects.nonNull(subjectDto) && Objects.nonNull(subjectDto.getTeachers()) && !subjectDto.getTeachers().isEmpty()) {
                    mapTeacherBySubject.putIfAbsent(rowDay.getLecture(), new HashSet<>());
                    Teacher teacher;
                    TeacherDto teacherDto;
                    do {
                        int teacherPosition = new Random().ints(0, subjectDto.getTeachers().size()).findFirst().getAsInt();
                        teacher = subjectDto.getTeachers().get(teacherPosition);
                        teacherDto = TeacherServiceImpl.mapEntityToDto(subjectDto.getTeachers().get(teacherPosition));
                    }while(mapTeacherBySubject.get(rowDay.getLecture()).size() < subjectDto.getTeachers().size() &&
                            mapTeacherBySubject.get(rowDay.getLecture()).contains(teacher));

                    if(Objects.nonNull(teacher)) {
                        mapTeacherBySubject.get(rowDay.getLecture()).add(teacher);
                        genotype.getLectureByTeacher().putIfAbsent(teacherDto.getId(), new ArrayList<>());
                        genotype.getLectureByTeacher().get(teacherDto.getId()).add(rowDay.getLecture());
                        rowDay.setTeacherDto(teacherDto);
                    }else{
                        rowDay.setTeacherDto(null);
                    }

                    //poprawic subject
                }
            });
        });
    }

    private void addSubject(Genotype genotype) {

        Arrays.asList(genotype.getGenotypeTable()).forEach(columnGroup -> {
                    Set<Subject> subjectSet = columnGroup[0].getCourseDto().getSubjectSet();
//                    List<RoomDto> listRoomWithoutEmpty =
//                            genotype.getRoomByGroup().get(geneticInitialData.getGroupDtoList().get(0)).stream().filter(Objects::nonNull).collect(Collectors.toList());
                    LinkedList<RoomDto> listRoom = genotype.getRoomByGroup().get(columnGroup[0].getGroupDto());
                    subjectSet.forEach(subject ->
                            {
                                SubjectDto subjectDto = SubjectServiceImpl.mapEntityToDto(subject);
                                genotype.getRoomBySubject().putIfAbsent(subjectDto.getId(), new ArrayList<>());
                                for (int j = 0; j < subject.getSize(); j++) {
                                    Integer roomPosition = -1;
                                    if (listRoom.size() != 0) {
                                        RoomDto roomDto;
                                        do {
                                            roomPosition = new Random().ints(0, listRoom.size()).findFirst().getAsInt();
                                            roomDto = listRoom.get(roomPosition);
                                        } while (Objects.isNull(roomDto));

                                        genotype.getRoomBySubject().get(subjectDto.getId()).add(roomDto);
                                        listRoom.remove(roomPosition);
                                        Cell cell = columnGroup[roomPosition];
                                        cell.setSubjectDto(subjectDto);
                                        columnGroup[roomPosition] = cell;
                                    } else {
                                        LOGGER.info("Rooms list is empty");
                                        if (subject.getSize() != j + 1) {
                                            LOGGER.info("End process break");
                                            break;
                                        }
                                    }


                                }
                            }
                    );
                }
        );
    }

    public Population mapHintNewPopulation(Population population) {
        population.getGenotypePopulation().forEach(this::mapHintGenotype);
        return population;
    }

    public Genotype crossoverReal(Genotype genotypeFirst, Genotype genotypeSecond) {
        Integer rowSize = genotypeFirst.getGenotypeTable().length;
        Integer columnSize = genotypeFirst.getGenotypeTable()[0].length;
        Map<Integer, List<Cell>> cellByLecture = new HashMap<>();
        Set<Integer> notEqualLectures = new HashSet<>();
        Genotype child = new Genotype(rowSize, columnSize);


        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (genotypeFirst.getGenotypeTable()[i][j] == genotypeSecond.getGenotypeTable()[i][j]) {
                    child.getGenotypeTable()[i][j] = genotypeFirst.getGenotypeTable()[i][j];
                } else {
                    child.getGenotypeTable()[i][j] = genotypeFirst.getGenotypeTable()[i][j];
                    notEqualLectures.add(genotypeFirst.getGenotypeTable()[i][j].getLecture());
                }
            }
        }

        for (int j = 0; j < columnSize; j++) {
            if (notEqualLectures.contains(j)) {
                Map<RoomDto, Cell> temporaryRoomMap = new HashMap<>();
                cellByLecture.putIfAbsent(j, new ArrayList<>());
                for (int i = 0; i < rowSize; i++) {
                    fillCellByLecture(genotypeFirst, cellByLecture, j, temporaryRoomMap, i);
                    fillCellByLecture(genotypeSecond, cellByLecture, j, temporaryRoomMap, i);
                }
//                Cell cell = genotypeFirst.getGenotypeTable()[0][j];
//                cell.setSubjectDto(null);
//                cell.setRoomDto(null);
//                cell.setTeacherDto(null);
//                cellByLecture.get(j).add(cell);
//                cellByLecture.get(j).forEach(cellLecture -> temporaryRoomMap.put(cellLecture.getRoomDto(), cellLecture));
                List<Cell> cellWithoutDuplicates = new ArrayList<>(temporaryRoomMap.values());
                Collections.shuffle(cellWithoutDuplicates);
                cellByLecture.put(j, cellWithoutDuplicates);

            }
        }

        cellByLecture.keySet().iterator().forEachRemaining(integer -> {
            for (int i = 0; i < rowSize; i++) {
                if(cellByLecture.get(integer).size() == 1 && Objects.isNull(cellByLecture.get(integer).get(0).getRoomDto())){
                    child.getGenotypeTable()[i][integer].setRoomDto(null);
                    child.getGenotypeTable()[i][integer].setSubjectDto(null);
                }else {
                    GroupDto groupDto = child.getGenotypeTable()[i][integer].getGroupDto();
                    Optional<Cell> cellByGroup = cellByLecture.get(integer).stream()
                            .filter(cell -> groupDto.equals(cell.getGroupDto())).findFirst();
                    child.getGenotypeTable()[i][integer].setRoomDto(
                            cellByGroup.map(Cell::getRoomDto).orElse(null));
                    child.getGenotypeTable()[i][integer].setSubjectDto(
                            cellByGroup.map(Cell::getSubjectDto).orElse(null));
                }
            }
        });

        return child;
    }

    private void fillCellByLecture(Genotype genotype, Map<Integer, List<Cell>> cellByLecture, int j, Map<RoomDto, Cell> temporaryRoomMap, int i) {
        GroupDto groupDto = genotype.getGenotypeTable()[i][j].getGroupDto();
        RoomDto roomDto = genotype.getGenotypeTable()[i][j].getRoomDto();
        if(temporaryRoomMap.containsKey(roomDto)) {
            if(cellByLecture.get(j).stream()
                    .filter(cell -> cell.getGroupDto().equals(groupDto)).collect(Collectors.toList()).size() <= 1){
                temporaryRoomMap.put(roomDto, genotype.getGenotypeTable()[i][j]);
            }
        }else{
            temporaryRoomMap.put(roomDto, genotype.getGenotypeTable()[i][j]);

        }
        cellByLecture.get(j).add(genotype.getGenotypeTable()[i][j]);
    }

    public Genotype crossoverChild(Genotype genotypeFirst, Genotype genotypeSecond) {
        Integer rowSize = genotypeFirst.getGenotypeTable().length;
        Integer columnSize = genotypeFirst.getGenotypeTable()[0].length;

        Genotype child = new Genotype(rowSize, columnSize);

        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (genotypeFirst.getGenotypeTable()[i][j] == genotypeSecond.getGenotypeTable()[i][j]) {
                    child.getGenotypeTable()[i][j] = genotypeFirst.getGenotypeTable()[i][j];
                } else {
                    boolean firstGenotypeMatch = new Random().nextBoolean();
                    if (firstGenotypeMatch) {
                        child.getGenotypeTable()[i][j] = genotypeFirst.getGenotypeTable()[i][j];
                    } else {
                        child.getGenotypeTable()[i][j] = genotypeSecond.getGenotypeTable()[i][j];
                    }
                }
            }
        }

        return child;
    }

    public void crossoverPlus(Genotype genotypeFirst, Genotype genotypeSecond, int lectureDays) {
        Integer rowSize = genotypeFirst.getGenotypeTable().length;
        Integer columnSize = genotypeFirst.getGenotypeTable()[0].length;

        for (int i = 0; i < rowSize; i++) {
            int lecturePositionToCut = new Random().ints(0, (lectureDays)).findFirst().getAsInt();
            boolean cutLeftOrRight = new Random().nextBoolean();
            int startPosition = 0;
            int endPosition = 0;
            if (cutLeftOrRight) {
                startPosition = lecturePositionToCut;
                endPosition = lectureDays;
            } else {
                endPosition = lecturePositionToCut;
            }

            for (int j = startPosition; j < endPosition; j++) {
                int daySearched = j + 1;
                try {
                    List<Cell> lecturePerDayFirst = Arrays.stream(genotypeFirst.getGenotypeTable()[i])
                            .filter(cell -> cell.getDay().equals(daySearched)).collect(Collectors.toList());
                    List<Cell> lecturePerDaySecond = Arrays.stream(genotypeSecond.getGenotypeTable()[i])
                            .filter(cell -> cell.getDay().equals(daySearched)).collect(Collectors.toList());


                    int lectureDayPositionToCut = new Random().ints(0, (lecturePerDayFirst.size())).findFirst().getAsInt();
                    int startDayPosition = 0;
                    int endDayPosition = 0;
                    if (cutLeftOrRight) {
                        startDayPosition = lectureDayPositionToCut;
                        endDayPosition = lecturePerDayFirst.size();
                    } else {
                        endDayPosition = lectureDayPositionToCut;
                    }
                    for (int k = startDayPosition; k < endDayPosition; k++) {
                        int dayPosition = k;
                        List<Cell> subjectListFirstAsSecound = Arrays.stream(genotypeFirst.getGenotypeTable()[i])
                                .filter(cell -> cell.getSubjectDto().equals(lecturePerDaySecond.get(dayPosition).getSubjectDto())).collect(Collectors.toList());
                        List<Cell> subjectListSecondAsFirst = Arrays.stream(genotypeSecond.getGenotypeTable()[i])
                                .filter(cell -> cell.getSubjectDto().equals(lecturePerDayFirst.get(dayPosition).getSubjectDto())).collect(Collectors.toList());
                        int subjectPosition = getRandomIntegerBetweenRange(0, subjectListFirstAsSecound.size());
                        genotypeFirst.getGenotypeTable()[i][subjectListFirstAsSecound.get(subjectPosition).getLecture()].setSubjectDto(lecturePerDayFirst.get(k).getSubjectDto());
                        subjectPosition = getRandomIntegerBetweenRange(0, subjectListSecondAsFirst.size());
                        genotypeFirst.getGenotypeTable()[i][subjectListSecondAsFirst.get(subjectPosition).getLecture()].setSubjectDto(lecturePerDaySecond.get(k).getSubjectDto());
                        genotypeFirst.getGenotypeTable()[i][lecturePerDayFirst.get(k).getLecture()] = lecturePerDaySecond.get(k);
                        genotypeSecond.getGenotypeTable()[i][lecturePerDayFirst.get(k).getLecture()] = lecturePerDayFirst.get(k);
                    }

                    LOGGER.debug("Crossover");
                } catch (Exception e) {
                    LOGGER.info("dasdas");
                }

            }
        }
    }

    public void crossover(Genotype genotypeFirst, Genotype genotypeSecond) {

        Integer rowSize = genotypeFirst.getGenotypeTable().length;
        Integer columnSize = genotypeFirst.getGenotypeTable()[0].length;

        Set<Integer> rowSet = new HashSet<>();
        Set<Integer> columnSet = new HashSet<>();
        for (int i = 0; i < rowSize; i++) {
            rowSet.add(new Random().ints(0, (rowSize)).findFirst().getAsInt());
        }
        for (int i = 0; i < columnSize; i++) {
            columnSet.add(new Random().ints(0, (columnSize)).findFirst().getAsInt());
        }
        rowSet.forEach(rowInteger -> {
            columnSet.forEach(columnInteger -> {
                Cell tempFirst = genotypeFirst.getGenotypeTable()[rowInteger][columnInteger];
                Cell tempSecond = genotypeSecond.getGenotypeTable()[rowInteger][columnInteger];
                genotypeFirst.getGenotypeTable()[rowInteger][columnInteger] = tempSecond;
                genotypeSecond.getGenotypeTable()[rowInteger][columnInteger] = tempFirst;
            });
        });
    }

    private RoomDto getRoomByRandomNumberWithNull(List<RoomDto> roomDtoList) {
        Integer nullRange = ((Long) Math.round(roomDtoList.size() * 0.25)).intValue();
        if (nullRange == 0) {
            return null;
        }
        Integer roomPosition = getRandomIntegerBetweenRange(0, roomDtoList.size() + nullRange);
        if (roomPosition >= roomDtoList.size()) {
            return null;
        } else {
            RoomDto roomDto = roomDtoList.get(roomPosition);
            roomDtoList.remove(roomDto);
            return roomDto;
        }
    }

    private RoomDto getRoomByRandomNumber(List<RoomDto> roomDtoList) {
        Integer roomPosition = getRandomIntegerBetweenRange(0, roomDtoList.size());
        RoomDto roomDto = roomDtoList.get(roomPosition);
        roomDtoList.remove(roomDto);
        return roomDto;
    }


    public static Integer getRandomIntegerBetweenRange(Integer min, Integer max) {
        Random random = new Random();
        return random.ints(min, max).findFirst().getAsInt();

    }

    private static Integer getLectureSlot(GeneticInitialData geneticInitialData) {
        return geneticInitialData.getLectureDescriptionDto().getNumberPerDay() * geneticInitialData.getLectureDescriptionDto().getDaysPerWeek();
    }

    private class GentoypeCriteriaDto {
        private GeneticInitialData geneticInitialData;
        private int lectureIncrementNumber;
        private GenotypeServiceDto genotypeServiceDto;
        private GroupDto groupDto;
        private int actualLecture;

        public GentoypeCriteriaDto(GeneticInitialData geneticInitialData, int lectureIncrementNumber, GenotypeServiceDto genotypeServiceDto, GroupDto groupDto, int actualLecture) {
            this.geneticInitialData = geneticInitialData;
            this.lectureIncrementNumber = lectureIncrementNumber;
            this.genotypeServiceDto = genotypeServiceDto;
            this.groupDto = groupDto;
            this.actualLecture = actualLecture;
        }

        public int getLectureIncrementNumber() {
            return lectureIncrementNumber;
        }

        public int getActualLecture() {
            return actualLecture;
        }

        public GentoypeCriteriaDto checkCriteriaAndContinueIfPossible() {
            genotypeServiceDto.setRoomByGroupRecovery(new ArrayList<>(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto)));
            boolean isValid = true;
//                    hardGenotypeCriteria.hasNoEmptyLectureByGroup(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).
//                            subList(actualLecture - lectureIncrementNumber + 1, genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).size())
//                    , geneticInitialData.getLectureDescriptionDto().getNumberPerDay());

            if (!isValid) {
                for (int k = genotypeServiceDto.getRoomByGroupRecovery().size() - 1; k > actualLecture - lectureIncrementNumber; k--) {
                    if (Objects.nonNull(genotypeServiceDto.getRoomByGroupRecovery().get(k))) {
                        genotypeServiceDto.getRoomListPerLecture().get(k).add(genotypeServiceDto.getRoomByGroupRecovery().get(k));
                    }
                    genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).removeLast();
                    genotypeServiceDto.getRoomByLectureTemporary().get(k).removeLast();
                }
                if (actualLecture < 5) {
                    actualLecture -= lectureIncrementNumber;
                } else {
                    actualLecture -= lectureIncrementNumber;
                }
            }
            lectureIncrementNumber = 1;
            return this;
        }
    }
}
