package pl.timetable.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.timetable.dto.*;
import pl.timetable.entity.Subject;

import java.util.*;

@Service
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
                        if(Objects.nonNull(cell.getSubjectDto())) {
                            genotype.getRoomBySubject().putIfAbsent(cell.getSubjectDto(), new ArrayList<>());
                            genotype.getRoomBySubject().get(cell.getSubjectDto()).add(cell.getRoomDto());
                        }

                    }
                }

            }

        }

//
    }

    public Genotype createInitialGenotype(GeneticInitialData geneticInitialData) {

        int lectureSize = geneticInitialData.getLectureDescription().getNumberPerDay() * geneticInitialData.getLectureDescription().getDaysPerWeek() * geneticInitialData.getLectureDescription().getWeeksPerSemester();
        int lectureIncrementNumber = 1;
//        int roomIncrementNumber = 0;
        GenotypeServiceDto genotypeServiceDto = new GenotypeServiceDto();

        fillRoomListPerLecture(geneticInitialData, lectureSize, genotypeServiceDto);
        Genotype genotype = new Genotype(geneticInitialData.getGroupDtoList().size(), lectureSize, geneticInitialData.getLectureDescription());
//        genotype.getRoomByLecture().put(lectureIncrementNumber, new ArrayList<>());
//        genotype.getLectureByRoom().put(geneticInitialData.getRoomDtoList().get(roomIncrementNumber), new ArrayList<>());
//        Integer putRoomPosition = getRandomIntegerBetweenRange(0, geneticInitialData.getRoomDtoList().size());
        //group iteration
        boolean initialGenotypeValid = false;
        while (!initialGenotypeValid) {
            createInitialGenotypesWithRoomAndGroup(geneticInitialData, lectureSize, lectureIncrementNumber, genotypeServiceDto, genotype);
            initialGenotypeValid = hardGenotypeCriteria.hasNoGroupDuplicatesByRoom(genotype.getGenotypeTable());
        }
        addSubject(genotype, geneticInitialData);
//        mapHintGenotype(genotype);
        Arrays.stream(genotype.getGenotypeTable()).forEach(group -> {
            if (Arrays.stream(group).filter(cell -> Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())).count() < 25) {
                LOGGER.info("Rooms not enough");
            }
        });
        LOGGER.info(genotype);
        return genotype;
    }

    private void createInitialGenotypesWithRoomAndGroup(GeneticInitialData geneticInitialData, int lectureSize, int lectureIncrementNumber, GenotypeServiceDto genotypeServiceDto, Genotype genotype) {
        for (int i = 0; i < genotype.getGenotypeTable().length; ) {
            GroupDto groupDto = geneticInitialData.getGroupDtoList().get(i);
//            Map<RoomDto, List<Integer>> lectureByRoomTemporary = new HashMap<>();
            CourseDto courseDto = CourseServiceImpl.mapEntityToDto(groupDto.getCourse());
            Integer subjectSize = getSubjectPerGroup(courseDto);
            genotypeServiceDto.getRoomByGroupTemporary().put(groupDto, new LinkedList<>());
//            lectureByRoomTemporary.put(geneticInitialData.getRoomDtoList().get(0), new ArrayList<>());
            //lecture iteration per group
            for (int actualLecture = 0; actualLecture < genotype.getGenotypeTable()[i].length; actualLecture++) {

                //end of iteration when enough room to subject by group
                if (subjectSize <= genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).stream().filter(Objects::nonNull).count()) {
                    int lectureToFinalCheck = actualLecture;
                    if (actualLecture == subjectSize + 1) {
                        lectureToFinalCheck = actualLecture - 1;
                    }
                    LecturePerDayIteration lecutrePerDayIteration =
                            new LecturePerDayIteration(geneticInitialData, lectureIncrementNumber, genotypeServiceDto, groupDto, lectureToFinalCheck)
                                    .invoke();
                    lectureIncrementNumber = lecutrePerDayIteration.getLectureIncrementNumber();
                    actualLecture = lecutrePerDayIteration.getActualLecture();
                    if (lectureIncrementNumber == 1) {
                        break;
                    }
                }
                RoomDto roomFounded = getRoomByRandomNumber(genotypeServiceDto.getRoomListPerLecture().get(actualLecture));
//                LOGGER.debug("Actual lecture " + actualLecture + " for group " + i + " " + roomFounded);
                genotype.getRoomByLecture().putIfAbsent(actualLecture, new LinkedList<>());
                genotype.getGenotypeTable()[i][actualLecture] = new Cell(actualLecture,
                        roomFounded,
                        courseDto, null,
                        groupDto);
                genotypeServiceDto.getRoomByLectureTemporary().putIfAbsent(actualLecture, new LinkedList<>());
                genotypeServiceDto.getRoomByLectureTemporary().get(actualLecture).add(roomFounded);
                genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).add(roomFounded);
                List<RoomDto> listToCheck = genotypeServiceDto.getRoomByGroupTemporary().get(groupDto);
                if (Arrays.stream(genotype.getGenotypeTable()[i]).filter(cell -> Objects.nonNull(cell) && Objects.nonNull(cell.getRoomDto())).count() == 26) {
                    LOGGER.error("To much cell for group " + i);
                }
                try {
                    if (genotype.getGenotypeTable()[i][actualLecture].getRoomDto() != genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).get(actualLecture)) {
                        LOGGER.info("Different cell for group " + i + "and lecture " + actualLecture);
                    }
                } catch (IndexOutOfBoundsException e) {
                    LOGGER.error("Index out of bound");
                }
                //check lecture group per day
                if (lectureIncrementNumber == geneticInitialData.getLectureDescription().getNumberPerDay() || actualLecture == genotype.getGenotypeTable()[i].length - 1) {
                    LecturePerDayIteration lecturePerDayIteration =
                            new LecturePerDayIteration(geneticInitialData, lectureIncrementNumber, genotypeServiceDto, groupDto, actualLecture)
                                    .invoke();
                    lectureIncrementNumber = lecturePerDayIteration.getLectureIncrementNumber();
                    actualLecture = lecturePerDayIteration.getActualLecture();
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
            boolean isValidCriteria = hardGenotypeCriteria.hasNoRoomDuplicatesByLecture(genotypeServiceDto.getRoomByLectureTemporary()) &&
                    hardGenotypeCriteria.hasEnoughSizeLectureToSubject(
                            ((Long) genotypeServiceDto.getRoomByLectureTemporary().values().stream().filter(roomDtos -> !roomDtos.isEmpty()).count()).intValue()
                            , subjectSize) &&
                    hardGenotypeCriteria.hasNoEmptyLectureByGroup(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto)
                            , geneticInitialData.getLectureDescription().getNumberPerDay());

            if (isValidCriteria) {
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

    private void fillRoomListPerLecture(GeneticInitialData geneticInitialData, int lectureSize, GenotypeServiceDto genotypeServiceDto) {
        for (int i = 0; i < lectureSize; i++) {
            genotypeServiceDto.getRoomListPerLecture().put(i, new ArrayList<>(geneticInitialData.getRoomDtoList()));
        }
    }


    private int getSubjectPerGroup(CourseDto courseDto) {

        return courseDto.getSubjectSet().stream().map(Subject::getSize).mapToInt(value -> value).sum();
    }

    private void addSubject(Genotype genotype, GeneticInitialData geneticInitialData) {

        Arrays.asList(genotype.getGenotypeTable()).forEach(columnGroup -> {
                    Set<Subject> subjectSet = columnGroup[0].getCourseDto().getSubjectSet();
//                    List<RoomDto> listRoomWithoutEmpty =
//                            genotype.getRoomByGroup().get(geneticInitialData.getGroupDtoList().get(0)).stream().filter(Objects::nonNull).collect(Collectors.toList());
                    LinkedList<RoomDto> listRoom = genotype.getRoomByGroup().get(columnGroup[0].getGroupDto());
                    subjectSet.forEach(subject ->
                            {
                                SubjectDto subjectDto = SubjectServiceImpl.mapEntityToDto(subject);
                                genotype.getRoomBySubject().putIfAbsent(subjectDto, new ArrayList<>());
                                for (int j = 0; j < subject.getSize(); j++) {
                                    Integer roomPosition = -1;
                                    if (listRoom.size() != 0) {
                                        RoomDto roomDto;
                                        do {
                                            roomPosition = new Random().ints(0, listRoom.size()).findFirst().getAsInt();
                                            roomDto = listRoom.get(roomPosition);
                                        } while (Objects.isNull(roomDto));

                                        genotype.getRoomBySubject().get(subjectDto).add(roomDto);
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

    private RoomDto getRoomByRandomNumber(List<RoomDto> roomDtoList) {
        Integer roomPosition = getRandomIntegerBetweenRange(0, roomDtoList.size());
        if (Objects.isNull(roomPosition)) {
            return null;
        } else {
            RoomDto roomDto = roomDtoList.get(roomPosition);
            roomDtoList.remove(roomDto);
            return roomDto;
        }
    }


    public static Integer getRandomIntegerBetweenRange(Integer min, Integer max) {
        Random random = new Random();
        Integer randomNumber = random.ints(min, (max + 1)).findFirst().getAsInt();
        if (Objects.equals(randomNumber, max) || max == 2) {
            return null;
        } else {
            return randomNumber;
        }
    }

    private static Integer getLectureSlot(GeneticInitialData geneticInitialData) {
        return geneticInitialData.getLectureDescription().getNumberPerDay() * geneticInitialData.getLectureDescription().getDaysPerWeek();
    }

    private class LecturePerDayIteration {
        private GeneticInitialData geneticInitialData;
        private int lectureIncrementNumber;
        private GenotypeServiceDto genotypeServiceDto;
        private GroupDto groupDto;
        private int actualLecture;

        public LecturePerDayIteration(GeneticInitialData geneticInitialData, int lectureIncrementNumber, GenotypeServiceDto genotypeServiceDto, GroupDto groupDto, int actualLecture) {
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

        public LecturePerDayIteration invoke() {
            genotypeServiceDto.setRoomByGroupRecovery(new ArrayList<>(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto)));
            boolean isValid = hardGenotypeCriteria.hasNoEmptyLectureByGroup(genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).
                            subList(actualLecture - lectureIncrementNumber + 1, genotypeServiceDto.getRoomByGroupTemporary().get(groupDto).size())
                    , geneticInitialData.getLectureDescription().getNumberPerDay());

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
