package pl.timetable.dto;

public class PagingRequestDto extends BaseDto {

    private Integer pageNumber;
    private Integer size;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
