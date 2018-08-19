package pl.timetable.entity;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findRoomByNameAndNumber",
                query = "from Room r where r.name = :name and r.number = :number"
        )
})
@Table(name = "room",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "number"})
)
public class Room extends BaseEntity {

    private String name;
    private String number;

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "number", nullable = false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
