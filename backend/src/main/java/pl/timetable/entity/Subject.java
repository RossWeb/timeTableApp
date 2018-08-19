package pl.timetable.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(
                name = "findSubjectByName",
                query = "from Subject s where s.name = :name"
        )
})
public class Subject extends BaseEntity {

    private String name;
    private boolean exists = false;
    private Integer size = 0;

    @Column(name = "name", nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "size", nullable = false)
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Transient
    public boolean isExists() {
        return exists;
    }
    @Transient
    public void setExists(boolean exists) {
        this.exists = exists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        return new EqualsBuilder()
                .append(name, subject.name)
                .append(id, subject.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(id)
                .toHashCode();
    }
}
