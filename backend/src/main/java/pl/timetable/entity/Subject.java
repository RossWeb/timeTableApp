package pl.timetable.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Transient;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Subject extends BaseEntity {

    private String name;

    @Transient
    private boolean exists = false;

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExists() {
        return exists;
    }

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
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
