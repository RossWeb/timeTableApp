package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class Genotype {

    //Vector<Vector<Double>>  matrix= new Vector<Vector<Double>>();
    private Cell[][] genotypeTable;
    private Map<Integer, LinkedList<RoomDto>> roomByLecture = new HashMap<>();
    private Map<GroupDto, LinkedList<RoomDto>> roomByGroup = new HashMap<>();
    private Map<Integer, List<RoomDto>> roomBySubject = new HashMap<>();
    private Map<Integer, List<Integer>> lectureByTeacher = new HashMap<>();
    private Double fitnessScore = 0.0;
    private Double softFitnessScore = 0.0;
    private Double hardFitnessScore = 0.0;

    public Genotype(Genotype another) {
        this.genotypeTable = Arrays.copyOf(another.getGenotypeTable(), another.getGenotypeTable().length);
        this.roomByLecture = new HashMap<>(another.getRoomByLecture());
        this.roomByGroup = new HashMap<>(another.getRoomByGroup());
        this.roomBySubject = new HashMap<>(another.getRoomBySubject());
        this.fitnessScore = new Double(another.getFitnessScore());
        this.softFitnessScore = new Double(another.getSoftFitnessScore());
        this.hardFitnessScore = new Double(another.getHardFitnessScore());
    }

    public Double getSoftFitnessScore() {
        return softFitnessScore;
    }

    public void setSoftFitnessScore(Double softFitnessScore) {
        this.softFitnessScore = softFitnessScore;
    }

    public Double getHardFitnessScore() {
        return hardFitnessScore;
    }

    public void setHardFitnessScore(Double hardFitnessScore) {
        this.hardFitnessScore = hardFitnessScore;
    }

    public Genotype(int x, int y){
        genotypeTable = new Cell[x][y];
    }

    public Cell[][] getGenotypeTable() {
        return genotypeTable;
    }

    public Map<Integer, LinkedList<RoomDto>> getRoomByLecture() {
        return roomByLecture;
    }

    public Map<Integer, List<RoomDto>> getRoomBySubject() {
        return roomBySubject;
    }

    public Map<GroupDto, LinkedList<RoomDto>> getRoomByGroup() {
        return roomByGroup;
    }

    public void setRoomByGroup(Map<GroupDto, LinkedList<RoomDto>> roomByGroup) {
        this.roomByGroup = roomByGroup;
    }

    public void setRoomByLecture(Map<Integer, LinkedList<RoomDto>> roomByLecture) {
        this.roomByLecture = roomByLecture;
    }

    public void setRoomBySubject(Map<Integer, List<RoomDto>> roomBySubject) {
        this.roomBySubject = roomBySubject;
    }

    public Map<Integer, List<Integer>> getLectureByTeacher() {
        return lectureByTeacher;
    }

    public void setLectureByTeacher(Map<Integer, List<Integer>> lectureByTeacher) {
        this.lectureByTeacher = lectureByTeacher;
    }

    public Double getFitnessScore() {
        return fitnessScore;
    }

    public void setFitnessScore(Double fitnessScore) {
        this.fitnessScore = fitnessScore;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("genotypeTable", genotypeTable)
//                .append("roomByLecture", roomByLecture)
//                .append("lectureByRoom", lectureByRoom)
//                .append("subjectByLecture", subjectByLecture)
//                .append("lectureBySubject", lectureBySubject)
                .toString();
    }
}
