package pl.timetable.dto;

public class TimeTablePagingDto extends PagingRequestDto {

    private Integer groupId;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
