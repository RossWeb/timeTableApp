package pl.timetable.dto;

import java.util.*;

public class GenotypeServiceDto {

    Map<GroupDto, LinkedList<RoomDto>> roomByGroupTemporary = new HashMap<>();
    Map<Integer, LinkedList<RoomDto>> roomByLectureTemporary = new HashMap<>();
    List<RoomDto> roomByGroupRecovery = new ArrayList<>();
    Map<Integer, List<RoomDto>> roomListPerLecture = new HashMap<>();

    public void setRoomByGroupRecovery(List<RoomDto> roomByGroupRecovery) {
        this.roomByGroupRecovery = roomByGroupRecovery;
    }

    public Map<GroupDto, LinkedList<RoomDto>> getRoomByGroupTemporary() {
        return roomByGroupTemporary;
    }

    public Map<Integer, LinkedList<RoomDto>> getRoomByLectureTemporary() {
        return roomByLectureTemporary;
    }

    public List<RoomDto> getRoomByGroupRecovery() {
        return roomByGroupRecovery;
    }

    public Map<Integer, List<RoomDto>> getRoomListPerLecture() {
        return roomListPerLecture;
    }
}
