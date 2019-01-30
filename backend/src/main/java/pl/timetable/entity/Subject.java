package pl.timetable.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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
    private Set<Teacher> teachers = new HashSet<>();

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

    @ManyToMany(mappedBy = "subjectSet", fetch = FetchType.EAGER)
    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
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
