package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class Genotype {

    //Vector<Vector<Double>>  matrix= new Vector<Vector<Double>>();
    private Cell[][] genotypeTable;
    private Map<Integer, LinkedList<RoomDto>> roomByLecture = new HashMap<>();
    private Map<GroupDto, LinkedList<RoomDto>> roomByGroup = new HashMap<>();
    private Map<SubjectDto, List<RoomDto>> roomBySubject = new HashMap<>();
    private Double fitnessScore = 0.0;
    private LectureDescription lectureDescription;

    public Genotype(int x, int y, LectureDescription lectureDescription){
        genotypeTable = new Cell[x][y];
        this.lectureDescription = lectureDescription;
    }

    public Cell[][] getGenotypeTable() {
        return genotypeTable;
    }

    public Map<Integer, LinkedList<RoomDto>> getRoomByLecture() {
        return roomByLecture;
    }

    public Map<SubjectDto, List<RoomDto>> getRoomBySubject() {
        return roomBySubject;
    }

    public Map<GroupDto, LinkedList<RoomDto>> getRoomByGroup() {
        return roomByGroup;
    }

    public LectureDescription getLectureDescription() {
        return lectureDescription;
    }

    public void setLectureDescription(LectureDescription lectureDescription) {
        this.lectureDescription = lectureDescription;
    }

    public void setRoomByGroup(Map<GroupDto, LinkedList<RoomDto>> roomByGroup) {
        this.roomByGroup = roomByGroup;
    }

    public void setRoomByLecture(Map<Integer, LinkedList<RoomDto>> roomByLecture) {
        this.roomByLecture = roomByLecture;
    }

    public void setRoomBySubject(Map<SubjectDto, List<RoomDto>> roomBySubject) {
        this.roomBySubject = roomBySubject;
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
