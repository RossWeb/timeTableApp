package pl.timetable.dto;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.*;

public class Genotype {

    //Vector<Vector<Double>>  matrix= new Vector<Vector<Double>>();
    private Cell[][] genotypeTable;
    private Map<Integer, List<RoomDto>> roomByLecture = new HashMap<>();
    private Map<RoomDto, List<Integer>> lectureByRoom = new HashMap<>();
    private Map<Integer, List<SubjectDto>> subjectByLecture = new HashMap<>();
    private Map<SubjectDto, List<Integer>> lectureBySubject = new HashMap<>();
    private Double fitnessScore = 0.0;

    public Genotype(int x, int y){
        genotypeTable = new Cell[x][y];
    }

    public Cell[][] getGenotypeTable() {
        return genotypeTable;
    }

    public Map<Integer, List<RoomDto>> getRoomByLecture() {
        return roomByLecture;
    }

    public Map<RoomDto, List<Integer>> getLectureByRoom() {
        return lectureByRoom;
    }

    public Map<Integer, List<SubjectDto>> getSubjectByLecture() {
        return subjectByLecture;
    }

    public Map<SubjectDto, List<Integer>> getLectureBySubject() {
        return lectureBySubject;
    }

    public void setRoomByLecture(Map<Integer, List<RoomDto>> roomByLecture) {
        this.roomByLecture = roomByLecture;
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
